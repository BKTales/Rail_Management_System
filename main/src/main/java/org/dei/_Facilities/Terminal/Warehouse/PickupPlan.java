package org.dei._Facilities.Terminal.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a picking plan in the warehouse system.
 * Each plan contains a set of trolleys and allocations following a specific heuristic.
 */
public class PickupPlan {
    private static int count = 0;
    private String planID;
    private double totalWeight;
    private List<Trolley> trolleyList;
    private Heuristic heuristic;
    private int lineNumber = 0;

    /**
     * Constructor to create a new picking plan with a specified heuristic.
     *
     * @param heuristic the heuristic used for this plan (e.g., FF, FFD, BFD)
     */
    public PickupPlan(Heuristic heuristic) {
        planID = "PL" + count;
        count++;

        this.heuristic = heuristic;
        this.trolleyList = new ArrayList<Trolley>();
    }
    /** @return the unique ID of the plan */
    public String getPlanID() {
        return planID;
    }

    /** @return the heuristic used for this plan */
    public Heuristic getHeuristic() {
        return heuristic;
    }

    /**
     * Adds a trolley to the plan and updates the total weight.
     *
     * @param trolley trolley to be added
     */
    public void addTrolley(Trolley trolley) {
        this.trolleyList.add(trolley);
        this.totalWeight += trolley.getCurrentWeight();
    }

    /** @return the list of trolleys in this plan */
    public List<Trolley> getTrolleys() {
        return trolleyList;
    }

    /**
     * @return the last trolley added to the plan, or null if none exist
     */
    public Trolley getLastTrolley() {
        if (trolleyList.isEmpty()) {
            return null;
        }
        return trolleyList.get(trolleyList.size() - 1);
    }

    /** @return the total weight of all trolleys in the plan */
    public double getWeight() {
        return totalWeight;
    }

    /** @return the list of trolleys in this plan (duplicate getter) */
    public List<Trolley> getTrolleyList() {
        return trolleyList;
    }

    /**
     * Computes the total capacity used by all trolleys.
     *
     * @return total weight of items currently in all trolleys
     */
    private double totalCapacityUsed(){

        double totalCapacityUsed = 0;

        for(Trolley t : trolleyList){
            totalCapacityUsed += t.getCurrentWeight();
        }

        return totalCapacityUsed;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                         PICKING PLAN                         \n");
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("Plan ID: ").append(planID).append("\n");
        sb.append("Heuristic: ").append(heuristic).append("\n");
        sb.append("Total Weight: ").append(String.format("%.2f", totalCapacityUsed())).append(" kg\n");
        sb.append("Total Trolleys: ").append(trolleyList.size()).append("\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        if (trolleyList.isEmpty()) {
            sb.append("No trolleys assigned to this plan.\n");
        } else {
            for (int i = 0; i < trolleyList.size(); i++) {
                Trolley trolley = trolleyList.get(i);
                double capacity = trolley.getCapacity();
                double currentWeight = trolley.getCurrentWeight();
                double utilization = capacity > 0 ? (currentWeight / capacity) * 100 : 0;

                sb.append("┌─────────────────────────────────────────────────────────┐\n");
                sb.append("│ TROLLEY #").append(String.format("%02d", i + 1)).append("                                 ");
                sb.append(String.format("%6.1f%% full", utilization)).append("│\n");
                sb.append("├─────────────────────────────────────────────────────────┤\n");

                String capacityLine = "│ Capacity:  " + String.format("%.2f", currentWeight) + " / " +
                        String.format("%.2f", capacity) + " kg" +
                        " (" + String.format("%.1f%%", utilization) + ")";
                sb.append(capacityLine);
                int spacesNeeded = 58 - capacityLine.length();
                sb.append(" ".repeat(Math.max(0, spacesNeeded))).append("│\n");

                sb.append("├─────────────────────────────────────────────────────────┤\n");

                List<Allocation> allocations = trolley.getAllocations();
                if (allocations.isEmpty()) {
                    sb.append("│ No allocations assigned.                              │\n");
                } else {
                    sb.append("│ Order ID    Line  Aisle  Bay    Box ID     SKU       Qty│\n");
                    sb.append("├─────────────────────────────────────────────────────────┤\n");

                    for (Allocation allocation : allocations) {
                        String positionStr = allocation.getPosition().toString();
                        String aisle = extractAisle(positionStr, allocation);
                        String bay = extractBay(positionStr, allocation);

                        sb.append("│ ")
                                .append(String.format("%-11s", allocation.getWarehouseId() != null ? allocation.getWarehouseId() : "N/A"))
                                .append(String.format("%-6s", getLineNumber()))
                                .append(String.format("%-7s", aisle))
                                .append(String.format("%-7s", bay))
                                .append(String.format("%-10s", allocation.getBoxId() != null ? allocation.getBoxId() : "N/A"))
                                .append(String.format("%-10s", allocation.getSku()))
                                .append(String.format("%4s", allocation.getQty()))
                                .append(" │\n");
                    }
                }
                sb.append("└─────────────────────────────────────────────────────────┘\n\n");
            }
        }

        // Summary
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                          SUMMARY                             \n");
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("• Total trolleys required: ").append(trolleyList.size()).append("\n");

        if (!trolleyList.isEmpty()) {
            sb.append("• Trolley utilization:\n");
            for (int i = 0; i < trolleyList.size(); i++) {
                Trolley trolley = trolleyList.get(i);
                double capacity = trolley.getCapacity();
                double currentWeight = trolley.getCurrentWeight();
                double utilization = capacity > 0 ? (currentWeight / capacity) * 100 : 0;

                sb.append("  - Trolley #").append(i + 1).append(": ")
                        .append(String.format("%.1f%%", utilization))
                        .append(" (").append(String.format("%.2f", currentWeight))
                        .append("/").append(String.format("%.2f", capacity)).append(" kg)\n");
            }
        }

        sb.append("═══════════════════════════════════════════════════════════════\n");

        return sb.toString();
    }

    // Helper methods for extracting position information
    private String extractAisle(String positionStr, Allocation allocation) {
        if (positionStr.contains("Aisle")) {
            String[] parts = positionStr.split("-");
            if (parts.length >= 2) return parts[1];
        }

        String aisleId = String.valueOf(allocation.getPosition().getAisleIndex());

        return aisleId;
    }

    private String extractBay(String positionStr, Allocation allocation) {
        if (positionStr.contains("Bay")) {
            String[] parts = positionStr.split("-");
            if (parts.length >= 4) return parts[3];
        }

        String bayId = String.valueOf(allocation.getPosition().getBayIndex());


        return bayId;
    }

    private String getLineNumber() {
        return String.valueOf(++lineNumber); // Placeholder
    }
}


