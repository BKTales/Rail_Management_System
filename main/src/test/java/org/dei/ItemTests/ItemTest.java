package org.dei.ItemTests;

import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testGetters() {
        // Arrange
        Unit unit = Unit.PACK;
        String sku = "SKU123";
        double volume = 10.5;
        double weight = 1.5;
        ItemType itemType = ItemType.GROCERY;

        Item item = new Item(unit, sku, volume, itemType, weight);

        // Act & Assert
        assertEquals(unit, item.getUnit());
        assertEquals(sku, item.getSku());
        assertEquals(volume, item.getVolume());
        assertEquals(itemType, item.getItemType());
    }

    @Test
    void testToString() {
        // Arrange
        Unit unit = Unit.PACK;
        double volume = 10.5;
        double weight = 1.5;
        ItemType itemType = ItemType.GROCERY;
        Item item = new Item(unit, "SKU123", volume, itemType, weight);

        // Act
        String result = item.toString();

        // Assert
        String expected = item.getSku() + " " + itemType.inString + " " + unit.inString + " " + volume;
        assertEquals(expected, result);
    }
}
