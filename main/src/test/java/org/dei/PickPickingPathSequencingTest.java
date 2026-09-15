package org.dei;

import org.dei._Facilities.Terminal.Warehouse.*;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1._Item.ItemType;
import org.dei.Sprint1._Item.Unit;
import org.dei._Facilities.Terminal.Warehouse.PickingPath;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Repository.ItemRepository;
import org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing.AscendingAisle;
import org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing.NearestNeighbour;
import org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing.PickPathSequencing;
import org.dei._Facilities.Terminal.Terminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PickPickingPathSequencingTest {

    private Terminal terminal;

    @BeforeEach
    void setup(){
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);
    }

    void setupItems(){
        ItemRepository itemRepository = ItemRepository.getInstance();
        Item i1 = new Item(Unit.PACK, "SKU00001", 2.5, ItemType.GROCERY, 2.5);
        Item i2 = new Item(Unit.BOTTLE, "SKU00002", 3.5, ItemType.CLEANING, 3.5);
        Item i3 = new Item(Unit.BOX, "SKU00003", 4.5, ItemType.HARDWARE, 4.5);
        itemRepository.addItem(i1);
        itemRepository.addItem(i2);
        itemRepository.addItem(i3);
    }

    @Test
    void testDistanceFromStart(){
        WarehousePosition p1 = new WarehousePosition(0, 0, 0);
        WarehousePosition p2 = new WarehousePosition(8, 1, 0);
        // (0,0) -> (1,8)
        // expected = 8
        int expected = p1.getBayIndex() + Math.abs(p1.getAisleIndex() - p2.getAisleIndex()) * 3 + p2.getBayIndex();
        int result = PickPathSequencing.distance(p1, p2);
        assertEquals(expected, result);
    }

    @Test
    void testDistanceSameAisle(){
        WarehousePosition p1 = new WarehousePosition(1, 1, 0);
        WarehousePosition p2 = new WarehousePosition(3, 1, 0);
        // (1,1) -> (1,3)
        // expected = 2
        int expected = Math.abs(p1.getBayIndex() - p2.getBayIndex());
        int result = PickPathSequencing.distance(p1, p2);
        assertEquals(expected, result);

        p1 = new WarehousePosition(5, 2, 0);
        p2 = new WarehousePosition(3, 2, 0);
        // (2,5) -> (2,3)
        expected = Math.abs(p1.getBayIndex() - p2.getBayIndex());
        result = PickPathSequencing.distance(p1, p2);
        assertEquals(expected, result);
    }

    @Test
    void testDistanceDifferentAisle(){
        WarehousePosition p1 = new WarehousePosition(2, 1, 0);
        WarehousePosition p2 = new WarehousePosition(3, 3, 0);
        // (1,2) -> (3,3)
        // expected = 11
        int expected = p1.getBayIndex() + Math.abs(p1.getAisleIndex() - p2.getAisleIndex()) * 3 + p2.getBayIndex();
        int result = PickPathSequencing.distance(p1, p2);
        assertEquals(expected, result);

        p1 = new WarehousePosition(5, 2, 0);
        p2 = new WarehousePosition(8, 1, 0);
        // (2,5) -> (1,8)
        // expected = 16
        expected = p1.getBayIndex() + Math.abs(p1.getAisleIndex() - p2.getAisleIndex()) * 3 + p2.getBayIndex();
        result = PickPathSequencing.distance(p1, p2);
        assertEquals(expected, result);
    }

    @Test
    void testMergePositions(){
        WarehousePosition p1 = new WarehousePosition(3, 2, 0);
        WarehousePosition p2 = new WarehousePosition(2, 1, 0);
        WarehousePosition p3 = new WarehousePosition(2, 1, 0);
        WarehousePosition p4 = new WarehousePosition(3, 2, 0);
        WarehousePosition p5 = new WarehousePosition(5,5, 0);
        WarehousePosition p6 = new WarehousePosition(2, 1, 0);

        ArrayList<WarehousePosition> warehousePositionList = new ArrayList<>();
        warehousePositionList.add(p1);
        warehousePositionList.add(p2);
        warehousePositionList.add(p3);
        warehousePositionList.add(p4);
        warehousePositionList.add(p5);
        warehousePositionList.add(p6);

        List<WarehousePosition> expectedMergedList = new ArrayList<>();
        expectedMergedList.add(p1);
        expectedMergedList.add(p2);
        expectedMergedList.add(p5);

        List<WarehousePosition> mergedWarehousePositionList = PickPathSequencing.mergePositions(warehousePositionList);

        assertEquals(expectedMergedList, mergedWarehousePositionList);
    }

    @Test
    void testAscendingAisle(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);
        WarehousePosition p2 = new WarehousePosition(8,1, 0);
        WarehousePosition p3 = new WarehousePosition(2,2, 0);
        WarehousePosition p4 = new WarehousePosition(4,3, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);
        warehousePositions.add(p2);
        warehousePositions.add(p3);
        warehousePositions.add(p4);

        ArrayList<WarehousePosition> allPositionsA = new ArrayList<>(warehousePositions);
        int totalDistanceA = AscendingAisle.processPathA(allPositionsA);
        int expected = 0;
        for(int i = 0; i < allPositionsA.size() - 1; i++){
            expected = expected + PickPathSequencing.distance(allPositionsA.get(i), allPositionsA.get(i + 1));
        }
        assertEquals(expected, totalDistanceA);
    }

    @Test
    void testAscendingAisleEmptyList(){
        List<WarehousePosition> warehousePositions = new ArrayList<>();
        ArrayList<WarehousePosition> allPositionsA = new ArrayList<>(warehousePositions);
        int totalDistanceA = AscendingAisle.processPathA(allPositionsA);
        int expected = 0;
        assertEquals(expected, totalDistanceA);
    }

    @Test
    void testAscendingAisleNoPositions(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);

        ArrayList<WarehousePosition> allPositionsA = new ArrayList<>(warehousePositions);
        int totalDistanceA = AscendingAisle.processPathA(allPositionsA);
        int expected = 0;
        assertEquals(expected, totalDistanceA);
    }

    @Test
    void testAscendingAisleOnePosition(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);
        WarehousePosition p2 = new WarehousePosition(8, 1, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);
        warehousePositions.add(p2);

        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        int expected = 11;
        assertEquals(expected, totalDistanceB);
    }

    @Test
    void testNearestNeighbour(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);
        WarehousePosition p2 = new WarehousePosition(8,1, 0);
        WarehousePosition p3 = new WarehousePosition(2,2, 0);
        WarehousePosition p4 = new WarehousePosition(4,3, 0);
        WarehousePosition p5 = new WarehousePosition(8,1, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);
        warehousePositions.add(p2);
        warehousePositions.add(p3);
        warehousePositions.add(p4);
        // duplicated position, merge will deduplicate-it
        warehousePositions.add(p5);

        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        int expected = 35;
        assertEquals(expected, totalDistanceB);
    }

    @Test
    void testNearestNeighbourEmptyList(){
        List<WarehousePosition> warehousePositions = new ArrayList<>();
        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        int expected = 0;
        assertEquals(expected, totalDistanceB);
    }

    @Test
    void testNearestNeighbourNoPositions(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);

        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        int expected = 0;
        assertEquals(expected, totalDistanceB);
    }

    @Test
    void testNearestNeighbourOnePosition(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);
        WarehousePosition p2 = new WarehousePosition(8, 1, 0);

        List<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p1);
        warehousePositions.add(p2);

        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        int expected = 11;
        assertEquals(expected, totalDistanceB);
    }

    @Test
    void testBuildPositionsList(){
        setupItems();

        Trolley trolley = new Trolley(50);
        WarehousePosition p1 = new WarehousePosition(5, 1, 0);
        WarehousePosition p2 = new WarehousePosition(2, 2, 0);
        WarehousePosition p3 = new WarehousePosition(6, 3, 0);
        Allocation a1 = new Allocation(p1, "W001", "BOX00001", 2, "SKU00001");
        Allocation a2 = new Allocation(p2, "W001", "BOX00002", 2, "SKU00002");
        Allocation a3 = new Allocation(p3, "W001", "BOX00003", 2, "SKU00003");
        trolley.addAllocation(a1);
        trolley.addAllocation(a2);
        trolley.addAllocation(a3);

        List<WarehousePosition> expectedWarehousePositionList = new ArrayList<>();
        expectedWarehousePositionList.add(p1);
        expectedWarehousePositionList.add(p2);
        expectedWarehousePositionList.add(p3);

        ArrayList<WarehousePosition> warehousePositions = PickPathSequencing.buildPositionsList(trolley);
        System.out.println(warehousePositions);

        assertEquals(expectedWarehousePositionList, warehousePositions);
    }

    @Test
    void testCalculatePaths(){
        WarehousePosition p1 = new WarehousePosition(0,0, 0);
        WarehousePosition p2 = new WarehousePosition(8,1, 0);
        WarehousePosition p3 = new WarehousePosition(2,2, 0);
        WarehousePosition p4 = new WarehousePosition(4,3, 0);
        ArrayList<WarehousePosition> warehousePositions = new ArrayList<>();
        warehousePositions.add(p2);
        warehousePositions.add(p3);
        warehousePositions.add(p4);

        List<WarehousePosition> finalPositionsA = new ArrayList<>();
        finalPositionsA.add(p1);
        finalPositionsA.add(p2);
        finalPositionsA.add(p3);
        finalPositionsA.add(p4);

        List<WarehousePosition> finalPositionsB = new ArrayList<>();
        finalPositionsB.add(p1);
        finalPositionsB.add(p3);
        finalPositionsB.add(p4);
        finalPositionsB.add(p2);

        PickingPath pickingPathA = new PickingPath(finalPositionsA, 33, "");
        PickingPath pickingPathB = new PickingPath(finalPositionsB, 35, "");

        PickingPath[] pickingPaths = PickPathSequencing.calculatePaths(warehousePositions, "");
        System.out.println(pickingPaths[0]);
        System.out.println(pickingPaths[1]);

        assertEquals(pickingPathA, pickingPaths[0]);
        assertEquals(pickingPathB, pickingPaths[1]);
    }

    @Test
    void testGetPathsNullPlan(){
        Map<Trolley, List<PickingPath>> pathMap = PickPathSequencing.getPaths(null);
        assertNull(pathMap);
    }

    @Test
    void testGetPathsNoTrolleys(){
        PickupPlan p = new PickupPlan(Heuristic.FF);
        Map<Trolley, List<PickingPath>> emptyPathMap = new HashMap<>();
        Map<Trolley, List<PickingPath>> pathMap = PickPathSequencing.getPaths(p);
        assertEquals(emptyPathMap, pathMap);
    }

    @Test
    void testGetPaths(){
        setupItems();

        PickupPlan p = new PickupPlan(Heuristic.FF);

        Trolley trolley1 = new Trolley(50);
        WarehousePosition p1 = new WarehousePosition(5, 1, 0);
        WarehousePosition p2 = new WarehousePosition(2, 2, 0);
        WarehousePosition p3 = new WarehousePosition(6, 3, 0);
        Allocation a1 = new Allocation(p1, "W001", "BOX00001", 2, "SKU00001");
        Allocation a2 = new Allocation(p2, "W001", "BOX00002", 2, "SKU00002");
        Allocation a3 = new Allocation(p3, "W001", "BOX00003", 2, "SKU00003");
        trolley1.addAllocation(a1);
        trolley1.addAllocation(a2);
        trolley1.addAllocation(a3);
        p.addTrolley(trolley1);

        Trolley trolley2 = new Trolley(55);
        WarehousePosition p4 = new WarehousePosition(4, 1, 0);
        WarehousePosition p5 = new WarehousePosition(2, 5, 0);
        WarehousePosition p6 = new WarehousePosition(3, 4, 0);
        Allocation a4 = new Allocation(p4, "W001", "BOX00001", 2, "SKU00001");
        Allocation a5 = new Allocation(p5, "W001", "BOX00002", 2, "SKU00002");
        Allocation a6 = new Allocation(p6, "W001", "BOX00003", 2, "SKU00003");
        trolley2.addAllocation(a4);
        trolley2.addAllocation(a5);
        trolley2.addAllocation(a6);
        p.addTrolley(trolley2);

        Map<Trolley, List<PickingPath>> expectedPathMap = new HashMap<>();

        String warehouseId1 = trolley1.getAllocations().getFirst().getWarehouseId();
        ArrayList<WarehousePosition>  positions1 = PickPathSequencing.buildPositionsList(trolley1);
        PickingPath[] trolleyPickingPaths = PickPathSequencing.calculatePaths(positions1,warehouseId1);
        expectedPathMap.put(trolley1, new ArrayList<>());
        expectedPathMap.get(trolley1).add(trolleyPickingPaths[0]);
        expectedPathMap.get(trolley1).add(trolleyPickingPaths[1]);

        String warehouseId2 = trolley2.getAllocations().getFirst().getWarehouseId();
        ArrayList<WarehousePosition>  positions2 = PickPathSequencing.buildPositionsList(trolley2);
        trolleyPickingPaths = PickPathSequencing.calculatePaths(positions2,warehouseId2);
        expectedPathMap.put(trolley2, new ArrayList<>());
        expectedPathMap.get(trolley2).add(trolleyPickingPaths[0]);
        expectedPathMap.get(trolley2).add(trolleyPickingPaths[1]);

        Map<Trolley, List<PickingPath>> actualPathMap = PickPathSequencing.getPaths(p);
        assertEquals(expectedPathMap, actualPathMap);
    }
}