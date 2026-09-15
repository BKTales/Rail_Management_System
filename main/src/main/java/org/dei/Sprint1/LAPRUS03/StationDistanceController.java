package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;

import java.util.*;

public class StationDistanceController {
    private RailwayNetwork railwayNetwork;
    private ExcelParserService parserService;
    private boolean isDataLoaded = false;
    private String loadedFilePath = null;

    public StationDistanceController() {
        this.parserService = new ExcelParserService();
        this.railwayNetwork = new RailwayNetwork();
    }

    public void loadNetworkData(String filePath) {
        // Check if we've already loaded this exact file
        if (isDataLoaded && loadedFilePath != null && loadedFilePath.equals(filePath)) {
            printInfoBox("Data Already Loaded", "Network data is already loaded from: " + filePath);
            return;
        }

        // Check if we have data but from a different file
        if (isDataLoaded && loadedFilePath != null && !loadedFilePath.equals(filePath)) {
            printWarningBox("Reloading Data",
                    "Switching from: " + loadedFilePath + "\n" +
                            "Loading new: " + filePath);
        }

        try {
            this.railwayNetwork = parserService.parseExcelFile(filePath);
            this.isDataLoaded = true;
            this.loadedFilePath = filePath;
            System.out.println(("\u001B[34m" + "Network Data Loaded\n" + "Excel file processed successfully" + "\u001B[0m"));
        } catch (Exception e) {
            printErrorBox("Loading Error", "Failed to load network data: " + e.getMessage());
        }
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public String getLoadedFilePath() {
        return loadedFilePath;
    }

    private void ensureDataLoaded() {
        if (!isDataLoaded) {
            throw new IllegalStateException("Network data not loaded. Call loadNetworkData() first.");
        }
    }

    public List<Facility> getFacilities() {
        ensureDataLoaded();
        return new ArrayList<>(railwayNetwork.getAllFacilities());
    }

    public List<Locomotive> getLocomotives() {
        ensureDataLoaded();
        return new ArrayList<>(railwayNetwork.getAllLocomotives());
    }

    public List<Operator> getOperators() {
        ensureDataLoaded();
        return new ArrayList<>(railwayNetwork.getAllOperators());
    }

    public List<Line> getLines() {
        ensureDataLoaded();
        return new ArrayList<>(railwayNetwork.getAllLines());
    }

    public TravelResult calculateDirectTravelTime(Facility start, Facility end, Locomotive locomotive) {
        ensureDataLoaded();
        Line directLine = railwayNetwork.findDirectLineBetweenFacilities(start.getId(), end.getId());

        if (directLine == null) {
            return null;
        }

        List<Segment> segments = railwayNetwork.getSegmentsForLine(directLine.getId());
        double totalLength = railwayNetwork.getLineTotalLength(directLine.getId());
        double travelTime = calculateTravelTimeForLine(segments, locomotive);

        TravelResult result = new TravelResult(start, end, locomotive);
        for (Segment segment : segments) {
            double segmentTime = (segment.getLength() / totalLength) * travelTime;
            result.addSegment(segment, segmentTime);
        }

        return result;
    }

    public String getNetworkStatistics() {
        ensureDataLoaded();
        return formatStatisticsBox(
                railwayNetwork.getAllFacilities().size(),
                railwayNetwork.getAllLines().size(),
                railwayNetwork.getAllSegments().size(),
                railwayNetwork.getAllLocomotives().size(),
                railwayNetwork.getAllOperators().size()
        );
    }

    public Facility findFacilityById(int id) {
        ensureDataLoaded();
        return railwayNetwork.getFacility(id);
    }

    public Facility findFacilityByName(String name) {
        ensureDataLoaded();
        return railwayNetwork.getAllFacilities().stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Locomotive findLocomotiveByNumber(String number) {
        ensureDataLoaded();
        return railwayNetwork.getLocomotive(number);
    }

    public Locomotive findLocomotiveByName(String name) {
        ensureDataLoaded();
        return railwayNetwork.getAllLocomotives().stream()
                .filter(l -> l.getModel().getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Line findLineById(int id) {
        ensureDataLoaded();
        return railwayNetwork.getLine(id);
    }

    public Line findLineByName(String name) {
        ensureDataLoaded();
        return railwayNetwork.getAllLines().stream()
                .filter(l -> l.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Segment> getSegmentsForLine(int lineId) {
        ensureDataLoaded();
        return railwayNetwork.getSegmentsForLine(lineId);
    }

    public double getLineTotalLength(int lineId) {
        ensureDataLoaded();
        return railwayNetwork.getLineTotalLength(lineId);
    }

    public boolean hasDirectConnection(Facility start, Facility end) {
        ensureDataLoaded();
        return railwayNetwork.findDirectLineBetweenFacilities(start.getId(), end.getId()) != null;
    }

    public List<Facility> getConnectedFacilities(Facility facility) {
        ensureDataLoaded();
        List<Facility> connected = new ArrayList<>();
        for (Line line : railwayNetwork.getAllLines()) {
            if (line.getStartFacilityId() == facility.getId()) {
                Facility end = railwayNetwork.getFacility(line.getEndFacilityId());
                if (end != null) connected.add(end);
            } else if (line.getEndFacilityId() == facility.getId()) {
                Facility start = railwayNetwork.getFacility(line.getStartFacilityId());
                if (start != null) connected.add(start);
            }
        }
        return connected;
    }

    public void reloadData() {
        if (loadedFilePath != null) {
            printInfoBox("Manual Reload", "Reloading data from: " + loadedFilePath);
            loadNetworkData(loadedFilePath);
        } else {
            printErrorBox("Reload Error", "No file path available for reloading");
        }
    }

    // Method to clear loaded data
    public void clearData() {
        this.railwayNetwork = new RailwayNetwork();
        this.isDataLoaded = false;
        this.loadedFilePath = null;
        printInfoBox("Data Cleared", "All network data has been cleared from memory");
    }

    // Travel time calculation helper methods
    private double calculateTravelTimeForLine(List<Segment> segments, Locomotive locomotive) {
        double totalLength = segments.stream().mapToDouble(Segment::getLength).sum();
        double effectiveSpeed = Math.min(locomotive.getModel().getMaxSpeed(), getLineMaxSpeed(segments));
        return (totalLength / 1000.0) / effectiveSpeed;
    }

    private double getLineMaxSpeed(List<Segment> segments) {
        return 100.0;
    }

    // Box printing methods
    private String formatStatisticsBox(int facilities, int lines, int segments, int locomotives, int operators) {
        StringBuilder sb = new StringBuilder();
        String header = "Network Statistics";
        int width = 49;

        sb.append("┌").append("─".repeat(width)).append("┐\n");
        sb.append("│").append(centerText(header, width)).append("│\n");
        sb.append("├").append("─".repeat(width)).append("┤\n");
        sb.append("│ ").append(String.format("%-20s", "Facilities:")).append(String.format("%-28s", facilities)).append("│\n");
        sb.append("│ ").append(String.format("%-20s", "Lines:")).append(String.format("%-28s", lines)).append("│\n");
        sb.append("│ ").append(String.format("%-20s", "Segments:")).append(String.format("%-28s", segments)).append("│\n");
        sb.append("│ ").append(String.format("%-20s", "Locomotives:")).append(String.format("%-28s", locomotives)).append("│\n");
        sb.append("│ ").append(String.format("%-20s", "Operators:")).append(String.format("%-28s", operators)).append("│\n");
        sb.append("└").append("─".repeat(width)).append("┘");

        return sb.toString();
    }

    private void printSuccessBox(String title, String content) {
        printBox("┌", "─", "┐", "├", "┤", "└", "┘", title, content);
    }

    private void printInfoBox(String title, String content) {
        printBox("┌", "─", "┐", "├", "┤", "└", "┘", title, content);
    }

    private void printWarningBox(String title, String content) {
        printBox("╔", "═", "╗", "╠", "╣", "╚", "╝", title, content);
    }

    private void printErrorBox(String title, String content) {
        printBox("╔", "─", "╗", "╠", "╣", "╚", "╝", title, content);
    }

    private void printBox(String topLeft, String horizontal, String topRight,
                          String middleLeft, String middleRight,
                          String bottomLeft, String bottomRight,
                          String title, String content) {
        int width = Math.max(title.length(), content.length()) + 4;
        width = Math.max(width, 41);

        // Handle multi-line content
        String[] contentLines = content.split("\n");
        int contentWidth = content.length();
        for (String line : contentLines) {
            contentWidth = Math.max(contentWidth, line.length());
        }
        width = Math.max(width, contentWidth + 4);

        System.out.println(topLeft + horizontal.repeat(width) + topRight);
        System.out.println("│" + centerText(title, width) + "│");
        System.out.println(middleLeft + horizontal.repeat(width) + middleRight);

        for (String line : contentLines) {
            System.out.println("│" + centerText(line, width) + "│");
        }

        System.out.println(bottomLeft + horizontal.repeat(width) + bottomRight);
        System.out.println();
    }


    public Path calculateIndirectTravelTime(Facility start, Facility end, Locomotive locomotive) {
        ensureDataLoaded();

        Line directLine = railwayNetwork.findDirectLineBetweenFacilities(start.getId(), end.getId());
        if (directLine != null) {
            return convertDirectToPath(start, end, locomotive, directLine);
        }

        List<Line> pathLines = findPathBFS(start, end);
        if (pathLines.isEmpty()) {
            return null;
        }

        return createPathFromLines(start, end, locomotive, pathLines);
    }

    private Path convertDirectToPath(Facility start, Facility end, Locomotive locomotive, Line directLine) {
        Path path = new Path(start, end, locomotive);
        List<Segment> segments = railwayNetwork.getSegmentsForLine(directLine.getId());
        double travelTime = calculateTravelTimeForLine(segments, locomotive);

        path.addSegment(directLine, segments, travelTime, start, end);
        return path;
    }

    private List<Line> findPathBFS(Facility start, Facility end) {
        Map<Integer, Line> cameFrom = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(start.getId());
        visited.add(start.getId());

        while (!queue.isEmpty()) {
            int currentId = queue.poll();

            if (currentId == end.getId()) {
                return reconstructPath(cameFrom, start.getId(), end.getId());
            }

            for (Line line : getLinesFromFacility(currentId)) {
                int neighborId = getOtherFacilityId(line, currentId);
                if (neighborId != 0 && !visited.contains(neighborId)) {
                    visited.add(neighborId);
                    cameFrom.put(neighborId, line);
                    queue.offer(neighborId);
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Line> getLinesFromFacility(int facilityId) {
        List<Line> lines = new ArrayList<>();
        for (Line line : railwayNetwork.getAllLines()) {
            if (line.getStartFacilityId() == facilityId || line.getEndFacilityId() == facilityId) {
                lines.add(line);
            }
        }
        return lines;
    }

    private int getOtherFacilityId(Line line, int facilityId) {
        if (line.getStartFacilityId() == facilityId) {
            return line.getEndFacilityId();
        } else if (line.getEndFacilityId() == facilityId) {
            return line.getStartFacilityId();
        }
        return 0;
    }

    private Facility getOtherFacility(Line line, Facility facility) {
        if (line.getStartFacilityId() == facility.getId()) {
            return railwayNetwork.getFacility(line.getEndFacilityId());
        } else if (line.getEndFacilityId() == facility.getId()) {
            return railwayNetwork.getFacility(line.getStartFacilityId());
        }
        return null;
    }

    private List<Line> reconstructPath(Map<Integer, Line> cameFrom, int startId, int endId) {
        List<Line> path = new ArrayList<>();
        int current = endId;

        while (current != startId) {
            Line line = cameFrom.get(current);
            if (line == null) break;
            path.add(0, line);
            current = getOtherFacilityId(line, current);
        }

        return path;
    }

    private Path createPathFromLines(Facility start, Facility end, Locomotive locomotive, List<Line> pathLines) {
        Path path = new Path(start, end, locomotive);
        Facility currentFacility = start;

        for (Line line : pathLines) {
            Facility nextFacility = getNextFacility(line, currentFacility);
            if (nextFacility == null) {
                System.err.println("Warning: Could not find next facility for line " + line.getName());
                break;
            }

            List<Segment> segments = railwayNetwork.getSegmentsForLine(line.getId());
            if (segments.isEmpty()) {
                System.err.println("Warning: No segments found for line " + line.getName());
                continue;
            }

            double travelTime = calculateTravelTimeForLine(segments, locomotive);

            path.addSegment(line, segments, travelTime, currentFacility, nextFacility);
            currentFacility = nextFacility;
        }

        return path;
    }

    private Facility getNextFacility(Line line, Facility currentFacility) {
        Facility startFacility = railwayNetwork.getFacility(line.getStartFacilityId());
        Facility endFacility = railwayNetwork.getFacility(line.getEndFacilityId());

        if (startFacility == null || endFacility == null) {
            System.err.println("Warning: Line " + line.getName() + " has invalid facility references");
            return null;
        }

        if (startFacility.equals(currentFacility)) {
            return endFacility;
        } else if (endFacility.equals(currentFacility)) {
            return startFacility;
        } else {
            System.err.println("Warning: Current facility " + currentFacility.getName() +
                    " is not connected to line " + line.getName());
            return null;
        }
    }

    public List<Path> findAllPaths(Facility start, Facility end, Locomotive locomotive, int maxTransfers) {
        ensureDataLoaded();
        List<Path> allPaths = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        findAllPathsDFS(start, end, locomotive, new ArrayList<>(), visited, allPaths, maxTransfers, 0);
        return allPaths;
    }

    private void findAllPathsDFS(Facility current, Facility end, Locomotive locomotive,
                                 List<Line> currentPath, Set<Integer> visited,
                                 List<Path> allPaths, int maxTransfers, int depth) {
        if (current.equals(end)) {
            if (!currentPath.isEmpty()) {
                Path path = createPathFromLines(getStartFacility(currentPath), end, locomotive, currentPath);
                if (path != null) {
                    allPaths.add(path);
                }
            }
            return;
        }

        if (depth > maxTransfers + 2) { // Limit depth to prevent infinite recursion
            return;
        }

        visited.add(current.getId());

        for (Line line : getLinesFromFacility(current.getId())) {
            if (!currentPath.contains(line)) {
                Facility next = getOtherFacility(line, current);
                if (next != null && !visited.contains(next.getId())) {
                    currentPath.add(line);
                    findAllPathsDFS(next, end, locomotive, currentPath, visited, allPaths, maxTransfers, depth + 1);
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }

        visited.remove(current.getId());
    }

    private Facility getStartFacility(List<Line> path) {
        if (path.isEmpty()) {
            return null;
        }
        Line firstLine = path.get(0);
        return railwayNetwork.getFacility(firstLine.getStartFacilityId());
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = width - text.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
}