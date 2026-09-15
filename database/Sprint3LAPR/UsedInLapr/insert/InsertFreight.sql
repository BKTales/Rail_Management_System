--USEDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE addFreight(p_freightId NUMBER, p_startPoint NUMBER, p_endPoint NUMBER)
IS
BEGIN

INSERT INTO Freight (freightID, startPoint, endPoint) VALUES (p_freightId, p_startPoint, p_endPoint);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/