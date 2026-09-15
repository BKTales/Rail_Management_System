package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private Facility start;
    private Facility end;
    private List<PathSegment> segments;
    private double totalDistance;
    private double totalTime;
    private Locomotive locomotive;

    public Path(Facility start, Facility end, Locomotive locomotive) {
        this.start = start;
        this.end = end;
        this.locomotive = locomotive;
        this.segments = new ArrayList<>();
        this.totalDistance = 0.0;
        this.totalTime = 0.0;
    }

    public void addSegment(Line line, List<Segment> trackSegments, double segmentTime, Facility startFacility, Facility endFacility) {
        PathSegment pathSegment = new PathSegment(line, trackSegments, segmentTime);
        pathSegment.setStartFacility(startFacility);
        pathSegment.setEndFacility(endFacility);
        segments.add(pathSegment);
        totalDistance += trackSegments.stream().mapToDouble(Segment::getLength).sum();
        totalTime += segmentTime;
    }

    public Facility getStart() { return start; }
    public Facility getEnd() { return end; }
    public Locomotive getLocomotive() { return locomotive; }
    public List<PathSegment> getSegments() { return new ArrayList<>(segments); }
    public double getTotalDistance() { return totalDistance; }
    public double getTotalTime() { return totalTime; }
    public double getTotalDistanceKm() { return totalDistance / 1000.0; }
    public int getNumberOfTransfers() { return Math.max(0, segments.size() - 1); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│                            PATH RESULTS                                │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ From: %-64s │\n", start.getName()));
        sb.append(String.format("│ To:   %-64s │\n", end.getName()));
        sb.append(String.format("│ Locomotive: %-58s │\n", locomotive.getModel().getName()));
        sb.append(String.format("│ Max Speed: %-46.0f km/h         │\n", locomotive.getModel().getMaxSpeed()));
        sb.append("├────────────────────────────────────────────────────────────────────────┤\n");

        // Show the complete route
        sb.append("│                              ROUTE                                     │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────┤\n");

        Facility currentFacility = start;
        int segmentNumber = 1;

        for (PathSegment segment : segments) {
            Facility nextFacility = segment.getEndFacility();
            Line line = segment.getLine();

            if (nextFacility != null) {
                sb.append(String.format("│ %2d. %-20s → %-20s                        │\n",
                        segmentNumber, currentFacility.getName(), nextFacility.getName()));
                sb.append(String.format("│     Via: %-61s │\n", line.getName()));

                sb.append(String.format("│     Distance: %8.2f km | Time: %5.2f hours %21s    │\n",
                        segment.getDistance() / 1000.0, segment.getTime(), ""));

                List<Segment> trackSegments = segment.getTrackSegments();
                if (!trackSegments.isEmpty()) {
                    sb.append(String.format("│     Segments: %-56d │\n", trackSegments.size()));
                    double segmentLength = trackSegments.stream().mapToDouble(Segment::getLength).sum();
                    // FIXED: Increased width for line length from %5.1f to %7.1f
                    sb.append(String.format("│     Line Length:  %7.1f m %-41s  │%n", segmentLength, ""));
                }

                sb.append("│                                                                        │\n");

                currentFacility = nextFacility;
                segmentNumber++;
            }
        }

        sb.append("├────────────────────────────────────────────────────────────────────────┤\n");
        sb.append("│                           SUMMARY                                      │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ Total Distance: %10.2f km %39s  │\n", getTotalDistanceKm(), ""));
        sb.append(String.format("│ Total Time:     %10.2f hours %36s  │\n", totalTime, ""));
        sb.append(String.format("│ Transfers:      %10d %42s  │\n", getNumberOfTransfers(), ""));
        sb.append(String.format("│ Average Speed:  %10.2f km/h %36s   │\n", getTotalDistanceKm() / totalTime, ""));
        sb.append("└────────────────────────────────────────────────────────────────────────┘");

        return sb.toString();
    }

    // Helper method to get detailed route description
    public String getDetailedRoute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Complete Route: ");

        List<Facility> routeFacilities = new ArrayList<>();
        routeFacilities.add(start);

        for (PathSegment segment : segments) {
            if (segment.getEndFacility() != null) {
                routeFacilities.add(segment.getEndFacility());
            }
        }

        for (int i = 0; i < routeFacilities.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(routeFacilities.get(i).getName());
        }

        return sb.toString();
    }
}