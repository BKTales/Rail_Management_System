--USEDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE cleanFreight(p_freightId NUMBER)

IS
BEGIN

DELETE FROM Wagon_Freight
WHERE freightId = p_freightId;

DELETE FROM Freight
WHERE freightId = p_freightId;


EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/