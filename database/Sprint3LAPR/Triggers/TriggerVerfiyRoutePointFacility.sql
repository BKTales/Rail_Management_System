CREATE OR REPLACE TRIGGER verifyRoutePointFacility
BEFORE INSERT ON Route_Point
FOR EACH ROW
DECLARE
    v_foundFacility NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_foundFacility
    FROM Route_Point
    WHERE routeId = :new.routeId AND facilityId = :new.facilityId;

    IF v_foundFacility <> 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'The facility with id ' || :new.facilityId || ' is already present in the provided route, please insert a different facility!');
    END IF;
END;
/