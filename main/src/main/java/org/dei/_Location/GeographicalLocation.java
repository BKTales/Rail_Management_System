package org.dei._Location;

public class GeographicalLocation {
    double latitude;
    double longitude;

    /**
     * Constructs a GeographicalLocation with specified coordinates.
     *
     * @param latitude the latitude coordinate in decimal degrees
     * @param longitude the longitude coordinate in decimal degrees
     */
    public GeographicalLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeographicalLocation() {
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * Returns the latitude coordinate of this location.
     *
     * @return the latitude value in decimal degrees
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude coordinate of this location.
     *
     * @return the longitude value in decimal degrees
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the latitude coordinate of this location.
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude coordinate of this location.
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lat: ");
        sb.append(latitude);
        sb.append(", Lon: ");
        sb.append(longitude);
        return sb.toString();
    }
}