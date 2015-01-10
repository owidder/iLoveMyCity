package hackthedrive.bmw.de.hackthedrive.service;

import android.content.Context;

import org.json.JSONObject;

import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class VehicleService {

    private JSONObject getServiceData(Context context) throws Exception {
        RestClient client = new RestClient(context.getString(R.string.eventUrl));
        client.AddHeader("MojioAPIToken", context.getString(R.string.mojioAPIToken));

        client.Execute(RestClient.RequestMethod.GET);

        String response = client.getResponse();

        JSONObject jsonObject = new JSONObject(response);

        return jsonObject;
    }


    public Vehicle getCurrentVehicle(Context context) {
        Vehicle vehicle = null;

        try {
            JSONObject serviceData = getServiceData(context);

            JSONObject location = serviceData.getJSONArray("Data").getJSONObject(0).getJSONObject("Location");

            Double lat = location.getDouble("Lat");
            Double lng = location.getDouble("Lng");

            vehicle.setLat(lat);
            vehicle.setLng(lng);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicle;
    }
}
