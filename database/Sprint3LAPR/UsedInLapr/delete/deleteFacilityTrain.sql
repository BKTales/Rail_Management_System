--USEDDDDDDDDD
CREATE OR REPLACE PROCEDURE deleteAllFacilityTrainToGivenId (p_trainId NUMBER)
IS
BEGIN

DELETE FROM Facility_Train
    WHERE Facility_Train.trainId = p_trainId;

EXCEPTION
    WHEN OTHERS THEN
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/