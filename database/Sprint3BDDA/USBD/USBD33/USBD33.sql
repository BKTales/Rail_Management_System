CREATE OR REPLACE TRIGGER trg_check_train_length
BEFORE INSERT OR UPDATE ON Train
FOR EACH ROW

DECLARE
    v_locomotivesLength FLOAT := 0;
    v_wagonsLength FLOAT := 0;
    v_totalLength FLOAT := 0;

BEGIN
        -- Get total length of all locomotives assigned to this train
    SELECT SUM(lm.length)
    INTO v_locomotivesLength
    FROM Locomotive_Train lt
             INNER JOIN Locomotive l ON l.locomotiveId = lt.locomotiveId
             INNER JOIN Locomotive_Model lm ON lm.locomotiveModelId = l.locomotiveModelId
    WHERE lt.trainId = :NEW.trainId;

    -- Get total length of all wagons in freights on this train's route
    SELECT SUM(wm.length)
    INTO v_wagonsLength
    FROM Freight f
             INNER JOIN Wagon_Freight wf ON wf.freightId = f.freightId
             INNER JOIN Wagon w ON w.wagonId = wf.wagonId
             INNER JOIN Wagon_Model wm ON wm.wagonModelId = w.wagonModelId
    WHERE f.routeId = :NEW.routeId;

    -- Handle NULL cases (when no locomotives or wagons exist)
    IF v_locomotivesLength IS NULL THEN
            v_locomotivesLength := 0;
    END IF;

        IF v_wagonsLength IS NULL THEN
            v_wagonsLength := 0;
    END IF;

        -- Calculate total length
        v_totalLength := v_locomotivesLength + v_wagonsLength;

        -- Check if total length exceeds maxSize
        IF v_totalLength > :NEW.maxSize THEN
            RAISE_APPLICATION_ERROR(-20005,
                'Train length exceeds maximum allowed size. ' ||
                'Total: ' || v_totalLength || ' m, ' ||
                'Max allowed: ' || :NEW.maxSize || ' m ' ||
                '(Locomotives: ' || v_locomotivesLength || ' m, ' ||
                'Wagons: ' || v_wagonsLength || ' m)');
    END IF;
END;
/