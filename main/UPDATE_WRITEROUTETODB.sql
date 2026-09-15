-- ============================================================================
-- UPDATE writeRouteToDB to include departureDate
-- This procedure must accept and store the departure time (departureDate)
-- 
-- IMPORTANT: Execute this script in your Oracle database to update the procedure
-- ============================================================================

-- Create or replace the procedure to include departureDate
-- Note: If your Route table doesn't have a departureDate column, you'll need to add it first:
-- ALTER TABLE Route ADD departureDate TIMESTAMP;
--USEDDDDDDD
CREATE OR REPLACE PROCEDURE writeRouteToDB(
    p_routeId IN NUMBER,
    p_startFacilityId IN NUMBER,
    p_endFacilityId IN NUMBER,
    p_departureDate IN TIMESTAMP
) AS
BEGIN
    INSERT INTO Route (routeId, startPoint, endPoint, departureDate)
    VALUES (p_routeId, p_startFacilityId, p_endFacilityId, p_departureDate);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END writeRouteToDB;
/

