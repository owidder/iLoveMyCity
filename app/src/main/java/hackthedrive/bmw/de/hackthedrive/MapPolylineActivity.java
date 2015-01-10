package hackthedrive.bmw.de.hackthedrive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapPolylineActivity extends BaseMapActivity {
    @Override
    protected void onCreateMapView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maps);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Map activity");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapPolylineActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.060547, 11.618569)).title("Marker"));

        List<LatLng> route = new ArrayList<LatLng>();
        route.add(new LatLng(48.061451, 11.616230));
        route.add(new LatLng(48.060605, 11.618590));
        route.add(new LatLng(48.060515, 11.621090));
        route.add(new LatLng(48.059956, 11.622951));

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(route.subList(0, 2));
        mMap.addPolyline(options);

        PolylineOptions options2 = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        options2.addAll(route.subList(1, route.size()-1));
        mMap.addPolyline(options2);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.060547, 11.618569), 10));
    }


}
