package hackthedrive.bmw.de.hackthedrive.factory;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;

/**
 * Created by ewa on 11.01.2015.
 */
public class TestDataFactory {
    public static List<Route> createTestRoutes(Context context){
        List<Route> result = new ArrayList<Route>();
        result.add(createTestRoute1(context, true));
        result.add(createTestRoute2(context, true));
        return result;
    }

    public static Route createTestRoute1(Context context, boolean resolveAdress) {
        Route route = new Route();
        route.setName("Test Route");

        route.setCostInDollar(30);
        route.setDistanceInMi(39);
        route.setRating(4.9f);

        route.setStart(LocationUtil.createLocation(37.788436, -122.401318));
        route.setEnd(LocationUtil.createLocation(37.593461, -122.36667));

        if( resolveAdress ) {
            route.setStartAddress(LocationUtil.geocodeLocation(context, route.getStart()));
            route.setEndAddress(LocationUtil.geocodeLocation(context, route.getEnd()));
        }

        List<Poi> viaPoints = new ArrayList<Poi>();
        viaPoints.add(createPoi(LocationUtil.createLocation(37.784254, -122.401481)));
        viaPoints.add(createPoi(LocationUtil.createLocation(37.698249, -122.392854)));
        viaPoints.add(createPoi(LocationUtil.createLocation(37.639802, -122.405671)));
        route.setPois(viaPoints);

        Area area = new Area();
        area.addLocation(LocationUtil.createLocation(37.784657, -122.401135));
        area.addLocation(LocationUtil.createLocation(37.784364, -122.401173));
        area.addLocation(LocationUtil.createLocation(37.784235, -122.401361));
        area.addLocation(LocationUtil.createLocation(37.784311, -122.401540));

        area.setText("On the north-western side you can see the best bars and restaurants in town. \n\nAt least that is what I have been told!");
        route.addDriveInArea(area);

        return route;
    }

    private static Poi createPoi(Location location){
        Poi viaPoint = new Poi();
        viaPoint.setLocation(location);
        viaPoint.setRadius(10);
        viaPoint.setName("ViaPoint");
        return viaPoint;
    }

    private static Route createTestRoute2(Context context, boolean resolveAdress) {
        Route route2 = new Route();
        route2.setName("Test Route #2");
        route2.setCostInDollar(30);
        route2.setDistanceInMi(39);
        route2.setStart(LocationUtil.createLocation(37.778845, -122.414722));
        route2.setEnd(LocationUtil.createLocation(37.779795, -122.407201));

        if( resolveAdress ) {
            route2.setStartAddress(LocationUtil.geocodeLocation(context, route2.getStart()));
            route2.setEndAddress(LocationUtil.geocodeLocation(context, route2.getEnd()));
        }
        route2.setRating(4.3f);
        return route2;
    }
}
