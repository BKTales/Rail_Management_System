--USEDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE deleteAllTimeInSegmentToGivenId (p_trainId NUMBER)
IS
BEGIN

DELETE FROM Time_In_Segment
    WHERE Time_In_Segment.trainId = p_trainId;

EXCEPTION
    WHEN OTHERS THEN
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/