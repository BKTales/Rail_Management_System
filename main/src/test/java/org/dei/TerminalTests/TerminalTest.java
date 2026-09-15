package org.dei.TerminalTests;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Order.Order;
import org.dei._Facilities.Terminal.Warehouse.PickupPlan;
import org.dei.Sprint1.Repository.ItemRepository;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Utils.Utils;
import org.dei.Sprint1.Parser.DoubleDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.dei._Facilities.Terminal.Warehouse.Allocation;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TerminalTest {
    private Terminal terminal;
    private Item itemA, itemB, itemC, itemD, itemE, itemF, itemG, itemH, itemI, itemJ, itemK;

    @BeforeEach
    void setUp() {
        terminal = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        itemA = new Item(Unit.valueOf("PACK"), "SKU_A", 0.5, ItemType.valueOf("ELECTRONICS"), 2.0);
        itemB = new Item(Unit.valueOf("PACK"), "SKU_B", 1.0, ItemType.valueOf("ELECTRONICS"), 5.0);
        itemC = new Item(Unit.valueOf("PACK"), "SKU_C", 2.0, ItemType.valueOf("ELECTRONICS"), 1.0);

        itemC = new Item(Unit.valueOf("PACK"), "SKU_C", 1.0, ItemType.valueOf("ELECTRONICS"), 1.0);
        itemD = new Item(Unit.valueOf("PACK"), "SKU_D", 1.0, ItemType.valueOf("ELECTRONICS"), 2.0);
        itemE = new Item(Unit.valueOf("PACK"), "SKU_E", 1.0, ItemType.valueOf("ELECTRONICS"), 3.0);
        itemF = new Item(Unit.valueOf("PACK"), "SKU_F", 1.0, ItemType.valueOf("ELECTRONICS"), 4.0);
        itemG = new Item(Unit.valueOf("PACK"), "SKU_G", 1.0, ItemType.valueOf("ELECTRONICS"), 5.0);
        itemH = new Item(Unit.valueOf("PACK"), "SKU_H", 1.0, ItemType.valueOf("ELECTRONICS"), 6.0);
        itemI = new Item(Unit.valueOf("PACK"), "SKU_I", 1.0, ItemType.valueOf("ELECTRONICS"), 7.0);
        itemJ = new Item(Unit.valueOf("PACK"), "SKU_J", 1.0, ItemType.valueOf("ELECTRONICS"), 8.0);
        itemK = new Item(Unit.valueOf("PACK"), "SKU_K", 1.0, ItemType.valueOf("ELECTRONICS"), 9.0);



        ItemRepository repo = ItemRepository.getInstance();
        repo.clear();
        repo.addItem(itemA);
        repo.addItem(itemB);
        repo.addItem(itemC);
        repo.addItem(itemD);
        repo.addItem(itemE);
        repo.addItem(itemF);
        repo.addItem(itemG);
        repo.addItem(itemH);
        repo.addItem(itemI);
        repo.addItem(itemJ);
        repo.addItem(itemK);

    }

    /**
     * Helper method to create an OrderLineEligibility and fill it with an Allocation.
     */
    private void makeOLE(Order order,int index) {
        if (index == 1) order.getItems().get(0).addAllocation("WH1",new WarehousePosition(1,1, 0),"Box1", 1);
        if (index == 2) {
            order.getItems().get(0).addAllocation("WH1",new WarehousePosition(1,1, 0),"Box1", 1);
            order.getItems().get(1).addAllocation("WH1",new WarehousePosition(2,1, 0),"Box2", 1);
        }if (index == 3) {
            order.getItems().get(0).addAllocation("WH1",new WarehousePosition(1,1, 0),"Box1", 1);
            order.getItems().get(1).addAllocation("WH1",new WarehousePosition(2,1, 0),"Box2", 1);
            order.getItems().get(2).addAllocation("WH1",new WarehousePosition(3,1, 0),"Box3", 1);
        }else if (index == 4){
            order.getItems().get(0).addAllocation("WH1",new WarehousePosition(1,1, 0),"Box1", 1);
            order.getItems().get(1).addAllocation("WH1",new WarehousePosition(2,1, 0),"Box2", 1);
            order.getItems().get(2).addAllocation("WH1",new WarehousePosition(3,1, 0),"Box3", 1);
            order.getItems().get(3).addAllocation("WH1",new WarehousePosition(4,1, 0),"Box4", 1);
            order.getItems().get(4).addAllocation("WH1",new WarehousePosition(5,1, 0),"Box5", 1);
            order.getItems().get(5).addAllocation("WH1",new WarehousePosition(6,1, 0),"Box6", 1);
            order.getItems().get(6).addAllocation("WH1",new WarehousePosition(7,1, 0),"Box7", 1);
            order.getItems().get(7).addAllocation("WH1",new WarehousePosition(8,1, 0),"Box8", 1);
            order.getItems().get(8).addAllocation("WH1",new WarehousePosition(9,1, 0),"Box9", 1);
        }
    }

    @Test
    void testConstructor() {
        // Act
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        // Assert
        assertNotNull(terminal, "Terminal should be instantiated without throwing an exception");
    }

    @Test
    void testToStringEmpty() {
        // Arrange
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        // Act
        String result = terminal.toString();

        // Assert
        assertEquals("Terminal is empty!", result, "toString should return an empty string when there are no warehouses");
    }

    @Test
    void testToStringWithWarehouses() {
        //Arrange
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);

        Warehouse w1 = new Warehouse("1");
        Warehouse w2 = new Warehouse("w2");

        terminal.getWarehouses().add(w1);
        terminal.getWarehouses().add(w2);


        // Act
        String result = terminal.toString();

        // Assert
        String expected = w1.toString() + "\n" + w2.toString() + "\n";
        assertEquals(expected, result, "toString should concatenate the toString of warehouses correctly");
    }

    // First Fit Tests
    @Test
    void testEmptyListFF() {
        Order order = new Order("1", LocalDateTime.now(), 1);
        PickupPlan pickupPlan = terminal.firstFit(order, 10.0);
        assertTrue(pickupPlan.getTrolleys().isEmpty(), "No trolleys should be created for empty input");
    }

    @Test
    void testSingleAllocationFitsFF() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        makeOLE(order,1);
        PickupPlan pickupPlan = terminal.firstFit(order, 10.0);
        assertEquals(1, pickupPlan.getTrolleys().size(), "One trolley should be created");
    }

    @Test
    void testTwoAllocationsFitInSameTrolleyFF() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        order.addItem(itemB,1);
        makeOLE(order,2);
        PickupPlan pickupPlan = terminal.firstFit(order, 10.0);
        assertEquals(1, pickupPlan.getTrolleys().size(), "Both allocations should fit in one trolley");
    }

    @Test
    void testTwoAllocationsNeedTwoTrolleysFF() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        order.addItem(itemB,2);
        makeOLE(order,2);
        PickupPlan pickupPlan = terminal.firstFit(order, 5);
        assertTrue(pickupPlan.getTrolleys().size() >= 2, "Should create at least two trolleys");
    }

    @Test
    void testAllocationExactlyFillsTrolleyFF() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,2);
        order.addItem(itemB,2);
        makeOLE(order,2);
        PickupPlan pickupPlan = terminal.firstFit(order, 10.0);
        assertEquals(1, pickupPlan.getTrolleys().size(), "One full trolley expected");
    }

    // First Fit Decreasing Tests
    @Test
    void testMergeSortAllocationDescendingOrder() {
        List<Allocation> allocations = new ArrayList<>();

        Allocation a1 = new Allocation(new WarehousePosition(1, 1, 0),"WH1","Box1", 1, itemA.getSku());
        Allocation a2 = new Allocation(new WarehousePosition(2, 1, 0),"WH1","Box2", 4, itemB.getSku());
        Allocation a3 = new Allocation(new WarehousePosition(3, 1, 0),"WH1", "Box3", 3, itemA.getSku());
        Allocation a4 = new Allocation(new WarehousePosition(4, 1, 0),"WH1", "Box4", 5, itemA.getSku());
        Allocation a5 = new Allocation(new WarehousePosition(5, 1, 0),"WH1", "Box5", 5, itemB.getSku());
        Allocation a6 = new Allocation(new WarehousePosition(6, 1, 0),"WH1", "Box6", 3, itemA.getSku());


        allocations.add(a1);
        allocations.add(a2);
        allocations.add(a3);
        allocations.add(a4);
        allocations.add(a5);
        allocations.add(a6);

        List<Allocation> sorted = Utils.mergeSortAllocationDESC(allocations);

        // Check if sorted in descending order by weighted value
        for (int i = 0; i < sorted.size() - 1; i++) {
            double current = sorted.get(i).getWeighted();
            double next = sorted.get(i + 1).getWeighted();
            assertTrue(current >= next,
                    String.format("List should be sorted descending: %.2f >= %.2f", current, next));
        }
    }


    @Test
    void testEmptyListFFD() {
        Order order = new Order("1", LocalDateTime.now(), 1);
        PickupPlan pickupPlan = terminal.firstFitDecreasing(order, 10.0);
        assertTrue(pickupPlan.getTrolleys().isEmpty(), "No trolleys should be created for empty input");

    }

    @Test
    void testSingleAllocationFitsFFD() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        makeOLE(order,1);
        PickupPlan pickupPlan = terminal.firstFitDecreasing(order, 10.0);
        assertEquals(1, pickupPlan.getTrolleys().size(), "One trolley should be created");
    }

    @Test
    void testTwoAllocationsFitInSameTrolleyAndOrderFFD() {
        // Arrange
        Order order = new Order("Order1", LocalDateTime.now(), 1);
        order.addItem(itemA, 1); // middle
        order.addItem(itemB, 1); // heavier
        order.addItem(itemC, 1); // lighter
        makeOLE(order, 3);


        // Act
        PickupPlan pickupPlan = terminal.firstFitDecreasing(order, 10.0);


        // Assert: all allocations should fit in one trolley
        assertEquals(1, pickupPlan.getTrolleys().size(), "Both allocations should fit in one trolley");
        List<Allocation> allocations = pickupPlan.getTrolleys().get(0).getAllocations();

        assertEquals(3, allocations.size(), "There should be three allocations in the trolley");

        // Heavier item should come before lighter
        Allocation first = allocations.get(0);
        Allocation second = allocations.get(1);
        Allocation last = allocations.get(2);

        double firstWeight = first.getWeighted();
        double secondWeight = second.getWeighted();
        double lastWeight = last.getWeighted();

        assertTrue((firstWeight >= secondWeight && secondWeight >= lastWeight),
                String.format("Allocations should be in decreasing order: %.2f >= %.2f", firstWeight, secondWeight));
    }

    @Test
    void testTwoAllocationsNeedTwoTrolleysFFD() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        order.addItem(itemB,1);
        makeOLE(order,2);
        PickupPlan pickupPlan = terminal.firstFitDecreasing(order, 6.0);

        assertTrue(pickupPlan.getTrolleys().size() >= 2, "Should create at least two trolleys");
    }

