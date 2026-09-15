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
         WHERE lmg.locomotiveModelId = lm.locomotiveModelId), 'Iberian gauge') as gauge_name
FROM Locomotive l
         LEFT JOIN Facility f ON l.startFacilityId = f.facilityId
         LEFT JOIN Locomotive_Model lm ON l.locomotiveModelId = lm.locomotiveModelId
         LEFT JOIN Make m ON lm.makeId = m.makeId
         LEFT JOIN Electric_Locomotive_Model elm ON lm.locomotiveModelId = elm.locomotiveModelId
         LEFT JOIN Diesel_Locomotive_Model dlm ON lm.locomotiveModelId = dlm.locomotiveModelId;

RETURN v_cursor;
END;
/