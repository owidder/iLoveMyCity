package hackthedrive.bmw.de.hackthedrive.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ewa on 11.01.2015.
 */
public class PolygonUtil {

    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    public static List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }
}
