package org.dei.Services.OrderAllocation;

import org.dei.Sprint1._Item.Box;
import org.dei._Facilities.Terminal.WarehouseServices.OrderAllocation.OrderAllocation;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Order.OrderMode;
import org.dei.Sprint1.Order.OrderStatus;
import org.dei.Sprint1.Repository.ItemRepository;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint1.US002.NotValidOrderException;
import org.dei._Train.Wagon;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

public class OrderAllocationTest {
    public Terminal setup() {
        ItemRepository itemRepo = ItemRepository.getInstance();
        Terminal c = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        itemRepo.addItem(a1);
        itemRepo.addItem(a2);
        itemRepo.addItem(a3);

        Order o = new Order("order01", LocalDateTime.of(2025, 10, 8, 14, 30, 0), 1);
        o.addItem(a1, 1);
        o.addItem(a2, 1);
        o.addItem(a3, 1);

        c.addOrder(o);

        Warehouse w = new Warehouse("W1");
        Aisle a = new Aisle();
        Bay b = new Bay(10);
        Bay e = new Bay(10);
        a.addBay(b);
        a.addBay(e);

        b.addBox(new Box("box2", 10, a2, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        b.addBox(new Box("box3", 10, a3, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        b.addBox(new Box("box1", 10, a1, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        c.getItemHash().put(a1.getSku(),new PriorityQueue<>());
        c.getItemHash().put(a2.getSku(),new PriorityQueue<>());
        c.getItemHash().put(a3.getSku(),new PriorityQueue<>());
        c.getItemHash().get(a1.getSku()).add(new Box("box2", 10, a1, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        c.getItemHash().get(a2.getSku()).add(new Box("box3", 10, a2, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        c.getItemHash().get(a3.getSku()).add(new Box("box1", 10, a3, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        w.addAisle(a);
        c.addWarehouse(w);
        return (c);
    }

    public Terminal setupNoOrder() {
        Terminal c = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);
        return (c);
    }

    public Terminal setupOrderEmpty() {
        ItemRepository itemRepo = ItemRepository.getInstance();
        Terminal c = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);


        Order o = new Order("order01", LocalDateTime.of(2025, 10, 8, 14, 30, 0), 1);

        c.addOrder(o);
        return (c);
    }

    public Terminal setupOrderNotFullyFulfilled() {
        ItemRepository itemRepo = ItemRepository.getInstance();
        Terminal c = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);


        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        itemRepo.addItem(a1);
        itemRepo.addItem(a2);
        itemRepo.addItem(a3);

        Order o = new Order("order01", LocalDateTime.of(2025, 10, 8, 14, 30, 0), 2);
        o.addItem(a1, 1);
        o.addItem(a2, 1);
        o.addItem(a3, 1);

        Order o2 = new Order("order02", LocalDateTime.of(4, 10, 8, 14, 30, 0), 1);
        o2.addItem(a1, 5);
        o2.addItem(a2, 5);
        o2.addItem(a3, 100);

        c.addOrder(o);
        c.addOrder(o2);

        Warehouse w = new Warehouse("W1");
        Aisle a = new Aisle();
        Bay b = new Bay(10);

        a.addBay(b);


        Box box1 = new Box("box1", 10, a1,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));

        Box box2 = new Box("box2", 10, a2,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));

        Box box3 = new Box("box3", 10, a3,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));
        box1.setPosition(new WarehousePosition(0,0,0));
        box2.setPosition(new WarehousePosition(0,0,0));
        box3.setPosition(new WarehousePosition(0,0,0));

        b.addBox(box1);
        b.addBox(box2);
        b.addBox(box3);

        c.getItemHash().put(a1.getSku(), new PriorityQueue<>());
        c.getItemHash().put(a2.getSku(), new PriorityQueue<>());
        c.getItemHash().put(a3.getSku(), new PriorityQueue<>());

        c.getItemHash().get(a1.getSku()).add(box1);
        c.getItemHash().get(a2.getSku()).add(box2);
        c.getItemHash().get(a3.getSku()).add(box3);

        w.addAisle(a);
        c.addWarehouse(w);
        return (c);
    }

    public Terminal newSetup() {
        ItemRepository itemRepo = ItemRepository.getInstance();
        Terminal c = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);


        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        itemRepo.addItem(a1);
        itemRepo.addItem(a2);
        itemRepo.addItem(a3);

        Order o = new Order("order01", LocalDateTime.of(2025, 10, 8, 14, 30, 0), 1);
        o.addItem(a1, 1); o.addItem(a2, 1); o.addItem(a3, 1);

        Order o2 = new Order("order02", LocalDateTime.of(4, 10, 8, 14, 30, 0), 2);
        o2.addItem(a1, 5); o2.addItem(a2, 5); o2.addItem(a3, 100);

        c.addOrder(o); c.addOrder(o2);

        Warehouse w = new Warehouse("W1");
        Aisle a = new Aisle();
        Bay b = new Bay(2);
        Bay b2 = new Bay(2);
        Bay b3 = new Bay(2);

        a.addBay(b); a.addBay(b2); a.addBay(b3); w.addAisle(a);


        Box box1 = new Box("box1", 10, a1,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));

        Box box2 = new Box("box2", 10, a2,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));

        Box box3 = new Box("box3", 10, a3,
                LocalDate.of(2025, 10, 10),
                LocalDateTime.of(2025, 10, 8, 14, 30, 0));

        Wagon wg = new Wagon("w1");
        wg.addBox(box1); wg.addBox(box2); wg.addBox(box3);

        c.addWarehouse(w);

        c.unloadWagon(wg);

        return (c);
    }
// ================== Something Empty (should trigger exception)  ================== //

    @Test
    public void testOrderAllocationOrderNull() {
        // Arrange
        Terminal t = setup();

        // Act & Assert
        assertThrows(
                NotValidOrderException.class,  // expected exception type
                () -> OrderAllocation.orderAllocation(t, null, OrderMode.STRICT)
        );
    }

    @Test
    public void testOrderAllocationEmptyOrderPartial() {
        ///  Arrange
        Terminal t = setupNoOrder();


        /// Act & Assert
        assertThrows(
                NotValidOrderException.class,  // expected exception type
                () -> OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.PARTIAL)
        );
    }

    @Test
    public void testOrderAllocationEmptyOrderStrict() {
        ///  Arrange
        Terminal t = setupNoOrder();


        /// Act & Assert
        assertThrows(
                NotValidOrderException.class,  // expected exception type
                () -> OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.STRICT)
        );
    }

    @Test
    public void testOrderAllocationOrderEmptyPartial() {
        ///  Arrange
        Terminal t = setupOrderEmpty();


        /// Act & Assert
        assertThrows(
                NotValidOrderException.class,  // expected exception type
                () -> OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.PARTIAL)
        );
    }

