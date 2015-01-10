package hackthedrive.bmw.de.hackthedrive.service;

import android.location.Location;

import hackthedrive.bmw.de.hackthedrive.domain.Route;

/**
 * Created by dst on 10.01.2015.
 */
public class RouteService {

    public Route createNewRoute(Location startingPoint){
        Route route = new Route();
        route.setStart(startingPoint);
        return route;
    }

    public void saveRoute(Route route){
        // TODO
    }
}
