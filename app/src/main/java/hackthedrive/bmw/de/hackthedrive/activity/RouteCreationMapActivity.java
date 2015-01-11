package hackthedrive.bmw.de.hackthedrive.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseMapActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleServiceAsyncWrapper;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

import static hackthedrive.bmw.de.hackthedrive.service.VehicleServiceAsyncWrapper.VehicleDataListener;

/**
 * Created by dst on 10.01.2015.
 */
public class RouteCreationMapActivity  extends BaseMapActivity implements VehicleDataListener {

    private static final LogUtil logger = LogUtil.getLogger();

    private static final double AREA_OFFSET = 0.000001;

    private static final LatLng SAN_FRAN = new LatLng(37.752407, -122.416829);
    private static final Location SAN_FRAN_LOC = LocationUtil.createLocation(37.752407, -122.416829);

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
        toolbar.setTitle("Create Tour");
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

    @Override
    protected void onResume() {
        super.onResume();

        VehicleServiceAsyncWrapper.instance(getApplicationContext()).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        VehicleServiceAsyncWrapper.instance(getApplicationContext()).removeListener(this);
    }

    public void onClickStartRecording(View view){
        startRecordingButton.setVisibility(View.GONE);
        stopRecordingButton.setVisibility(View.VISIBLE);
        addViaPoiButton.setVisibility(View.VISIBLE);
        addPoiButton.setVisibility(View.VISIBLE);

        Location currentLocation = getCurrentLocation();
        route = routeService.createNewRoute(currentLocation);

        mMap.addMarker(new MarkerOptions().position(LocationUtil.toLatLng(currentLocation)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    public void onClickStopRecording(View v){
        openModalDialog(v.getContext());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Poi result= GsonDeserializer.deserialize(data.getStringExtra("poi"), Poi.class);
                logger.d("Adding new poi: %s", result);
                route.addPoi(result);

                Toast.makeText(getApplicationContext(), "Poi added + ("+result.getName()+")", Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(LocationUtil.toLatLng(result.getLocation())).icon(BitmapDescriptorFactory.fromResource(R.drawable.liberty50)));
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void onClickAddViaPoint(View v){
        Location currentLocation = getCurrentLocation();
        route.addViaPoint(currentLocation);
        Area area = createAreaFromViaPoint(getCurrentLocation());
        if(area != null) {
            route.addDriveInArea(area);
        }
        Toast.makeText(getApplicationContext(), "Via-Point added", Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(LocationUtil.toLatLng(currentLocation)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
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
        Location location = VehicleServiceAsyncWrapper.instance(getApplicationContext()).getCurrentLocation();
        if(null == location){
            location = SAN_FRAN_LOC;
        }
        return location;
    }

    private void saveRoute(){
        Intent i = new Intent(this, SaveNewRouteActivity.class);
        i.putExtra("route", GsonSerializer.serialize(route));
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

    private Area createAreaFromViaPoint(Location viaPoint) {
        Area area = null;

        if(viaPoint != null) {
            LatLng center = new LatLng(viaPoint.getLatitude(), viaPoint.getLongitude());

            List<LatLng> cornersLatLng = LocationUtil.createRectangle(center, 0.0001, 0.0001);
            List<Location> cornersLocation = LocationUtil.listOfLatLng2listLOfLocation(cornersLatLng);

            area = new Area();
            area.setViaPoint(viaPoint);

            area.setLocations(cornersLocation);
        }

        return area;
    }

    @Override
    public void onVehicleDataChanged(final Vehicle data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng = new LatLng(data.getLat(), data.getLng());
                marker.setPosition(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });
    }
}
