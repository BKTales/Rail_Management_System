CREATE OR REPLACE PROCEDURE add_facility(
    p_name          Facility.name%TYPE,
    p_facilityID    Facility.facilityID%TYPE
)
IS
    v_name Facility.name%TYPE;
BEGIN
    v_name := INITCAP(TRIM(REGEXP_REPLACE(p_name,'\s+',' ')));
    INSERT INTO Facility (facilityID, name)
    VALUES (p_facilityID, v_name);
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
/
-- Inserção válida
BEGIN
    add_facility('Nova Facility', 101);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Nome NULL
BEGIN
    add_facility(NULL, 102);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Nome duplicado (case-insensitive)
BEGIN
    add_facility('nova facility', 103);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Nome com espaços extras
BEGIN
    add_facility('   outra   facility  ', 104);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- Duplicata case-insensitive
BEGIN
    add_facility('OuTrA FaCiLiTy', 105);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

-- FacilityID duplicado
BEGIN
    add_facility('Nova Unique', 101);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
