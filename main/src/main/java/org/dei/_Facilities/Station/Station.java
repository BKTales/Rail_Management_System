package org.dei._Facilities.Station;

import org.dei._Facilities.Facility;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.Country;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;

import java.util.Comparator;
import java.util.Objects;

public class Station extends Facility {
    private String              name;
    private boolean             isCity;
    private boolean             isAirport;
    private boolean             isMainStation;

    public Station(GeographicalLocation location, TimeZoneGroup timeZoneGroup, TimeZone timeZone,
                    Country country, String name, int id, boolean isCity, boolean isAirport, boolean isMainStation) {
        super(location, timeZoneGroup, timeZone, country, name, id);
        this.name = name;
        this.isCity = isCity;
        this.isAirport = isAirport;
        this.isMainStation = isMainStation;
    }

    /**
     * Returns the country where this station is located.
     *
     * @return the country object
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Checks if this station is an airport.
     *
     * @return true if this station is an airport, false otherwise
     */
    public boolean isAirport() {
        return isAirport;
    }

    /**
     * Checks if this station is a main station.
     *
     * @return true if this is a main station, false otherwise
     */
    public boolean isMainStation() {
        return isMainStation;
    }

    /**
     * Checks if this station is located in a city.
     *
     * @return true if the station is in a city, false otherwise
     */
    public boolean isCity() {
        return isCity;
    }

    /**
     * Returns the geographical location of this station.
     *
     * @return the geographical location object
     */
    public GeographicalLocation getLocation() {
        return location;
    }



    /**
     * Returns the timezone location of this station.
     *
     * @return the timezone object
     */
    public TimeZoneGroup getTimeZoneGroup() {
        return timeZoneGroup;
    }

    /**
     * Returns the name of this station.
     *
     * @return the station name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.name);
        result.append("\n");
        result.append(this.location.toString());
        result.append("\n");
        result.append(this.country.toString());
        result.append("\n");
        result.append(this.timeZone.toString());
        result.append("\n");
        result.append(this.timeZoneGroup.toString());
        result.append("\n");
        if(isCity){
            result.append("isCity\n");
        }
        if(isMainStation){
            result.append("isMainStation\n");
        }
        if(isAirport){
            result.append("isAirport\n");
        }

        return result.toString();
    }

    public static Comparator<Station> getComparatorByCity(){
        return (o1, o2) -> {
            if(o1.isCity == o2.isCity) return 0;
            if(o1.isCity) return -1;
            else return 1;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Station station = (Station) o;
        return isCity == station.isCity && isAirport == station.isAirport && isMainStation == station.isMainStation && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, isCity, isAirport, isMainStation);
    }
}