// BFD TESTS ----
    @Test
    void testEmptyListBFD(){
        Order order = new Order("Order1", LocalDateTime.now(),1);
        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 10.0);

        assertTrue(pickupPlan.getTrolleys().isEmpty(), "No trolleys should be created");
    }

    @Test
    void testSingleAllocationFitsBFD() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        makeOLE(order,1);
        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 10.0);

        assertEquals(1, pickupPlan.getTrolleys().size(), "One trolley should be created");
    }

    @Test
    void testDoubleAllocationOneTrolleyBFD(){
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);
        order.addItem(itemB,1);

        makeOLE(order,2);

        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 25.0);

        assertEquals(1, pickupPlan.getTrolleys().size(), "Both allocations should fit in one trolley");
    }

    @Test
    void testTwoAllocationsNeedTwoTrolleysBFD() {
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,4);
        order.addItem(itemB,2);
        makeOLE(order,2);

        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 5.0);

        assertTrue(pickupPlan.getTrolleys().size() >= 2, "Should create at least two trolleys");
    }

    @Test
    void testNoCapacityForTrolleyBFD(){
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemA,1);

        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 0.5);

        assertEquals(0, pickupPlan.getTrolleys().size(), "No trolleys should be created");
    }

    @Test
    void testBFDFeature(){
        Order order = new Order("Order1", LocalDateTime.now(),1);
        order.addItem(itemC,1);
        order.addItem(itemD, 1);
        order.addItem(itemE, 1);
        order.addItem(itemF, 1);
        order.addItem(itemG, 1);
        order.addItem(itemH, 1);
        order.addItem(itemI, 1);
        order.addItem(itemJ, 1);
        order.addItem(itemK, 1);

        makeOLE(order,4);

        PickupPlan pickupPlan = terminal.bestFitDecreasing(order, 10.0);

        assertEquals(5, pickupPlan.getTrolleys().size(), "Three trolley should be created");
    }

    // ---------

    @Test
    void testFirstAvailableWarehouse() throws DoubleDefinitionException {
        Warehouse w = terminal.firstAvailableWarehouse();
        assertNull(w);

        terminal.addBayToWarehouse("W001", 0, 0, 2);
        assertEquals("W001", terminal.firstAvailableWarehouse().getId());

        terminal.addBayToWarehouse("W002", 0, 0, 5);
        assertEquals("W001", terminal.firstAvailableWarehouse().getId());

        Item a = new Item(Unit.BOTTLE, "SKU003", 2.2, ItemType.ELECTRONICS, 30);
        LocalDate date = LocalDate.parse("2020-10-05");
        LocalDateTime dateTime = LocalDateTime.parse("2025-08-14T02:43:00");
        Box box = new Box("box01", 4, a, date, dateTime);

        Warehouse w1 = terminal.firstAvailableWarehouse();
        for(Aisle as : w1.getAisles()){
            for(Bay b : as.getBays()){
                while(!b.isFull()){
                    b.addBox(box);
                }
            }
        }
        // W001 IS NOW FULL

        Warehouse w2 = terminal.firstAvailableWarehouse();
        assertEquals("W002", w2.getId());

        for(Aisle as : w2.getAisles()){
            for(Bay b : as.getBays()){
                while(!b.isFull()){
                    b.addBox(box);
                }
            }
        }
        // W002 IS NOW FULL, no more available warehouses left, should return null

        Warehouse w3 = terminal.firstAvailableWarehouse();
        assertNull(w3);
    }
}