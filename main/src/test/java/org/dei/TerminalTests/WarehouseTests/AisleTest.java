package org.dei.TerminalTests.WarehouseTests;

import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AisleTest {

    @Test
    void testAddandGetBay() {
        // Arrange
        Aisle aisle = new Aisle();
        Bay bay = new Bay(3);

        // Act
        aisle.addBay(bay);

        // Assert
        assertEquals(bay, aisle.getBay(0), "getBay should return the bay that was added at index 0");
    }

    @Test
    void testAddMultipleBaysOrder() {
        // Arrange
        Aisle aisle = new Aisle();
        Bay bay1 = new Bay(3);
        Bay bay2 = new Bay(3);

        // Act
        aisle.addBay(bay1);
        aisle.addBay(bay2);

        // Assert
        assertEquals(bay1, aisle.getBay(0), "First bay should be bay1");
        assertEquals(bay2, aisle.getBay(1), "Second bay should be bay2");
    }
}
