package isep.labdsof.backend.utils;

import static java.lang.Math.*;

public class LocationUtils {

    private static final double EARTH_RADIUS = 6371000; // Earth's radius in meters

    /**
     * Calculates the distance in meters between two geographic coordinates using the Haversine formula.
     *
     * @param lat1 Latitude of the first location in decimal degrees
     * @param lon1 Longitude of the first location in decimal degrees
     * @param lat2 Latitude of the second location in decimal degrees
     * @param lon2 Longitude of the second location in decimal degrees
     * @return Distance in meters between the two locations
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = toRadians(lat1);
        double lon1Rad = toRadians(lon1);
        double lat2Rad = toRadians(lat2);
        double lon2Rad = toRadians(lon2);

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = sin(dLat / 2) * sin(dLat / 2)
                + cos(lat1Rad) * cos(lat2Rad)
                * sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        // Distance in meters
        return EARTH_RADIUS * c;
    }
}

