package org.dei._Facilities.Terminal.Warehouse;

import org.dei.Sprint1._Item.QuarantineItem;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Represents a quarantine area in the warehouse where returned items are stored temporarily.
 * <p>
 * Items are stored in a LIFO order using a {@link PriorityQueue} sorted by timestamp in reverse order.
 * </p>
 */
public class Quarantine {// LIFO
    private final int MAX_ITEMS = 50;// LIFO
    private PriorityQueue<QuarantineItem> returnedItems;

    /**
     * Constructs a new empty {@code Quarantine} with a maximum capacity of 50 items.
     */
    public Quarantine() {
        Comparator<QuarantineItem> comparator = Comparator.comparing(QuarantineItem::getTimestamp).reversed();
        this.returnedItems = new PriorityQueue<>(comparator);
    }

    /**
     * Returns the maximum number of items that can be stored in quarantine.
     *
     * @return the maximum capacity
     */
    public int getMAX_ITEMS() {
        return MAX_ITEMS;
    }

    /**
     * Returns the current number of items in the quarantine.
     *
     * @return the number of items currently stored
     */
    public int getCurrentItems() {
        return returnedItems.size();
    }

    /**
     * Adds a returned item to the quarantine if there is space available.
     *
     * @param item the {@link QuarantineItem} to add
     */
    public void addItem(QuarantineItem item) {
        if (returnedItems.size() < MAX_ITEMS)
            returnedItems.add(item);
    }

    /**
     * Function will remove the last item added to quarantine
     * @return the last item added to quarantine
     */
    public QuarantineItem poll() {
        return returnedItems.poll();
    }

    /**
     * Returns a string representation of all items currently in the quarantine.
     *
     * @return a formatted string showing all quarantine items
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("============ QuarantineItems ===========");
        for (QuarantineItem item : returnedItems) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
