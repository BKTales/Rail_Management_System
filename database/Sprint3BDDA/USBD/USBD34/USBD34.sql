CREATE OR REPLACE PROCEDURE assignFreightToTrain(p_trainId Train.trainId%TYPE, p_freightId Freight.freightId%TYPE) 
IS 
    v_trainCount NUMBER;
    v_freightCount NUMBER;

    v_locomotivesLength Locomotive_Model.length%TYPE;

    v_trainFreights SYS_REFCURSOR;
    v_trainSize Train.maxSize%TYPE;
    v_trainRoute Train.routeId%TYPE;
    v_freightId Freight.freightId%TYPE;
    v_freightLength FLOAT := 0;

    v_lengthCount FLOAT := 0;

    v_trainStartInstant Train.startDate%TYPE;
    v_trainEndInstant Train.endDate%TYPE;
    v_freight_wagons_cursor SYS_REFCURSOR;
    v_wagonId Wagon.wagonId%TYPE;
    v_invalidFreightsCount NUMBER;


    exNullTrainId EXCEPTION;
    exNullFreightId EXCEPTION;
    exTrainIdNotFound EXCEPTION;
    exFreightIdNotFound EXCEPTION;
    exTrainLengthExceeded EXCEPTION;
    exFoundWagonInvalidPeriod EXCEPTION;
BEGIN
    -- verify if train id is null
    IF p_trainId is NULL THEN
        RAISE exNullTrainId;
    END IF;

    -- verify if freight id is null
    IF p_freightId is NULL THEN
        RAISE exNullFreightId;
    END IF;

    -- verify if train was found
    SELECT COUNT(*)
    INTO v_trainCount
    FROM Train
    WHERE trainId = p_trainId;

    IF v_trainCount = 0 THEN
        RAISE exTrainIdNotFound;
    END IF;

    -- verify if freight was found
    SELECT COUNT(*)
    INTO v_freightCount
    FROM Freight
    WHERE freightId = p_freightId;

    IF v_freightCount = 0 THEN
        RAISE exFreightIdNotFound;
    END IF;

    -- get train's locomotives total length
    SELECT SUM(Locomotive_Model.length)
    INTO v_locomotivesLength
    FROM Locomotive_Model
    INNER JOIN Locomotive ON Locomotive.locomotiveModelId = Locomotive_Model.locomotiveModelId
    INNER JOIN Locomotive_Train ON Locomotive_Train.locomotiveId = Locomotive.locomotiveId
    WHERE Locomotive_Train.trainId = p_trainId;

    -- add locomotives length to total length count
    v_lengthCount := v_lengthCount + v_locomotivesLength;

    -- get all freight lengths associated with the train's route to verify train's size
    SELECT routeId
    INTO v_trainRoute
    FROM Train
    WHERE trainId = p_trainId;
 
    OPEN v_trainFreights FOR
        SELECT freightId
        FROM Freight
        WHERE routeId = v_trainRoute;
    
    LOOP
        FETCH v_trainFreights INTO v_freightId;
        EXIT WHEN v_trainFreights%NOTFOUND;

        SELECT SUM(Wagon_Model.length)
        INTO v_freightLength
        FROM Wagon_Model
        INNER JOIN Wagon ON Wagon.wagonModelId = Wagon_Model.wagonModelId
        INNER JOIN Wagon_Freight ON Wagon_Freight.wagonId = Wagon.wagonId
        WHERE Wagon_Freight.freightId = v_freightId;

        -- add freight length to total train length
        v_lengthCount := v_lengthCount + v_freightLength;

    END LOOP;

    CLOSE v_trainFreights;

    -- get new freight total length to check train's final length
    SELECT SUM(Wagon_Model.length)
    INTO v_freightLength
    FROM Wagon_Model
    INNER JOIN Wagon ON Wagon.wagonModelId = Wagon_Model.wagonModelId
    INNER JOIN Wagon_Freight ON Wagon_Freight.wagonId = Wagon.wagonId
    WHERE Wagon_Freight.freightId = p_freightId;

    -- add new freight length to total train length
    v_lengthCount := v_lengthCount + v_freightLength;

    SELECT maxSize
    INTO v_trainSize
    FROM Train
    WHERE trainId = p_trainId;

    IF v_lengthCount > v_trainSize THEN
        RAISE exTrainLengthExceeded;
    END IF;

    -- get train start and end instants
    SELECT startDate, endDate
    INTO v_trainStartInstant, v_trainEndInstant
    FROM Train
    WHERE trainId = p_trainId;

    -- verify if wagons associated with the freight are available in the train period
    OPEN v_freight_wagons_cursor FOR
        SELECT wagonId
        FROM Wagon_Freight
        WHERE freightId = p_freightId;

    LOOP
    
        FETCH v_freight_wagons_cursor INTO v_wagonId;
        EXIT WHEN v_freight_wagons_cursor%NOTFOUND;

        SELECT COUNT(*)
        INTO v_invalidFreightsCount
        FROM Wagon_Freight wf
        JOIN Freight f ON f.freightId = wf.freightId
        JOIN Route r ON r.routeId = f.routeId
        JOIN Train t ON t.routeId = r.routeId
        WHERE wf.wagonId = v_wagonId
        AND f.freightId <> p_freightId
        AND t.startDate <= v_trainEndInstant
        AND t.endDate   >= v_trainStartInstant;

        IF v_invalidFreightsCount > 0 THEN
            CLOSE v_freight_wagons_cursor;
            RAISE exFoundWagonInvalidPeriod;
        END IF;

    END LOOP;

    CLOSE v_freight_wagons_cursor;

    UPDATE Freight
    SET routeId = v_trainRoute
    WHERE freightId = p_freightId;

