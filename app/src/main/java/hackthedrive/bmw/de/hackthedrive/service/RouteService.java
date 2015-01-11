package hackthedrive.bmw.de.hackthedrive.service;

import android.location.Location;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.adapter.DbHelper;
import hackthedrive.bmw.de.hackthedrive.adapter.RouteDataSource;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by dst on 10.01.2015.
 */
public class RouteService {

    private static final LogUtil logger = LogUtil.getLogger();

    private RouteDataSource routeDs;

    public RouteService(){
        routeDs = new RouteDataSource();
    }

    public Route createNewRoute(Location startingPoint){
        Route route = new Route();
        route.setStart(startingPoint);
        return route;
    }

    public void save(Route route){
        routeDs.save(route);
    }

    public List<Route> getAllRoutes(){
        return routeDs.getAllRoutes();
    }

    public void saveRouteRest(Route route){
        RestClient client = new RestClient("http://172.16.1.110:8080/rest/route/new");
        try {
            client.AddParam("completeJson", GsonSerializer.serialize(route));
            client.Execute(RestClient.RequestMethod.PUT);
        } catch (Exception e) {
            logger.e(e, "Route could not be saved. %s", e.getMessage());
        }
    }
}
