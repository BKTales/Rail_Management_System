package org.dei.Sprint3.Graph;

import org.dei._Facilities.Station.Station;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.Country;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint3.Graph.RailNetworkGraphs.RailNetworkGraphService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * CSV Parser for rail network data.
 * Parses two CSV files:
 * 1. stations.csv: Station id, Station, Lat, Lon, CoordX, CoordY
 * 2. lines.csv: departure_stid, arrival_stid, dist, capacity, cost
 *
 * Loads data into RailNetworkGraphService.
 */
public class ParserBerlgianNW {

    private static final String DELIMITER = ",";
    private static final int STATIONS_EXPECTED_COLUMNS = 6;
    private static final int LINES_EXPECTED_COLUMNS = 5;

    private RailNetworkGraphService graphService;

    public ParserBerlgianNW(RailNetworkGraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * Loads rail network from two CSV files.
     *
     * @param stationsFilePath path to stations.csv
     * @param linesFilePath path to lines.csv
     * @return true if both files loaded successfully, false otherwise
     */
    public boolean loadRailNetwork(String stationsFilePath, String linesFilePath) {

        boolean stationsLoaded = loadStationsFromCSV(stationsFilePath);
        if (!stationsLoaded) {
            return false;
        }

        boolean linesLoaded = loadLinesFromCSV(linesFilePath);
        if (!linesLoaded) {
            return false;
        }

        return true;
    }

    /**
     * Parses stations.csv and adds each station to the graph.
     *
     * CSV format: Station id, Station, Lat, Lon, CoordX, CoordY
     * Example: 1, "Brussels Central", 50.8353, 4.3595, 100.5, 200.3
     */
    private boolean loadStationsFromCSV(String filePath) {
        int lineNumber = 0;
        int successCount = 0;
        int failCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                if (!parseAndAddStation(line, lineNumber)) {
                    failCount++;
                } else {
                    successCount++;
                }
            }
            return failCount == 0;

        } catch (IOException e) {
            return false;
        }
    }

    private boolean parseAndAddStation(String line, int lineNumber) {
        try {
            String[] fields = line.split(DELIMITER, -1);
            if (fields.length != STATIONS_EXPECTED_COLUMNS) {
                return false;
            }

            int stationId = Integer.parseInt(fields[0].trim());
            String stationName = fields[1].trim();
            double latitude = Double.parseDouble(fields[2].trim());
            double longitude = Double.parseDouble(fields[3].trim());
            double coordX = Double.parseDouble(fields[4].trim());
            double coordY = Double.parseDouble(fields[5].trim());

            if (stationName.isEmpty()) {
                return false;
            }

            Country country = new Country("BE");
            TimeZone timeZone = new TimeZone("Europe/Brussels");
            TimeZoneGroup timeZoneGroup = TimeZoneGroup.CET;
            GeographicalLocation location = new GeographicalLocation(latitude, longitude);

            Station station = new Station(
                    location,
                    timeZoneGroup,
                    timeZone,
                    country,
                    stationName,
                    stationId,
                    false,
                    false,
                    false
            );

            return graphService.addStation(station, coordX, coordY);

        } catch (NumberFormatException e) {
            System.err.println("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Line " + lineNumber + ": Error parsing station - " + e.getMessage());
            return false;
        }
    }

    /**
     * Parses lines.csv and adds each rail line (edge) to the graph.
     *
     * CSV format: departure_stid, arrival_stid, dist, capacity, cost
     * Example: 1, 2, 45.5, 100, 500.25
     */
    private boolean loadLinesFromCSV(String filePath) {
        int lineNumber = 0;
        int successCount = 0;
        int failCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                if (!parseAndAddLine(line, lineNumber)) {
                    failCount++;
                } else {
                    successCount++;
                }
            }
            return failCount == 0;

        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    private boolean parseAndAddLine(String line, int lineNumber) {
        try {
            String[] fields = line.split(DELIMITER, -1);
            if (fields.length != LINES_EXPECTED_COLUMNS) {
                return false;
            }

            int departureStationId = Integer.parseInt(fields[0].trim());
            int arrivalStationId = Integer.parseInt(fields[1].trim());
            double distance = Double.parseDouble(fields[2].trim());
            double capacity = Double.parseDouble(fields[3].trim());
            double cost = Double.parseDouble(fields[4].trim());

            if (distance < 0) {
                return false;
            }
            if (capacity < 0) {
                return false;
            }

            return graphService.addLine(departureStationId, arrivalStationId, distance, capacity, cost);

        } catch (NumberFormatException e) {
            System.err.println("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Line " + lineNumber + ": Error parsing line - " + e.getMessage());
            return false;
        }
    }
}
