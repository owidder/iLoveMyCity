package hackthedrive.bmw.de.hackthedrive.service;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by dst on 11.01.2015.
 */
public class PoiService {

    private static final LogUtil logger = LogUtil.getLogger(PoiService.class);

    // private String GET_POI_REQUEST = "//places.demo.api.here.com/places/v1/discover/explore?at=52.5159%2C13.3777&cat=sights-museums&accept=application%2Fjson&app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg";
    private String GET_POI_REQUEST = "http://places.demo.api.here.com/places/v1/discover/explore?at=";
    private String GET_POI_REQUEST1 = "%2C";
    private String GET_POI_REQUEST2 = "&cat=";
    private String GET_POI_REQUEST3 = "&accept=application%2Fjson&app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg";

    public List<Poi> getPublicPois(Location location){

        List<Poi> resultList = new ArrayList<>();
        String category = "sights-museums";
        String dynamicRequest = GET_POI_REQUEST + location.getLatitude() + GET_POI_REQUEST1 + location.getLongitude() + GET_POI_REQUEST2 + category + GET_POI_REQUEST3;

        final RestClient client = new RestClient(dynamicRequest);

        try {
            client.Execute(RestClient.RequestMethod.GET);
            String response = client.getResponse();
            logger.d(response);

            if (response != null && response.length() > 0) {
                JSONObject jsonObject = new JSONObject(response);

                resultList = parseResult(jsonObject);
            } else {
                return null;
            }
        } catch (Exception e){
logger.e(e, "Error getting pois.", e.getMessage());
        }
        return resultList;
    }

    private  List<Poi> parseResult(JSONObject input){
        List<Poi> pois = new ArrayList<>();
        try {
            if (input.has("results")) {
                JSONArray results =  input.getJSONObject("results").getJSONArray("items");
                for(int i=0; i<results.length(); i++){
                    JSONObject entry = (JSONObject)results.get(i);
                    JSONArray position = entry.getJSONArray("position");
                    String title = entry.getString("title");
                    Poi poi = new Poi();
                    poi.setName(title);
                    poi.setLocation(LocationUtil.createLocation(position.getDouble(0), position.getDouble(1)));
                    pois.add(poi);
                    logger.d("Adding poi: %s", poi.toString());
                }
            }
        } catch (JSONException e){
            logger.e(e, "Error getting pois.", e.getMessage());
        }
        return pois;
    }
}
