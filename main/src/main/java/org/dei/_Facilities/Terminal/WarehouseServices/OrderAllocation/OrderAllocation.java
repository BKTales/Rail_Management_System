package org.dei._Facilities.Terminal.WarehouseServices.OrderAllocation;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1.Order.Order;
import org.dei.Sprint1.Order.OrderLine;
import org.dei.Sprint1.Order.OrderMode;
import org.dei.Sprint1.Order.OrderStatus;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.WarehousePosition;
import org.dei.Sprint1.US002.NotValidOrderException;

import java.util.*;
/**
 * Handles the allocation of items from warehouse boxes to customer orders.
 * <p>
 * The {@code OrderAllocation} class provides static methods to manage item allocation
 * according to different allocation modes (e.g., strict or partial). It is responsible for:
 * <ul>
 *     <li>Iterating through order lines and attempting item allocation based on the selected {@link OrderMode}.</li>
 *     <li>Updating order and order line statuses after allocation attempts.</li>
 *     <li>Performing box-level allocation and inventory adjustments when applicable.</li>
 *     <li>Returning a summary of the allocation results with visual ANSI color feedback in the console.</li>
 * </ul>
 * </p>
 *
 * <p>This class is not meant to be instantiated — all methods are static and operate
 * directly on the provided {@link Terminal}, {@link Order}, and {@link OrderLine} objects.</p>
 */
public class OrderAllocation {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";

    /**
     * Try to allocate items to a specified order with a specific orderMode
     * @param t Terminal in which the operation is happening
     * @param order Order to allocate items to
     * @param orderMode OrderMode indicating how to allcate the items
     * @return (Allocation generated status[string])
     */
    public static String orderAllocation(Terminal t, Order order, OrderMode orderMode) {
        if (orderMode == null)
            orderMode = OrderMode.STRICT;

        if (order == null)      throw new NotValidOrderException("order is null");
        if (order.isEmpty())    throw new NotValidOrderException("order is empty");

        for (OrderLine orderLine : order.getItems()) {
            if (!orderLine.getStatus().equals(OrderStatus.ELIGIBLE))
                processOrderLine(t, orderLine, orderMode);
        }

        return (returnStatus(order.getStatus()) + order);
    }

    /**
     * Try to allocate the requested quantity to one orderLine of the requester Order
     * @param t The terminal that the allocation in taking place
     * @param orderLine OrderLine to allocate items to
     * @param orderMode OrderMode indicating how to allcate the items
     */
    private static void processOrderLine(Terminal t, OrderLine orderLine, OrderMode orderMode) {
        int                         remainingQty = orderLine.getNecessaryQty();
        Item                        item = orderLine.getItem();

        if (orderMode == OrderMode.PARTIAL) {
            remainingQty = searchBoxesDirect(t, item, remainingQty, orderLine);
        }else if (orderMode == OrderMode.STRICT) {
            remainingQty = StrictMode.searchBoxesStrict(t, item, remainingQty, orderLine);
        }

        settingStatus(t, orderLine, orderMode, remainingQty);
    }

    /**
     * Set the status to the OrderLine considering the mode selected by the user
     * and the allocations that were done to that line
     * @param t Terminal in which the Order is
     * @param orderLine Line that have
     * @param orderMode selected mode for allocation
     * @param remainingQty qnt that still need to be allocated to fulfill the line
     */
    private static void settingStatus(Terminal t, OrderLine orderLine, OrderMode orderMode, int remainingQty) {
        if (remainingQty == 0)
        {
            orderLine.setStatus(OrderStatus.ELIGIBLE);
        }
        else if (remainingQty != orderLine.getRequestedQty())
        {
            if (orderMode == OrderMode.PARTIAL)
                orderLine.setStatus(OrderStatus.PARTIAL);
            else
                orderLine.setStatus(OrderStatus.UNDISPATCHABLE);
        }
    }

    /**
     * Set the return string with the status of what was done ot the order in the service
     * @param orderStatus status of the order after allocations
     * @return (string explaining the allocations according to the status of the order )
     */
    private static String returnStatus(OrderStatus orderStatus){
        if (orderStatus.equals(OrderStatus.ELIGIBLE))
            return (ANSI_BLUE + "The order was fully allocated!\n" + ANSI_RESET);
        else if (orderStatus.equals(OrderStatus.PARTIAL))
            return (ANSI_YELLOW + "The order was partially allocated!\n" + ANSI_RESET);
        else
            return (ANSI_RED + "The order was not allocated!\n" + ANSI_RESET);
    }

    /**
     * Function go through each of the boxes in a given bay, taking Item quantity from
     * them and allocating to OrderLine
     * @param item item being searched
     * @param qty quantity requested
     * @return (remaining quantity that needs to be allocated >= 0)
     */
    static int searchBoxesDirect(Terminal t, Item item, int qty, OrderLine orderLine) {
        Map<String, PriorityQueue<Box>> map = t.getItemHash();
        String sku = item.getSku();
        PriorityQueue<Box> boxes = map.get(sku);
        if (boxes == null || boxes.isEmpty()) {
            return qty;
        }

        return allocateFromBoxes(boxes, qty, orderLine);
    }

    /**
     * Allocates a specified quantity of an item from a priority queue of boxes.
     * <p>
     * This method retrieves boxes from the {@code boxes} queue (ordered by priority) and takes quantities
     * from them until the requested quantity ({@code qty}) is fulfilled or there are no more boxes available.
     * Each allocation is recorded in the given {@link OrderLine}, and any empty boxes are removed
     * </p>
     * @param boxes      the priority queue of boxes available for the SKU
     * @param qty        the quantity that needs to be allocated
     * @param orderLine  the order line where the allocation details will be recorded
     * @return the remaining quantity that could not be allocated (0 if fully allocated)
     */
    private static int allocateFromBoxes(PriorityQueue<Box> boxes, int qty, OrderLine orderLine) {
        ArrayList<Box> tempBoxes = new ArrayList<>();
        while (!boxes.isEmpty() && qty > 0) {
            Box box = boxes.poll();
            int allocQty = box.takeQuantity(qty);
            qty -= allocQty;

            orderLine.addAllocatedQty(allocQty);
            orderLine.addAllocation(
                    "W" + box.getWarehouseIndex() + 1,
                    new WarehousePosition(box.getBayIndex() + 1, box.getAisleIndex() + 1, box.getWarehouseIndex() + 1),
                    box.getBoxID(),
                    allocQty
            );

            if (box.getQntY() > 0) {
                tempBoxes.add(box);
            }
        }

        boxes.addAll(tempBoxes);

        return qty;
    }

}
