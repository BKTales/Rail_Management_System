--USEDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE writeRoutePointToDB(
    p_routeId Route.routeId%TYPE,
    p_order Route_Point."order"%TYPE,
    p_facilityId Facility.facilityId%TYPE
)
IS
    v_foundRoute NUMBER;
    v_foundFacility NUMBER;

    exRouteNotFound EXCEPTION;
    exFacilityNotFound EXCEPTION;
BEGIN
    SELECT COUNT(*)
    INTO v_foundRoute
    FROM Route
    WHERE routeId = p_routeId;

    IF v_foundRoute = 0 THEN
        RAISE exRouteNotFound;
    END IF;

    SELECT COUNT(*)
    INTO v_foundFacility
    FROM Facility
    WHERE facilityId = p_facilityId;

    IF v_foundFacility = 0 THEN
        RAISE exFacilityNotFound;
    END IF;

    INSERT INTO Route_Point (routeId, "order", facilityId)
    VALUES (p_routeId, p_order, p_facilityId);

EXCEPTION
    WHEN exRouteNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'Route was not found!');

    WHEN exFacilityNotFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'Facility was not found!');
END;
/