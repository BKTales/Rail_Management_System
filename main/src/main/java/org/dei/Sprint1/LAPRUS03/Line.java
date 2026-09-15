package org.dei.Sprint1.LAPRUS03;

public class Line {
    private int id;
    private String name;
    private String owner;
    private int startFacilityId;
    private int endFacilityId;
    private int gauge;

    public Line(int id, String name, String owner, int startFacilityId, int endFacilityId, int gauge) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.startFacilityId = startFacilityId;
        this.endFacilityId = endFacilityId;
        this.gauge = gauge;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getOwner() { return owner; }
    public int getStartFacilityId() { return startFacilityId; }
    public int getEndFacilityId() { return endFacilityId; }
    public int getGauge() { return gauge; }

    @Override
    public String toString() {
        return String.format("%d - %s", id, name);
    }
}
