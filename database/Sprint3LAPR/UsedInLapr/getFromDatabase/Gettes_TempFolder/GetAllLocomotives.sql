--USEDDDDDDDDDDDDDD
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
            NVL((SELECT MIN(lmg.gaugeName)
             FROM Locomotive_Model_Gauge lmg
             WHERE lmg.locomotiveModelId = lm.locomotiveModelId), 'Iberian gauge') as gaugeName
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
