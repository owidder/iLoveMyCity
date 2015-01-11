package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.HtdApplication;
import hackthedrive.bmw.de.hackthedrive.activity.DriveInEventActivity;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by ewa on 11.01.2015.
 */
public class DriveInService {
    private static final LogUtil logger = LogUtil.getLogger(DriveInService.class);

    private static DriveInService instance;
    private Context context;
    private List<Area> areas;

    public static DriveInService getInstance(Context context){
        if( instance == null ){
            instance = new DriveInService(context);
        }
        return instance;
    }

    public DriveInService(Context context) {
        this.context = context;
    }

    public synchronized void startMonitoring(List<Area> areas){
        this.areas = areas;
    }

    public synchronized void processLocationChange(Vehicle currentVehicle){
        if( areas == null || areas.size() == 0 ){
            return;
        }

        Location current = LocationUtil.createLocation(currentVehicle.getLat(), currentVehicle.getLng());

        for(int i = 0; i < areas.size(); i++) {
            Area area = areas.get(i);
            if (LocationUtil.isInArea(current, area)){
                if( area.getState() == Area.AreaState.OUTSIDE){
                    area.setState(Area.AreaState.INSIDE);

                    if(area.getViaPoint() != null) {
                        sendNextAddressToCar(i);
                    } else {
                        showDriveInEvent(area);
                    }
                }
            }else{
                area.setState(Area.AreaState.OUTSIDE);
            }
        }
    }

    private void sendNextAddressToCar(int currentAreaPosition) {
        Area nextPoiArea = findNextPoiAreaAfterPosition(currentAreaPosition);
        if(nextPoiArea != null) {
            Intent intent = new Intent(context, DriveInEventActivity.class);

            String message = String.format("Send next address to car: lat = %d, lng = %d",
                    nextPoiArea.getViaPoint().getLatitude(),
                    nextPoiArea.getViaPoint().getLongitude());

            intent.putExtra(BaseActivity.DRIVE_IN_EVENT_INTENT_EXTRA, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private Area findNextPoiAreaAfterPosition(int position) {
        Area nextViaPointArea = null;

        if(areas.size() > position+1) {
            for(int j = position+1; j < areas.size(); j++) {
                Area maybeViaPointArea = areas.get(j);
                if(maybeViaPointArea.getViaPoint() != null) {
                    nextViaPointArea = maybeViaPointArea;
                    break;
                }
            }
        }

        return nextViaPointArea;
    }

    private void showDriveInEvent(Area area) {
        Intent intent = new Intent(context, DriveInEventActivity.class);
        intent.putExtra(BaseActivity.DRIVE_IN_EVENT_INTENT_EXTRA, GsonSerializer.serialize(area));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
