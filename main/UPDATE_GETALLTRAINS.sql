-- ============================================================================
-- UPDATE getAllTrainsCursor to return endDate
-- This function must return endDate (arrival time) in addition to trainId, startDate, routeId
-- 
-- IMPORTANT: Execute this script in your Oracle database to update the function
-- ============================================================================

-- Create or replace the function to return endDate (arrival time)
-- USEDDDDDDDDDDD
CREATE OR REPLACE FUNCTION getAllTrainsCursor RETURN SYS_REFCURSOR AS
    train_cursor SYS_REFCURSOR;
BEGIN
    OPEN train_cursor FOR
        SELECT trainId, startDate, endDate, routeId
        FROM Train
        ORDER BY startDate;
    RETURN train_cursor;
END getAllTrainsCursor;
/

