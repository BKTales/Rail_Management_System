CREATE OR REPLACE FUNCTION getUnusedWagons(startInstant date := NULL, endInstant date := NULL) 
RETURN SYS_REFCURSOR
IS 
    wagon_cursor SYS_REFCURSOR;

    exNullStartInstant EXCEPTION;
    exNullEndInstant EXCEPTION;
    exInvalidPeriod EXCEPTION;
BEGIN
    IF startInstant is NULL THEN
        RAISE exNullStartInstant;
    END IF;

    IF endInstant is NULL THEN
        RAISE exNullEndInstant;
    END IF;

    IF startInstant > endInstant THEN
        RAISE exInvalidPeriod;
    END IF;

    OPEN wagon_cursor FOR
        SELECT DISTINCT Wagon.wagonId, Wagon.operatorId, Wagon.wagonModelId
        FROM Wagon
        WHERE NOT EXISTS(
            SELECT 1
            FROM Freight_Wagon
                     INNER JOIN Freight ON Freight.freightId = Freight_Wagon.freightId
                     INNER JOIN Route_Freight ON Route_Freight.freightId = Freight.freightId
                     INNER JOIN Route ON Route.routeId = Route_Freight.routeId
                     INNER JOIN Train ON Train.routeId = Route.routeId
            WHERE Wagon.wagonId = Freight_Wagon.wagonId AND Train.startDate BETWEEN startInstant AND endInstant)
        ORDER BY Wagon.wagonId;

    RETURN wagon_cursor;

EXCEPTION
    WHEN exNullStartInstant THEN
        dbms_output.put_line('The provided start instant is invalid. Provide a valid start instant!');
        RETURN NULL;

    WHEN exNullEndInstant THEN
        dbms_output.put_line('The provided end instant is invalid. Provide a valid end instant!');
        RETURN NULL;

    WHEN exInvalidPeriod THEN
        dbms_output.put_line('The provided period is invalid. Provide a valid period!');
        RETURN NULL;
END;
/

-- ------------Tests------------

-- Null start instant
DECLARE
    startInstant Train.startDate%TYPE := NULL;
    endInstant Train.startDate%TYPE := TO_DATE('06/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    res SYS_REFCURSOR;
BEGIN
    res := getUnusedWagons(startInstant, endInstant);
END;
/

-- Null end instant
DECLARE
    startInstant Train.startDate%TYPE := TO_DATE('06/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    endInstant Train.startDate%TYPE := NULL;
    res SYS_REFCURSOR;
BEGIN
    res := getUnusedWagons(startInstant, endInstant);
END;
/

-- Invalid date
DECLARE
    startInstant Train.startDate%TYPE := TO_DATE('06/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    endInstant Train.startDate%TYPE := TO_DATE('05/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    res SYS_REFCURSOR;
BEGIN
    res := getUnusedWagons(startInstant, endInstant);
END;
/

DECLARE
    startInstant Train.startDate%TYPE := TO_DATE('05/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    endInstant Train.startDate%TYPE := TO_DATE('07/10/2025 10:00:00', 'dd/mm/yy hh24:mi:ss');
    wagonId Wagon.wagonId%TYPE;
    wagonOperatorId Wagon.operatorId%TYPE;
    wagonModelId Wagon.wagonModelId%TYPE;

    cont NUMBER := 0;

    res SYS_REFCURSOR;
BEGIN
    res := getUnusedWagons(startInstant, endInstant);

    IF res IS NOT NULL THEN
        dbms_output.put_line('--- List of unused wagons between ' || TO_CHAR(startInstant, 'dd/mm/yy hh24:mi:ss') || ' and ' || TO_CHAR(endInstant, 'dd/mm/yy hh24:mi:ss') || ' ---');
        LOOP
            FETCH res INTO wagonId, wagonOperatorId, wagonModelId;
            EXIT WHEN res%NOTFOUND;
            dbms_output.put_line('Wagon ID: ' || wagonId || ' | Operator ID: ' || wagonOperatorId || ' | Wagon Model - ' || wagonModelId);
            cont := cont + 1;
        END LOOP;
        IF cont = 0 THEN
            dbms_output.put_line('WARNING - No wagons were found!');
        END IF;
        CLOSE res;
    ELSE
            dbms_output.put_line('There was an error retriving the list of wagons!');
    END IF;

END;
/