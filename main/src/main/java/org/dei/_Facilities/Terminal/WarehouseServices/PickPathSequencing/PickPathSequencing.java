package org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing;

import org.dei._Facilities.Terminal.Warehouse.PickupPlan;
import org.dei._Facilities.Terminal.Warehouse.PickingPath;
import org.dei._Facilities.Terminal.Warehouse.Allocation;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei._Facilities.Terminal.Warehouse.Trolley;

import java.util.*;

public class PickPathSequencing {
    /**
     * Generates picking paths for all trolleys in a plan.
     * <p>
     * For each trolley, two paths are computed using the following strategies:
     * <ul>
     *     <li><b>Strategy A:</b> Ascending aisle order</li>
     *     <li><b>Strategy B:</b> Nearest-neighbour (greedy)</li>
     * </ul>
     *
     * @param pickupPlan The picking plan containing all trolleys and their allocations.
     * @return A map where each trolley is associated with a list of two paths [Path A, Path B].
     */

    public static Map<Trolley, List<PickingPath>> getPaths(PickupPlan pickupPlan) {
        if(pickupPlan == null) return null;


        Map<Trolley, List<PickingPath>> paths = new HashMap<>();

        for (Trolley trolley : pickupPlan.getTrolleys()) {
            String warehouseId = trolley.getAllocations().getFirst().getWarehouseId();
            ArrayList<WarehousePosition> warehousePositions = buildPositionsList(trolley);
            PickingPath[] trolleyPickingPaths = calculatePaths(warehousePositions,warehouseId);

            paths.put(trolley, new ArrayList<>());
            paths.get(trolley).add(trolleyPickingPaths[0]); // Path A
            paths.get(trolley).add(trolleyPickingPaths[1]); // Path B
        }

        return (paths);
    }


    /**
     * Builds a list of positions for a trolley's allocations.
     *
     * @param trolley The trolley whose allocations' positions will be processed.
     * @return A list of positions including (0,0) in the start of the list marking the entrance of the warehouse.
     */
    public static ArrayList<WarehousePosition> buildPositionsList(Trolley trolley) {
        ArrayList<WarehousePosition> warehousePositions = new ArrayList<>();

        for (Allocation allocation : trolley.getAllocations()) {
            warehousePositions.add(allocation.getPosition());
        }

        return warehousePositions;
    }

    /**
     * Calculates picking paths for a trolley using two strategies:
     * <ul>
     *     <li><b>Strategy A:</b> Ascending aisle (deterministic sweep)</li>
     *     <li><b>Strategy B:</b> Nearest-neighbour (greedy heuristic)</li>
     * </ul>
     * Each path contains the ordered sequence of positions to visit and the total distance traveled.
     *
     * @param warehousePositions The list of positions (aisles,bays) to be visited in the warehouse.
     * @param warehouseId The identifier of the warehouse.
     * @return An array containing two paths: [0] → Path A, [1] → Path B.
     */
    public static PickingPath[] calculatePaths(ArrayList<WarehousePosition> warehousePositions, String warehouseId) {
        ArrayList<WarehousePosition> allPositionsA = new ArrayList<>(warehousePositions);
        ArrayList<WarehousePosition> allPositionsB = new ArrayList<>(warehousePositions);
        int totalDistanceA = AscendingAisle.processPathA(allPositionsA);
        int totalDistanceB = NearestNeighbour.processPathB(allPositionsB);
        PickingPath pickingPathA = new PickingPath(allPositionsA, totalDistanceA,warehouseId);
        PickingPath pickingPathB = new PickingPath(allPositionsB, totalDistanceB,warehouseId);
        return new PickingPath[]{pickingPathA, pickingPathB};
    }

    /**
     * Removes duplicate positions from a list, keeping only one reference per position.
     *
     * @param warehousePositions List of positions to deduplicate.
     * @return A new ArrayList containing unique positions.
     */
    public static ArrayList<WarehousePosition> mergePositions(ArrayList<WarehousePosition> warehousePositions){
        Set<WarehousePosition> warehousePositionSet = new HashSet<>(warehousePositions);
        return new ArrayList<>(warehousePositionSet);
    }

    /**
     * Computes the distance between two positions using the warehouse movement model:
     * <ul>
     *     <li>Same aisle: {@code distance = |bay1 - bay2|}</li>
     *     <li>Different aisles: {@code distance = bay1 + |aisle1 - aisle2| * 3 + bay2}</li>
     * </ul>
     *
     * @param o1 The first position.
     * @param o2 The second position.
     * @return The distance between {@code o1} and {@code o2}.
     */
    public static int distance(WarehousePosition o1, WarehousePosition o2){
        int distance;
        int multiplier = 3;
        if (o1.getAisleIndex() == o2.getAisleIndex()) {
            distance = Math.abs(o1.getBayIndex() - o2.getBayIndex());
        }else{
            distance = o1.getBayIndex() + Math.abs(o1.getAisleIndex() - o2.getAisleIndex()) * multiplier + o2.getBayIndex();
        }
        return distance;
    }
}