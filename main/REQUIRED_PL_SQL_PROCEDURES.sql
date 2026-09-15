-- ============================================================================
-- 1. addFreight
-- ============================================================================
CREATE OR REPLACE PROCEDURE addFreight(
    p_freightId IN VARCHAR2,
    p_startPoint IN NUMBER,
    p_endPoint IN NUMBER
) AS
BEGIN
INSERT INTO Freight (freightId, startPoint, endPoint)
VALUES (p_freightId, p_startPoint, p_endPoint);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END addFreight;
/

-- ============================================================================
-- 2. addLocomotiveToTrain
-- ============================================================================
CREATE OR REPLACE PROCEDURE addLocomotiveToTrain(
    p_trainId IN VARCHAR2,
    p_locomotiveId IN VARCHAR2
) AS
BEGIN
INSERT INTO Locomotive_Train (locomotiveId, trainId)
VALUES (TO_NUMBER(p_locomotiveId), TO_NUMBER(p_trainId));
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END addLocomotiveToTrain;
/

-- ============================================================================
-- 3. addFacilityTrain
-- ============================================================================
CREATE OR REPLACE PROCEDURE addFacilityTrain(
    p_facilityId IN NUMBER,
    p_trainId IN NUMBER,
    p_arriveTime IN TIMESTAMP,
    p_leaveTime IN TIMESTAMP
) AS
BEGIN
INSERT INTO Facility_Train (facilityId, trainId, arriveTime, leaveTime)
VALUES (p_facilityId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END addFacilityTrain;
/

-- ============================================================================
-- 4. addTimeInSegment
-- ============================================================================
CREATE OR REPLACE PROCEDURE addTimeInSegment(
    p_segmentId IN NUMBER,
    p_trainId IN NUMBER,
    p_arriveTime IN TIMESTAMP,
    p_leaveTime IN TIMESTAMP
) AS
BEGIN
INSERT INTO Time_In_Segment (railLineId, trainId, arriveTime, leaveTime)
VALUES (p_segmentId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END addTimeInSegment;
/

-- ============================================================================
-- 5. updateWagonLocation
-- ============================================================================
CREATE OR REPLACE PROCEDURE updateWagonLocation(
    p_wagonId IN NUMBER,
    p_facilityId IN NUMBER
) AS
BEGIN
UPDATE Wagon
SET facilityId = p_facilityId
WHERE wagonId = p_wagonId;
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END updateWagonLocation;
/

-- ============================================================================
-- 6. cleanLogisticsData
--    (Removed Train_Freight deletion logic)
-- ============================================================================
CREATE OR REPLACE PROCEDURE cleanLogisticsData AS
BEGIN
    -- 1. Delete from simple junction tables
DELETE FROM Locomotive_Train;
DELETE FROM Facility_Train;
DELETE FROM Time_In_Segment;
DELETE FROM Wagon_Freight;

-- 2. Delete from Schedule (Dynamic SQL in case table doesn't exist)
BEGIN EXECUTE IMMEDIATE 'DELETE FROM Schedule'; EXCEPTION WHEN OTHERS THEN NULL; END;

    -- 3. Delete from main logistics tables
DELETE FROM Freight;
DELETE FROM Train;
DELETE FROM Route_Point;
DELETE FROM Route;

-- 4. Delete from dynamic tables
BEGIN EXECUTE IMMEDIATE 'DELETE FROM Train_Wagons'; EXCEPTION WHEN OTHERS THEN NULL; END;
BEGIN EXECUTE IMMEDIATE 'DELETE FROM Path'; EXCEPTION WHEN OTHERS THEN NULL; END;

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END cleanLogisticsData;
/

CREATE OR REPLACE FUNCTION get_available_locomotivesTransitINF(p_start_station_id IN NUMBER)
RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT
    l.locomotiveId,
    CASE
        WHEN EXISTS (
            SELECT 1 FROM Locomotive_Train lt
                              JOIN Train t ON lt.trainId = t.trainId
            WHERE lt.locomotiveId = l.locomotiveId
              AND t.startDate <= SYSDATE AND t.endDate >= SYSDATE
        ) THEN 'IN TRANSIT'
        ELSE 'PARKED'
        END AS status,
    NVL(f.name, 'Unknown Location') AS location_name,
    CASE WHEN l.startFacilityId = p_start_station_id THEN 0 ELSE 999 END AS distance_metric,

    -- Usamos NVL para garantir que não vem NULL
    NVL(m.name, 'Unknown Make') AS make_name,
    NVL(lm.name, 'Unknown Model') AS model_name,
    NVL(lm.power, 0) AS power,
    NVL(lm.length, 0) AS length,
    NVL(lm.width, 0) AS width,
    NVL(lm.height, 0) AS height,
    NVL(lm.weight, 0) AS weight,
    0 AS max_speed,

    -- Engine Type seguro
    CASE
        WHEN elm.locomotiveModelId IS NOT NULL THEN 'ELECTRIC'
        WHEN dlm.locomotiveModelId IS NOT NULL THEN 'DIESEL'
        ELSE 'UNKNOWN'
        END AS engine_type,

    NVL((SELECT MIN(gaugeName) FROM Locomotive_Model_Gauge lmg WHERE lmg.locomotiveModelId = lm.locomotiveModelId), 'Standard') as gauge_name

FROM Locomotive l
         LEFT JOIN Facility f ON l.startFacilityId = f.facilityId
         LEFT JOIN Locomotive_Model lm ON l.locomotiveModelId = lm.locomotiveModelId
         LEFT JOIN Make m ON lm.makeId = m.makeId
         LEFT JOIN Electric_Locomotive_Model elm ON lm.locomotiveModelId = elm.locomotiveModelId
         LEFT JOIN Diesel_Locomotive_Model dlm ON lm.locomotiveModelId = dlm.locomotiveModelId;

RETURN v_cursor;
END;
/

CREATE OR REPLACE  FUNCTION get_station_wagon_counts
RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT
    f.facilityId,
    f.name,
    -- Count wagons that are NOT associated with an active freight/train
    (SELECT COUNT(*)
     FROM Wagon w
     WHERE w.facilityId = f.facilityId
       AND NOT EXISTS (
         SELECT 1 FROM Wagon_Freight wf
                           JOIN Freight fr ON wf.freightId = fr.freightId
                           JOIN Route r ON fr.routeId = r.routeId
                           JOIN Train t ON r.routeId = t.routeId
         WHERE wf.wagonId = w.wagonId
           AND t.startDate <= SYSDATE AND t.endDate >= SYSDATE
     )
    ) as wagon_count
FROM Facility f
ORDER BY f.name ASC;
RETURN v_cursor;
END;
/

CREATE OR REPLACE PROCEDURE cleanTrain(p_trainId NUMBER)
IS
BEGIN

DELETE FROM Locomotive_Train
WHERE trainId = p_trainId;

DELETE FROM Time_In_Segment
WHERE trainId = p_trainId;

DELETE FROM Facility_Train
WHERE trainId = p_trainId;

DELETE FROM Train
WHERE trainId = p_trainId;


EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE FUNCTION get_available_wagonsTransitINF(p_start_station_id IN NUMBER)
RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT
    ws.wagonId,
    ws.model_name,
    ws.type_desc,
    ws.status,
    f.name AS location_name,
    CASE
        WHEN ws.location_id = p_start_station_id THEN 0
        ELSE 9999 -- ainda precisa calcular "distance from the starting point"
        END AS distance_metric
FROM (
         -- Subquery equivalente ao Wagon_Status
         SELECT
             w.wagonId,
             wm.name AS model_name,
             wt.description AS type_desc,
             CASE
                 WHEN lf.startDate IS NOT NULL
                     AND lf.startDate <= SYSDATE
                     AND lf.endDate >= SYSDATE THEN 'IN TRANSIT'
                 ELSE 'PARKED'
                 END AS status,
             COALESCE(lf.destination_id, w.facilityId) AS location_id
         FROM Wagon w
                  JOIN Wagon_Model wm ON w.wagonModelId = wm.wagonModelId
                  JOIN Wagon_Type wt ON wm.wagonTypeId = wt.wagonTypeId
                  LEFT JOIN (
             -- Subquery para pegar o último freight de cada wagon
             SELECT
                 wagonId,
                 startDate,
                 endDate,
                 destination_id
             FROM (
                      SELECT
                          w2.wagonId,
                          t.startDate,
                          t.endDate,
                          r.endPoint AS destination_id,
                          ROW_NUMBER() OVER (PARTITION BY w2.wagonId ORDER BY t.startDate DESC) AS rn
                      FROM Wagon w2
                               LEFT JOIN Wagon_Freight wf ON w2.wagonId = wf.wagonId
                               LEFT JOIN Freight f ON wf.freightId = f.freightId
                               LEFT JOIN Route r ON f.routeId = r.routeId
                               LEFT JOIN Train t ON r.routeId = t.routeId
                  )
             WHERE rn = 1
         ) lf ON w.wagonId = lf.wagonId
     ) ws
         LEFT JOIN Facility f ON ws.location_id = f.facilityId
ORDER BY
    distance_metric ASC;

RETURN v_cursor;
END;
/

--USEDDDDDDDDDDDD
CREATE OR REPLACE FUNCTION get_available_locomotivesTransitINF(p_start_station_id IN NUMBER, p_reference_datetime IN DATE)
    RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT
    l.locomotiveId,
    CASE
        WHEN EXISTS (
            SELECT 1
            FROM Locomotive_Train lt
                     JOIN Train t ON lt.trainId = t.trainId
            WHERE lt.locomotiveId = l.locomotiveId
              AND t.startDate <= p_reference_datetime
              AND t.endDate >= p_reference_datetime
        ) THEN 'IN TRANSIT'
        ELSE 'PARKED'
        END AS status,
    NVL(f.name, 'Unknown Location') AS location_name,
    CASE
        WHEN l.startFacilityId = p_start_station_id THEN 0
        ELSE 999
        END AS distance_metric,
    NVL(m.name, 'Unknown Make') AS make_name,
    NVL(lm.name, 'Unknown Model') AS model_name,
    NVL(lm.power, 0) AS power,
    NVL(lm.length, 0) AS length,
    NVL(lm.width, 0) AS width,
    NVL(lm.height, 0) AS height,
    NVL(lm.weight, 0) AS weight,
    0 AS max_speed,
    CASE
        WHEN elm.locomotiveModelId IS NOT NULL THEN 'ELECTRIC'
        WHEN dlm.locomotiveModelId IS NOT NULL THEN 'DIESEL'
        ELSE 'UNKNOWN'
        END AS engine_type,
    NVL((SELECT MIN(gaugeName)
         FROM Locomotive_Model_Gauge lmg
         WHERE lmg.locomotiveModelId = lm.locomotiveModelId), 'Standard') as gauge_name
FROM Locomotive l
         LEFT JOIN Facility f ON l.startFacilityId = f.facilityId
         LEFT JOIN Locomotive_Model lm ON l.locomotiveModelId = lm.locomotiveModelId
         LEFT JOIN Make m ON lm.makeId = m.makeId
         LEFT JOIN Electric_Locomotive_Model elm ON lm.locomotiveModelId = elm.locomotiveModelId
         LEFT JOIN Diesel_Locomotive_Model dlm ON lm.locomotiveModelId = dlm.locomotiveModelId;

RETURN v_cursor;
END;
/

CREATE OR REPLACE FUNCTION getAllFacilities
RETURN SYS_REFCURSOR
IS
    fac_cursor SYS_REFCURSOR;
BEGIN
OPEN fac_cursor FOR
SELECT
    facilityId,
    name,
    latitude,
    longitude
FROM Facility
ORDER BY facilityId;

RETURN fac_cursor;
END getAllFacilities;
/

CREATE OR REPLACE FUNCTION getFacilityByLocomotive(p_locomotiveId IN NUMBER)
RETURN NUMBER
IS
    v_facilityId NUMBER;
BEGIN
SELECT startFacilityId
INTO v_facilityId
FROM Locomotive
WHERE locomotiveId = p_locomotiveId;

RETURN v_facilityId;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE FUNCTION getAllWagons
RETURN SYS_REFCURSOR
IS
    wagon_cursor SYS_REFCURSOR;
BEGIN
OPEN wagon_cursor FOR
SELECT
    w.wagonId,
    o.vatNumber as operatorId,
    wm.wagonModelId,
    wm.name as modelName,
    wm.length,
    wm.width,
    wm.height,
    wm.weight,
    wm.maxSpeed,
    wm.payload,
    wm.volumeCapacity,
    wm.tare,
    wt.description as wagonType,
    wm.numOfBogies as boxCapacity,
    (SELECT MIN(wmg.gaugeName)
     FROM Wagon_Model_Gauge wmg
     WHERE wmg.wagonModelId = wm.wagonModelId) as gaugeName
FROM Wagon w
         INNER JOIN Wagon_Model wm ON w.wagonModelId = wm.wagonModelId
         INNER JOIN Wagon_Type wt ON wm.wagonTypeId = wt.wagonTypeId
         INNER JOIN Operator o ON w.operatorId = o.vatNumber
ORDER BY w.wagonId;

RETURN wagon_cursor;
END getAllWagons;
/

CREATE OR REPLACE FUNCTION getAllLocomotives
RETURN SYS_REFCURSOR
IS
    locomotive_cursor SYS_REFCURSOR;
BEGIN
OPEN locomotive_cursor FOR
SELECT
    l.locomotiveId,
    l.yearOfService,
    lm.numOfBogies as numberBogies,
    o.vatNumber as operatorId,
    lm.locomotiveModelId,
    lm.power,
    lm.maxSpeed,
    lm.length,
    lm.width,
    lm.height,
    lm.weight,
    lm.acceleration,
    m.name as make,
    lm.name as modelName,
    CASE
        WHEN elm.locomotiveModelId IS NOT NULL THEN 'ELECTRIC'
        WHEN dlm.locomotiveModelId IS NOT NULL THEN 'DIESEL'
        ELSE 'UNKNOWN'
        END as locomotiveType,
    elm.voltage,
    elm.frequency,
    dlm.fuelCapacity,
    l.startFacilityId,
    (SELECT MIN(lmg.gaugeName)
     FROM Locomotive_Model_Gauge lmg
     WHERE lmg.locomotiveModelId = lm.locomotiveModelId) as gaugeName
FROM Locomotive l
         INNER JOIN Locomotive_Model lm ON l.locomotiveModelId = lm.locomotiveModelId
         INNER JOIN Make m ON lm.makeId = m.makeId
         INNER JOIN Operator o ON l.operatorId = o.vatNumber
         LEFT JOIN Electric_Locomotive_Model elm ON lm.locomotiveModelId = elm.locomotiveModelId
         LEFT JOIN Diesel_Locomotive_Model dlm ON lm.locomotiveModelId = dlm.locomotiveModelId
ORDER BY l.locomotiveId;

RETURN locomotive_cursor;
END getAllLocomotives;
/

CREATE OR REPLACE FUNCTION getRailSegmentsForLinR(p_railLineId IN NUMBER)
RETURN SYS_REFCURSOR
IS
    segments_cursor SYS_REFCURSOR;
BEGIN
OPEN segments_cursor FOR
SELECT
    rs.segmentId,
    rs.railLineId,
    rs."order" AS segmentOrder,
    g.width AS gaugeWidth,
    rs.trackElectrification,
    rs.numberOfTracks,
    rs.length,
    rs.maxWeight,
    rs.speedLimit,
    s.startPosition,
    s.length AS sidingLength
FROM Rail_Segment rs
         INNER JOIN Gauge g ON rs.gaugeName = g.gaugeName
         LEFT JOIN Siding s ON rs.sidingId = s.sidingId
WHERE rs.railLineId = p_railLineId
ORDER BY rs."order";

RETURN segments_cursor;
END getRailSegmentsForLinR;
/

CREATE OR REPLACE PROCEDURE getAllFreightDetails(
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
OPEN p_cursor FOR
SELECT
    f.freightId,
    -- Dados Start Facility
    fs.facilityId AS startId,
    fs.name AS startName,
    fs.latitude AS startLat,
    fs.longitude AS startLon,
    -- Dados End Facility
    fe.facilityId AS endId,
    fe.name AS endName,
    fe.latitude AS endLat,
    fe.longitude AS endLon,
    -- Dados Wagon/Model (Usamos INNER JOIN para evitar registros nulos)
    w.wagonId,
    wm.wagonModelId,
    wm.length,
    wm.height,
    wm.width,
    wm.weight,
    wm.maxSpeed,
    wm.volumeCapacity,
    -- Subquery para obter o Gauge (evita duplicar linhas se houver múltiplos gauges)
    (SELECT MIN(gaugeName)
     FROM Wagon_Model_Gauge wmg
     WHERE wmg.wagonModelId = wm.wagonModelId) AS gaugeName
FROM Freight f
         INNER JOIN Facility fs ON f.startPoint = fs.facilityId
         INNER JOIN Facility fe ON f.endPoint = fe.facilityId
         INNER JOIN Wagon_Freight wf ON f.freightId = wf.freightId
         INNER JOIN Wagon w ON wf.wagonId = w.wagonId
         INNER JOIN Wagon_Model wm ON w.wagonModelId = wm.wagonModelId
ORDER BY f.freightId;
END;
/

CREATE OR REPLACE FUNCTION getFacilityByWagon(p_wagonId IN NUMBER)
RETURN NUMBER
IS
    v_facilityId NUMBER;
BEGIN
SELECT facilityId
INTO v_facilityId
FROM Wagon
WHERE wagonId = p_wagonId;

RETURN v_facilityId;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
WHEN OTHERS THEN
        RAISE;
END;
/


CREATE OR REPLACE FUNCTION getRailLines
RETURN SYS_REFCURSOR
AS
    railLine_cursor SYS_REFCURSOR;

BEGIN
OPEN railLine_cursor FOR
SELECT * FROM Rail_Line;

RETURN railLine_cursor;
END;
/

CREATE OR REPLACE PROCEDURE associateFreightRoute(
    p_routeId NUMBER,
    p_freightId NUMBER
)
IS
    v_routeStartDate Route.departureDate%TYPE;
    v_freightExists  NUMBER;
BEGIN
    -- Verify route exists and get its departure date
BEGIN
SELECT departureDate
INTO v_routeStartDate
FROM Route
WHERE routeId = p_routeId;
EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Route ID ' || p_routeId || ' does not exist');
END;

    -- Verify freight exists
SELECT COUNT(*)
INTO v_freightExists
FROM Freight
WHERE freightId = p_freightId;
IF v_freightExists = 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Freight ID ' || p_freightId || ' does not exist');
END IF;

    -- Check all wagons in this freight
FOR o_line IN (
            SELECT wagonId
            FROM Wagon_Freight wf
            WHERE wf.freightId = p_freightId)
        LOOP
            -- Check if this wagon is already assigned to any train during this time
            FOR l_line IN (
                SELECT t.startDate, t.endDate
                FROM Wagon_Freight wf
                INNER JOIN Freight ft ON ft.freightId = wf.freightId
                INNER JOIN Train t ON t.routeId = ft.routeId
                WHERE wf.wagonId = o_line.wagonId)
                LOOP
                    -- Check for overlap (handle NULL endDate for ongoing trains)
                    IF l_line.startDate <= TRUNC(v_routeStartDate) AND
                       (l_line.endDate >= TRUNC(v_routeStartDate)) THEN
                        RAISE_APPLICATION_ERROR(-20004, 'Wagon ' || o_line.wagonId || ' already assigned to a train during this time interval');
END IF;
END LOOP;
END LOOP;

    -- Update the freight to associate it with the route
UPDATE Freight
SET routeId = p_routeId
WHERE freightId = p_freightId;

COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE addFacilityTrain (p_facilityId NUMBER, p_trainId NUMBER, p_arriveTime TIMESTAMP, p_leaveTime TIMESTAMP)

IS
BEGIN

INSERT INTO Facility_Train (facilityId, trainId, arriveTime, leaveTime) VALUES (p_facilityId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE addFreight(p_freightId NUMBER, p_startPoint NUMBER, p_endPoint NUMBER)
IS
BEGIN

INSERT INTO Freight (freightID, startPoint, endPoint) VALUES (p_freightId, p_startPoint, p_endPoint);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE insertLocomotiveTrain(p_trainId NUMBER, p_locomotiveId NUMBER)
IS
BEGIN

INSERT INTO Locomotive_Train (trainId, locomotiveId)
VALUES (p_trainId, p_locomotiveId);

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;

END;
/

CREATE OR REPLACE PROCEDURE writeRouteToDB(
    p_routeId IN NUMBER,
    p_startFacilityId IN NUMBER,
    p_endFacilityId IN NUMBER,
    p_departureDate IN TIMESTAMP
) AS
BEGIN
INSERT INTO Route (routeId, startPoint, endPoint, departureDate)
VALUES (p_routeId, p_startFacilityId, p_endFacilityId, p_departureDate);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END writeRouteToDB;
/



CREATE OR REPLACE PROCEDURE writeRoutePointToDB(
    p_routeId Route.routeId%TYPE,
    p_order Route_Point."order"%TYPE,
    p_facilityId Facility.facilityId%TYPE
)
IS
    v_foundRoute NUMBER;
    v_foundFacility NUMBER;

    exRouteNotFound EXCEPTION;
    exFacilityNotFound EXCEPTION;
BEGIN
SELECT COUNT(*)
INTO v_foundRoute
FROM Route
WHERE routeId = p_routeId;

IF v_foundRoute = 0 THEN
        RAISE exRouteNotFound;
END IF;

SELECT COUNT(*)
INTO v_foundFacility
FROM Facility
WHERE facilityId = p_facilityId;

IF v_foundFacility = 0 THEN
        RAISE exFacilityNotFound;
END IF;

INSERT INTO Route_Point (routeId, "order", facilityId)
VALUES (p_routeId, p_order, p_facilityId);

EXCEPTION
    WHEN exRouteNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'Route was not found!');

WHEN exFacilityNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'Facility was not found!');
END;
/

CREATE OR REPLACE PROCEDURE addTimeInSegment (p_railSegmentId NUMBER, p_trainId NUMBER, p_arriveTime TIMESTAMP, p_leaveTime TIMESTAMP)

IS
BEGIN

INSERT INTO Time_In_Segment (railSegmentId, trainId, arriveTime, leaveTime) VALUES (p_railSegmentId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE addTrain(p_trainId NUMBER, p_routeId NUMBER, p_maxSize FLOAT, p_startDate DATE, p_endDate DATE)
IS

BEGIN

INSERT INTO Train (trainId,routeId,maxSize,startDate,endDate) VALUES (p_trainId, p_routeId, p_maxSize, p_startDate, p_endDate);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE addWagonToFreight (p_freightId NUMBER, p_wagonId NUMBER)
IS
BEGIN

INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (p_wagonId, p_freightId);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE updateTrainEndDate (p_trainId Train.trainId%TYPE, p_endDate Train.endDate%TYPE)
IS
BEGIN

UPDATE Train
SET Train.endDate = p_endDate
WHERE Train.trainId = p_trainId;
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE cleanTrain(p_trainId NUMBER)
IS
BEGIN

DELETE FROM Locomotive_Train
WHERE trainId = p_trainId;

DELETE FROM Time_In_Segment
WHERE trainId = p_trainId;

DELETE FROM Facility_Train
WHERE trainId = p_trainId;

DELETE FROM Train
WHERE trainId = p_trainId;


EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE deleteAllTimeInSegmentToGivenId (p_trainId NUMBER)
IS
BEGIN

DELETE FROM Time_In_Segment
WHERE Time_In_Segment.trainId = p_trainId;

EXCEPTION
    WHEN OTHERS THEN
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/

CREATE OR REPLACE PROCEDURE cleanFreight(p_freightId NUMBER)
IS
BEGIN

DELETE FROM Wagon_Freight
WHERE freightId = p_freightId;

DELETE FROM Freight
WHERE freightId = p_freightId;


EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE deleteAllFacilityTrainToGivenId (p_trainId NUMBER)
IS
BEGIN

DELETE FROM Facility_Train
WHERE Facility_Train.trainId = p_trainId;

EXCEPTION
    WHEN OTHERS THEN
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/


CREATE OR REPLACE FUNCTION is_freight_available(
    p_freightId IN NUMBER,
    p_departureDate IN TIMESTAMP
) RETURN NUMBER IS
    v_conflict_count NUMBER := 0;
BEGIN
SELECT COUNT(*)
INTO v_conflict_count
FROM Wagon_Freight wf_atual
WHERE wf_atual.freightId = p_freightId
  AND EXISTS (
    SELECT 1
    FROM Wagon_Freight wf_outro
             JOIN Freight f_outro ON wf_outro.freightId = f_outro.freightId
             JOIN Train t_outro ON f_outro.routeId = t_outro.routeId
    WHERE wf_outro.wagonId = wf_atual.wagonId
      AND f_outro.freightId <> p_freightId
      AND p_departureDate >= t_outro.startDate
      AND (t_outro.endDate IS NULL OR p_departureDate <= t_outro.endDate)
);

IF v_conflict_count = 0 THEN
        RETURN 1;
ELSE
        RETURN 0;
END IF;
END is_freight_available;
/

CREATE OR REPLACE FUNCTION getNextRouteId
RETURN NUMBER
IS
    v_next_id NUMBER;
BEGIN

SELECT NVL(MAX(routeId), 100000) + 1
INTO v_next_id
FROM Route;

RETURN v_next_id;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 1; -- Fallback caso ocorra algum erro inesperado
END;
/