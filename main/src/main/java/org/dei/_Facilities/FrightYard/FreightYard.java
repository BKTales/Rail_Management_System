package org.dei._Facilities.FrightYard;

import org.dei._Facilities.Facility;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Train.Freight;
import org.dei._Train.Wagon;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a freight yard containing wagons.
 * <p>
 * Implements a singleton pattern to manage a global instance of the freight yard.
 * Provides methods to add, remove, retrieve, and list wagons.
 * </p>
 */
public class FreightYard extends Facility {
    private List<Wagon>             wagons;
    private List<Freight> pendingFreights; // pending Freights


    public FreightYard(GeographicalLocation location, TimeZoneGroup timeZoneGroup, TimeZone timeZone,
                            Country country, String name, int id) {
        super(location, timeZoneGroup, timeZone, country, name, id);
        this.wagons = new ArrayList<>();
        pendingFreights = new ArrayList<>();
    }

    /**
     * Adds a wagon to the freight yard.
     *
     * @param wagon the {@link Wagon} to add
     */
    public void addWagon(Wagon wagon) {
        wagons.add(wagon);
    }
    public void addFreight(Freight freight) {
        pendingFreights.add(freight);
    }

    /**
     * Retrieves an existing wagon by ID or creates a new one if it doesn't exist.
     *
     * @param wagonID the ID of the wagon
     * @return the existing or newly created {@link Wagon}
     */
    public Wagon getOrCreateWagon(String wagonID) {
        for (Wagon wagon : wagons) {
            if (wagon.getWagonId().equals(wagonID)) {
                return wagon;
            }
        }
        Wagon newWagon = new Wagon(wagonID);
        wagons.add(newWagon);
        return newWagon;
    }

    /**
     * Removes and returns the wagon at the specified index.
     *
     * @param index the index of the wagon to remove
     * @return the removed {@link Wagon}
     */
    public Wagon removeWagonByIndex(int index) {
        return (wagons.remove(index));
    }

    /**
     * Removes the specified wagon from the freight yard.
     *
     * @param wagon the {@link Wagon} to remove
     */
    public void removeWagon(Wagon wagon) {
        wagons.remove(wagon);
    }

    /**
     * Removes and returns the wagon at the specified index.
     *
     * @param index the index of the wagon to remove
     * @return the removed {@link Wagon}
     */
    public Freight removeFreightByIndex(int index) {
        return (pendingFreights.remove(index));
    }

    /**
     * Removes the specified wagon from the freight yard.
     *
     * @param freight the {@link Freight} to remove
     */
    public void removeFreight(Freight freight) {
        pendingFreights.remove(freight);
    }

    /**
     * Returns the number of wagons in the freight yard.
     *
     * @return the wagon count
     */
    public int size() {
        return wagons.size();
    }

    /**
     * Returns an array of strings listing all wagons.
     *
     * @return an array of wagon descriptions, or null if no wagons exist
     */
    public String[] listWagons() {
        StringBuilder s = new StringBuilder();
        int i = 0;

        if (wagons.isEmpty())
            return (null);
        for (Wagon wagon : wagons) {
            s.append("[" + i + "] - Wagon " + (i) + "\n");
            i++;
        }
        String[] result = s.toString().split("\n");
        return (result);
    }

    /**
     * Returns a formatted string representation of all wagons in the freight yard.
     *
     * @return a string showing the wagons in a table-like format
     */
    public String listPrintWagon() {
        StringBuilder s = new StringBuilder();
        int totalWidth = 20; // adjust to desired line length
        int i = 0;

        s.append("╔════════════════════╗\n");
        s.append("║     WagonsList     ║\n");
        s.append("╠════════════════════╣\n");
        for (Wagon w : wagons){
            String text = String.format("[" + i + "] - Wagon %s", w.getWagonId());
            String formatted = String.format("║ %-"+ (totalWidth - 2) +"s ║", text);
            s.append(formatted + "\n");
            i++;
        }
        s.append("╚════════════════════╝\n");

        return s.toString();
    }

    public List<Wagon> getWagons() { return wagons; }
    public void setWagons(List<Wagon> wagons) { this.wagons = wagons; }
    public List<Freight> getPendingFreights() { return pendingFreights; }
    public void setPendingFreights(List<Freight> pendingFreights) { this.pendingFreights = pendingFreights; }

    /**
     * Returns a string representation of the freight yard, including all wagons.
     *
     * @return a concatenated string of all wagons' string representations
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Wagon wagon : wagons) {
            s.append(wagon.toString());
        }
        return s.toString();

    }
}
