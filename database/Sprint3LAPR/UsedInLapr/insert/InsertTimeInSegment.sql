--USEDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE addTimeInSegment (p_railSegmentId NUMBER, p_trainId NUMBER, p_arriveTime TIMESTAMP, p_leaveTime TIMESTAMP)

IS
BEGIN

INSERT INTO Time_In_Segment (railSegmentId, trainId, arriveTime, leaveTime) VALUES (p_railSegmentId, p_trainId, p_arriveTime, p_leaveTime);
COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE; -- will raise all the row errors that are defined in the table(UNIQUE, FOREIGN KEY, NOT NULL)
END;
/