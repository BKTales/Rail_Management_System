package org.dei._Facilities.Terminal.Warehouse;

import org.dei.Sprint1._Item.Box;
import org.dei.Sprint1._Item.Item;

import java.util.*;

/**
 * Represents an aisle in a warehouse, containing multiple {@link Bay} objects.
 * <p>
 * This class manages bays, allows adding and retrieving them, and handles the
 * storage of {@link Box} objects into appropriate bays.
 * </p>
 */
public class Aisle {
    private List <Bay> bays;

    /**
     * Constructs a new empty {@code Aisle} with no bays.
     */
    public Aisle() {
        bays = new ArrayList<>();
    }

    /**
     * Checks if a bay exists at the specified index.
     *
     * @param bay the index to check
     * @return true if a bay exists at the given index; false otherwise
     */
    public boolean hasBay(int bay) {
        return (bays.size() > bay);
    }

    /**
     * Adds a bay to this aisle.
     *
     * @param bay the {@link Bay} to add
     */
    public void addBay(Bay bay) {
        bays.add(bay);
    }

    /**
     * Returns the bay at the specified position in this aisle.
     *
     * @param pos the index of the bay to retrieve
     * @return the {@link Bay} at the specified index
     */
    public Bay getBay(int pos) {
        return bays.get(pos);
    }

    /**
     * Returns a list of all bays in this aisle.
     *
     * @return the list of bays
     */
    public List<Bay> getBays() { return bays; }

    /**
     * Returns the number of bays in this aisle.
     *
     * @return the number of bays
     */
    public int getManyBays() { return bays.size(); }

    /**
     * Function will check if in the aisle bays there is space to store the wanted Item Boxes
     * @param item the {@link Item} to check space for
     * @return the index of the bay with space, or -1 if no space is available
     */
    public int isThereSpaceForItem(Item item)  {
        for (Bay bay : bays) {

        }
        return (-1);
    }

    /**
     * Receive the box from the Warehouse and will sort it between its bays
     * @param box the {@link Box} to store
     * @param p the {@link WarehousePosition} object that will record where the box was allocated
     * @return true if the box was successfully stored; false if the aisle is full
     */
    public boolean receiveBox(Box box, WarehousePosition p) {
        for (int i = 0; i < bays.size(); i++)
        {
            p.setBayIndex(i);
            if (bays.get(i).receiveBox(box, p))
                return (true);
        }
        return (false);
    }

    /**
     * Returns a string representation of the aisle, showing the number of boxes
     * and maximum capacity of each bay.
     *
     * @return a formatted string representing the aisle's bays
     */
    public String toString(){
        StringBuilder s = new StringBuilder();

        for (Bay bay : bays){
            s.append(bay.getManyBoxes()).append("/").append(bay.getMAX_BOXES()).append("\t");
        }
        return s.toString();
    }
}