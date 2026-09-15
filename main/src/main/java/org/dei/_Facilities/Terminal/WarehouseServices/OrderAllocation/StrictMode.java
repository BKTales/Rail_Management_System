package org.dei._Facilities.Terminal.WarehouseServices.OrderAllocation;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;
import org.dei.Sprint1.Order.OrderLine;
import org.dei._Facilities.Terminal.Terminal;

import java.util.*;

/**
 * The {@code StrictMode} class provides methods for allocating items to orders
 * in a strict warehouse traversal mode.
 * <p>
 * In strict mode, the system iterates through the warehouse aisles, bays,
 * and boxes in a fixed sequential order until the requested quantity is
 * allocated. This mode ensures a deterministic and location-consistent
 * item retrieval process, typically used when inventory distribution rules
 * require sequential stock access.
 * </p>
 *
 * <p>All methods in this class are static and internally used by the
 * {@link OrderAllocation} service.</p>
 */
public class StrictMode {


    /**
     * Performs a strict sequential check to determine whether the requested quantity
     * of an item can be allocated from the available warehouse boxes.
     *
     * <p>This method retrieves the {@link Box} instances associated with the specified
     * {@link Item} (identified by its SKU) and verifies, in strict traversal order,
     * whether the requested quantity can be fully satisfied. It does not directly
     * modify the box quantities during this check.</p>
     *
     * <p>If the quantity can be completely fulfilled (i.e., no remaining quantity),
     * the method triggers a direct allocation update through
     * {@link OrderAllocation#searchBoxesDirect(Terminal, Item, int, OrderLine)}.</p>
     *
     * @param t          the {@link Terminal} instance containing warehouse data and item-box mappings
     * @param item       the {@link Item} whose stock availability is being verified
     * @param qty        the requested quantity to allocate
     * @param orderLine  the {@link OrderLine} representing the current order line
     * @return the remaining quantity that could not be allocated;
     *         returns {@code 0} if the requested quantity can be fully allocated
     */


    public static int searchBoxesStrict(Terminal t, Item item, int qty, OrderLine orderLine) {
        Map<String, PriorityQueue<Box>> map = t.getItemHash();
        String sku = item.getSku();
        PriorityQueue<Box> boxes = map.get(sku);

        if (boxes == null || boxes.isEmpty()) {
           return qty;
        }
        int requestedQty = qty;

        qty = allocateFromBoxes( boxes, qty);
        if (qty == 0){
            OrderAllocation.searchBoxesDirect(t,item,requestedQty,orderLine);
        }
        return qty;
    }

    /**
     * Checks whether the given quantity can be fulfilled from the available boxes in the queue.
     * <p>
     * This method iterates through the {@link PriorityQueue} of {@code Box} objects,
     * temporarily removing them to calculate how much of the requested quantity could
     * theoretically be covered based on their available quantities.
     * It does not modify the actual quantities of the boxes.
     * <p>
     * After processing, all boxes are returned to the queue in their original order.
     *
     * @param boxes the priority queue containing available boxes
     * @param qty the quantity to check for availability
     * @return the remaining quantity that could not be covered;
     *         returns {@code 0} if the requested quantity can be fully covered
     */
    private static int allocateFromBoxes(PriorityQueue<Box> boxes, int qty) {
        ArrayList<Box> tempBoxes = new ArrayList<>();

        while (!boxes.isEmpty() && qty > 0) {
            Box box = boxes.poll();

            int allocQty = Math.min(qty,box.getQntY());
            qty -= allocQty;
            tempBoxes.add(box);
        }

        boxes.addAll(tempBoxes);
        return qty;
    }

}