    @Test
    public void testOrderAllocationOrderEmptyStrict() {
        ///  Arrange
        Terminal t = setupOrderEmpty();


        /// Act & Assert
        assertThrows(
                NotValidOrderException.class,  // expected exception type
                () -> OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.STRICT)
        );
    }

// ================== Full Fulfilling Orders ================== //

    /// this should work, all the item from the order are in the warehouse
    @Test
    public void testOrderAllocationValidOrderPartial() {
        ///  Arrange
        Terminal t = setup();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.PARTIAL);

        ///  Assert
        assertEquals(t.getOrder(0).getStatus(), OrderStatus.ELIGIBLE);
    }

    /// this should work, all the item from the order are in the warehouse
    @Test
    public void testOrderAllocationValidOrderStrict() {
        ///  Arrange
        Terminal t = setup();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrder(0), OrderMode.STRICT);

        ///  Assert
        assertEquals(t.getOrder(0).getStatus(), OrderStatus.ELIGIBLE);

    }

// ================== Not Fulfilling Orders in Strict Mode ================== //

    @Test
    public void testOrderAllocationValidOrderPartial2() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();


        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrderToFulfill(), OrderMode.PARTIAL);

        ///  Assert
        assertEquals(t.getOrderToFulfill().getStatus(), OrderStatus.PARTIAL);
        assertEquals(t.getOrderToFulfill().getItems().get(2).getAllocatedQty(), 10);
    }

    @Test
    public void testOrderAllocationNotValidOrderStrict() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrder(1), OrderMode.STRICT);

        ///  Assert
        assertEquals(t.getOrder(0).getStatus(), OrderStatus.UNDISPATCHABLE);
        assertEquals(t.getOrderToFulfill().getItems().get(2).getAllocatedQty(), 0);
    }

