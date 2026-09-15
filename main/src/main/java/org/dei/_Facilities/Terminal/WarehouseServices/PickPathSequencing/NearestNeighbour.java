package org.dei._Facilities.Terminal.WarehouseServices.PickPathSequencing;

import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;

import java.util.ArrayList;

public class NearestNeighbour {
    /**
     * Processes a set of warehouse positions using Strategy B (nearest-neighbour).
     * <ul>
     *     <li>Removes duplicates using {@link PickPathSequencing#mergePositions(ArrayList)}.</li>
     *      <li>Applies the greedy nearest-neighbour algorithm to determine the optimal picking order.</li>
     *      <li>Calculates the total distance traveled along the generated path.</li>
     *      <li>Adds the ordered positions to the provided list {@code newPositionsB}, including the starting position (0,0,0) at the end.</li>
     *  </ul>
     *
     *  @param newPositionsB The list of positions to be ordered according to the nearest-neighbour path.
     *                       After processing, this list will contain the ordered path including the starting point.
     *  @return The total distance traveled following the nearest-neighbour path.
     *
     */
    public static int processPathB(ArrayList<WarehousePosition> newPositionsB) {
        ArrayList<WarehousePosition> merged = PickPathSequencing.mergePositions(newPositionsB);
        newPositionsB.clear();
        newPositionsB.addAll(merged);
        newPositionsB.add(new WarehousePosition(0,0,0));
        return nearestNeighbour(newPositionsB);
    }

    /**
     * Reorders a list of positions using the nearest-neighbour greedy algorithm.
     * - Starts from the first position in the list.
     * - At each step, selects the closest unvisited position according to the distance function.
     * - Rebuilds the list in-place to reflect the picking order.
     * - Calculates the total distance travelled.
     *
     * @param warehousePositions List of positions to reorder in nearest-neighbour order.
     * @return Total distance travelled following the nearest-neighbour path.
     */
    public static int nearestNeighbour(ArrayList<WarehousePosition> warehousePositions){
        if (warehousePositions == null || warehousePositions.isEmpty()){
            return 0;
        }
        ArrayList<WarehousePosition> path = new ArrayList<>(warehousePositions);
        warehousePositions.clear();
        boolean[] visited = new boolean[path.size()];
        int totalDistance = 0;
        WarehousePosition current = path.getLast();
        warehousePositions.add(current);
        visited[path.size() - 1] = true;

        for (int step = 1; step < path.size(); step++){

            int minDistance = Integer.MAX_VALUE;
            int minIndex = -1;

            for (int i = 0; i < path.size(); i++){

                if (!visited[i]){
                    int distance = PickPathSequencing.distance(current,path.get(i));
                    if (distance < minDistance) {
                        minIndex = i;
                        minDistance = distance;
                    }
                }
            }

            current = path.get(minIndex);
            warehousePositions.add(current);
            visited[minIndex] = true;
            totalDistance += minDistance;
        }
        return totalDistance;
    }
}
