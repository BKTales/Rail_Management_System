package org.dei;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._Train.Wagon;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WagonTest {

    private Wagon wagon;
    private Item a1, a2, a3;
    private LocalDate date1, date2, date3;
    private LocalDateTime dateTime1, dateTime2, dateTime3;
    private Box box1, box2, box3;
    private List<Box> orderedBoxList;


    void setup(){
        wagon = new Wagon("5");

        a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);

        box1 = new Box("box01", 4, a1, date1, dateTime1);
        box2 = new Box("box02", 1, a2, date2, dateTime2);
        box3 = new Box("box03", 2, a3, date3, dateTime3);
    }

    void initializeNoOrder(){
        date1 = LocalDate.parse("2000-10-03");
        date2 = LocalDate.parse("2000-01-02");
        date3 = LocalDate.parse("2001-06-05");

        dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime2 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime3 = LocalDateTime.parse("2025-08-15T02:43:00");

        setup();
    }

    void initializeOrderedByExpiry(){
        date1 = LocalDate.parse("2000-10-03");
        date2 = LocalDate.parse("2000-01-02");
        date3 = LocalDate.parse("2001-06-05");

        dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime2 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime3 = LocalDateTime.parse("2025-08-15T02:43:00");

        setup();

        wagon.addBox(box2);
        wagon.addBox(box3);
        wagon.addBox(box1);

        orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box2);
        orderedBoxList.add(box1);
        orderedBoxList.add(box3);
    }

    void initializeOrderedByReceivedAt(){
        date1 = LocalDate.parse("2000-10-03");
        date2 = LocalDate.parse("2000-10-03");
        date3 = LocalDate.parse("2000-10-03");

        dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime2 = LocalDateTime.parse("2025-08-01T02:43:00");
        dateTime3 = LocalDateTime.parse("2025-09-15T02:43:00");

        setup();

        wagon.addBox(box2);
        wagon.addBox(box3);
        wagon.addBox(box1);

        orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box2);
        orderedBoxList.add(box1);
        orderedBoxList.add(box3);
    }

    void initializeOrderedByBoxID(){
        date1 = LocalDate.parse("2000-10-03");
        date2 = LocalDate.parse("2000-10-03");
        date3 = LocalDate.parse("2000-10-03");

        dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime2 = LocalDateTime.parse("2025-08-14T02:43:00");
        dateTime3 = LocalDateTime.parse("2025-08-14T02:43:00");

        setup();

        wagon.addBox(box2);
        wagon.addBox(box3);
        wagon.addBox(box1);

        orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box1);
        orderedBoxList.add(box2);
        orderedBoxList.add(box3);
    }

    @Test
    void testGetWagonId() {
        Wagon wagon = new Wagon("10");
        assertEquals("10", wagon.getWagonId());
    }

//    @Test
//    void testToString() {
//        Wagon wagon = new Wagon("5");
//        assertEquals("WagonId: 5\n", wagon.toString());
//    }

    @Test
    void testAddBox(){
        initializeNoOrder();

        assertEquals(0, wagon.getSize());

        wagon.addBox(box1);
        assertEquals(1, wagon.getSize());

        wagon.addBox(box2);
        assertEquals(2, wagon.getSize());
    }

    @Test
    void testBoxesSize(){
        initializeNoOrder();

        wagon.addBox(box2);
        wagon.addBox(box3);
        wagon.addBox(box1);

        int expected = 3;

        assertEquals(expected, wagon.getSize());
    }

    @Test
    void testGetUnloadBox(){
        initializeNoOrder();

        wagon.addBox(box2);
        wagon.addBox(box3);
        wagon.addBox(box1);

        Box unloadedBox = wagon.getUnloadBox();
        assertEquals(unloadedBox, box2);

        unloadedBox = wagon.getUnloadBox();
        assertEquals(unloadedBox, box1);

        unloadedBox = wagon.getUnloadBox();
        assertEquals(unloadedBox, box3);

        unloadedBox = wagon.getUnloadBox();
        assertNull(unloadedBox);
    }

    @Test
    void testBoxOrderByExpiryDate() {
        initializeOrderedByExpiry();

        List<Box> boxList1 = new ArrayList<>();
        while(!wagon.getBoxes().isEmpty()){
            boxList1.add(wagon.getBoxes().poll());
        }

        assertEquals(boxList1, orderedBoxList);
    }

    @Test
    void testBoxOrderByReceivedAtDate() {
        initializeOrderedByReceivedAt();

        List<Box> boxList1 = new ArrayList<>();
        while(!wagon.getBoxes().isEmpty()){
            boxList1.add(wagon.getBoxes().poll());
        }

        assertEquals(boxList1, orderedBoxList);
    }

    @Test
    void testBoxOrderByBoxID() {
        initializeOrderedByBoxID();

        List<Box> boxList1 = new ArrayList<>();
        while(!wagon.getBoxes().isEmpty()){
            boxList1.add(wagon.getBoxes().poll());
        }

        assertEquals(boxList1, orderedBoxList);
    }
}