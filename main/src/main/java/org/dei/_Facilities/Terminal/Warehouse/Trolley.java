package org.dei._Facilities.Terminal.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a trolley used in the warehouse to pick and transport allocations.
 * <p>
 * Tracks the trolley's maximum capacity, current weight, usage percentage, and list of {@link Allocation}s.
 * </p>
 */
public class Trolley {
    private double capacity;
    private double currentWeight;
    private double percentageOfUse;
    private List<Allocation> allocations = new ArrayList<>();

    /**
     * Constructs a new {@code Trolley} with the specified maximum capacity.
     *
     * @param capacity the maximum weight capacity of the trolley
     */
    public Trolley(double capacity) {
        this.capacity = capacity;
        this.percentageOfUse = 0.0;
        this.currentWeight = 0.0;
    }

    public boolean isFull() {
        if(capacity == currentWeight)
            return true;
        return false;
    }

    /**
     * Returns the current weight of the trolley.
     *
     * @return the current weight
     */
    public double getCurrentWeight() {
        return currentWeight;
    }

    /**
     * Adds an allocation to the trolley if there is enough capacity.
     * Updates the current weight and usage percentage.
     *
     * @param allocation the {@link Allocation} to add
     * @return true if the allocation was successfully added; false otherwise
     */
    public boolean addAllocation(Allocation allocation) {
        boolean aux = false;
        double totalWeight = allocation.getWeighted();

        if(currentWeight + totalWeight <= capacity) {
            aux = allocations.add(allocation);
            addWeight(totalWeight);
            percentageOfUse = (currentWeight / capacity) * 100;
        }

        return aux;
    }

    /**
     * Returns the list of allocations currently on the trolley.
     *
     * @return the list of {@link Allocation}s
     */
    public List<Allocation> getAllocations() {
        return allocations;
    }


    /**
     * Adds weight to the current trolley weight.
     *
     * @param weight the weight to add
     */
    public void addWeight(double weight) {
        this.currentWeight += weight;
    }

    /**
     * Returns the maximum capacity of the trolley.
     *
     * @return the capacity
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * Returns a string representation of the trolley, including capacity, current weight,
     * usage percentage, and allocations.
     *
     * @return a formatted string describing the trolley
     */
    @Override
    public String toString() {
        String allocationsStr = "No allocations";
        if (allocations != null && !allocations.isEmpty()) {
            allocationsStr = allocations.stream()
                    .map(Allocation::toString)
                    .collect(Collectors.joining("\n  "));
        }

        return "\nTrolley [Capacity: " + capacity +
                " | Current: " + currentWeight +
                " | Usage: " + String.format("%.1f", percentageOfUse) + "%" +
                " | Allocations: " + (allocations != null ? allocations.size() : 0) + " items]\n  " +
                allocationsStr;
    }
}
