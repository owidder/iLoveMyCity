package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseMapActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.FuelStation;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.factory.TestDataFactory;
import hackthedrive.bmw.de.hackthedrive.service.ActiveRouteService;
import hackthedrive.bmw.de.hackthedrive.service.DriveInService;
import hackthedrive.bmw.de.hackthedrive.service.FuelStationService;
import hackthedrive.bmw.de.hackthedrive.service.LocationMockService;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleServiceAsyncWrapper;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

public class StartRouteActivity extends BaseMapActivity  implements VehicleServiceAsyncWrapper.VehicleDataListener {
    private static final LogUtil logger = LogUtil.getLogger(StartRouteActivity.class);

    private TextView txtPoiCount;
    private TextView txtDistance;
    private TextView txtCost;

    private Route route;
    private ImageButton startRouteButton;

    private RouteService routeService;
    private VehicleServiceAsyncWrapper vehicleService;

    private Marker markerCar;

    @Override
    protected void onCreateMapView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start_route);

        if( !getIntent().hasExtra(ROUTE_INTENT_EXTRA) ){
            route = TestDataFactory.createTestRoute1(getApplicationContext(), false);
        }else{
            String routeStr = getIntent().getStringExtra(ROUTE_INTENT_EXTRA);
            logger.d("Received route: %s", routeStr);
            route = GsonDeserializer.deserialize(routeStr, Route.class);
        }

        txtPoiCount = (TextView)findViewById(R.id.txtPoiCount);
        txtPoiCount.setText(String.valueOf(route.getPois().size() + 2));

        txtDistance = (TextView)findViewById(R.id.txtDistance);
        txtDistance.setText(String.valueOf(route.getDistanceInMi()));

        txtCost = (TextView)findViewById(R.id.txtCost);
        txtCost.setText(String.valueOf(route.getCostInDollar()));

        startRouteButton = (ImageButton)findViewById(R.id.start_route);
        startRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigation();
            }
        });

        routeService = new RouteService();
        vehicleService = new VehicleServiceAsyncWrapper(getApplication());

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Route: "  + route.getName());
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartRouteActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationUtil.toLatLng(route.getStart()), 15));
        LocationMockService.getInstance(this).pushLocation(route.getStart());

        //addPolygons();

        List<Marker> markers = addMarkers();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
            }
        });

        onVehicleDataChanged(VehicleServiceAsyncWrapper.instance(getApplicationContext()).getLastVehicle());
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

    private void addPolygons() {
        for(Area area : route.getDriveInAreas()){
            addPolygon(area);
        }
    }
    private void addPolygon(Area area){
        PolygonOptions options = new PolygonOptions().addAll(area.getLocationsLatLng());
        mMap.addPolygon(options
                .strokeWidth(1)
                .strokeColor(Color.BLACK)
                .fillColor(Color.BLUE));
    }

    private List<Marker> addMarkers(){
        List<Marker> markers = new ArrayList<Marker>();
        markers.add(addMarker(LocationUtil.toLatLng(route.getStart()), "Start", "Start of the route", BitmapDescriptorFactory.HUE_GREEN));
        markers.add(addMarker(LocationUtil.toLatLng(route.getEnd()), "End", "End of the route", BitmapDescriptorFactory.HUE_RED));

        for(Poi poi: route.getPois()){
            if (poi.isViaPoint()){
                markers.add(addMarker(LocationUtil.toLatLng(poi.getLocation()), "Via Point", "Via Point of the route", BitmapDescriptorFactory.HUE_YELLOW));
            } else {
                addMarker(LocationUtil.toLatLng(poi.getLocation()), "Poi", "",  BitmapDescriptorFactory.fromResource(R.drawable.liberty50));
            }
        }

        new AsyncTask<Void, Void, List<FuelStation>>(){
            @Override
            protected List<FuelStation> doInBackground(Void... params) {
                try {
                    FuelStationService fuelStationService = new FuelStationService();
                    route = fuelStationService.addChargingStations(route);
                    return route.getFuelStations();
                } catch(RuntimeException e){
                    logger.w(e, "Problem fetching stations: %s", e.getMessage());
                }
                return new ArrayList<FuelStation>();
            }

            @Override
            protected void onPostExecute(List<FuelStation> fuelStations) {
                for(FuelStation fuelStation : fuelStations){
                    addMarker(LocationUtil.toLatLng(fuelStation.getLocation()), "Charging Station", "",  BitmapDescriptorFactory.fromResource(R.drawable.icon_gas_station));
                }

            }
        }.execute();

        return markers;
    }

    private Marker addMarker(LatLng markerLoc, String title, String poiDescription, BitmapDescriptor bDesc){
        return mMap.addMarker(new MarkerOptions()
                .position(markerLoc)
                .title(title)
                .snippet(poiDescription)
                .icon(bDesc));
    }

    private Marker addMarker(LatLng markerLoc, String title, String poiDescription, float color){
        return addMarker(markerLoc, title, poiDescription, BitmapDescriptorFactory.defaultMarker(color));
    }

    private void startNavigation(){
        //String url = "https://www.google.com/maps/dir/Current+Location/";
        String url = "https://www.google.com/maps/dir/";
        url += route.getStart().getLatitude()+","+route.getStart().getLongitude() + "/";
        url += route.getEnd().getLatitude()+","+route.getEnd().getLongitude();

        /**
        if( route.getPois().size() > 0 ){
            for(int i = 0; i < route.getPois().size(); i++ ){
                Location viaPoint = route.getPois().get(i).getLocation();
                url += viaPoint.getLatitude()+","+viaPoint.getLongitude() + "/";
            }
        }*/

        routeService.startRoute(route,vehicleService );

        DriveInService.getInstance(getApplicationContext()).startMonitoring(route.getDriveInAreas());
        ActiveRouteService.getInstance(getApplicationContext()).setActiveRoute(route);

        logger.e("Starting with url: %s", url);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public void onVehicleDataChanged(final Vehicle data) {
        if( data == null ){
            return;
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng = new LatLng(data.getLat(), data.getLng());
                if( markerCar == null ) {
                    markerCar = addMarker(LocationUtil.toLatLng(route.getStart()), "Start", "Start of the route", BitmapDescriptorFactory.HUE_GREEN);
                    markerCar.setAlpha(0.4f);
                }
                else{
                    markerCar.setPosition(latLng);
                }
            }
        });
    }
}
