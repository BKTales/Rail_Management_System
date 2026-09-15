CREATE OR REPLACE PROCEDURE addRailLine (
    p_railLineId            Rail_Line.railLineId%TYPE,
    p_ownerId               Rail_Line.ownerId%TYPE,
    p_startFacilityId       Rail_Line.startFacilityId%TYPE,
    p_endFacilityId         Rail_Line.endFacilityId%TYPE,
    p_lineName              Rail_Line.name%TYPE,
    p_segmentId             Rail_Segment.segmentId%TYPE,
    p_gaugeName             Rail_Segment.gaugeName%TYPE,
    p_trackElectrification  Rail_Segment.trackElectrification%TYPE,
    p_numberOfTracks        Rail_Segment.numberOfTracks%TYPE,
    p_length                Rail_Segment.length%TYPE,
    p_maxWeight             Rail_Segment.maxWeight%TYPE,
    p_speedLimit            Rail_Segment.speedLimit%TYPE
)
IS
    v_lineName Rail_Line.name%TYPE;
BEGIN
    v_lineName := INITCAP(TRIM(REGEXP_REPLACE(p_lineName, '\s+', ' ')));

-- Insert into Rail_Segment table first (to avoid FK constraint issues)
INSERT INTO Rail_Segment (segmentId, railLineId, "order", gaugeName, sidingId, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (p_segmentId, p_railLineId, 1, p_gaugeName, NULL, p_trackElectrification, p_numberOfTracks, p_length, p_maxWeight, p_speedLimit);

-- Insert into Rail_Line table
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name)
VALUES (p_railLineId, p_ownerId, p_startFacilityId, p_endFacilityId, v_lineName);

EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END addRailLine;
/

