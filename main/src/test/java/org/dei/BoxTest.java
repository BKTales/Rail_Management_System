package org.dei;

import org.dei.Sprint1._Item.*;
import org.dei._Facilities.Terminal.WarehouseServices.WarehouseRelocation;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Train.Wagon;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {
    public Terminal setup() {
        Terminal t = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);


        Warehouse w = new Warehouse("W1");

        Aisle a = new Aisle();
        Bay b = new Bay(10);
        Bay e = new Bay(10);
        a.addBay(b);
        a.addBay(e);

        b.addBox(new Box("box1", 10, a1, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        b.addBox(new Box("box3", 10, a3, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        e.addBox(new Box("box2", 10, a2, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        w.addAisle(a);
        t.addWarehouse(w);
        return (t);
    }

    public Terminal setup2() {
        Terminal t = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        Item a4 = new Item(Unit.BOTTLE, "SKU004", 2.2, ItemType.ELECTRONICS, 30);

        Wagon wagon = new Wagon("wagon1");
        wagon.addBox(new Box("box3", 10, a3, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        wagon.addBox(new Box("box1", 10, a1, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));
        wagon.addBox(new Box("box2", 10, a2, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        Warehouse w = new Warehouse("W1");

        Aisle a = new Aisle();
        Bay b = new Bay(10);
        Bay e = new Bay(10);
        a.addBay(b);
        a.addBay(e);

        w.addAisle(a);

        t.addWarehouse(w);
        e.addBox(new Box("box1", 10, a4, LocalDate.of(2025, 10,10), LocalDateTime.of(2025, 10, 8, 14, 30, 0) ));

        t.unloadWagon(wagon);
        return (t);
    }

    public Terminal setup3() {
        Terminal t = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        Item a4 = new Item(Unit.BOTTLE, "SKU004", 2.2, ItemType.ELECTRONICS, 30);

        Warehouse w = new Warehouse("W1");
        w.getQuarantine().addItem(new QuarantineItem("box3", 10, a3, Reason.CYCLE_COUNT, LocalDate.of(2025, 10,10),new Timestamp(2025, 10, 8, 14, 30, 0, 0)));
        w.getQuarantine().addItem(new QuarantineItem("box1", 10, a1, Reason.CYCLE_COUNT, LocalDate.of(2025, 10,10),new Timestamp(2025, 10, 8, 14, 30, 0, 0)));
        w.getQuarantine().addItem(new QuarantineItem("box2", 10, a2, Reason.CYCLE_COUNT, LocalDate.of(2025, 10,10),new Timestamp(2025, 10, 8, 14, 30, 0, 0)));

        Aisle a = new Aisle();
        Bay b = new Bay(2);
        Bay e = new Bay(10);
        a.addBay(b);
        a.addBay(e);

        w.addAisle(a);

        t.addWarehouse(w);

        return (t);
    }


    @Test
    void testGetters() {
        // Arrange
        Item product = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Box box = new Box("1", 20, product, LocalDate.of(2025, 12, 31), LocalDateTime.parse("2025-01-01T10:00:00"));

        // Act & Assert
        assertEquals("1", box.getBoxID());
        assertEquals(20, box.getQntY());
        assertEquals(product, box.getProduct());
    }

    @Test
    void testTakeQntY() {
        // Arrange
        Item product = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Box box = new Box("1", 20, product, LocalDate.of(2025, 12, 31), LocalDateTime.parse("2025-01-01T10:00:00"));

        // Act
        int remaining = box.takeQntY(5);

        // Assert
        assertEquals(15, remaining, "Remaining quantity should be 15 after taking 5");
        assertEquals(15, box.getQntY(), "Getter should reflect updated quantity");
    }

    @Test
    void    testToString() {
        // Arrange
        Item product = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Box box = new Box("1", 20, product, LocalDate.of(2025, 12, 31), LocalDateTime.parse("2025-01-01T10:00:00"));

        // Act
        String result = box.toString();

        // Assert
        String expected = "BoxID=1, qntY=20, product=" + product + '}';
        assertEquals(expected, result, "toString output mismatch");
    }

    // ====== Test refresh positions ====== //

    /**
     * The test checks if the position of the box is changed whenever we use relocation to it
     */
    @Test
    void testGetChangePositionInBay() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);
        Box b = w.getAisle(0).getBay(0).getBox(1);

        /// Assert
        assertEquals(0, b.getBayIndex());
        assertEquals(0, b.getAisleIndex());
    }

    /**
     * This test check if the position inside an item that entered the warehouse through
     * the quarantine, has a position well set in hashMap
     */
    @Test
    void testGetPositionAddingQuarantineMap() {
        /// Arrange
        Terminal t = setup3();

        /// Act
        t.restockWarehouse();
        Box box = t.getItemHash().get("SKU001").poll();

        // t.printItemInHash();
        //System.out.println(t.toString());

        /// Assert
        assertEquals(0, box.getAisleIndex());
        assertEquals(1, box.getBayIndex());
    }

    /**
     * This test checks that the position when relocating also refresh in the
     * ItemHash and at the warehouse (checking if they are equal, and if they
     * are equal to the correct value)
     */
    @Test
    void testGetChangePositionInMap() {
        /// Arrange
        Terminal t = setup3();
        t.restockWarehouse();
        Warehouse w = t.getWarehouse("W1");

        /// Act
        w.getAisle(0).getBay(0).getBox(0);
        WarehouseRelocation.reallocateItemInWarehouse(w);

        Box box = t.getItemHash().get("SKU001").poll();
        Box box2 = w.getAisle(0).getBay(0).getBox(1);
        System.out.println(box2.getProduct().getSku());

        /// Assert
        assertEquals(box2.getAisleIndex(), box.getAisleIndex());
        assertEquals(box2.getBayIndex(), box.getBayIndex());
        assertEquals(0, box.getAisleIndex());
        assertEquals(0, box.getBayIndex());
    }
}