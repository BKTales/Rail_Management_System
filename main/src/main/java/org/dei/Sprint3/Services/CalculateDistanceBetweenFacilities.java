package org.dei.Sprint3.Services;

public class CalculateDistanceBetweenFacilities {

    private static final double EARTH_RADIUS = 6371; // earth radius in kilometers

    /**
     * Calculates the Haversine distance between two geographical points.
     * This gives the great-circle distance in kilometers.
     *
     * @param lat1 first point latitude
     * @param lon1 first point longitude
     * @param lat2 second point latitude
     * @param lon2 second point longitude
     * @return the distance in kilometers
     */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // convert degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(lon2 - lon1);

        // convert starting and ending latitudes to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply the Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLong / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // calculate the final distance
        return EARTH_RADIUS * c;
    }
}
