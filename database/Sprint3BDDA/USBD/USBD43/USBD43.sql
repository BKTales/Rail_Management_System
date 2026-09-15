CREATE OR REPLACE FUNCTION fn_create_electric_model(
    p_make_id       IN Make.makeId%TYPE,
    p_name          IN Locomotive_Model.name%TYPE,
    p_weight        IN Locomotive_Model.weight%TYPE,
    p_power         IN Locomotive_Model.power%TYPE,
    p_acceleration  IN Locomotive_Model.acceleration%TYPE,
    p_length        IN Dimension.length%TYPE,
    p_width         IN Dimension.width%TYPE,
    p_height        IN Dimension.height%TYPE,
    p_voltage       IN Electric_Locomotive_Model.voltage%TYPE,
    p_frequency     IN Electric_Locomotive_Model.frequency%TYPE,
    p_gauge_name    IN Gauge.gaugeName%TYPE,
    p_bogie_name    IN Bogie.name%TYPE,
    p_num_of_bogies IN Locomotive_Model.numOfBogies%TYPE,
    p_max_speed     IN Locomotive_Model.maxSpeed%TYPE -- Added Parameter
) RETURN Locomotive_Model.locomotiveModelId%TYPE IS

    v_new_model_id   Locomotive_Model.locomotiveModelId%TYPE;
    v_check_count    NUMBER;

    v_stored_length  Dimension.length%TYPE;
    v_stored_width   Dimension.width%TYPE;
    v_stored_height  Dimension.height%TYPE;

    e_make_not_found  EXCEPTION;
    e_gauge_not_found EXCEPTION;
    e_bogie_not_found EXCEPTION;

BEGIN
    SELECT COUNT(*) INTO v_check_count FROM Make WHERE makeId = p_make_id;
    IF v_check_count = 0 THEN
        RAISE e_make_not_found;
    END IF;

    SELECT COUNT(*) INTO v_check_count FROM Gauge WHERE gaugeName = p_gauge_name;
    IF v_check_count = 0 THEN
        RAISE e_gauge_not_found;
    END IF;

    SELECT COUNT(*) INTO v_check_count FROM Bogie WHERE name = p_bogie_name;
    IF v_check_count = 0 THEN
        RAISE e_bogie_not_found;
    END IF;

    BEGIN
        INSERT INTO Dimension (length, width, height)
        VALUES (p_length, p_width, p_height);
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            NULL; 
    END;

    SELECT length, width, height
    INTO v_stored_length, v_stored_width, v_stored_height
    FROM Dimension
    WHERE length = p_length
      AND width = p_width
      AND height = p_height
      AND ROWNUM = 1;

    INSERT INTO Locomotive_Model (
        makeId, name, weight, power, acceleration, length, width, height, bogieName, numOfBogies, maxSpeed
    ) VALUES (
        p_make_id, p_name, p_weight, p_power, p_acceleration,
        v_stored_length, v_stored_width, v_stored_height, p_bogie_name, p_num_of_bogies, p_max_speed
    ) RETURNING locomotiveModelId INTO v_new_model_id;

    INSERT INTO Electric_Locomotive_Model (
        locomotiveModelId, voltage, frequency
    ) VALUES (
        v_new_model_id, p_voltage, p_frequency
    );

    INSERT INTO Locomotive_Model_Gauge (
        locomotiveModelId, gaugeName
    ) VALUES (
        v_new_model_id, p_gauge_name
    );

    RETURN v_new_model_id;

EXCEPTION
    WHEN e_make_not_found THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error: Manufacturer (MakeID) not found.');
    
    WHEN e_gauge_not_found THEN
        RAISE_APPLICATION_ERROR(-20002, 'Error: Gauge "' || p_gauge_name || '" not found.');

    WHEN e_bogie_not_found THEN
        RAISE_APPLICATION_ERROR(-20003, 'Error: Bogie "' || p_bogie_name || '" not found.');

    WHEN OTHERS THEN
        RAISE;
END fn_create_electric_model;
/

BEGIN
    BEGIN
        INSERT INTO Make (name) VALUES ('Siemens Mobility');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Gauge (gaugeName, width) VALUES ('Iberian gauge', 1668);
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Gauge (gaugeName, width) VALUES ('Standart gauge', 1435);
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Bogie (name) VALUES ('Flexx Power');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    COMMIT;
END;
/

SET SERVEROUTPUT ON;

