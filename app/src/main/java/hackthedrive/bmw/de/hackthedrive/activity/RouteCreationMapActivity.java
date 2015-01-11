package hackthedrive.bmw.de.hackthedrive.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import hackthedrive.bmw.de.hackthedrive.BaseMapActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleService;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 10.01.2015.
 */
public class RouteCreationMapActivity  extends BaseMapActivity {

    private static final LogUtil logger = LogUtil.getLogger();

    private static final LatLng SAN_FRAN = new LatLng(37.752407, -122.416829);

    private static int RESULT_ADD_POI = 1;

    private ImageButton startRecordingButton;
    private ImageButton stopRecordingButton;
    private ImageButton addPoiButton;
    private ImageButton addViaPoiButton;

    private RouteService routeService;
    private Route route;

    private VehicleService vehicleService;
    private Marker marker;

    @Override
    protected void onCreateMapView(Bundle savedInstanceState) {
        setContentView(R.layout.create_route_map);
        setupToolbar();
        startRecordingButton = (ImageButton)findViewById(R.id.startRecordingButton);
        stopRecordingButton = (ImageButton)findViewById(R.id.stopRecordingButton);
        addPoiButton = (ImageButton)findViewById(R.id.addPoiButton);
        addViaPoiButton = (ImageButton)findViewById(R.id.addViaPointButton);
        routeService = new RouteService();
        vehicleService = new VehicleService(getApplication());
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Create new route");
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN_FRAN, 15));
        Location location = getCurrentLocation();
        LatLng mapLocation = null;
        if (location == null) {
            mapLocation = SAN_FRAN;
        } else {
            mapLocation = new LatLng(location.getLatitude()
                    , location.getLongitude());
        }
        marker = mMap.addMarker(new MarkerOptions().position(mapLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
    }

    public void onClickStartRecording(View view){
        startRecordingButton.setVisibility(View.GONE);
        stopRecordingButton.setVisibility(View.VISIBLE);
        addViaPoiButton.setVisibility(View.VISIBLE);
        addPoiButton.setVisibility(View.VISIBLE);

        route = routeService.createNewRoute(getCurrentLocation());
    }

    public void onClickStopRecording(View v){
        openModalDialog(v.getContext());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Poi result= (Poi)data.getSerializableExtra("poi");
                logger.d("Adding new poi: %s", result);
                route.addPoi(result);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void onClickAddViaPoint(View v){
        route.addViaPoint(getCurrentLocation());
    }

    public void onClickAddPoi(View v){
        Intent i = new Intent(this, AddPoiActivity.class);
        startActivityForResult(i, RESULT_ADD_POI);
    }

    private void resetButtons() {
        startRecordingButton.setVisibility(View.VISIBLE);
        stopRecordingButton.setVisibility(View.INVISIBLE);
        addViaPoiButton.setVisibility(View.INVISIBLE);
        addPoiButton.setVisibility(View.INVISIBLE);
    }

    ////

    private Location getCurrentLocation(){
        return vehicleService.getCurrentLocation();
    }

    private void saveRoute(){
        Intent i = new Intent(this, SaveNewRouteActivity.class);
        i.putExtra("route", route);
        startActivity(i);
        finish();
    }

    private void openModalDialog(Context context){
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage("Save new route?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        route.setEnd(getCurrentLocation());
                        saveRoute();
                    }
                }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        resetButtons();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
