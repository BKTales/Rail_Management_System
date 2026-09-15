--USEDDDDDDDDDDDDDDDDD
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