EXCEPTION
    WHEN exNullTrainId THEN
        RAISE_APPLICATION_ERROR(-20010, 'Train ID is NULL!');

    WHEN exNullFreightId THEN
        RAISE_APPLICATION_ERROR(-20010, 'Freight ID is NULL!');

    WHEN exTrainIdNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'No train was found with the provided ID = ' || p_trainId || '!');

    WHEN exFreightIdNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'No freight was found with the provided ID = ' || p_freightId || '!');

    WHEN exTrainLengthExceeded THEN
        RAISE_APPLICATION_ERROR(-20010, 'Cannot associate the freight with the train because it would surpass the train maximum length!');

    WHEN exFoundWagonInvalidPeriod THEN
        RAISE_APPLICATION_ERROR(-20010, 'Cannot associate the freight with the train because one or more wagons are already in use during the train period ' || v_trainStartInstant || ' to ' || v_trainEndInstant || '!');
END;
/

-- ------------Tests------------

-- Null train ID
DECLARE
    trainId Train.trainId%TYPE := NULL;
    freightId Freight.freightId%TYPE := 1;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Null freight ID
DECLARE
    trainId Train.trainId%TYPE := 1;
    freightId Freight.freightId%TYPE := NULL;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Train ID not found
DECLARE
    trainId Train.trainId%TYPE := 420420;
    freightId Freight.freightId%TYPE := 1;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Freight ID not found
DECLARE
    trainId Train.trainId%TYPE := 701;
    freightId Freight.freightId%TYPE := 420333;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Max size exceeded
DECLARE
    trainId Train.trainId%TYPE := 703;
    freightId Freight.freightId%TYPE := 804;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Wagon in use during train period
DECLARE
    trainId Train.trainId%TYPE := 704;
    freightId Freight.freightId%TYPE := 805;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/

-- Valid test
DECLARE
    trainId Train.trainId%TYPE := 701;
    freightId Freight.freightId%TYPE := 804;
BEGIN
    assignFreightToTrain(trainId, freightId);
END;
/