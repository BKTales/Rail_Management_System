package org.dei.Sprint1.LAPRUS03;

import java.util.List;

public class PathSegment {
    private Line line;
    private List<Segment> trackSegments;
    private double time;
    private Facility startFacility;
    private Facility endFacility;

    public PathSegment(Line line, List<Segment> trackSegments, double time) {
        this.line = line;
        this.trackSegments = trackSegments;
        this.time = time;
    }

    // Getters
    public Line getLine() { return line; }
    public List<Segment> getTrackSegments() { return trackSegments; }
    public double getTime() { return time; }
    public double getDistance() {
        return trackSegments.stream().mapToDouble(Segment::getLength).sum();
    }

    public Facility getStartFacility() {
        return startFacility;
    }

    public Facility getEndFacility() {
        return endFacility;
    }

    public void setStartFacility(Facility startFacility) {
        this.startFacility = startFacility;
    }

    public void setEndFacility(Facility endFacility) {
        this.endFacility = endFacility;
    }

    public String getSegmentDescription() {
        if (startFacility != null && endFacility != null) {
            return String.format("%s → %s via %s (%.2f km, %.2f hours)",
                    startFacility.getName(), endFacility.getName(), line.getName(),
                    getDistance() / 1000.0, time);
        }
        return line.getName();
    }
}