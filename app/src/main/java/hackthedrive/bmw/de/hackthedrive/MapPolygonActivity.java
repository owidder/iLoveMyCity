package hackthedrive.bmw.de.hackthedrive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.activity.WelcomeActivity;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;

public class MapPolygonActivity extends BaseMapActivity {
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

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
                Intent intent = new Intent(MapPolygonActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void setUpMap() {
        mMap.addPolygon(new PolygonOptions()
                .addAll(createRectangle(new LatLng(-20, 130), 5, 5))
                .addHole(createRectangle(new LatLng(-22, 128), 1, 1))
                .addHole(createRectangle(new LatLng(-18, 133), 0.5, 1.5))
                .fillColor(Color.CYAN)
                .strokeColor(Color.BLUE)
                .strokeWidth(5));

        // Create a rectangle centered at Sydney.
        PolygonOptions options = new PolygonOptions().addAll(createRectangle(SYDNEY, 5, 8));

        mMap.addPolygon(options
                .strokeWidth(3)
                .strokeColor(Color.BLACK)
                .fillColor(Color.BLUE));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
    }
    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return LocationUtil.createRectangle(center, halfWidth, halfHeight);
    }
}
