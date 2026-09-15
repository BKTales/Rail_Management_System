CREATE OR REPLACE PROCEDURE register_freight (
  p_freightID Freight.freightID%TYPE,
  p_startPoint Freight.startPoint%TYPE,
  p_endPoint Freight.endPoint%TYPE,
  p_wagonType Wagon_Type.wagonTypeID%TYPE,
  p_quantity  NUMBER
) IS
    v_available_wagons      NUMBER;
    v_count                 NUMBER := 0;
    v_wagonId               WAGON.wagonId%TYPE;
    exInvalidQuantity       EXCEPTION;
    exQuantityNotAvailable  EXCEPTION;
    exWagonTypeNotExist     EXCEPTION;
    CURSOR curValidWagons IS
    SELECT w.wagonId
    FROM Wagon w
             INNER JOIN Wagon_Model wm ON w.wagonModelID = wm.wagonModelID
             INNER JOIN Wagon_Type wt  ON wm.wagonTypeID = wt.wagonTypeID
    WHERE w.facilityID = p_startPoint AND wt.wagonTypeID = p_wagonType;

BEGIN
    IF p_quantity <= 0 THEN
        RAISE exInvalidQuantity;
    END IF;


    SELECT COUNT(*) INTO v_count
    FROM Wagon_Type
    WHERE wagonTypeID = p_wagonType;

    IF v_count = 0 THEN
            RAISE exWagonTypeNotExist;
    END IF;


SELECT COUNT(*) INTO v_available_wagons
    FROM Wagon w
             INNER JOIN Wagon_Model wm ON w.wagonModelID = wm.wagonModelID
             INNER JOIN Wagon_Type wt  ON wm.wagonTypeID = wt.wagonTypeID
    WHERE w.facilityID = p_startPoint
      AND wt.wagonTypeID = p_wagonType;

    IF v_available_wagons < p_quantity THEN
        RAISE exQuantityNotAvailable;
    END IF;



    INSERT INTO Freight(freightId, startPoint, endPoint)
    VALUES (p_freightID, p_startPoint, p_endPoint);

    v_count := 0;
    OPEN curValidWagons;

    LOOP
        FETCH curValidWagons INTO v_wagonId;
        EXIT WHEN curValidWagons%NOTFOUND OR v_count = p_quantity;
        INSERT INTO Wagon_Freight(wagonId,freightId) VALUES (v_wagonId,p_freightId);
        v_count := v_count + 1;
    END LOOP;

    CLOSE curValidWagons;

EXCEPTION
    WHEN exInvalidQuantity THEN
        RAISE_APPLICATION_ERROR(-20001, 'Quantity must be greater than 0.');
    WHEN exQuantityNotAvailable THEN
        RAISE_APPLICATION_ERROR(-20002, 'Not enough wagons of the given type at the departure facility.');
    WHEN exWagonTypeNotExist THEN
        RAISE_APPLICATION_ERROR(-20003, 'Wagon type does not exist.');
    WHEN OTHERS THEN
        RAISE;
END;

-- ------------Tests------------

--------------------------------------------------
--        Invalid Requested Quantity (<= 0)
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 1001;
    v_startPoint  Freight.startPoint%TYPE := 1;
    v_endPoint  Freight.endPoint%TYPE := 2;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 1;
    v_quantity   NUMBER := 0;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 1: ' || SQLERRM);
ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--      Departure Facility Not Existent
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 1002;
    v_startPoint  Freight.startPoint%TYPE := 9999;
    v_endPoint  Freight.endPoint%TYPE := 2;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 1;
    v_quantity   NUMBER := 2;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 2: ' || SQLERRM);
ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--      Arrival Facility Not Existent
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 1003;
    v_startPoint  Freight.startPoint%TYPE := 1;
    v_endPoint  Freight.endPoint%TYPE := 9999;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 1;
    v_quantity   NUMBER := 2;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 3: ' || SQLERRM);
ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--          Wagon Type Not Existent
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 1004;
    v_startPoint  Freight.startPoint%TYPE := 1;
    v_endPoint  Freight.endPoint%TYPE := 2;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 9999;
    v_quantity   NUMBER := 2;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 4: ' || SQLERRM);
        ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--  Requested quantity greater than available
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 1005;
    v_startPoint  Freight.startPoint%TYPE := 1;
    v_endPoint  Freight.endPoint%TYPE := 2;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 1;
    v_quantity   NUMBER := 999;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 5: ' || SQLERRM);
        ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--      Duplicated Freight ID
--------------------------------------------------
DECLARE
v_freightID  Freight.freightID%TYPE := 801;
    v_startPoint  Freight.startPoint%TYPE := 1;
    v_endPoint  Freight.endPoint%TYPE := 2;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 1;
    v_quantity   NUMBER := 1;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
COMMIT;
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Teste 6: Freight ID duplicado');
        ROLLBACK;
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Teste 6: ' || SQLERRM);
        ROLLBACK;
END;
/
--------------------------------------------------

--------------------------------------------------
--              Happy Path
--------------------------------------------------
DECLARE
    v_freightID  Freight.freightID%TYPE := 90;
    v_startPoint  Freight.startPoint%TYPE := 7;
    v_endPoint  Freight.endPoint%TYPE := 14;
    v_wagonType  Wagon_Type.wagonTypeID%TYPE := 6;
    v_quantity   NUMBER := 1;
BEGIN
    register_freight(v_freightID, v_startPoint, v_endPoint, v_wagonType, v_quantity);
    DBMS_OUTPUT.PUT_LINE('Teste 7: Freight criado com sucesso');
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE(
            'Erro inesperado no Teste 7: ' || SQLERRM
        );
END;
/
--------------------------------------------------