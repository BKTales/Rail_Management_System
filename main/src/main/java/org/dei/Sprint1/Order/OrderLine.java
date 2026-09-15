package org.dei.Sprint1.Order;

import org.dei.Sprint1._Item.Item;
import org.dei._Facilities.Terminal.Warehouse.Allocation;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;


import java.util.ArrayList;

/**
 * Represents a single line in an {@link Order}, containing information about a specific {@link Item},
 * its requested quantity, allocated quantity, and associated {@link Allocation}s.
 * <p>
 * Each {@code OrderLine} tracks how much of the item has been allocated to different warehouse boxes
 * and maintains its own {@link OrderStatus}.
 * </p>
 *
 * <p>
 * This class provides methods for adding requested quantities, recording allocations,
 * and updating or retrieving status information.
 * </p>
 */
public class OrderLine {
    private final ArrayList<Allocation> allocations;
    private int                         allocatedQty;
    private int                         requestedQty;
    private Item                        item;
    private OrderStatus                 status;

    /**
     * Constructs a new {@code OrderLine} for a specific item and requested quantity.
     *
     * @param item         the {@link Item} being ordered
     * @param requestedQty the total quantity requested for this item
     */
    public OrderLine(Item item, int requestedQty) {
        this.allocatedQty = 0;
        this.requestedQty = requestedQty;
        this.item = item;
        this.status = OrderStatus.valueOf("UNDISPATCHABLE");

        allocations = new ArrayList<>();
    }

    /**
     * Returns the list of {@link Allocation} objects associated with this order line.
     *
     * @return a list of allocations
     */
    public ArrayList<Allocation> getAllocations() {
        return allocations;
    }

    /**
     * Returns the total quantity that has been allocated for this order line.
     *
     * @return the allocated quantity
     */
    public int getAllocatedQty() {
        return (allocatedQty);
    }

    /**
     * Increases the requested quantity for this order line.
     *
     * @param qnt the quantity to add to the requested amount
     */
    public void addRequestedQty(int qnt) {
        requestedQty += qnt;
    }

    /**
     * Returns the {@link Item} associated with this order line.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Calculates and returns the remaining quantity that still needs to be allocated.
     *
     * @return the unallocated (necessary) quantity
     */
    public int getNecessaryQty() {
        return (requestedQty - allocatedQty);
    }

    /**
     * Returns the total requested quantity for this order line.
     *
     * @return the requested quantity
     */
    public int getRequestedQty() {
        return requestedQty;
    }

    /**
     * Adds a new {@link Allocation} record to this order line.
     *
     * @param warehouseId the identifier of the warehouse from which the allocation was made
     * @param p           the {@link WarehousePosition} of the allocation
     * @param boxId       the ID of the box from which the stock was taken
     * @param qty         the quantity allocated from that box
     */
    public void addAllocation(String warehouseId, WarehousePosition p , String boxId, int qty) {
        Allocation allocation = new Allocation( p, warehouseId, boxId, qty, item.getSku());
        allocations.add(allocation);
    }

    /**
     * Increases the allocated quantity for this order line.
     *
     * @param qty the quantity to add to the allocated total
     */
    public void addAllocatedQty(int qty) {
        this.allocatedQty += qty;
    }

    /**
     * Sets the current {@link OrderStatus} for this order line.
     *
     * @param status the new status to assign
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Returns the current {@link OrderStatus} of this order line.
     *
     * @return the order line status
     */
    public OrderStatus getStatus() {
        return (status);
    }

    /**
     * Returns a string representation of this order line,
     * including item SKU, requested and allocated quantities, and status.
     *
     * @return a formatted string describing this order line
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: ").append(item.getSku())
                .append(", Requested: ").append(requestedQty)
                .append(", Allocated: ").append(allocatedQty)
                .append(", Status: ").append(status != null ? status.getName() : "null")
                .append("\n");

        return sb.toString();
    }
}
