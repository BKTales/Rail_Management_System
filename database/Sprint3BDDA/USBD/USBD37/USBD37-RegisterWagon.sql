CREATE OR REPLACE FUNCTION fn_register_wagon(
    p_wagon_id       IN Wagon.wagonId%TYPE,
    p_facility_id    IN Facility.facilityId%TYPE,
    p_operator_id    IN Operator.vatNumber%TYPE,
    p_wagon_model_id IN Wagon_Model.wagonModelId%TYPE
) RETURN Wagon.wagonId%TYPE IS

    v_check_count  NUMBER;

    e_wagon_already_exists  EXCEPTION;
    e_facility_not_found    EXCEPTION;
    e_operator_not_found    EXCEPTION;
    e_model_not_found       EXCEPTION;

BEGIN
    SELECT COUNT(*) INTO v_check_count FROM Wagon WHERE wagonId = p_wagon_id;
    IF v_check_count > 0 THEN
        RAISE e_wagon_already_exists;
    END IF;

    SELECT COUNT(*) INTO v_check_count FROM Facility WHERE facilityId = p_facility_id;
    IF v_check_count = 0 THEN
        RAISE e_facility_not_found;
    END IF;

    SELECT COUNT(*) INTO v_check_count FROM Operator WHERE vatNumber = p_operator_id;
    IF v_check_count = 0 THEN
        RAISE e_operator_not_found;
    END IF;

    SELECT COUNT(*) INTO v_check_count FROM Wagon_Model WHERE wagonModelId = p_wagon_model_id;
    IF v_check_count = 0 THEN
        RAISE e_model_not_found;
    END IF;

    INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId)
    VALUES (p_wagon_id, p_facility_id, p_operator_id, p_wagon_model_id);

    RETURN p_wagon_id;

EXCEPTION
    WHEN e_wagon_already_exists THEN
        RAISE_APPLICATION_ERROR(-20010, 'Error: Wagon ID (' || p_wagon_id || ') already exists.');

    WHEN e_facility_not_found THEN
        RAISE_APPLICATION_ERROR(-20011, 'Error: Facility ID (' || p_facility_id || ') does not exist.');
    
    WHEN e_operator_not_found THEN
        RAISE_APPLICATION_ERROR(-20012, 'Error: Operator with VAT (' || p_operator_id || ') does not exist.');
    
    WHEN e_model_not_found THEN
        RAISE_APPLICATION_ERROR(-20013, 'Error: Wagon Model ID (' || p_wagon_model_id || ') does not exist.');

    WHEN OTHERS THEN
        RAISE;
END fn_register_wagon;
/

BEGIN
    BEGIN
        INSERT INTO Facility (facilityId, name) VALUES (2000, 'Lisbon North Depot');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Operator (vatNumber, name, shortName) VALUES ('PT111222', 'Atlantic Cargo', 'AC');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Make (name) VALUES ('Alstom');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Wagon_Type (description) VALUES ('Flat Car');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Dimension (length, width, height) VALUES (15, 3, 2);
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;

    BEGIN
        INSERT INTO Bogie (name) VALUES ('Y25-Bogie');
    EXCEPTION WHEN DUP_VAL_ON_INDEX THEN NULL; END;
    
    DECLARE
        v_make_id NUMBER;
        v_type_id NUMBER;
    BEGIN
        SELECT makeId INTO v_make_id FROM Make WHERE name = 'Alstom';
        SELECT wagonTypeId INTO v_type_id FROM Wagon_Type WHERE description = 'Flat Car';

        INSERT INTO Wagon_Model (
            wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, 
            name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies
        ) VALUES (
            600, 
            'Y25-Bogie', 
            v_make_id, 
            v_type_id, 
            15, 3, 2, 
            'Flat-Model-Y', 
            18, 40, 50, 18, 100, 2
        );
    EXCEPTION 
        WHEN DUP_VAL_ON_INDEX THEN NULL; 
    END;

    COMMIT;
END;
/

SET SERVEROUTPUT ON;

DECLARE
    v_returned_id    Wagon.wagonId%TYPE;
    v_input_id       Wagon.wagonId%TYPE := 5555; 
    v_facility       Facility.facilityId%TYPE := 2000;
    v_operator       Operator.vatNumber%TYPE := 'PT111222';
    v_model          Wagon_Model.wagonModelId%TYPE := 600;
BEGIN
    DELETE FROM Wagon WHERE wagonId = v_input_id;

    v_returned_id := fn_register_wagon(
        p_wagon_id       => v_input_id,
        p_facility_id    => v_facility,
        p_operator_id    => v_operator,
        p_wagon_model_id => v_model
    );

    DBMS_OUTPUT.PUT_LINE('Success! New Wagon Created with Manual ID: ' || v_returned_id);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Test Failed: ' || SQLERRM);
END;
/

DECLARE
    v_returned_id Wagon.wagonId%TYPE;
    v_input_id    Wagon.wagonId%TYPE := 5555; 
BEGIN
    v_returned_id := fn_register_wagon(
        p_wagon_id       => v_input_id,
        p_facility_id    => 2000,
        p_operator_id    => 'PT111222',
        p_wagon_model_id => 600
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed duplicate ID insertion!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20010 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly caught e_wagon_already_exists.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/

DECLARE
    v_returned_id Wagon.wagonId%TYPE;
BEGIN
    v_returned_id := fn_register_wagon(
        p_wagon_id       => 5556,
        p_facility_id    => 99999, 
        p_operator_id    => 'PT111222',
        p_wagon_model_id => 600
    );
    
    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed invalid Facility!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20011 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly caught e_facility_not_found.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/

DECLARE
    v_returned_id Wagon.wagonId%TYPE;
BEGIN
    v_returned_id := fn_register_wagon(
        p_wagon_id       => 5557,
        p_facility_id    => 2000,
        p_operator_id    => 'INVALID_VAT', 
        p_wagon_model_id => 600
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed invalid Operator!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20012 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly caught e_operator_not_found.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/

DECLARE
    v_returned_id Wagon.wagonId%TYPE;
BEGIN
    v_returned_id := fn_register_wagon(
        p_wagon_id       => 5558,
        p_facility_id    => 2000,
        p_operator_id    => 'PT111222',
        p_wagon_model_id => -100 
    );

    DBMS_OUTPUT.PUT_LINE('FAILED: Function allowed invalid Wagon Model!');

EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -20013 THEN
            DBMS_OUTPUT.PUT_LINE('PASSED: Correctly caught e_model_not_found.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('FAILED: Unexpected error: ' || SQLERRM);
        END IF;
END;
/