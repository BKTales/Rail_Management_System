CREATE OR REPLACE FUNCTION getEndpointList(routeId_ NUMBER := NULL) 
RETURN SYS_REFCURSOR
IS
    exNullRouteId EXCEPTION;
    exInvalidRouteId EXCEPTION;
    exRouteIdNotFound EXCEPTION;

    r_count INTEGER;
    endPointList SYS_REFCURSOR;
BEGIN
    IF routeId_ IS NULL THEN
        RAISE exNullRouteId;
    END IF;

    IF routeId_ < 0 THEN
        RAISE exInvalidRouteId;
    END IF;

    -- Check if there's any Route with the provided ID
    SELECT COUNT(*)
    INTO r_count
    FROM Route
    WHERE Route.routeId = routeId_;

    IF r_count = 0 THEN
        RAISE exRouteIdNotFound;
    END IF;

    OPEN endPointList FOR
        SELECT DISTINCT Facility.name, Facility.facilityId
        FROM Facility
        INNER JOIN Path_Point ON Path_Point.facilityId = Facility.facilityId
        INNER JOIN Path ON Path.pathId = Path_Point.pathId
        INNER JOIN Route ON Route.pathId = Path.pathId
        WHERE Route.routeId = routeId_;

    RETURN endPointList;

EXCEPTION
    WHEN exNullRouteId THEN
        dbms_output.put_line('The provided route id is NULL. Please insert a valid route ID!');
        RETURN NULL;

    WHEN exInvalidRouteId THEN
        dbms_output.put_line('The provided route id is invalid. Please insert a valid route ID!');
        RETURN NULL;

    WHEN exRouteIdNotFound THEN
        dbms_output.put_line('There was no route found with the provided ID!');
        RETURN NULL;
END;
/

-- ------------Tests------------

-- Should raise route null exception
DECLARE
    routeId Route.routeId%TYPE := NULL;
    res SYS_REFCURSOR;
BEGIN
    res := getEndpointList(routeId);
END;
/

-- Should raise invalid route id exception
DECLARE
    routeId Route.routeId%TYPE := -1;
    res SYS_REFCURSOR;
BEGIN
    res := getEndpointList(routeId);
END;
/

-- Should raise route not found exception
DECLARE
    routeId Route.routeId%TYPE := 5;
    res SYS_REFCURSOR;
BEGIN
    res := getEndpointList(routeId);
END;
/

DECLARE
    routeId Route.routeId%TYPE := 1;
    facilityName Facility.name%TYPE;
    facilityId Facility.facilityId%TYPE;

    res SYS_REFCURSOR;
BEGIN
    res := getEndpointList(routeId);

    IF res IS NOT NULL THEN
        dbms_output.put_line('--- List of endpoints for route ID: ' || routeId || ' ---');
        LOOP
            FETCH res INTO facilityName, facilityId;
            EXIT WHEN res%NOTFOUND;
            dbms_output.put_line('Facility ID: ' || facilityId || ' | Name - ' || facilityName);
        END LOOP;
        CLOSE res;
    ELSE
        dbms_output.put_line('There was an error retriving the list of endpoints associated with the routeID: ' || routeId || '!');
    END IF;

END;
/