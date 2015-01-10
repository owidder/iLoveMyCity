package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import hackthedrive.bmw.de.hackthedrive.BaseMapActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.WelcomeActivity;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;

/**
 * Created by dst on 10.01.2015.
 */
public class RouteCreationMapActivity  extends BaseMapActivity {
    private static final LatLng SAN_FRAN = new LatLng(37.752407, -122.416829);

    private Button startRecordingButton;
    private Button stopRecordingButton;
    private Button addPoiButton;
    private Button addViaPoiButton;

    private RouteService routeService;
    private Route route;

    @Override
    protected void onCreateMapView(Bundle savedInstanceState) {
        setContentView(R.layout.create_route_map);
        setupToolbar();
        startRecordingButton = (Button)findViewById(R.id.startRecordingButton);
        stopRecordingButton = (Button)findViewById(R.id.stopRecordingButton);
        addPoiButton = (Button)findViewById(R.id.addPoiButton);
        addViaPoiButton = (Button)findViewById(R.id.addViaPointButton);
        routeService = new RouteService();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Map activity");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteCreationMapActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN_FRAN, 10));
    }

    public void onClickStartRecording(View view){
        startRecordingButton.setVisibility(View.GONE);
        stopRecordingButton.setVisibility(View.VISIBLE);
        addViaPoiButton.setVisibility(View.VISIBLE);
        addPoiButton.setVisibility(View.VISIBLE);

        route = routeService.createNewRoute(getCurrentLocation());
    }

    public void onClickStopRecording(View v){
        startRecordingButton.setVisibility(View.VISIBLE);
        stopRecordingButton.setVisibility(View.INVISIBLE);
        addViaPoiButton.setVisibility(View.INVISIBLE);
        addPoiButton.setVisibility(View.INVISIBLE);

        route.setEnd(getCurrentLocation());
        routeService.saveRoute(route);
    }

    ////

    private Location getCurrentLocation(){
        return null;
    }
}
