--USEDDDDDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE insertLocomotiveTrain(p_trainId NUMBER, p_locomotiveId NUMBER)
IS
BEGIN

INSERT INTO Locomotive_Train (trainId, locomotiveId)
VALUES (p_trainId, p_locomotiveId);

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;

END;
/