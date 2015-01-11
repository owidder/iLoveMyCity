package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;

/**
 * Created by ewa on 10.01.2015.
 */
public class VehicleServiceAsyncWrapper {
    public static final long POLLING_INTERVAL = 2000;

    private List<VehicleDataListener> listeners = new ArrayList<VehicleDataListener>();
    private Thread updateThread;
    private VehicleUpdateRunnable updateRunnable;
    private VehicleService vehicleService;
    private Vehicle currentVehicle;
    private Context context;

    private static VehicleServiceAsyncWrapper instance;
    public static VehicleServiceAsyncWrapper instance(Context context){
        if( instance == null ){
            instance = new VehicleServiceAsyncWrapper(context);
        }

        return instance;
    }

    public VehicleServiceAsyncWrapper(Context context) {
        this.context = context;
        vehicleService = new VehicleService(context);
    }

    private synchronized void start(){
        if( updateThread != null ){
            return;
        }

        updateRunnable = new VehicleUpdateRunnable(vehicleService);
        updateThread = new Thread(updateRunnable);
        updateThread.start();
    }
    private synchronized void stop(){
        if( updateRunnable != null ){
            updateRunnable.suspend();
        }

        updateRunnable = null;
        updateThread = null;
    }

    public synchronized Location getCurrentLocation(){
        if( getLastVehicle() != null ){
            return LocationUtil.createLocation(getLastVehicle().getLat(), getLastVehicle().getLng());
        }

        return vehicleService.getCurrentLocation();
    }
    public synchronized Vehicle getLastVehicle(){
        return currentVehicle;
    }

    public synchronized void addListener(VehicleDataListener listener){
        if( listeners.size() == 0 ){
            start();
        }
        listeners.add(listener);
    }
    public synchronized void removeListener(VehicleDataListener listener){
        listeners.remove(listener);

        if( listeners.size() == 0 ){
            stop();
        }
    }

    public interface VehicleDataListener {
        void onVehicleDataChanged(Vehicle data);
    }

    public class VehicleUpdateRunnable implements Runnable{
        private boolean suspended = false;
        private VehicleService vehicleService;

        public VehicleUpdateRunnable(VehicleService vehicleService){
            this.vehicleService = vehicleService;
        }

        @Override
        public void run() {
            while(true) {
                if (suspended) {
                    return;
                }

                currentVehicle = vehicleService.getCurrentVehicle();

                if (suspended) {
                    return;
                }

                if( currentVehicle != null ) {
                    for (VehicleDataListener listener : listeners) {
                        listener.onVehicleDataChanged(currentVehicle);
                    }
                    DriveInService.getInstance(context).processLocationChange(currentVehicle);
                    ActiveRouteService.getInstance(context).processDataChange(currentVehicle);
                }

                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void suspend(){
            suspended = true;
        }
    }
}
