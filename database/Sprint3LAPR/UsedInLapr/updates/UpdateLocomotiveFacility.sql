CREATE OR REPLACE PROCEDURE updateLocomotivePosition (p_locomotiveId NUMBER, p_facilityId NUMBER)

BEGIN

UPDATE Locomotive
    SET Locomotive.startFacilityId = p_facilityId
    WHERE Locomotive.wagonId = p_wagonId;
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;