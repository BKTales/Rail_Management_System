package org.dei.RepositoryTests;

import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1.Repository.ItemRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    @Test
    void testAddItem() {
        // Arrange
        ItemRepository repo = new ItemRepository();
        Item item = new Item(Unit.BAG, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);

        // Act
        repo.addItem(item);

        // Assert
        assertEquals(1, repo.size(), "Size should be 1 after adding an item");
    }

    @Test
    void testRemoveItem() {
        // Arrange
        ItemRepository repo = new ItemRepository();
        Item item = new Item(Unit.BAG, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        repo.addItem(item);

        // Act
        repo.removeItem(item.getSku());

        // Assert
        assertEquals(0, repo.size(), "Size should be 0 after removing the item");
    }

    @Test
    void testSizeMultipleItems() {
        // Arrange
        ItemRepository repo = new ItemRepository();
        Item item1 = new Item(Unit.UNIT, "SKU1", 5.0, ItemType.BEVERAGE, 1.5);
        Item item2 = new Item(Unit.BOTTLE, "SKU2", 3.0, ItemType.BEVERAGE, 2);

        // Act
        repo.addItem(item1);
        repo.addItem(item2);

        // Assert
        assertEquals(2, repo.size(), "Size should be 2 after adding two items");
    }
}