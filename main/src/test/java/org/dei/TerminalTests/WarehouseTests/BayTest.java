package org.dei.TerminalTests.WarehouseTests;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BayTest {

    @Test
    void testAddAndGetBox() {
        // Arrange
        Bay bay = new Bay(10);
        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box = new Box("box01", 4, a, date, dateTime);

        // Act
        bay.addBox(box);

        // Assert
        Box retrieved = bay.getBox(0);
        assertEquals(box, retrieved, "getBox should return the box that was added");
    }

    @Test
    void testGetFirstBoxEmpty() {
        // Arrange
        Bay bay = new Bay(3);

        // Act
        Box retrieved = bay.getFirstBox();

        // Assert
        assertNull(retrieved, "getBox should return null when the bay is empty");
    }

    @Test
    void testAddMultipleBoxesOrder() {
        // Arrange
        Bay bay = new Bay(10);
        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.BEVERAGE, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box1 = new Box("box01", 4, a, date, dateTime);
        Item b = new Item(Unit.BOX, "SKU004", 2.2, ItemType.PERSONAL_CARE, 30);
        LocalDate date2 = LocalDate.parse("2021-10-05");
        LocalDateTime dateTime2 = LocalDateTime.parse("2025-04-14T02:43:00");
        Box box2 = new Box("box02", 4, b, date2, dateTime2);

        // Act
        bay.addBox(box1);
        bay.addBox(box2);

        // Assert
        Box first = bay.getBox(0);
        Box second = bay.getBox(0);

        assertTrue((first.equals(box1) && second.equals(box2)),
                "Both boxes should be retrieved from the bay");
    }
}
