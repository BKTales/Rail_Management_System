package org.dei._Facilities.Terminal.Warehouse;

import org.dei.Sprint1._Item.Box;

import java.util.*;

/**
 * Represents a warehouse that contains aisles, bays, and a quarantine area.
 * <p>
 * Responsible for storing boxes, managing quarantine, generating pick-up plans, and restocking items.
 * </p>
 */
public class Warehouse {

    private final String id ;
    private Quarantine quarantine ;
    private final List< Aisle > aisles;

    /**
     * Constructs a new {@code Warehouse} with the specified ID.
     *
     * @param id the warehouse identifier
     */
    public Warehouse(String id) {
        this.id = id;
        this.quarantine = new Quarantine();
        this.aisles = new ArrayList<>();
    }

    // =========== Getters =========== //

    /**
     * Returns the {@link Quarantine} associated with this warehouse.
     *
     * @return the quarantine instance
     */
    public Quarantine getQuarantine() {
        return quarantine;
    }

    /**
     * Returns the ID of the warehouse.
     *
     * @return the warehouse ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the list of aisles in the warehouse.
     *
     * @return list of {@link Aisle} objects
     */
    public List<Aisle> getAisles() {
        return aisles;
    }

    /**
     * Returns the aisle at the specified index.
     *
     * @param index the aisle index
     * @return the {@link Aisle} at the index
     */
    public Aisle getAisle(int index) {
        return  aisles.get(index);
    }

    /**
     * Returns the number of aisles in the warehouse.
     *
     * @return the aisle count
     */
    public int getManyAisle() {
        return aisles.size();
    }

    // =========== Set/add =========== //

    /**
     * Sets the {@link Quarantine} for the warehouse.
     *
     * @param quarantine the quarantine instance to set
     */
    public void setQuarantine(Quarantine quarantine) {
        this.quarantine = quarantine;
    }

    /**
     * Adds an aisle to the warehouse.
     *
     * @param aisle the {@link Aisle} to add
     */
    public void addAisle(Aisle aisle) {
        this.aisles.add(aisle);
    }

    // =========== Logic Functions =========== //

    /**
     * Function try to store box in a bay inside it
     * @param box Box wanted to be stored
     * @param p Position to save where the box was allocated to
     * @return (true - box storage) (false - warehouse full)
     */
    public boolean storeBox(Box box, WarehousePosition p) {
        for (int i = p.getAisleIndex(); i < aisles.size(); i++)
        {
            p.setAisleIndex(i); // add

            if (aisles.get(i).receiveBox(box, p))
                return (true);
        }
        return (false);
    }

    /**
     * Checks if the warehouse has an aisle at the specified index.
     *
     * @param index the aisle index
     * @return true if aisle exists; false otherwise
     */
    public boolean hasAisle(int index) {
        return this.aisles.size() > index;
    }

    /**
     * Returns the total number of available box slots in all aisles.
     *
     * @return the number of available spaces
     */
    public int availableSpace(){
        int availableSpace = 0;
        for(Aisle a : getAisles()){
            for(Bay b : a.getBays()){
                if(!b.isFull()){
                    availableSpace = availableSpace + b.availableBoxes();
                }
            }
        }
        return availableSpace;
    }



    /**
     * Returns a string representation of the warehouse, including all aisles and their contents.
     *
     * @return a formatted string describing the warehouse
     */
    public String toString(){

        StringBuilder s = new StringBuilder();
        s.append("╔═════════════════════════════════╗\n");
        s.append("║          Warehouse " + id + "           ║\n");
        s.append("╚═════════════════════════════════╝\n");

        int i =0;
        for (Aisle aisle : this.aisles){
            s.append("Aisle " + i + ":\t" + aisle.toString()).append("\n");
            i++;
        }
        return (s.toString());
    }
}
