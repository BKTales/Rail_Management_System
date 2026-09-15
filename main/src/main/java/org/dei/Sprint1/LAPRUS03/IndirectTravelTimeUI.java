package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;
import org.dei.Utils.Utils;

import java.util.List;

public class IndirectTravelTimeUI {
    private StationDistanceController controller;

    public IndirectTravelTimeUI(StationDistanceController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println("┌────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                      INDIRECT PATH CALCULATOR                          │");
        System.out.println("└────────────────────────────────────────────────────────────────────────┘");

        List<Facility> facilities = controller.getFacilities();
        List<Locomotive> locomotives = controller.getLocomotives();

        System.out.println("\nAvailable Facilities:");
        for (int i = 0; i < facilities.size(); i++) {
            System.out.printf("%s\n", facilities.get(i));
        }

        int startIndex = Utils.readValue("Select start facility (number): ", facilities.size(), 1) - 1;
        Facility startFacility = facilities.get(startIndex);

        int endIndex = Utils.readValue("Select end facility (number): ", facilities.size(), 1) - 1;
        Facility endFacility = facilities.get(endIndex);

        System.out.println("\nAvailable Locomotives:");
        for (int i = 0; i < locomotives.size(); i++) {
            System.out.printf("%2d. %s\n", i + 1, locomotives.get(i));
        }

        int locoIndex = Utils.readValue("Select locomotive (number): ", locomotives.size() + 1, 1) - 1;
        Locomotive selectedLocomotive = locomotives.get(locoIndex);

        calculateAndDisplayIndirectPath(startFacility, endFacility, selectedLocomotive);
    }

    private void calculateAndDisplayIndirectPath(Facility start, Facility end, Locomotive locomotive) {
        System.out.println("\n" + "─".repeat(80));

        TravelResult directResult = controller.calculateDirectTravelTime(start, end, locomotive);
        if (directResult != null) {
            System.out.println("DIRECT PATH FOUND:");
            System.out.println(directResult);
            System.out.println("\n" + "─".repeat(80));
            return;
        }

        System.out.println("🔍 Searching for indirect path...");
        Path indirectPath = controller.calculateIndirectTravelTime(start, end, locomotive);

        if (indirectPath != null && !indirectPath.getSegments().isEmpty()) {
            System.out.println("INDIRECT PATH FOUND (" + indirectPath.getNumberOfTransfers() + " transfers):");
            System.out.println(indirectPath);

            System.out.println("\n📋 ROUTE SUMMARY:");
            System.out.println("  " + indirectPath.getDetailedRoute());

            System.out.println("\nLOCOMOTIVE INFORMATION:");
            System.out.printf("  %s - %s %s\n", locomotive.getNumber(), locomotive.getModel().getName(), locomotive.getType());
            System.out.printf("  Max Speed: %.0f km/h | Power: %.0f kW | Weight: %.0f tons\n",
                    locomotive.getModel().getMaxSpeed(), locomotive.getModel().getPower(), locomotive.getModel().getWeight());

            System.out.println("\n" + "─".repeat(80));
            showAlternativePaths(start, end, locomotive);
        } else {
            System.out.println("No path found between " + start.getName() + " and " + end.getName());
            System.out.println("Please select different facilities.");
        }

    }

    private void showAlternativePaths(Facility start, Facility end, Locomotive locomotive) {

        List<Path> allPaths = controller.findAllPaths(start, end, locomotive, 3);

        if (allPaths.size() > 1) {
            System.out.println("\n📊 FOUND " + allPaths.size() + " ALTERNATIVE PATHS:");

            for (int i = 0; i < Math.min(allPaths.size(), 3); i++) {
                Path path = allPaths.get(i);
                System.out.println("\nAlternative #" + (i + 1) + ":");
                System.out.println("  Transfers: " + path.getNumberOfTransfers());
                System.out.println("  Distance: " + String.format("%.2f", path.getTotalDistanceKm()) + " km");
                System.out.println("  Time: " + String.format("%.2f", path.getTotalTime()) + " hours");
                System.out.println("  Route: " + path.getDetailedRoute());
            }
        }
    }
}