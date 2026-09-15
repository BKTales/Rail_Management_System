CREATE OR REPLACE FUNCTION getFacilitiesWithGrainSilosButNoWarehouses
RETURN SYS_REFCURSOR
IS
    facilities_cursor SYS_REFCURSOR;
BEGIN
    OPEN facilities_cursor FOR
        SELECT facilityId, name
        FROM Facility
        WHERE hasGrainSilo = 1 AND hasWarehouse = 0
        ORDER BY facilityId;

    RETURN facilities_cursor;
END;
/

DECLARE
    facilityId Facility.facilityId%TYPE;
    name Facility.name%TYPE;
    cont NUMBER := 0;

    res SYS_REFCURSOR;
BEGIN
    res := getFacilitiesWithGrainSilosButNoWarehouses();

    IF res IS NOT NULL THEN
        dbms_output.put_line('--- List of facilties with grain silos but no warehouses ---');
        LOOP
            FETCH res INTO facilityId, name;
            EXIT WHEN res%NOTFOUND;
            dbms_output.put_line('Facility ID: ' || facilityId || ' | Name - ' || name);
            cont := cont + 1;
        END LOOP;
        IF cont = 0 THEN
            dbms_output.put_line('WARNING - No facilities were found!');
        END IF;
        CLOSE res;
    ELSE
        dbms_output.put_line('There was an error retriving the list of facilities with grain silos but no warehouses!');
    END IF;

END;
/