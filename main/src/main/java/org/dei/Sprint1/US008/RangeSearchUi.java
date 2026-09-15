package org.dei.Sprint1.US008;

import org.dei.Sprint1.US002.NotInitializedException;
import org.dei.Utils.Utils;

public class RangeSearchUi {

    private final double SELECTED_LAT_MIN = -90;
    private final double SELECTED_LAT_MAX = 90;
    private final double SELECTED_LON_MIN = -180;
    private final double SELECTED_LON_MAX = 180;
    private final int SELECTED_INDEX_MAX = 3;
    private final int SELECTED_INDEX_MIN = 0;

    private RangeSearchController controller;
    private double latMin;
    private double latMax;
    private double lonMin;
    private double lonMax;
    private boolean isCity;
    private boolean isMainStation;
    private int countryIndex;

    public RangeSearchUi() {
        controller = new RangeSearchController();
    }

    public void run() {
        try {
            readCoord();
            readFilters();
            search();
        } catch (NotInitializedException e) {
            System.out.println("Invalid tree!");
        }
    }

    public void readCoord() {
        readLats();
        readLongs();
    }

    private void readLats() {
        System.out.println("Define the latitude range for your search.");

        latMin = Utils.readDoubleValue(
                "Enter minimum latitude (" + SELECTED_LAT_MIN + " to " + SELECTED_LAT_MAX + "):",
                SELECTED_LAT_MAX + 1,
                SELECTED_LAT_MIN
        );

        latMax = Utils.readDoubleValue(
                "Enter maximum latitude (" + latMin + " to " + SELECTED_LAT_MAX + "):",
                SELECTED_LAT_MAX + 1,
                latMin
        );
    }

    private void readLongs() {
        System.out.println("Define the longitude range for your search.");

        lonMin = Utils.readDoubleValue(
                "Enter minimum longitude (" + SELECTED_LON_MIN + " to " + SELECTED_LON_MAX + "):",
                SELECTED_LON_MAX + 1,
                SELECTED_LON_MIN
        );

        lonMax = Utils.readDoubleValue(
                "Enter maximum longitude (" + lonMin + " to " + SELECTED_LON_MAX + "):",
                SELECTED_LON_MAX + 1,
                lonMin
        );
    }

    private void readFilters() {
        System.out.println("Define optional filters for your search.");

        isCity = Utils.readValueYorN("Filter only cities? (y/n):");
        isMainStation = Utils.readValueYorN("Filter only main stations? (y/n):");

        countryIndex = Utils.readValue("Select country (0=PT,1=ES,2=ALL):", SELECTED_INDEX_MAX, SELECTED_INDEX_MIN);
    }

    private void search(){
        System.out.println(controller.search(latMin,latMax,lonMin,lonMax,isCity,countryIndex,isMainStation));
    }

}
