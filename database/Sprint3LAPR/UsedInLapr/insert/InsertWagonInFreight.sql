--USEDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE addWagonToFreight (p_freightId NUMBER, p_wagonId NUMBER)
IS
BEGIN

INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (p_wagonId, p_freightId);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/