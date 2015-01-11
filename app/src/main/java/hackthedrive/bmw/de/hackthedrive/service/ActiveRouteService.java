package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.activity.DestinationReachedActivity;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.PolygonUtil;

/**
 * Created by ewa on 11.01.2015.
 */
public class ActiveRouteService {

    private static ActiveRouteService instance;
    private Context context;
    private Route route;
    private List<Location> endPolygon;

    public static ActiveRouteService getInstance(Context context){
        if( instance == null ){
            instance = new ActiveRouteService(context);
        }
        return instance;
    }

    public ActiveRouteService(Context context) {
        this.context = context;
    }

    public synchronized void setActiveRoute(Route route){
        this.route = route;
        this.endPolygon = new ArrayList<Location>();

        List<LatLng> rectangle = PolygonUtil.createRectangle(LocationUtil.toLatLng(route.getEnd()), 0.0003, 0.0003);
        for(LatLng p : rectangle){
            endPolygon.add(LocationUtil.createLocation(p.latitude, p.longitude));
        }
    }

    public synchronized void processDataChange(Vehicle currentVehicle){
        if( this.route == null ){
            return;
        }
        if( !LocationUtil.isInArea(LocationUtil.createLocation(currentVehicle.getLat(), currentVehicle.getLng()), endPolygon)){
            return;
        }

        this.route = null;
        this.endPolygon  = null;

        DriveInService.getInstance(context).startMonitoring(null);

        Intent intent = new Intent(context, DestinationReachedActivity.class);
        intent.putExtra(BaseActivity.ROUTE_INTENT_EXTRA, GsonSerializer.serialize(new Route()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
