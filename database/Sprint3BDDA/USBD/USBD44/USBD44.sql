CREATE OR REPLACE PROCEDURE addSegmentToLine(
    p_segmentId Rail_Segment.segmentId%TYPE,

    p_railLineId Rail_Segment.railLineId%TYPE,
    p_gaugeName Rail_Segment.gaugeName%TYPE,
    p_sidingId Rail_Segment.sidingId%TYPE,
    
    p_order Rail_Segment."order"%TYPE,
    p_trackElectrification Rail_Segment.trackElectrification%TYPE,
    p_numberOfTracks Rail_Segment.numberOfTracks%TYPE,
    p_length Rail_Segment.length%TYPE,
    p_maxWeight Rail_Segment.maxWeight%TYPE,
    p_speedLimit Rail_Segment.speedLimit%TYPE
)
IS
    v_foundSiding NUMBER;
    v_foundSegmentInLine NUMBER;

    exSidingAlreadyFound EXCEPTION;
    exInvalidOrder EXCEPTION;
BEGIN
    IF p_sidingId IS NOT NULL THEN
        SELECT COUNT(*)
        INTO v_foundSiding
        FROM Rail_Segment
        WHERE sidingId = p_sidingId;

        IF v_foundSiding <> 0 THEN
            RAISE exSidingAlreadyFound;
        END IF;
    END IF;

    SELECT COUNT(*)
    INTO v_foundSegmentInLine
    FROM Rail_Segment
    WHERE railLineId = p_railLineId AND "order" = p_order;

    IF v_foundSegmentInLine <> 0 THEN
        RAISE exInvalidOrder;
    END IF;

    INSERT INTO Rail_Segment (segmentId, railLineId, gaugeName, sidingId, "order", trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
    VALUES (
        p_segmentId,

        p_railLineId,
        p_gaugeName,
        p_sidingId,
        
        p_order,
        p_trackElectrification,
        p_numberOfTracks,
        p_length,
        p_maxWeight,
        p_speedLimit
    );

EXCEPTION
    WHEN exSidingAlreadyFound THEN
        RAISE_APPLICATION_ERROR(-20010, 'The provided siding was already found in an existing segment!');

    WHEN exInvalidOrder THEN
        RAISE_APPLICATION_ERROR(-20010, 'There already exists a segment in the line (ID: ' || p_railLineId || ') with order ' || p_order || '!');
END;
/

-- ------------Tests------------

-- siding already found
DECLARE
    segmentId Rail_Segment.segmentId%TYPE := 1000;
    v_order Rail_Segment."order"%TYPE := 3;

    railLineId Rail_Segment.railLineId%TYPE := 1;
    gaugeName Rail_Segment.gaugeName%TYPE := 'Iberian gauge';
    sidingId Rail_Segment.sidingId%TYPE := 1;

    trackElectrification Rail_Segment.trackElectrification%TYPE := 1;
    numberOfTracks Rail_Segment.numberOfTracks%TYPE := 2;
    length Rail_Segment.length%TYPE := 10;
    maxWeight Rail_Segment.maxWeight%TYPE := 10;
    speedLimit Rail_Segment.speedLimit%TYPE := 10;

BEGIN
    addSegmentToLine(
        segmentId,
        railLineId,
        gaugeName,
        sidingId,
        v_order,
        trackElectrification,
        numberOfTracks,
        length,
        maxWeight,
        speedLimit
    );
END;
/

-- order already found
DECLARE
    segmentId Rail_Segment.segmentId%TYPE := 2000;
    v_order Rail_Segment."order"%TYPE := 1;

    railLineId Rail_Segment.railLineId%TYPE := 1;
    gaugeName Rail_Segment.gaugeName%TYPE := 'Iberian gauge';
    sidingId Rail_Segment.sidingId%TYPE := NULL;

    trackElectrification Rail_Segment.trackElectrification%TYPE := 1;
    numberOfTracks Rail_Segment.numberOfTracks%TYPE := 2;
    length Rail_Segment.length%TYPE := 10;
    maxWeight Rail_Segment.maxWeight%TYPE := 10;
    speedLimit Rail_Segment.speedLimit%TYPE := 10;

BEGIN
    addSegmentToLine(
        segmentId,
        railLineId,
        gaugeName,
        sidingId,
        v_order,
        trackElectrification,
        numberOfTracks,
        length,
        maxWeight,
        speedLimit
    );
END;
/

-- valid segment with null siding (valid)
DECLARE
    segmentId Rail_Segment.segmentId%TYPE := 3000;
    v_order Rail_Segment."order"%TYPE := 4;

    railLineId Rail_Segment.railLineId%TYPE := 1;
    gaugeName Rail_Segment.gaugeName%TYPE := 'Iberian gauge';
    sidingId Rail_Segment.sidingId%TYPE := NULL;

    trackElectrification Rail_Segment.trackElectrification%TYPE := 1;
    numberOfTracks Rail_Segment.numberOfTracks%TYPE := 2;
    length Rail_Segment.length%TYPE := 10;
    maxWeight Rail_Segment.maxWeight%TYPE := 10;
    speedLimit Rail_Segment.speedLimit%TYPE := 10;

BEGIN
    addSegmentToLine(
        segmentId,
        railLineId,
        gaugeName,
        sidingId,
        v_order,
        trackElectrification,
        numberOfTracks,
        length,
        maxWeight,
        speedLimit
    );
END;
/


DECLARE
    segmentId1 Rail_Segment.segmentId%TYPE := 4000;
    segmentId2 Rail_Segment.segmentId%TYPE := 5000;
    v_order1 Rail_Segment."order"%TYPE := 5;
    v_order2 Rail_Segment."order"%TYPE := 6;

    railLineId Rail_Segment.railLineId%TYPE := 1;
    gaugeName Rail_Segment.gaugeName%TYPE := 'Iberian gauge';
    sidingId1 Rail_Segment.sidingId%TYPE := 23;
    sidingId2 Rail_Segment.sidingId%TYPE := 24;

    trackElectrification Rail_Segment.trackElectrification%TYPE := 1;
    numberOfTracks Rail_Segment.numberOfTracks%TYPE := 2;
    length Rail_Segment.length%TYPE := 10;
    maxWeight Rail_Segment.maxWeight%TYPE := 10;
    speedLimit Rail_Segment.speedLimit%TYPE := 10;

BEGIN
    addSegmentToLine(
        segmentId1,
        railLineId,
        gaugeName,
        sidingId1,
        v_order1,
        trackElectrification,
        numberOfTracks,
        length,
        maxWeight,
        speedLimit
    );

    addSegmentToLine(
        segmentId2,
        railLineId,
        gaugeName,
        sidingId2,
        v_order2,
        trackElectrification,
        numberOfTracks,
        length,
        maxWeight,
        speedLimit
    );
END;
/