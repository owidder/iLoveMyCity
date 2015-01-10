package hackthedrive.bmw.de.hackthedrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import hackthedrive.bmw.de.hackthedrive.activity.RouteCreationMapActivity;
import hackthedrive.bmw.de.hackthedrive.activity.StartRouteActivity;


public class ListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        setupToolbar();
        setupList();
    }


    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("List of activities");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setupList() {
        final ListView listview = (ListView) findViewById(R.id.listview);

        final ArrayList<ListItem> list = new ArrayList<ListItem>();
        list.add(new ListItem("Welcome", "Welcome example", WelcomeActivity.class));
        list.add(new ListItem("Menu", "Menu example", MenuActivity.class));
        list.add(new ListItem("Panels", "Panels example", PanelsActivity.class));
        list.add(new ListItem("REST Api", "Sample rest call (GSON)", RestApiActivity.class));
        list.add(new ListItem("Map Initial", "Map-Activity created by Studio", MapActivity.class));
        list.add(new ListItem("Map Polygon", "Map-Activity with a polygon", MapPolygonActivity.class));
        list.add(new ListItem("Map Colored Polyline", "Map-Activity with colored polyline", MapPolylineActivity.class));
        list.add(new ListItem("Map Heatmap", "Map-...", MapHeatmapActivity.class));
        list.add(new ListItem("Create route", "Create Route", RouteCreationMapActivity.class));

        final ListItemArrayAdapter adapter = new ListItemArrayAdapter(this, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final ListItem item = (ListItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), item.getActivityToStart());
                startActivity(intent);

            }
        });
    }

    public class ListItem{
        private final String name;
        private final String description;
        private final Class<? extends Activity> activityToStart;

        public ListItem(String name, String description, Class<? extends Activity> activityToStart){
            this.name = name;
            this.description = description;
            this.activityToStart = activityToStart;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Class<? extends Activity> getActivityToStart() {
            return activityToStart;
        }
    }

    public class ListItemArrayAdapter extends ArrayAdapter<ListItem> {
        private final Context context;
        private final ArrayList<ListItem> values;

        public ListItemArrayAdapter(Context context, ArrayList<ListItem> values) {
            super(context, R.layout.list_row_template, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_row_template, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            textView.setText(values.get(position).getName());

            TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            textView2.setText(values.get(position).getDescription());
            return rowView;
        }
    }

}
