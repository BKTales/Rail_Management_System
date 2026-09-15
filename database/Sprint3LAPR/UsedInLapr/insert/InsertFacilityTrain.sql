--USEDDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE addFacilityTrain (p_facilityId NUMBER, p_trainId NUMBER, p_arriveTime TIMESTAMP, p_leaveTime TIMESTAMP)

IS
BEGIN

INSERT INTO Facility_Train (facilityId, trainId, arriveTime, leaveTime) VALUES (p_facilityId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/