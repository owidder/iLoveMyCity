package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Context;
import android.content.Intent;
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
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.factory.TestDataFactory;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;


public class SearchRouteActivity extends BaseActivity {

    private static final LogUtil logger = LogUtil.getLogger(SearchRouteActivity.class);

    private RouteService routeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeService = new RouteService();

        setContentView(R.layout.activity_search_route);

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
                Intent intent = new Intent(SearchRouteActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setupList() {
        final ListView listview = (ListView) findViewById(R.id.listview);

        AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {
            List<Route> list;

            @Override
            protected Void doInBackground(Void... params) {
                logger.i("Start loading routes");
                list = TestDataFactory.createTestRoutes(getApplicationContext());
                logger.i("Added test routes");
                try {
                    List<Route> localRoutes = routeService.getAllRoutes();
                    logger.d("Adding %s local routes.", localRoutes.size());
                    for (Route route : localRoutes) {
                        // get start address: HACK just for NOW
                        route.setStartAddress(LocationUtil.geocodeLocation(getApplicationContext(), LocationUtil.toLatLng(route.getStart())));
                        route.setEndAddress(LocationUtil.geocodeLocation(getApplicationContext(), LocationUtil.toLatLng(route.getStart())));
                        list.add(route);
                    }
                    logger.i("Added db routes");
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
                        final Route item = (Route) parent.getItemAtPosition(position);
                        Intent intent = new Intent(getApplicationContext(), StartRouteActivity.class);
                        intent.putExtra(ROUTE_INTENT_EXTRA, GsonSerializer.serialize(item));
                        startActivity(intent);
                    }
                });
            }
        }.execute();
    }

    public class RouteArrayAdapter extends ArrayAdapter<Route> {
        private final Context context;
        private final List<Route> values;

        public RouteArrayAdapter(Context context, List<Route> values) {
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
            from.setText("From: " + values.get(position).getStartAddress());

            TextView to = (TextView) rowView.findViewById(R.id.to);
            to.setText("To: " + values.get(position).getEndAddress());

            TextView rating = (TextView) rowView.findViewById(R.id.rating);
            rating.setText(String.valueOf(values.get(position).getRating()));
            return rowView;
        }
    }
}
