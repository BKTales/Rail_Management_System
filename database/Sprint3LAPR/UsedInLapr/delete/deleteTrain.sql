--USEDDDDDDDDDDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE cleanTrain(p_trainId NUMBER)
IS
BEGIN

DELETE FROM Locomotive_Train
WHERE trainId = p_trainId;

DELETE FROM Time_In_Segment
WHERE trainId = p_trainId;

DELETE FROM Facility_Train
WHERE trainId = p_trainId;

DELETE FROM Train
WHERE trainId = p_trainId;


EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/