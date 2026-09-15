-- use the original create tables and inserts


--default information, they should all be parked
DECLARE
v_loco_cursor SYS_REFCURSOR;
    v_loco_id     NUMBER;
    v_loco_model  VARCHAR2(255);
    v_loco_oper   VARCHAR2(255);
    v_loco_status VARCHAR2(50);
    v_loco_loc    VARCHAR2(255);
    v_loco_dist   NUMBER;

    v_wagon_cursor SYS_REFCURSOR;
    v_wagon_id     NUMBER;
    v_wagon_model  VARCHAR2(255);
    v_wagon_type   VARCHAR2(255);
    v_wagon_status VARCHAR2(50);
    v_wagon_loc    VARCHAR2(255);
    v_wagon_dist   NUMBER;

    -- ID da Estação "Leixões"
    v_leixoes_station_id NUMBER := 50;

BEGIN
    DBMS_OUTPUT.PUT_LINE('=======================================================');
    DBMS_OUTPUT.PUT_LINE('  TEST Getters - TRAFFIC MANAGER  ');
    DBMS_OUTPUT.PUT_LINE('  Reference station: Leixoes (ID ' || v_leixoes_station_id || ')');
    DBMS_OUTPUT.PUT_LINE('=======================================================');


    DBMS_OUTPUT.PUT_LINE(CHR(10) || '>>> Locomotive List <<<');
    DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------');

    v_loco_cursor := get_available_locomotivesTransitINF(v_leixoes_station_id);

    LOOP
FETCH v_loco_cursor INTO v_loco_id, v_loco_model, v_loco_oper, v_loco_status, v_loco_loc, v_loco_dist;
        EXIT WHEN v_loco_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('ID: ' || v_loco_id ||
                             ' | Mod: ' || v_loco_model ||
                             ' | Status: ' || RPAD(v_loco_status, 10) ||
                             ' | Loc/Dest: ' || RPAD(NVL(v_loco_loc, 'N/A'), 20) ||
                             ' | Dist: ' || v_loco_dist);
END LOOP;
CLOSE v_loco_cursor;


DBMS_OUTPUT.PUT_LINE(CHR(10) || '>>> Wagon List <<<');
    DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------');

    v_wagon_cursor := get_available_wagonsTransitINF(v_leixoes_station_id);

    LOOP
FETCH v_wagon_cursor INTO v_wagon_id, v_wagon_model, v_wagon_type, v_wagon_status, v_wagon_loc, v_wagon_dist;
        EXIT WHEN v_wagon_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('ID: ' || v_wagon_id ||
                             ' | Tipo: ' || RPAD(v_wagon_type, 15) ||
                             ' | Status: ' || RPAD(v_wagon_status, 10) ||
                             ' | Loc/Dest: ' || v_wagon_loc);
END LOOP;
CLOSE v_wagon_cursor;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('ERRO CRITICO: ' || SQLERRM);
END;
/


--in order to test if a locomotive/wagon is in transit we should build a mock train in wich start date is recent and didn't end its route
--insert this code before using the code before to test it

INSERT INTO Train (trainId, routeId, startDate)
VALUES (9999, 1, SYSDATE);

INSERT INTO Locomotive_Train (locomotiveId, trainId)
VALUES (1903, 9999);

INSERT INTO Freight (freightId, endpointA, endpointB)
VALUES (9001, 50, 11);

INSERT INTO Freight_Wagon (freightId, wagonId)
VALUES (9001, 823045);

INSERT INTO Route_Freight (routeId, freightId)
VALUES (1, 9001);

COMMIT;

