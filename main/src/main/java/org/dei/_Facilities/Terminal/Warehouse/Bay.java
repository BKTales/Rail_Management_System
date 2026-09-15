package org.dei._Facilities.Terminal.Warehouse;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;

import java.util.*;
/**
 * Represents a bay in a warehouse aisle, which can hold a limited number of {@link Box} objects.
 * <p>
 * The bay manages storing boxes in order and retrieving boxes by index or by the contained item.
 * </p>
 */
public class Bay {
    private final int MAX_BOXES;
    private LinkedList<Box> boxes; // change to linked list!

    // change to linked list or double liked list:
    // - retrieve items, always the first one to show of the wanted type
    // - store, always after store, sort it!


    /**
     * Constructs a new {@code Bay} with a specified maximum number of boxes.
     *
     * @param maxBoxes the maximum number of boxes the bay can hold
     */
    public Bay(int maxBoxes)  {
        MAX_BOXES = maxBoxes;
        boxes = new LinkedList<>();
    }

    /**
     * Adds a box to the bay in sorted order based on {@link Box#compareTo(Box)}.
     *
     * @param box the {@link Box} to add
     */
    public void addBox(Box box) {
        int i = 0;

        for (Box b : boxes)
        {
            // applied as -1 means that box is "smaller" than b
            int compareResult = box.compareTo(b);

            if (compareResult <= 0)
            {
                boxes.add(i, box);
                return ;
            }
            i++;
        }
        boxes.add(box);
    }

    /**
     * Returns and removes the first box in the bay.
     *
     * @return the first {@link Box}, or null if the bay is empty
     */
    public Box getFirstBox() {
        if (boxes.isEmpty())
            return null;
        return boxes.removeFirst();
    }

    /**
     * Returns all boxes currently in the bay.
     *
     * @return a {@link LinkedList} of boxes
     */
    public LinkedList<Box> getBoxes() {
        return boxes;
    }

    /**
     * Get the box in the given index and remove it from the Linked List
     * @param index  index to retrieve item from
     * @return (Box - valid index) (null - invalid index)
     */
    public Box getBox(int index) {
        return (boxes.remove(index));
    }

    /**
     * Search for a specified item inside the boxes, if found, take it away
     * from the list and return it.
     * @param item  Wanted Item
     * @return (Box - if found item) (null - if item not found)
     */
    public Box getFirstBoxWithItem(Item item)
    {
        for (Box b : boxes)
        {
            if (b.getProduct().equals(item))
            {
                boxes.remove(b);
                return (b);
            }
        }
        return (null);
    }

    /**
     * Returns the maximum number of boxes that this bay can hold.
     *
     * @return the maximum box capacity
     */
    public int getMAX_BOXES() {
        return MAX_BOXES;
    }

    /**
     * Returns the current number of boxes stored in this bay.
     *
     * @return the number of boxes
     */
    public int getManyBoxes(){
        return boxes.size();
    }

    /**
     * Returns the number of available slots in the bay.
     *
     * @return the number of empty box slots
     */
    public int availableBoxes() { return getMAX_BOXES() - boxes.size(); }

    /**
     * Checks if the bay is full.
     *
     * @return true if the bay is full; false otherwise
     */
    public boolean isFull() { return availableBoxes() == 0; }

    /**
     * Checks if the bay is empty.
     *
     * @return true if the bay has no boxes; false otherwise
     */
    public boolean isEmpty() { return boxes.isEmpty(); }

    /**
     * Attempts to store a box in the bay if it is not full.
     * <p>
     * Also updates the position of the box.
     * </p>
     *
     * @param box the {@link Box} to store
     * @param p   the {@link WarehousePosition} object used to set the box location
     * @return true if the box was successfully stored; false if the bay is full
     */
    public boolean receiveBox(Box box, WarehousePosition p) {
        if (isFull())
            return (false);
        addBox(box);
        box.setPosition(new WarehousePosition(p.getBayIndex(), p.getAisleIndex(), p.getWarehouseIndex()));
        return (true);
    }
}
