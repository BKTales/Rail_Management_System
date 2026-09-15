CREATE OR REPLACE PROCEDURE associateLocomotiveWTrain (
       p_locomotiveId NUMBER, p_trainId NUMBER )

IS

    v_trainStartDate DATE;
    v_trainEndDate   DATE;

BEGIN
    -- Get time interval of the train being assigned
    SELECT startDate, endDate INTO  v_trainStartDate, v_trainEndDate
        FROM Train
        WHERE trainId = :NEW.trainId;

    -- Check for overlapping assignments of the same locomotive
    FOR l_line IN (
            SELECT t.trainId, t.startDate, t.endDate
            FROM Train t
            INNER JOIN Locomotive_Train lt ON t.trainId = lt.trainId
            WHERE lt.locomotiveId = :NEW.locomotiveId )
        LOOP
            IF l_line.startDate <= v_trainEndDate AND l_line.endDate >= v_trainStartDate THEN
                RAISE_APPLICATION_ERROR(-20004, 'Locomotive already assigned in this time interval');
            END IF;
    END LOOP;

    INSERT INTO Locomotive_Train (locomotiveId, trainId)
                            VALUES (p_locomotiveId, p_trainId);
    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20005, 'Train not found for the provided trainId');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/



-- Test 1: Locomotive ID that does not exist (9999) - Should fail with FK constraint
BEGIN
    associateLocomotiveWTrain(9999, 701);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 2: Train ID that does not exist (9999) - Should fail with FK constraint
BEGIN
    associateLocomotiveWTrain(3002, 9999);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 3: Both Locomotive and Train IDs do not exist - Should fail with FK constraint
BEGIN
    associateLocomotiveWTrain(9999, 9999);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 4: Duplicate association - Should fail with UNIQUE/PK constraint
BEGIN
    associateLocomotiveWTrain(5601, 701);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 5a: NULL locomotiveId - Should fail with NOT NULL constraint
BEGIN
    associateLocomotiveWTrain(NULL, 701);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 5b: NULL trainId - Should fail with NOT NULL constraint
BEGIN
    associateLocomotiveWTrain(3002, NULL);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 5c: Both parameters NULL - Should fail with NOT NULL constraint
BEGIN
    associateLocomotiveWTrain(NULL, NULL);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 6a: Valid association - Locomotive 3002 with Train 701
BEGIN
    associateLocomotiveWTrain(3002, 701);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Test 6b: Valid association - Locomotive 3002 with Train 702
BEGIN
    associateLocomotiveWTrain(3002, 702);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/



-- Test 6c: Valid association - Assuming we add another locomotive first
-- First, insert a new locomotive for testing (locomotive 3003)
-- Then associate it with train 701
DECLARE
v_locomotive_exists NUMBER;
BEGIN
    -- Check if locomotive 3003 exists, if not create it for testing
SELECT COUNT(*) INTO v_locomotive_exists FROM Locomotive WHERE locomotiveId = 3003;

IF v_locomotive_exists = 0 THEN
        INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
        VALUES (3003, 'PT507832388', 6, 8, 2021);
COMMIT;
END IF;

    -- Now associate the locomotive with train 702
    associateLocomotiveWTrain(3003, 702);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/