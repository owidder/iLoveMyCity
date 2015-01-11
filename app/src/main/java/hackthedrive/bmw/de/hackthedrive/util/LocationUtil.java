package hackthedrive.bmw.de.hackthedrive.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import hackthedrive.bmw.de.hackthedrive.domain.Area;

public final class LocationUtil {
    private static final LogUtil logger = LogUtil.getLogger(LocationUtil.class);

    public static Location createLocation(double lat, double lon) {
        Location loc = new Location("vehicle");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    public static LatLng toLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }


    public static String geocodeLocation(Context context, Location loc) {
        return geocodeLocation(context, toLatLng(loc));
    }

    public static String geocodeLocation(Context context, LatLng loc) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(loc.latitude, loc.longitude, 100);
            if (addresses.size() > 0 && addresses != null) {
                Address address = addresses.get(0);
                String addressText = String.format(
                        "%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality());
                return addressText;
            }
        } catch (IOException e) {
            logger.e(e, "Error geocoding location");
        }

        return loc.latitude + "," + loc.longitude;
    }

    public static double getDistanceOfLocationFromPolygon(final Location loc, final List<Location> positions) {

        if (positions.size() < 2 || positions == null) {
            return Double.MAX_VALUE;
        }
        double lat = loc.getLatitude() * 1E6;
        double lon = loc.getLongitude() * 1E6;
        double mindist = Double.MAX_VALUE;
        boolean result = false;
        for (int i = 0, j = positions.size() - 1; i < positions.size(); i++) {
            double latI = positions.get(i).getLatitude() * 1E6;
            double lonI = positions.get(i).getLongitude() * 1E6;
            double latJ = positions.get(j).getLatitude() * 1E6;
            double lonJ = positions.get(j).getLongitude() * 1E6;

            if ((lonI < lon && lonJ >= lon || lonJ < lon && lonI >= lon)
                    && latI + (lon - lonI) / (lonJ - lonI) * (latJ - latI) < lat) {
                result = !result;
            }

            // additionally calculate distance of location to current line
            final double dist = getDistanceOfLocationFromLine(loc, latI, lonI, latJ, lonJ);
            if (dist < mindist) {
                mindist = dist;
            }

            // move to next line
            j = i;
        }
        // an even number of intersections means inside of polygon => distance is negative
        double distance = mindist * 60D * 1852D;
        return result ? -1D * distance : distance;
    }

    private static double getDistanceOfLocationFromLine(final Location loc, double lat1, double lon1, double lat2,
                                                        double lon2) {
        final double locLat = loc.getLatitude() * 1E6;
        final double locLon = loc.getLongitude() * 1E6;

        final double xDelta = lat2 - lat1;
        final double yDelta = lon2 - lon1;
        final double u = ((locLat - lat1) * xDelta + (locLon - lon1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
        final double distx;
        final double disty;
        if (u < 0) {
            distx = lat1 - locLat;
            disty = lon1 - locLon;
        }
        else if (u > 1) {
            distx = lat2 - locLat;
            disty = lon2 - locLon;
        }
        else {
            distx = lat1 + u * xDelta - locLat;
            disty = lon1 + u * yDelta - locLon;
        }
        final double dist = Math.sqrt(distx * distx + disty * disty);
        return dist / 1E6;
    }

    public static boolean isInArea(final Location loc, final Area area) {
        return getDistanceOfLocationFromPolygon(loc, area.getLocations()) < 1D;
    }

    public static boolean isInArea(final Location loc, List<Location> area) {
        return getDistanceOfLocationFromPolygon(loc, area) < 1D;
    }

    public static List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }


    public static Location latLng2Location(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static List<Location> listOfLatLng2listLOfLocation(List<LatLng> listOfLatLng) {
        List<Location> listOfLocation = new ArrayList<>();

        for (LatLng latLng : listOfLatLng) {
            Location location = LocationUtil.latLng2Location(latLng);
            listOfLocation.add(location);
        }

        return listOfLocation;
    }
}
