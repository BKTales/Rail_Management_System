--USEEDDDDDDDDDDDDDD
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