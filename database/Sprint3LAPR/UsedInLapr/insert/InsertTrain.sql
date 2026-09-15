--USEDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE addTrain(p_trainId NUMBER, p_routeId NUMBER, p_maxSize FLOAT, p_startDate DATE, p_endDate DATE)
IS

BEGIN

INSERT INTO Train (trainId,routeId,maxSize,startDate,endDate) VALUES (p_trainId, p_routeId, p_maxSize, p_startDate, p_endDate);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/