package org.dei._Train;

import org.dei._Facilities.Facility;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Freight {
    private final String id;
    private List<Wagon> wagons;
    private Facility startFacility;
    private Facility endFacility;

    public Freight(String id, Facility startFacility, Facility endFacility) {
        this.id = id;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.wagons = new ArrayList<>();
    }

    public Freight(String id, Facility startFacility, Facility endFacility, List<Wagon> wagons) {
        this.id = id;
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.wagons = wagons;
    }

    public Freight(Facility startFacility, Facility endFacility, List<Wagon> wagons) {
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        this.wagons = wagons;
        this.id = "idNotDefined";
    }

    public Freight(Facility startFacility, Facility endFacility) {
        this.startFacility = startFacility;
        this.endFacility = endFacility;
        wagons = new ArrayList<>();
        this.id = "idNotDefined";
    }


    public void addWagon(Wagon wg) { this.wagons.add(wg); }

    public String       getId() { return id; }
    public List<Wagon>  getWagons() { return wagons; }
    public Facility     getEndFacility() { return endFacility; }
    public Facility     getStartFacility() { return startFacility; }

    public void setWagons(List<Wagon> wagons) { this.wagons = wagons; }
    public void setEndFacility(Facility endFacility) { this.endFacility = endFacility; }
    public void setStartFacility(Facility startFacility) { this.startFacility = startFacility; }

    /**
     * Checks if all wagons in this freight have the same gauge.
     * @return The gauge string (e.g., "1435mm") or null if mixed/empty.
     */
    public String getUniformGauge() {
        if (wagons.isEmpty()) return null;

        String referenceGauge = wagons.get(0).getWagonModel().getGauge();
        for (Wagon w : wagons) {
            if (!w.getWagonModel().getGauge().equals(referenceGauge)) {
                throw new IllegalStateException("Freight contains wagons with mixed gauges.");
            }
        }
        return referenceGauge;
    }

    @Override
    public String toString() {
        return "Freight " + id + " [" + startFacility.getName() + " -> " + endFacility.getName() +
                "] | Wagons: " + wagons.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Freight freight = (Freight) o;
        return Objects.equals(id, freight.id); // Comparação de String ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}