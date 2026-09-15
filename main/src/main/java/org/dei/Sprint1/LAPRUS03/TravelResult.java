package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;

import java.util.ArrayList;
import java.util.List;

public class TravelResult {
    private Facility start;
    private Facility end;
    private Locomotive locomotive;
    private List<Segment> segments;
    private List<Double> segmentTimes;
    private double totalDistance; // meters
    private double totalTime; // hours

    public TravelResult(Facility start, Facility end, Locomotive locomotive) {
        this.start = start;
        this.end = end;
        this.locomotive = locomotive;
        this.segments = new ArrayList<>();
        this.segmentTimes = new ArrayList<>();
        this.totalDistance = 0.0;
        this.totalTime = 0.0;
    }

    public void addSegment(Segment segment, double segmentTime) {
        segments.add(segment);
        segmentTimes.add(segmentTime);
        totalDistance += segment.getLength();
        totalTime += segmentTime;
    }

    // Getters
    public Facility getStart() { return start; }
    public Facility getEnd() { return end; }
    public Locomotive getLocomotive() { return locomotive; }
    public List<Segment> getSegments() { return new ArrayList<>(segments); }
    public List<Double> getSegmentTimes() { return new ArrayList<>(segmentTimes); }
    public double getTotalDistance() { return totalDistance; }
    public double getTotalTime() { return totalTime; }
    public double getTotalDistanceKm() { return totalDistance / 1000.0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════╗\n");
        sb.append("║                TRAVEL RESULTS                  ║\n");
        sb.append("╠════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ From: %-40s ║\n", start.getName()));
        sb.append(String.format("║ To:   %-40s ║\n", end.getName()));
        sb.append(String.format("║ Locomotive: %-34s ║\n", locomotive.getModel().getName()));
        sb.append("╠════════════════════════════════════════════════╣\n");

        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            double time = segmentTimes.get(i);
            sb.append(String.format("║ %d. Segment %d: %6.1f m, %18.2f min ║\n",
                    i + 1, segment.getId(), segment.getLength(), time * 60));
        }

        sb.append("╠════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Total: %6.1f km, %5.2f hours %15s  ║\n",
                getTotalDistanceKm(), totalTime, ""));
        sb.append("╚════════════════════════════════════════════════╝");

        return sb.toString();
    }
}