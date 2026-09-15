package org.dei;

import org.dei.Utils.Exceptions.WarehousesFullException;
import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Train.Wagon;
import org.dei.Sprint1.Parser.DoubleDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UnloadWagonTest {

    private Terminal terminal;

    @BeforeEach
    void setup(){
        this.terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);
    }

    @Test
    void testUnloadWagon() throws DoubleDefinitionException {
        Wagon wagon = new Wagon("W1");
        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box = new Box("box01", 4, a, date, dateTime);

        int n = 10;
        for(int i = 0; i < n; i++){
            wagon.addBox(box);
        }

        System.out.println("The wagon contains " + n + " boxes.");

        terminal.addBayToWarehouse("W001", 0, 0, 3);
        terminal.addBayToWarehouse("W001", 0, 1, 5);

        terminal.addBayToWarehouse("W002", 0, 0, 1);
        terminal.addBayToWarehouse("W002", 0, 1, 4);

        Warehouse w1 = terminal.getWarehouse("W001");
        Warehouse w2 = terminal.getWarehouse("W002");

        System.out.println("available space in w1 before unloading = " + w1.availableSpace());
        System.out.println("available space in w2 before unloading = " + w2.availableSpace());

        int expected1, expected2;
        if((w1.availableSpace() - wagon.getSize()) < 0){
            expected1 = 0;
            expected2 = w2.availableSpace() + (w1.availableSpace() - wagon.getSize());
            if(expected2 < 0) {
                expected2 = 0;
            }
        } else {
            expected1 = w1.availableSpace() - wagon.getSize();
            expected2 = w2.availableSpace();
        }

        terminal.unloadWagon(wagon);
        System.out.println("Wagon size after unloading: " + wagon.getSize() + " boxes.");

        System.out.println("available space in w1 after unloading = " + w1.availableSpace());
        System.out.println("available space in w2 after unloading = " + w2.availableSpace());

        assertEquals(expected1, w1.availableSpace());
        assertEquals(expected2, w2.availableSpace());
    }

    @Test
    void testUnavailableWarehouses() throws DoubleDefinitionException {
        Wagon wagon = new Wagon("W1");
        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box = new Box("box01", 4, a, date, dateTime);

        wagon.addBox(box);

        try{
            terminal.unloadWagon(wagon);
            fail();
        } catch (WarehousesFullException e) {
            assertTrue(true);
        }
    }

    @Test
    void testAllWarehousesFull() throws DoubleDefinitionException {
        Wagon wagon = new Wagon("W1");
        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box = new Box("box01", 4, a, date, dateTime);

        terminal.addBayToWarehouse("W001", 0, 0, 3);
        terminal.addBayToWarehouse("W002", 0, 0, 1);

        Warehouse w1 = terminal.getWarehouse("W001");
        Warehouse w2 = terminal.getWarehouse("W002");

        int totalSize = w1.availableSpace() + w2.availableSpace();
        for(int i = 0; i < totalSize; i++){
            wagon.addBox(box);
        }

        int expected1, expected2;
        if((w1.availableSpace() - wagon.getSize()) < 0){
            expected1 = 0;
            expected2 = w2.availableSpace() + (w1.availableSpace() - wagon.getSize());
            if(expected2 < 0) {
                expected2 = 0;
            }
        } else {
            expected1 = w1.availableSpace() - wagon.getSize();
            expected2 = w2.availableSpace();
        }

        terminal.unloadWagon(wagon);
        assertEquals(expected1, w1.availableSpace());
        assertEquals(expected2, w2.availableSpace());

        wagon.addBox(box);
        try{
            terminal.unloadWagon(wagon);
            fail();
        } catch (WarehousesFullException e) {
            assertTrue(true);
        }
    }
}
