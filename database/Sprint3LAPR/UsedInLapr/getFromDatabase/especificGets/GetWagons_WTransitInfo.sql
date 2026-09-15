--USEDDDDDDDDDDDDD
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