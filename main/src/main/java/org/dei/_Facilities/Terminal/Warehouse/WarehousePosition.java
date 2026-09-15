package org.dei._Facilities.Terminal.Warehouse;

import java.util.Objects;

/**
 * Represents the position of a box within a warehouse.
 * <p>
 * Tracks the bay index, aisle index, and warehouse index of a box.
 * </p>
 */
public class WarehousePosition {
    private int bayIndex;
    private int aisleIndex;
    private int warehouseIndex;

    /**
     * Constructs a new {@code Position} with the specified bay, aisle, and warehouse indices.
     *
     * @param bayIndex       the index of the bay
     * @param aisleIndex     the index of the aisle
     * @param warehouseIndex the index of the warehouse
     */
    public WarehousePosition(int bayIndex, int aisleIndex, int warehouseIndex) {
        this.bayIndex = bayIndex;
        this.aisleIndex = aisleIndex;
        this.warehouseIndex = warehouseIndex;
    }

    /**
     * Returns the bay index.
     *
     * @return the bay index
     */
    public int getBayIndex() {
        return bayIndex;
    }

    /**
     * Returns the aisle index.
     *
     * @return the aisle index
     */
    public int getAisleIndex() {
        return aisleIndex;
    }

    /**
     * Returns the warehouse index.
     *
     * @return the warehouse index
     */
    public int getWarehouseIndex() {
        return warehouseIndex;
    }

    /**
     * Sets the warehouse index.
     *
     * @param warehouseIndex the new warehouse index
     */
    public void setWarehouseIndex(int warehouseIndex) {
        this.warehouseIndex = warehouseIndex;
    }

    /**
     * Sets the aisle index.
     *
     * @param aisleIndex the new aisle index
     */
    public void setAisleIndex(int aisleIndex) {
        this.aisleIndex = aisleIndex;
    }

    /**
     * Sets the bay index.
     *
     * @param bayIndex the new bay index
     */
    public void setBayIndex(int bayIndex) {
        this.bayIndex = bayIndex;
    }

    /**
     * Returns a string representation of the position.
     *
     * @return a formatted string showing bay and aisle indices
     */
    @Override
    public String toString() {
        return "Position [bayIndex= " + bayIndex + ", aisleIndex=" + aisleIndex + "]";
    }

    /**
     * Compares this position to another object for equality.
     *
     * @param o the object to compare
     * @return true if the other object is a {@code Position} with the same bay and aisle indices; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WarehousePosition warehousePosition = (WarehousePosition) o;
        return bayIndex == warehousePosition.bayIndex && aisleIndex == warehousePosition.aisleIndex;
    }

    /**
     * Returns a hash code value for the position.
     *
     * @return the hash code based on bay and aisle indices
     */
    @Override
    public int hashCode() {
        return Objects.hash(bayIndex, aisleIndex);
    }
}
