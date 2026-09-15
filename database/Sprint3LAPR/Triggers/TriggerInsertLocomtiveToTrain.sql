CREATE OR REPLACE TRIGGER AddLocomotiveToTrainTrigger
    BEFORE INSERT ON Locomotive_Train
    FOR EACH ROW

DECLARE
    v_trainStartDate DATE;
    v_trainEndDate   DATE;

BEGIN
    -- get time interval of the train being assigned
SELECT Train.startDate, Train.endDate
    INTO   v_trainStartDate, v_trainEndDate
    FROM   Train
    WHERE  TrainId = :NEW.trainId;

-- check for overlapping assignments of the same locomotive
FOR l_line IN (SELECT trainId, startDate, endDate
                FROM Train WHERE locomotiveId = :NEW.locomotiveId)
    LOOP
        IF l_line.startDate <= v_trainEndDate AND l_line.endDate   >= v_trainStartDate  THEN
            RAISE_APPLICATION_ERROR(-20004, 'Locomotive already assigned in this time interval');
        END IF;
    END LOOP;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20005, 'Train not found for the provided trainId');
END;
/
