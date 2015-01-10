package hackthedrive.bmw.de.hackthedrive.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public final class LocationUtil {
    public static Location createLocation(double lat, double lon){
        Location loc = new Location("vehicle");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    public static LatLng toLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

}
