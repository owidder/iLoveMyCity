package hackthedrive.bmw.de.hackthedrive.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.rest.RestData;
import hackthedrive.bmw.de.hackthedrive.domain.rest.RouteObject;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class CarDataUtil {
    private static final LogUtil logger = LogUtil.getLogger(CarDataUtil.class);

    public void pollCarData(final Context context, final TextView textView) {
        new AsyncTask<Void, Void, Void>(){
            JSONObject jsonObject = null;

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    RestClient client = new RestClient(context.getString(R.string.eventUrl));
                    client.AddHeader("MojioAPIToken", context.getString(R.string.mojioAPIToken));

                    client.Execute(RestClient.RequestMethod.GET);

                    String response = client.getResponse();

                    jsonObject = new JSONObject(response);

                    Double lat = jsonObject.getJSONArray("Data").getJSONObject(0).getJSONObject("Location").getDouble("Lat");

                    textView.setText(lat.toString());
                } catch (Exception e) {
                    logger.e(e, "Error retrieving data");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if(jsonObject == null ){
                    logger.e("Error retrieving data");
                    return;
                }
            }
        }.execute();
    }
}
