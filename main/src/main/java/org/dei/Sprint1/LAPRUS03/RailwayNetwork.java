package org.dei.Sprint1.LAPRUS03;

import org.dei._Train.Locomotive;

import java.util.*;

public class RailwayNetwork {
    private Map<Integer, Facility> facilities;
    private Map<Integer, Line> lines;
    private Map<Integer, Segment> segments;
    private Map<String, Locomotive> locomotives;
    private Map<String, Operator> operators;

    public RailwayNetwork() {
        this.facilities = new HashMap<>();
        this.lines = new HashMap<>();
        this.segments = new HashMap<>();
        this.locomotives = new HashMap<>();
        this.operators = new HashMap<>();
    }

    // Add methods
    public void addFacility(Facility facility) {
        facilities.put(facility.getId(), facility);
    }

    public void addLine(Line line) {
        lines.put(line.getId(), line);
    }

    public void addSegment(Segment segment) {
        segments.put(segment.getId(), segment);
    }

    public void addLocomotive(Locomotive locomotive) {
        locomotives.put(locomotive.getNumber(), locomotive);
    }

    public void addOperator(Operator operator) {
        operators.put(operator.getShortName(), operator);
    }

    // Get methods
    public Facility getFacility(int id) {
        return facilities.get(id);
    }

    public Line getLine(int id) {
        return lines.get(id);
    }

    public Segment getSegment(int id) {
        return segments.get(id);
    }

    public Locomotive getLocomotive(String number) {
        return locomotives.get(number);
    }

    public Operator getOperator(String shortName) {
        return operators.get(shortName);
    }

    // Get collections
    public Collection<Facility> getAllFacilities() {
        return new ArrayList<>(facilities.values());
    }

    public Collection<Line> getAllLines() {
        return new ArrayList<>(lines.values());
    }

    public Collection<Segment> getAllSegments() {
        return new ArrayList<>(segments.values());
    }

    public Collection<Locomotive> getAllLocomotives() {
        return new ArrayList<>(locomotives.values());
    }

    public Collection<Operator> getAllOperators() {
        return new ArrayList<>(operators.values());
    }

    // Business logic methods
    public List<Segment> getSegmentsForLine(int lineId) {
        List<Segment> lineSegments = new ArrayList<>();
        for (Segment segment : segments.values()) {
            if (segment.getLineId() == lineId) {
                lineSegments.add(segment);
            }
        }
        lineSegments.sort(Comparator.comparingInt(Segment::getOrder));
        return lineSegments;
    }

    public double getLineTotalLength(int lineId) {
        return getSegmentsForLine(lineId).stream()
                .mapToDouble(Segment::getLength)
                .sum();
    }

    public Facility getLineStartFacility(int lineId) {
        Line line = lines.get(lineId);
        return line != null ? facilities.get(line.getStartFacilityId()) : null;
    }

    public Facility getLineEndFacility(int lineId) {
        Line line = lines.get(lineId);
        return line != null ? facilities.get(line.getEndFacilityId()) : null;
    }

    public Line findDirectLineBetweenFacilities(int facility1Id, int facility2Id) {
        for (Line line : lines.values()) {
            if ((line.getStartFacilityId() == facility1Id && line.getEndFacilityId() == facility2Id) ||
                    (line.getStartFacilityId() == facility2Id && line.getEndFacilityId() == facility1Id)) {
                return line;
            }
        }
        return null;
    }
}
