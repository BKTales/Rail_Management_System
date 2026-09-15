-- USEDDDDDDDDDDD
CREATE OR REPLACE FUNCTION getRailSegmentsForLine(p_railLineId IN NUMBER)
RETURN SYS_REFCURSOR
IS
    segments_cursor SYS_REFCURSOR;
BEGIN
    OPEN segments_cursor FOR
    SELECT
        rs.segmentId,
        rs.railLineId,
        rs."order" AS segmentOrder,
        g.width AS gaugeWidth,
        rs.trackElectrification,
        rs.numberOfTracks,
        rs.length,
        rs.maxWeight,
        rs.speedLimit,
        s.startPosition,
        s.length AS sidingLength
    FROM Rail_Segment rs
             INNER JOIN Gauge g ON rs.gaugeName = g.gaugeName
             LEFT JOIN Siding s ON rs.sidingId = s.sidingId
    WHERE rs.railLineId = p_railLineId
    ORDER BY rs."order";

RETURN segments_cursor;
END getRailSegmentsForLine;
/
