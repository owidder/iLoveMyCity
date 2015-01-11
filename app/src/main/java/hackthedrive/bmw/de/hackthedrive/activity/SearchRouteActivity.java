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

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.WelcomeActivity;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;


public class SearchRouteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_route);

        setupToolbar();
        setupList();
    }


    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Search for routes");
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
                list = createTestRoutes();
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
                        //Intent intent = new Intent(getApplicationContext(), item.getActivityToStart());
                        //startActivity(intent);
                    }
                });
            }
        }.execute();
    }

    private List<Route> createTestRoutes(){
        List<Route> result = new ArrayList<Route>();

        Route route = new Route();
        route.setName("Test Route");
        route.setCostInDollar(30);
        route.setDistanceInMi(39);
        route.setStart(LocationUtil.createLocation(37.778845, -122.414722));
        route.setEnd(LocationUtil.createLocation(37.779795, -122.407201));
        route.setStartAddress(LocationUtil.geocodeLocation(this, route.getStart()));
        route.setEndAddress(LocationUtil.geocodeLocation(this, route.getEnd()));
        route.setRating(4.9f);
        result.add(route);

        Route route2 = new Route();
        route2.setName("Test Route #2");
        route2.setCostInDollar(30);
        route2.setDistanceInMi(39);
        route2.setStart(LocationUtil.createLocation(37.778845, -122.414722));
        route2.setEnd(LocationUtil.createLocation(37.779795, -122.407201));
        route2.setStartAddress(LocationUtil.geocodeLocation(this, route2.getStart()));
        route2.setEndAddress(LocationUtil.geocodeLocation(this, route2.getEnd()));
        route2.setRating(4.3f);
        result.add(route2);

        return result;
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
