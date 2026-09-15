--USEDDDDDDDDDD
CREATE OR REPLACE PROCEDURE updateTrainEndDate (p_trainId Train.trainId%TYPE, p_endDate Train.endDate%TYPE)

IS
BEGIN

UPDATE Train
SET Train.endDate = p_endDate
WHERE Train.trainId = p_trainId;
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/