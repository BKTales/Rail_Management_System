CREATE OR REPLACE PROCEDURE updateRouteFreight(p_routeId Route.routeId%TYPE, p_freightId Freight.freightId%TYPE)
IS
BEGIN

UPDATE Freight
SET Freight.routeId = p_routeId
WHERE Freight.freightId = p_freightId;
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/