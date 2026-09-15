--USEDDDDDDDDDDD
CREATE OR REPLACE FUNCTION getRailLines
RETURN SYS_REFCURSOR
AS
    railLine_cursor SYS_REFCURSOR;

BEGIN
    OPEN railLine_cursor FOR
        SELECT * FROM Rail_Line;

    RETURN railLine_cursor;
END;
/
