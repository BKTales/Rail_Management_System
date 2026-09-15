package org.dei.ItemTests;

import org.dei.Sprint1._Item.*;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class QuarantineItemTest {

    @Test
    void testGetters() {
        // Arrange
        Item item = new Item(Unit.PACK, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Reason reason = Reason.EXPIRED;
        int qty = 5;
        String id = "Q123";
        LocalDate expireDate = LocalDate.now();
        Timestamp timestamp = Timestamp.valueOf("2025-01-01 10:00:00");

        QuarantineItem qItem = new QuarantineItem(id, qty, item, reason, expireDate, timestamp);

        // Act & Assert
        assertEquals(qty, qItem.getQty());
        assertEquals(item, qItem.getItem());
        assertEquals(reason, qItem.getReason());
        assertEquals(expireDate, qItem.getExpireDate());
        assertEquals(timestamp, qItem.getTimestamp());
    }

    @Test
    void testToString() {
        // Arrange
        Item item = new Item(Unit.PACK, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Reason reason = Reason.EXPIRED;
        String id = "Q123";
        int qty = 5;
        LocalDate expireDate = LocalDate.now();
        Timestamp timestamp = Timestamp.valueOf("2025-01-01 10:00:00");
        QuarantineItem qItem = new QuarantineItem(id, qty, item, reason, expireDate, timestamp);

        // Act
        String result = qItem.toString();

        // Assert
        String expected = reason.inString + " " + item.toString();
        assertEquals(expected, result);
    }
}
