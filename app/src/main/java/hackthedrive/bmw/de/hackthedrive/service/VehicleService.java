package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONObject;

import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class VehicleService {

    private static final LogUtil logger = LogUtil.getLogger();

    private Context context;

    public VehicleService(Context context) {
        this.context = context;
    }

    private JSONObject getServiceData() throws Exception {
        final RestClient client = new RestClient(context.getString(R.string.eventUrl));
        client.AddHeader("MojioAPIToken", context.getString(R.string.mojioAPIToken));
        client.AddParam("limit", "1");
        client.AddParam("offset", "0");

        client.Execute(RestClient.RequestMethod.GET);
        String response = client.getResponse();

        if(response != null && response.length() > 0) {
            JSONObject jsonObject = new JSONObject(response);

            return jsonObject;
        } else {
            return null;
        }
    }

    public Location getCurrentLocation(){
        try {
            JSONObject serviceData = getServiceData();

            if(serviceData != null) {
                JSONObject location = serviceData.getJSONArray("Data").getJSONObject(0).getJSONObject("Location");

                Double lat = location.getDouble("Lat");
                Double lon = location.getDouble("Lng");

                return LocationUtil.createLocation(lat, lon);
            }
        } catch (Exception e) {
           logger.e(e, "Problem with rest call. %s", e.getMessage());
        }
        return null;
    }


    public Vehicle getCurrentVehicle() {
        Vehicle vehicle = null;

        try {
            JSONObject serviceData = getServiceData();

            if(serviceData != null) {
                JSONObject data = serviceData.getJSONArray("Data").getJSONObject(0);
                if (data == null) {
                    return null;
                }

                JSONObject location = data.getJSONObject("LastLocation");
                if (location == null) {
                    return null;
                }

                Double lat = location.getDouble("Lat");
                Double lng = location.getDouble("Lng");

                Double heading = data.getDouble("LastHeading");
                Double batteryLevel = data.getDouble("LastBatteryLevel");
                Integer speed = data.getInt("LastSpeed");

                vehicle = new Vehicle();
                vehicle.setLat(lat);
                vehicle.setLng(lng);
                vehicle.setHeading(heading);
                vehicle.setBatteryLevel(batteryLevel);
                vehicle.setSpeed(speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicle;
    }
}
