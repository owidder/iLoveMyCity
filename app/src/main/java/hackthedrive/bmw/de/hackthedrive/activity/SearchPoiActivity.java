package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.service.PoiService;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;


public class SearchPoiActivity extends BaseActivity {

    private static final LogUtil logger = LogUtil.getLogger(SearchPoiActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pois);

        setupToolbar();
        setupList();
    }


    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Explore Tours");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchPoiActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private static final Location SAN_FRAN_LOC = LocationUtil.createLocation(37.752407, -122.416829);
    private void setupList() {
        final ListView listview = (ListView) findViewById(R.id.listview);

        AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {
            List<Poi> list;

            @Override
            protected Void doInBackground(Void... params) {
                logger.i("Start loading pois");
                try {
                    PoiService poiService = new PoiService();
                    list = poiService.getPublicPois(SAN_FRAN_LOC);
                    logger.d("Adding %s local routes.", list.size());
                } catch (Exception ex){
                    logger.e(ex,"Error loading routes: %s", ex.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                final RouteArrayAdapter adapter = new RouteArrayAdapter(getApplicationContext(), list);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                       // TODO
                    }
                });
            }
        }.execute();
    }

    public class RouteArrayAdapter extends ArrayAdapter<Poi> {
        private final Context context;
        private final List<Poi> values;

        public RouteArrayAdapter(Context context, List<Poi> values) {
            super(context, R.layout.route_row_template, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.route_row_template, parent, false);

            TextView name = (TextView) rowView.findViewById(R.id.name);
            name.setText(values.get(position).getName());

            TextView from = (TextView) rowView.findViewById(R.id.from);
            from.setText("Where: " + values.get(position).getLocation());

            return rowView;
        }
    }
}
