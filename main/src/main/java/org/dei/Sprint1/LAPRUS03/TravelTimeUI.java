package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;
import org.dei.Utils.Utils;

import java.util.List;

public class TravelTimeUI {
    private StationDistanceController controller;

    public TravelTimeUI(StationDistanceController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║          Direct Travel Time Calculator         ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        controller.loadNetworkData("main/src/main/resources/train_station_dataset/Dataset_Sprint_1_v0.xlsx");

        System.out.println(controller.getNetworkStatistics());

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

        calculateAndDisplayTravelTime(startFacility, endFacility, selectedLocomotive);
    }

    private void calculateAndDisplayTravelTime(Facility start, Facility end, Locomotive locomotive) {
        TravelResult travelResult = controller.calculateDirectTravelTime(start, end, locomotive);

        System.out.println("\n" + "═".repeat(50));

        if (travelResult != null) {
            System.out.println(travelResult);
        } else {
            System.out.println("No direct connection found between " + start.getName() + " and " + end.getName());
            System.out.println("Please select facilities that are directly connected by a line.");
        }

        System.out.println("═".repeat(50));
    }
}