package hackthedrive.bmw.de.hackthedrive;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import org.json.JSONObject;

import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;
import hackthedrive.bmw.de.hackthedrive.util.RestClient;

/**
 * Created by oliverwidder on 10/01/15.
 */
public class CarDataService extends Service {

    private static final LogUtil logger = LogUtil.getLogger(RestApiActivity.class);

    private JSONObject pollCarData() {
        final JSONObject jsonObject = null;

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    RestClient client = new RestClient(getString(R.string.eventUrl));
                    client.AddHeader("MojioAPIToken", getString(R.string.mojioAPIToken));

                    client.Execute(RestClient.RequestMethod.GET);

                    String response = client.getResponse();

                    JSONObject jsonObject = new JSONObject(response);
                } catch (Exception e) {
                    logger.e(e, "Error retrieving data");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if( jsonObject == null ){
                    logger.e("Error retrieving data");
                    return;
                }
            }
        }.execute();

        return jsonObject;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
