package org.dei.TerminalTests.WarehouseTests;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehousePositionTest {

    @Test
    void testGetters() {
        // Arrange & Act
        WarehousePosition warehousePosition = new WarehousePosition(2, 5, 0);

        // Assert
        assertEquals(2, warehousePosition.getBayIndex(), "getBayIndex should return the correct bay index");
        assertEquals(5, warehousePosition.getAisleIndex(), "getAisleIndex should return the correct aisle index");
    }

    @Test
    void testToString() {
        // Arrange
        WarehousePosition warehousePosition = new WarehousePosition(2, 5, 0);

        // Act
        String result = warehousePosition.toString();

        // Expected
        String expected = "Position [bayIndex= 2, aisleIndex=5]";

        // Assert
        assertEquals(expected, result, "toString should return the correct string representation");
    }
}
