CREATE OR REPLACE FUNCTION fn_register_wagon_model(
    p_make_id           IN Make.makeId%TYPE,
    p_wagon_type_id     IN Wagon_Type.wagonTypeId%TYPE,
    p_name              IN Wagon_Model.name%TYPE,
    p_length            IN Wagon_Model.length%TYPE,
    p_width             IN Wagon_Model.width%TYPE,
    p_height            IN Wagon_Model.height%TYPE,
    p_weight            IN Wagon_Model.weight%TYPE,
    p_payload           IN Wagon_Model.payload%TYPE,
    p_volume_capacity   IN Wagon_Model.volumeCapacity%TYPE,
    p_tare              IN Wagon_Model.tare%TYPE,
    p_max_speed         IN Wagon_Model.maxSpeed%TYPE,
    p_num_of_bogies     IN NUMBER,
    p_gauge_list        IN SYS.ODCIVARCHAR2LIST -- This a string list in sql..?
) RETURN Wagon_Model.wagonModelId%TYPE IS

    v_new_model_id  Wagon_Model.wagonModelId%TYPE;
    v_check_count   NUMBER;
    v_gauge_name    Gauge.gaugeName%TYPE;

BEGIN
SELECT COUNT(*) INTO v_check_count FROM Make WHERE makeId = p_make_id;
IF v_check_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error: Manufacturer (MakeID) not found.');
END IF;

SELECT COUNT(*) INTO v_check_count FROM Wagon_Type WHERE wagonTypeId = p_wagon_type_id;
IF v_check_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Error: Wagon Type ID not found.');
END IF;

    IF p_gauge_list IS NOT NULL AND p_gauge_list.COUNT > 0 THEN
        FOR i IN 1 .. p_gauge_list.COUNT LOOP
            v_gauge_name := p_gauge_list(i);

SELECT COUNT(*) INTO v_check_count FROM Gauge WHERE gaugeName = v_gauge_name;
IF v_check_count = 0 THEN
                RAISE_APPLICATION_ERROR(-20003, 'Error: Gauge "' || v_gauge_name || '" does not exist.');
END IF;
END LOOP;
ELSE
        RAISE_APPLICATION_ERROR(-20004, 'Error: At least one supported gauge must be provided.');
END IF;

    v_new_model_id := SEQ_WAGON_MODEL.NEXTVAL;

INSERT INTO Wagon_Model (
    wagonModelId, makeId, wagonTypeId, name, length, width, height,
    weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies
) VALUES (
             v_new_model_id, p_make_id, p_wagon_type_id, p_name, p_length, p_width, p_height,
             p_weight, p_payload, p_volume_capacity, p_tare, p_max_speed, p_num_of_bogies
         );

FOR i IN 1 .. p_gauge_list.COUNT LOOP
        INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName)
        VALUES (v_new_model_id, p_gauge_list(i));
END LOOP;

RETURN v_new_model_id;

EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END fn_register_wagon_model;
/

-- Test A: Success creation
SET SERVEROUTPUT ON;
DECLARE
v_returned_id     Wagon_Model.wagonModelId%TYPE;
    v_valid_make      Make.makeId%TYPE := 101;
    v_valid_type      Wagon_Type.wagonTypeId%TYPE := 1;
    v_test_name       Wagon_Model.name%TYPE := 'FreightMaster X1';

    v_gauges_to_add   SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST('Standard', 'Iberian');
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test A: Success creation ---');

    v_returned_id := fn_register_wagon_model(
        p_make_id           => v_valid_make,
        p_wagon_type_id     => v_valid_type,
        p_name              => v_test_name,
        p_length            => 14.5,
        p_width             => 2.9,
        p_height            => 3.8,
        p_weight            => 25.0,
        p_payload           => 60.0,
        p_volume_capacity   => 85.0,
        p_tare              => 25.0,
        p_max_speed         => 120,
        p_num_of_bogies     => 2,
        p_gauge_list        => v_gauges_to_add
    );

    DBMS_OUTPUT.PUT_LINE('Success! New Wagon Model ID: ' || v_returned_id);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test Failed: ' || SQLERRM);
END;
/

-- Test B: Empty Gauge List
DECLARE
v_returned_id     Wagon_Model.wagonModelId%TYPE;
    v_empty_list      SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST(); -- Initialized but empty
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test B: Empty Gauge List ---');

    v_returned_id := fn_register_wagon_model(
        p_make_id           => 101,
        p_wagon_type_id     => 1,
        p_name              => 'Ghost Wagon',
        p_length            => 10, p_width => 3, p_height => 3,
        p_weight            => 20, p_payload => 50, p_volume_capacity => 60,
        p_tare              => 20, p_max_speed => 100, p_num_of_bogies => 2,
        p_gauge_list        => v_empty_list -- Empty list
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: The function allowed a model with no gauges!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20004 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly blocked empty gauge list.');
ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
END IF;
END;
/

-- Test C: Unique Name Constraint
DECLARE
v_returned_id   Wagon_Model.wagonModelId%TYPE;
    v_test_name     Wagon_Model.name%TYPE := 'Unique_Name_Test';
    v_gauges        SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST('Standard');
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test C: Unique Name Constraint ---');

    v_returned_id := fn_register_wagon_model(
        p_make_id => 101, p_wagon_type_id => 1, p_name => v_test_name, -- First use of name
        p_length => 10, p_width => 3, p_height => 3, p_weight => 20, p_payload => 50,
        p_volume_capacity => 60, p_tare => 20, p_max_speed => 100, p_num_of_bogies => 2,
        p_gauge_list => v_gauges
    );
    DBMS_OUTPUT.PUT_LINE('First insertion successful (ID: ' || v_returned_id || ')');

    v_returned_id := fn_register_wagon_model(
        p_make_id => 101, p_wagon_type_id => 1, p_name => v_test_name, -- RE-USE same name
        p_length => 10, p_width => 3, p_height => 3, p_weight => 20, p_payload => 50,
        p_volume_capacity => 60, p_tare => 20, p_max_speed => 100, p_num_of_bogies => 2,
        p_gauge_list => v_gauges
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: The function allowed a duplicate name!');

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('PASSED: Unique Constraint violation caught (ORA-00001).');
WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
END;
/

-- Test D: Invalid Wagon Type
DECLARE
v_returned_id   Wagon_Model.wagonModelId%TYPE;
    v_invalid_type  Wagon_Type.wagonTypeId%TYPE := -99999; -- Non-existent ID
    v_gauges        SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST('Standard');
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test D: Invalid Wagon Type ---');

    v_returned_id := fn_register_wagon_model(
        p_make_id           => 101,
        p_wagon_type_id     => v_invalid_type,
        p_name              => 'Bad Type Wagon',
        p_length            => 10, p_width => 3, p_height => 3,
        p_weight            => 20, p_payload => 50, p_volume_capacity => 60,
        p_tare              => 20, p_max_speed => 100, p_num_of_bogies => 2,
        p_gauge_list        => v_gauges
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: The function allowed an invalid Wagon Type!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20002 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly caught invalid Wagon Type ID.');
ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
END IF;
END;
/