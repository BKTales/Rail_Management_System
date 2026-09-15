--USEDDDDDDDDDDDDDDD
CREATE OR REPLACE FUNCTION getFacilityByLocomotive(p_locomotiveId IN NUMBER)
RETURN NUMBER
IS
    v_facilityId NUMBER;
BEGIN
SELECT startFacilityId
INTO v_facilityId
FROM Locomotive
WHERE locomotiveId = p_locomotiveId;

RETURN v_facilityId;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
WHEN OTHERS THEN
        RAISE;
END;
/