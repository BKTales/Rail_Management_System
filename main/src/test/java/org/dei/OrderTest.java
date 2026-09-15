package org.dei;

import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Order.OrderLine;
import org.dei._Facilities.Terminal.Terminal;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testAddItem() {
        LocalDateTime time = LocalDateTime.now();
        // Arrange
        Order order = new Order("1", time, 1);
        Item item1 = new Item(Unit.UNIT, "SKU123", 10.5, ItemType.PERSONAL_CARE, 1);
        Item item2 = new Item(Unit.BAG, "SKU456", 5.0, ItemType.GROCERY, 1);

        // Act
        order.addItem(item1, 3);
        order.addItem(item2, 5);

        // Assert
        ArrayList<OrderLine> orderLines = order.getItems();
        assertEquals(orderLines.get(0).getItem(), item1, "Item1 should exist in the order");
        assertEquals(orderLines.get(1).getItem(), item2, "Item2 should exist in the order");
        assertEquals(3, orderLines.get(0).getRequestedQty(), "Item1 quantity should be 3");
        assertEquals(5, orderLines.get(1).getRequestedQty(), "Item2 quantity should be 5");
    }

    @Test
    void testAddItemUpdateQuantity() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Order order = new Order("1", time, 1);
        Item item = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.GROCERY, 1);

        // Act
        order.addItem(item, 5);
        order.addItem(item, 8); // soma à quantidade existente
        int qty = 13;

        // Assert
        int returned = order.getItems().getFirst().getRequestedQty();

        assertEquals(qty, returned);
    }

    @Test
    void testToStringEmptyOrder() {
        LocalDateTime time = LocalDateTime.now();
        Order order = new Order("1", time, 1);
        int returned = order.getItems().size();

        assertEquals(0 , returned);
    }

    @Test
    void testGetItemQnt() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Order order = new Order("1", time, 1);
        Item item1 = new Item(Unit.BAG, "SKU1", 10.0, ItemType.GROCERY, 1);
        Item item2 = new Item(Unit.BOTTLE, "SKU2", 5.0, ItemType.BEVERAGE, 1);

        order.addItem(item1, 3);
        order.addItem(item2, 5);

        // Act & Assert
        assertEquals(3, order.getItemQnt(item1), "getItemQnt should return the correct quantity for item1");
        assertEquals(5, order.getItemQnt(item2), "getItemQnt should return the correct quantity for item2");
    }

    @Test
    void testGetItemQntItemNotPresent() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Order order = new Order("1", time, 1);
        Item item = new Item(Unit.BOTTLE, "SKU1", 10.0, ItemType.BEVERAGE, 1);

        // Act
        int qty = order.getItemQnt(item);

        // Assert
        assertEquals(-1, qty, "getItemQnt should return -1 if item is not in the order");
    }

    @Test
    void comparatorTestDiffPriority() {
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Order wantedOrder = new Order("A", LocalDateTime.now().plusDays(1), 1);

        terminal.addOrder(wantedOrder);
        terminal.addOrder(new Order("B", LocalDateTime.now().plusDays(1), 2));

        Order o = terminal.getOrderToFulfill();
        assertEquals(o, wantedOrder);
    }

    @Test
    void comparatorTestSamePriorityDiffDates() {
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Order wantedOrder = new Order("A", LocalDateTime.now().plusDays(1), 1);

        terminal.addOrder(wantedOrder);
        terminal.addOrder(new Order("B", LocalDateTime.now().plusDays(3), 1));
        terminal.addOrder(new Order("C", LocalDateTime.now().plusDays(1), 2));

        Order o = terminal.getOrderToFulfill();
        assertEquals(o, wantedOrder);
    }
}