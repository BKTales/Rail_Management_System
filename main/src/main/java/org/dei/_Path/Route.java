package org.dei._Path;

import org.dei._Train.Freight;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a train route (simple or complex)
 * Simple route: single freight, A to B
 * Complex route: multiple freights, A to B to C...
 */
public class Route {
    private LocalDateTime     departureDay;
    private final boolean           isComplex;
    private final int               routeId;

    private final Path              path;
    private LocalDateTime           arrivalDay;
    private List<Freight>           freights;

    public Route(Path path, int routeId, List<Freight> freight, LocalDateTime departure) {
        this.arrivalDay     = null;
        this.path           = path;
        this.routeId        = routeId;
        this.freights        = freight;
        this.isComplex      = freight.size() > 1 ;
        this.departureDay   = departure;
    }

    public Route(Path path, int routeId, Freight freight, LocalDateTime departure) {
        this.arrivalDay     = null;
        this.path           = path;
        this.routeId        = routeId;
        this.freights       = new ArrayList<>();
        this.isComplex      = false;
        this.departureDay   = departure;
        freights.add(freight);
    }

    public void addFreight(Freight freight) { this.freights.add(freight); }
    public void removeFreight(Freight freight) { this.freights.remove(freight);}
    public void setArrivalDay(LocalDateTime arrivalDay) { this.arrivalDay = arrivalDay;}
    public void setDepartureDay(LocalDateTime departureDay) { this.departureDay = departureDay;}

    public Path     getPath() { return path; }
    public int   getRouteId() { return routeId; }
    public List<Freight>  getFreight() { return freights; }
    public boolean  isComplex () { return isComplex; }
    public LocalDateTime getArrivalDay() { return arrivalDay; }
    public LocalDateTime     getDepartureDay() { return departureDay; }
    public List<Freight> getFreights(){
        return freights;
    }

    @Override
    public String toString() {
        return "Route " + routeId + " has " + freights.size() + "freights";
    }
}
