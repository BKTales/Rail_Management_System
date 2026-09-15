-- USEDDDDDDDDDDD
CREATE OR REPLACE FUNCTION getAllFacilities
RETURN SYS_REFCURSOR
IS
    fac_cursor SYS_REFCURSOR;
BEGIN
    OPEN fac_cursor FOR
        SELECT 
            facilityId,
            name,
            latitude,
            longitude
        FROM Facility
        ORDER BY facilityId;
    
    RETURN fac_cursor;
END getAllFacilities;
/

