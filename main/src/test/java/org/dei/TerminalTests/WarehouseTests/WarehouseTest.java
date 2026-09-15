package org.dei.TerminalTests.WarehouseTests;


import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {


    @Test
    void testConstructor() {
        // Act
        Warehouse warehouse = new Warehouse("1");

        // Assert
        assertEquals("1", warehouse.getId(), "Warehouse ID should match the constructor parameter");
        assertNotNull(warehouse.getQuarantine(), "Quarantine should be initialized in constructor");
        assertNotNull(warehouse.getAisles(), "Aisle list should be initialized");
        assertTrue(warehouse.getAisles().isEmpty(), "Aisle list should be empty initially");
    }
    

}
