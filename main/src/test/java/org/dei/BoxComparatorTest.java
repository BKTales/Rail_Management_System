package org.dei;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei.Utils.ComparatorsUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoxComparatorTest {
    @Test
    void testBoxComparatorExpiringDate() {
        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        Item a4 = new Item(Unit.BOTTLE, "SKU004", 2.2, ItemType.ELECTRONICS, 30);

        LocalDate date1 = LocalDate.parse("2000-10-03");
        LocalDate date2 = LocalDate.parse("2000-01-02");
        LocalDate date3 = LocalDate.parse("2001-06-05");
        LocalDate date4 = null;

        LocalDateTime dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        LocalDateTime dateTime2 = LocalDateTime.parse("2025-08-14T02:43:00");
        LocalDateTime dateTime3 = LocalDateTime.parse("2025-08-15T02:43:00");
        LocalDateTime dateTime4 = LocalDateTime.parse("2025-09-15T02:43:00");

        Box box1 = new Box("box01", 4, a1, date1, dateTime1);
        Box box2 = new Box("box02", 1, a3, date2, dateTime2);
        Box box3 = new Box("box03", 2, a2, date3, dateTime3);
        Box box4 = new Box("box04", 3, a4, date4, dateTime4);

        List<Box> orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box2);
        orderedBoxList.add(box1);
        orderedBoxList.add(box3);
        orderedBoxList.add(box4);

        PriorityQueue<Box> boxQueue = new PriorityQueue<>(ComparatorsUtils.boxComparatorByDates);
        boxQueue.add(box3);
        boxQueue.add(box4);
        boxQueue.add(box2);
        boxQueue.add(box1);

        List<Box> boxQueueList = new ArrayList<>();
        while(!boxQueue.isEmpty()){
            boxQueueList.add(boxQueue.poll());
        }

        assertEquals(orderedBoxList, boxQueueList);
    }

    @Test
    void testBoxComparatorReceivedAtDate() {
        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);

        LocalDate date1 = LocalDate.parse("2000-10-03");
        LocalDate date2 = LocalDate.parse("2000-10-03");
        LocalDate date3 = LocalDate.parse("2000-10-03");

        LocalDateTime dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        LocalDateTime dateTime2 = LocalDateTime.parse("2025-08-01T02:43:00");
        LocalDateTime dateTime3 = LocalDateTime.parse("2025-09-15T02:43:00");

        Box box1 = new Box("box01", 4, a1, date1, dateTime1);
        Box box2 = new Box("box02", 1, a3, date2, dateTime2);
        Box box3 = new Box("box03", 2, a2, date3, dateTime3);

        List<Box> orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box2);
        orderedBoxList.add(box1);
        orderedBoxList.add(box3);

        PriorityQueue<Box> boxQueue = new PriorityQueue<>(ComparatorsUtils.boxComparatorByDates);
        boxQueue.add(box3);
        boxQueue.add(box2);
        boxQueue.add(box1);

        List<Box> boxQueueList = new ArrayList<>();
        while(!boxQueue.isEmpty()){
            boxQueueList.add(boxQueue.poll());
        }

        assertEquals(orderedBoxList, boxQueueList);
    }

    @Test
    void testBoxComparatorBoxID(){
        Item a1 = new Item(Unit.BOTTLE, "SKU001", 2.2, ItemType.GROCERY, 10);
        Item a2 = new Item(Unit.BOTTLE, "SKU002", 2.2, ItemType.GROCERY, 20);
        Item a3 = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);

        LocalDate date1 = LocalDate.parse("2000-10-03");
        LocalDate date2 = LocalDate.parse("2000-10-03");
        LocalDate date3 = LocalDate.parse("2000-10-03");

        LocalDateTime dateTime1 = LocalDateTime.parse("2025-08-14T02:43:00");
        LocalDateTime dateTime2 = LocalDateTime.parse("2025-08-14T02:43:00");
        LocalDateTime dateTime3 = LocalDateTime.parse("2025-08-14T02:43:00");

        Box box1 = new Box("box01", 4, a1, date1, dateTime1);
        Box box2 = new Box("box02", 1, a3, date2, dateTime2);
        Box box3 = new Box("box03", 2, a2, date3, dateTime3);

        List<Box> orderedBoxList = new ArrayList<>();
        orderedBoxList.add(box1);
        orderedBoxList.add(box2);
        orderedBoxList.add(box3);

        PriorityQueue<Box> boxQueue = new PriorityQueue<>(ComparatorsUtils.boxComparatorByDates);
        boxQueue.add(box2);
        boxQueue.add(box3);
        boxQueue.add(box1);

        List<Box> boxQueueList = new ArrayList<>();
        while(!boxQueue.isEmpty()){
            boxQueueList.add(boxQueue.poll());
        }

        assertEquals(orderedBoxList, boxQueueList);
    }
}
