package hackthedrive.bmw.de.hackthedrive.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

}
