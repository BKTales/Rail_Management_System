package org.dei._Facilities.Terminal.Warehouse;

import java.util.*;

/**
 * Represents a path through a warehouse.
 * <p>
 * Stores the list of positions to traverse, the total distance of the path,
 * and the associated warehouse ID.
 * </p>
 */
public class PickingPath {
    private List<WarehousePosition> paths;
    private int distance;
    private String warehouseId;

    /**
     * Constructs a new Path with the given positions, distance, and warehouse ID.
     *
     * @param warehousePositions   the list of positions representing the path
     * @param distance    the total distance of the path
     * @param warehouseId the ID of the warehouse
     */
    public PickingPath(List<WarehousePosition> warehousePositions, Integer distance, String warehouseId) {
        paths = warehousePositions;
        this.warehouseId = warehouseId;
        this.distance = distance;
    }

    /**
     * Returns the list of positions in this path.
     *
     * @return the positions of the path
     */
    public List<WarehousePosition> getPaths() {
        return paths;
    }

    /**
     * Returns the warehouse ID associated with this path.
     *
     * @return the warehouse ID
     */
    public String getWarehouseId() {
        return warehouseId;
    }


    /**
     * Returns the total distance of this path.
     *
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns a string representation of the path, including warehouse ID,
     * formatted list of positions, and total distance.
     *
     * @return a formatted string representing the path
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho do warehouse
        sb.append(String.format("║  Warehouse ID: %-41s ║\n", warehouseId));
        sb.append("╠──────────────────────────────────────────────────────────╣\n");

        // Montar rota
        List<String> posStrings = new ArrayList<>();
        for (WarehousePosition pos : paths) {
            posStrings.add("(" + pos.getBayIndex() + "," + pos.getAisleIndex() + ")");
        }
        String route = String.join(" -> ", posStrings);

        // Adicionar linhas
        sb.append(String.format("║    Path: %-47s ║\n", route));
        sb.append(String.format("║    Distance: %-43d ║\n", distance));

        return sb.toString();
    }



    /**
     * Compares this path to another object for equality.
     * Two paths are equal if their list of positions and warehouse IDs are equal.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PickingPath pickingPath = (PickingPath) o;
        return Objects.equals(paths, pickingPath.paths) && Objects.equals(warehouseId, pickingPath.warehouseId);
    }

    /**
     * Returns a hash code for this path based on positions and warehouse ID.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(paths, warehouseId);
    }
}
