--USEDDDDDDDDDDDDDDDDDDD
CREATE OR REPLACE PROCEDURE associateFreightRoute(
    p_routeId NUMBER,
    p_freightId NUMBER
)
IS
    v_routeStartDate Route.departureDate%TYPE;
    v_freightExists  NUMBER;
BEGIN
    -- Verify route exists and get its departure date
BEGIN
SELECT departureDate
INTO v_routeStartDate
FROM Route
WHERE routeId = p_routeId;
EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Route ID ' || p_routeId || ' does not exist');
END;

    -- Verify freight exists
SELECT COUNT(*)
INTO v_freightExists
FROM Freight
WHERE freightId = p_freightId;
IF v_freightExists = 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Freight ID ' || p_freightId || ' does not exist');
END IF;

    -- Check all wagons in this freight
FOR o_line IN (
            SELECT wagonId
            FROM Wagon_Freight wf
            WHERE wf.freightId = p_freightId)
        LOOP
            -- Check if this wagon is already assigned to any train during this time
            FOR l_line IN (
                SELECT t.startDate, t.endDate
                FROM Wagon_Freight wf
                INNER JOIN Freight ft ON ft.freightId = wf.freightId
                INNER JOIN Train t ON t.routeId = ft.routeId
                WHERE wf.wagonId = o_line.wagonId)
                LOOP
                    -- Check for overlap (handle NULL endDate for ongoing trains)
                    IF l_line.startDate <= TRUNC(v_routeStartDate) AND
                       (l_line.endDate >= TRUNC(v_routeStartDate)) THEN
                        RAISE_APPLICATION_ERROR(-20004, 'Wagon ' || o_line.wagonId || ' already assigned to a train during this time interval');
END IF;
END LOOP;
END LOOP;

    -- Update the freight to associate it with the route
UPDATE Freight
SET routeId = p_routeId
WHERE freightId = p_freightId;

COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/