-- TEST 1: SUCCESS CASE
DECLARE
    v_returned_id  NUMBER;
    v_make_id      NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test A: Success creation ---');

    SELECT makeId INTO v_make_id FROM Make WHERE name = 'Siemens Mobility' AND ROWNUM = 1;

    v_returned_id := fn_create_electric_model(
        p_make_id       => v_make_id,
        p_name          => 'EuroSprinter ES64',
        p_weight        => 90,
        p_power         => 6400,
        p_acceleration  => 1.2,
        p_length        => 20.5,
        p_width         => 3.0,
        p_height        => 4.1,
        p_voltage       => 25000,
        p_frequency     => 50,
        p_gauge_name    => 'Iberian gauge',
        p_bogie_name    => 'Flexx Power',
        p_num_of_bogies => 2,
        p_max_speed     => 230 
    );

    DBMS_OUTPUT.PUT_LINE('SUCCESS: Model created successfully.');
    DBMS_OUTPUT.PUT_LINE('New ID: ' || v_returned_id);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('FAILED: ' || SQLERRM);
END;
/

-- TEST 2: INVALID MANUFACTURER
DECLARE
    v_returned_id NUMBER;
    v_invalid_make_id NUMBER := -999;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test B: Invalid Manufacturer ---');

    v_returned_id := fn_create_electric_model(
        p_make_id       => v_invalid_make_id,
        p_name          => 'Invalid Make Test',
        p_weight        => 100,
        p_power         => 5000,
        p_acceleration  => 2,
        p_length        => 19,
        p_width         => 3,
        p_height        => 4,
        p_voltage       => 25000,
        p_frequency     => 50,
        p_gauge_name    => 'Iberian gauge',
        p_bogie_name    => 'Flexx Power',
        p_num_of_bogies => 2,
        p_max_speed     => 140
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed invalid Make ID.');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20001 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Caught error -20001 (Invalid Make).');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/

-- TEST 3: INVALID GAUGE
DECLARE
    v_returned_id NUMBER;
    v_make_id     NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test C: Invalid Gauge ---');

    SELECT makeId INTO v_make_id FROM Make WHERE name = 'Siemens Mobility' AND ROWNUM = 1;

    v_returned_id := fn_create_electric_model(
        p_make_id       => v_make_id,
        p_name          => 'Gauge Test',
        p_weight        => 100,
        p_power         => 5000,
        p_acceleration  => 2,
        p_length        => 19,
        p_width         => 3,
        p_height        => 4,
        p_voltage       => 25000,
        p_frequency     => 50,
        p_gauge_name    => 'DOES NOT EXIST',
        p_bogie_name    => 'Flexx Power',
        p_num_of_bogies => 2,
        p_max_speed     => 140
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed invalid Gauge.');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20002 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Caught error -20002 (Invalid Gauge).');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/

-- TEST 4: DATA VERIFICATION
DECLARE
    v_id          NUMBER;
    v_make_id     NUMBER;
    v_chk_voltage NUMBER;
    v_chk_name    VARCHAR2(255);
    v_in_voltage  NUMBER := 15000;
    v_in_name     VARCHAR2(255) := 'Integrity Check Loco';
BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Starting Test D: Data Integrity ---');

    SELECT makeId INTO v_make_id FROM Make WHERE name = 'Siemens Mobility' AND ROWNUM = 1;

    v_id := fn_create_electric_model(
        p_make_id       => v_make_id, 
        p_name          => v_in_name, 
        p_weight        => 88, 
        p_power         => 5600, 
        p_acceleration  => 1.1, 
        p_length        => 19.5, 
        p_width         => 3.0, 
        p_height        => 4.2, 
        p_voltage       => v_in_voltage, 
        p_frequency     => 16.7, 
        p_gauge_name    => 'Standart gauge',
        p_bogie_name    => 'Flexx Power',
        p_num_of_bogies => 2,
        p_max_speed     => 230
    );

    SELECT name INTO v_chk_name FROM Locomotive_Model WHERE locomotiveModelId = v_id;
    SELECT voltage INTO v_chk_voltage FROM Electric_Locomotive_Model WHERE locomotiveModelId = v_id;

    IF v_chk_name = v_in_name AND v_chk_voltage = v_in_voltage THEN
        DBMS_OUTPUT.PUT_LINE('PASSED: Data matches in both tables.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('FAILED: Data mismatch.');
    END IF;

    ROLLBACK;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('FAILED: ' || SQLERRM);
END;
/