// ================== Fulfilling and not fulfilling checking OrdersLines status ================== //

    @Test
    public void testOrderAllocationOrderPartialItemPartial() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrderToFulfill(), OrderMode.PARTIAL);

        ///  Assert
        assertEquals(t.getOrderToFulfill().getItems().get(2).getStatus(), OrderStatus.PARTIAL);
    }

    @Test
    public void testOrderAllocationOrderPartialItemEligible() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrderToFulfill(), OrderMode.PARTIAL);

        ///  Assert
        assertEquals(t.getOrderToFulfill().getItems().get(0).getStatus(), OrderStatus.ELIGIBLE);
    }

    @Test
    public void testOrderAllocationOrderStrictItemPartial() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrderToFulfill(), OrderMode.STRICT);

        ///  Assert
            assertEquals(t.getOrderToFulfill().getItems().get(2).getStatus(), OrderStatus.UNDISPATCHABLE);
    }

    @Test
    public void testOrderAllocationOrderStrictItemEligible() {
        ///  Arrange
        Terminal t = setupOrderNotFullyFulfilled();

        ///  Act
        OrderAllocation.orderAllocation(t, t.getOrderToFulfill(), OrderMode.PARTIAL);

        ///  Assert
        assertEquals(t.getOrderToFulfill().getItems().get(0).getStatus(), OrderStatus.ELIGIBLE);
    }

    /**
     * Normal Behaviour is:
     * - A order has only 2 status: ELIGIBLE or UNDISPATCHABLE
     * - A orderLine has only 3 status: ELIGIBLE, PARTIAL or UNDISPATCHABLE
     *
     * Tests scope:
     *  - Testing the allocating quantity
     *  - Testing the refresh of the orderStatus
     *  - Testing the refresh of OrderLines status
     */

    @Test
    public void testOrderAllocationOrderP() {
        ///  Arrange
        Terminal t = newSetup();

        ///  Act
        System.out.println(t.listOrders());
        System.out.println();

        for (Order order:  t.getOrders()) {
            OrderAllocation.orderAllocation(t, order, OrderMode.PARTIAL);
        }

        ///  Assert
        assertEquals(1, t.getItemHash().get("SKU001").size());
        assertEquals(1, t.getItemHash().get("SKU002").size());
        assertEquals(0, t.getItemHash().get("SKU003").size());

        assertEquals(4, t.getItemHash().get("SKU001").poll().getQntY());
        assertEquals(4, t.getItemHash().get("SKU002").poll().getQntY());
    }

    @Test
    public void testOrderAllocationOrderP2() {
        ///  Arrange
        Terminal t = newSetup();

        ///  Act
        //System.out.println(t.listOrders());
        //System.out.println();

        for (Order order:  t.getOrders()) {
            OrderAllocation.orderAllocation(t, order, OrderMode.STRICT);
        }

        //System.out.println();
        //System.out.println(t.listOrders());

        ///  Assert

        assertEquals(4, t.getItemHash().get("SKU001").poll().getQntY());
        assertEquals(4, t.getItemHash().get("SKU002").poll().getQntY());
        assertEquals(9, t.getItemHash().get("SKU003").poll().getQntY());
    }
}