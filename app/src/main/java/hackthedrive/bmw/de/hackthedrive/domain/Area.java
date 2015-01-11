package hackthedrive.bmw.de.hackthedrive.domain;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;

/**
 * Created by ewa on 11.01.2015.
 */
public class Area {
    public enum AreaState{
        INSIDE,
        OUTSIDE
    }

    public Location getViaPoint() {
        return viaPoint;
    }

    public void setViaPoint(Location viaPoint) {
        this.viaPoint = viaPoint;
    }

    /**
     * Only set if the area is around a Via Point
     */
    private Location viaPoint = null;

    private List<Location> locations;
    private String text;
    private AreaState state = AreaState.OUTSIDE;

    public List<Location> getLocations() {
        return locations;
    }

    public List<LatLng> getLocationsLatLng() {
        List<LatLng> result = new ArrayList<LatLng>();
        for(Location loc : locations){
            result.add(LocationUtil.toLatLng(loc));
        }

        return result;
    }


    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location){
        if( locations == null ){
            locations = new ArrayList<Location>();
        }
        locations.add(location);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AreaState getState() {
        return state;
    }

    public void setState(AreaState state) {
        this.state = state;
    }
}
