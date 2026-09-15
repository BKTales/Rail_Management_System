package org.dei.Services.Warehouse;

import org.dei.Sprint1._Item.Box;
import org.dei._Facilities.Terminal.WarehouseServices.WarehouseRelocation;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WarehouseRelocationTest {

    Terminal setup(){
        Terminal t = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Warehouse w = new Warehouse("W1");
        t.addWarehouse(w);

        Aisle a = new Aisle();
        w.addAisle(a);

        for (int i = 0; i < 10; i++)
        {
            a.addBay(new Bay(10));
        }
        return t;
    }

    void addingBoxesInAllAisles(Aisle a, int manyBoxes){
        Item product = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Box genericBox = new Box("1", 20, product, LocalDate.of(2025, 12, 31), LocalDateTime.parse("2025-01-01T10:00:00"));;

        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < manyBoxes; j++)
            {
                a.getBay(i).addBox(genericBox);
            }
        }
    }

    //============================================================================//

    @Test
    void testRelocationEmpty() {
        /// Arrange
        Terminal t = setup();

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(t.getWarehouse("W1"));

        /// Assert
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(0).getManyBoxes(), 0);
    }

    //============================================================================//

    @Test
    void testRelocationOneItemInEachBayCheckIndex0() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 1);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);


        /// Assert
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(0).getManyBoxes(), 10);
    }

    @Test
    void testRelocationOneItemInEachBayCheckIndex1() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 1);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);


        /// Assert
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(1).getManyBoxes(), 0);
    }

    //============================================================================//

    @Test
    void testRelocationThreeItemInEachBayCheckIndex2() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 3);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);


        /// Assert
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(2).getManyBoxes(), 10);
    }

    @Test
    void testRelocationThreeItemInEachBayCheckIndex3() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 3);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);

        /// Assert
        //assertEquals(t.getWarehouse("W1").getAisle(0).getBay(2).getManyBoxes(), 10);
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(3).getManyBoxes(), 0);
    }

    //============================================================================//

    @Test
    void testRelocationOneItemOnLastBay() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        Item product = new Item(Unit.BOTTLE, "SKU123", 10.5, ItemType.BEVERAGE, 1.5);
        Box genericBox = new Box("1", 20, product, LocalDate.of(2025, 12, 31), LocalDateTime.parse("2025-01-01T10:00:00"));;
        w.getAisle(0).getBay(9).addBox(genericBox);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);


        /// Assert
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(0).getManyBoxes(), 1);
    }

    //============================================================================//


    @Test
    void testRelocationNineItemInEachBayCheckIndex8() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 9);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);

        /// Assert
        //assertEquals(t.getWarehouse("W1").getAisle(0).getBay(2).getManyBoxes(), 10);
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(8).getManyBoxes(), 10);
    }
    
    @Test
    void testRelocationNineItemInEachBayCheckIndex9() {
        /// Arrange
        Terminal t = setup();
        Warehouse w = t.getWarehouse("W1");
        addingBoxesInAllAisles(w.getAisle(0), 9);

        /// Act
        WarehouseRelocation.reallocateItemInWarehouse(w);

        /// Assert
        //assertEquals(t.getWarehouse("W1").getAisle(0).getBay(2).getManyBoxes(), 10);
        assertEquals(t.getWarehouse("W1").getAisle(0).getBay(9).getManyBoxes(), 0);


    }
}