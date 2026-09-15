package org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing;

import org.dei.Utils.ComparatorsUtils;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;

import java.util.ArrayList;

public class AscendingAisle {
    /**
     * Processes a set of warehouse positions using Strategy A (ascending aisle).
     * <ul>
     *  <li>Removes duplicates using {@link PickPathSequencing#mergePositions(ArrayList)}.</li>
     *  <li>Sorts the positions by aisle and bay in ascending order.</li>
     *  <li>Calculates the total distance traveled along the sorted path.</li>
     *  <li>Adds the sorted positions to the provided list {@code newPositionsA}, including the starting position (0,0,0) at the end.</li>
     * </ul>
     *
     * @param newPositionsA The list of positions to be sorted by ascending aisle and bay.
     *                      After processing, this list will contain the ordered path including the starting point.
     * @return The total distance traveled following the ascending aisle path.
     */
    public static int processPathA(ArrayList<WarehousePosition> newPositionsA) {
        ArrayList<WarehousePosition> merged = PickPathSequencing.mergePositions(newPositionsA);
        newPositionsA.clear();
        newPositionsA.addAll(merged);
        newPositionsA.add(new WarehousePosition(0,0,0));
        return ascendingAisle(newPositionsA);
    }

    /**
     * Sorts a list of positions in ascending aisle order.
     * If two positions are in the same aisle, they are sorted by bay index.
     * Also computes the total distance for visiting the positions in this order.
     *
     * @param warehousePositions The list of positions to be sorted and traversed.
     * @return The total distance for visiting the positions in ascending aisle order.
     */
    public static int ascendingAisle(ArrayList<WarehousePosition> warehousePositions){
        warehousePositions.sort(ComparatorsUtils.ascendingComparator);
        int distance = 0;
        for (int i = 0; i < warehousePositions.size() - 1; i++) {
            distance+= PickPathSequencing.distance(warehousePositions.get(i), warehousePositions.get(i+1));
        }
        return distance;
    }
}