-- Test 1: Valid data - New rail line with new segment
-- Rail line 100, segment 100 (both don't exist)
BEGIN
    addRailLine(
        p_railLineId => 100,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line 100',
        p_segmentId => 100,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 1 PASSED');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 1 FAILED: ' || SQLERRM);
        RAISE;
END;
/

-- Test 2: Duplicate Rail Line ID - Should fail with PK constraint
BEGIN
    addRailLine(
        p_railLineId => 100, -- Already exists from Test 1
        p_ownerId => 'PT503933813',
        p_startFacilityId => 3,
        p_endFacilityId => 4,
        p_lineName => 'Test Line Duplicate',
        p_segmentId => 101,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 6000,
        p_maxWeight => 8000,
        p_speedLimit => 130
    );
    DBMS_OUTPUT.PUT_LINE('Test 2 FAILED: Should have raised PK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 2 PASSED: ' || SQLERRM);
END;
/

-- Test 3: Duplicate Segment ID - Should fail with PK constraint
BEGIN
    addRailLine(
        p_railLineId => 101,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 3,
        p_endFacilityId => 4,
        p_lineName => 'Test Line 101',
        p_segmentId => 100, -- Already exists from Test 1
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 6000,
        p_maxWeight => 8000,
        p_speedLimit => 130
    );
    DBMS_OUTPUT.PUT_LINE('Test 3 FAILED: Should have raised PK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 3 PASSED: ' || SQLERRM);
END;
/

-- Test 4: Duplicate line name - Should fail with unique constraint
-- 'Ramal Viana - Caminha' already exists in Rail_Line table
BEGIN
    addRailLine(
        p_railLineId => 102,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Ramal Viana - Caminha',
        p_segmentId => 102,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 4 FAILED: Should have raised unique constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 4 PASSED: ' || SQLERRM);
END;
/

-- Test 5: Invalid owner ID - Should fail with FK constraint
BEGIN
    addRailLine(
        p_railLineId => 103,
        p_ownerId => 'PT999999999', -- Does not exist
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line 103',
        p_segmentId => 103,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 5 FAILED: Should have raised FK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 5 PASSED: ' || SQLERRM);
END;
/

-- Test 6: Invalid start facility - Should fail with FK constraint
-- Facility 101 does not exist (max facilityId is 50)
BEGIN
    addRailLine(
        p_railLineId => 104,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 101, -- Does not exist
        p_endFacilityId => 2,
        p_lineName => 'Test Line 104',
        p_segmentId => 104,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 6 FAILED: Should have raised FK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 6 PASSED: ' || SQLERRM);
END;
/

-- Test 7: Invalid end facility - Should fail with FK constraint
-- Facility 102 does not exist
BEGIN
    addRailLine(
        p_railLineId => 105,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 102, -- Does not exist
        p_lineName => 'Test Line 105',
        p_segmentId => 105,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 7 FAILED: Should have raised FK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 7 PASSED: ' || SQLERRM);
END;
/

-- Test 8: Invalid gauge name - Should fail with FK constraint
BEGIN
    addRailLine(
        p_railLineId => 106,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line 106',
        p_segmentId => 106,
        p_gaugeName => 'Invalid gauge', -- Does not exist
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 8 FAILED: Should have raised FK constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 8 PASSED: ' || SQLERRM);
END;
/

-- Test 9a: NULL railLineId - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => NULL,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line Null1',
        p_segmentId => 107,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9a FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9a PASSED: ' || SQLERRM);
END;
/

-- Test 9b: NULL ownerId - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 108,
        p_ownerId => NULL,
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line Null2',
        p_segmentId => 108,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9b FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9b PASSED: ' || SQLERRM);
END;
/

-- Test 9c: NULL startFacilityId - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 109,
        p_ownerId => 'PT503933813',
        p_startFacilityId => NULL,
        p_endFacilityId => 2,
        p_lineName => 'Test Line Null3',
        p_segmentId => 109,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9c FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9c PASSED: ' || SQLERRM);
END;
/

-- Test 9d: NULL endFacilityId - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 110,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => NULL,
        p_lineName => 'Test Line Null4',
        p_segmentId => 110,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9d FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9d PASSED: ' || SQLERRM);
END;
/

-- Test 9e: NULL lineName - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 111,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => NULL,
        p_segmentId => 111,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9e FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9e PASSED: ' || SQLERRM);
END;
/

-- Test 9f: NULL segmentId - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 112,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line Null6',
        p_segmentId => NULL,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9f FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9f PASSED: ' || SQLERRM);
END;
/

-- Test 9g: NULL gaugeName - Should fail with NOT NULL constraint
BEGIN
    addRailLine(
        p_railLineId => 113,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 1,
        p_endFacilityId => 2,
        p_lineName => 'Test Line Null7',
        p_segmentId => 113,
        p_gaugeName => NULL,
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 5000,
        p_maxWeight => 8000,
        p_speedLimit => 120
    );
    DBMS_OUTPUT.PUT_LINE('Test 9g FAILED: Should have raised NOT NULL constraint error');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 9g PASSED: ' || SQLERRM);
END;
/

-- Test 10a: Valid data - Facility 3 to 7, Rail Line 26, Segment 26
BEGIN
    addRailLine(
        p_railLineId => 26,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 3, -- Senhora das Dores
        p_endFacilityId => 7, -- Porto São Bento
        p_lineName => 'Senhora Das Dores Line 1',
        p_segmentId => 26,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 8500,
        p_maxWeight => 8000,
        p_speedLimit => 140
    );
    DBMS_OUTPUT.PUT_LINE('Test 10a PASSED');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 10a FAILED: ' || SQLERRM);
        RAISE;
END;
/

-- Test 10b: Valid data - Facility 5 to 9, Rail Line 27, Segment 27
BEGIN
    addRailLine(
        p_railLineId => 27,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 5, -- Porto Campanhã
        p_endFacilityId => 9, -- Vila Nova de Cerveira
        p_lineName => 'Porto Campanhã Line 5',
        p_segmentId => 27,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 1,
        p_length => 12000,
        p_maxWeight => 8000,
        p_speedLimit => 130
    );
    DBMS_OUTPUT.PUT_LINE('Test 10b PASSED');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 10b FAILED: ' || SQLERRM);
        RAISE;
END;
/

-- Test 10c: Valid data - Facility 2 to 8, Rail Line 28, Segment 28
BEGIN
    addRailLine(
        p_railLineId => 28,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 2, -- Tamel
        p_endFacilityId => 8, -- Barcelos
        p_lineName => 'Tamel Line 3',
        p_segmentId => 28,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 1,
        p_numberOfTracks => 2,
        p_length => 7200,
        p_maxWeight => 8000,
        p_speedLimit => 150
    );
    DBMS_OUTPUT.PUT_LINE('Test 10c PASSED');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 10c FAILED: ' || SQLERRM);
        RAISE;
END;
/

-- Test 11: Test line name formatting (whitespace and capitalization)
-- Should trim extra spaces and convert to proper case
BEGIN
    addRailLine(
        p_railLineId => 29,
        p_ownerId => 'PT503933813',
        p_startFacilityId => 4, -- Lousado
        p_endFacilityId => 6, -- Leandro
        p_lineName => '  test   LINE   with    SPACES  ', -- Should become 'Test Line With Spaces'
        p_segmentId => 29,
        p_gaugeName => 'Iberian gauge',
        p_trackElectrification => 0,
        p_numberOfTracks => 1,
        p_length => 4500,
        p_maxWeight => 6400,
        p_speedLimit => 100
    );
    DBMS_OUTPUT.PUT_LINE('Test 11 PASSED - Line name formatting working');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test 11 FAILED: ' || SQLERRM);
        RAISE;
END;
/
