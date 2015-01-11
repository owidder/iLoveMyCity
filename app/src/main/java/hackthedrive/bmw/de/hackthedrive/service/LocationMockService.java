package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.lang.reflect.Method;

import hackthedrive.bmw.de.hackthedrive.HtdApplication;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by ewa on 11.01.2015.
 */
public class LocationMockService {
    private static final LogUtil logger = LogUtil.getLogger(HtdApplication.class);

    private static LocationMockService instance;
    private VehicleServiceAsyncWrapper vehicleServiceAsyncWrapper;
    private Location lastLoc = null;
    private final LocationManager lm;

    public static LocationMockService getInstance(Context context){
        if( instance == null ){
            instance = new LocationMockService(context);
        }
        return instance;
    }

    public LocationMockService(Context context) {
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        lm.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, true, false, false, 0, 5);
        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        vehicleServiceAsyncWrapper = new VehicleServiceAsyncWrapper(context);
        vehicleServiceAsyncWrapper.addListener(new VehicleServiceAsyncWrapper.VehicleDataListener() {

            @Override
            public void onVehicleDataChanged(Vehicle data) {
                pushLocation(data);
            }
        });
    }

    private void pushLocation(Vehicle data){
        Location loc = new Location(LocationManager.GPS_PROVIDER);
        loc.setLatitude(data.getLat());
        loc.setLongitude(data.getLng());
        loc.setAltitude(30.0);
        loc.setTime(System.currentTimeMillis());
        if( data.getHeading() != null ) {
            loc.setBearing(data.getHeading().floatValue());
        }
        loc.setAccuracy(16F);
        loc.setAltitude(0D);

        pushLocation(loc);
    }

    public void pushLocation(Location loc){
        makeLocationComplete(loc);

        if( lastLoc == null ){
            lastLoc = loc;
        }
        else{
            if( lastLoc.getLatitude() == loc.getLatitude() && lastLoc.getLongitude() == loc.getLongitude()){
                logger.d("Location did not change: %s", loc);
                return;
            }
        }

        logger.d("Set location to test provider: %s", loc);
        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, loc);
    }

    private void makeLocationComplete(Location loc) {
        try {
            Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
            if (locationJellyBeanFixMethod != null) {
                locationJellyBeanFixMethod.invoke(loc);
            }
        }
        catch(Exception ex){

        }
    }

}
