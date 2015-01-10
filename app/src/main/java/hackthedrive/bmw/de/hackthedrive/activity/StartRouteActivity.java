package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import hackthedrive.bmw.de.hackthedrive.BaseMapActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.WelcomeActivity;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

public class StartRouteActivity extends BaseMapActivity {
    private static final LogUtil logger = LogUtil.getLogger(StartRouteActivity.class);

    private TextView routeNameTxt;
    private Route route;
    private Button startRouteButton;

    @Override
    protected void onCreateMapView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start_route);
        setupToolbar();

        route = createTestRoute();

        routeNameTxt = (TextView)findViewById(R.id.route_name);
        routeNameTxt.setText("Route name: " + route.getName());

        startRouteButton = (Button)findViewById(R.id.start_route);
        startRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigation();
            }
        });
    }
    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Navigation");
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

        addMarker(LocationUtil.toLatLng(route.getStart()), "Start", "Start of the route", BitmapDescriptorFactory.HUE_GREEN);
        addMarker(LocationUtil.toLatLng(route.getEnd()), "End", "End of the route", BitmapDescriptorFactory.HUE_RED);

        for(Location viaPoint : route.getViaPoints()){
            addMarker(LocationUtil.toLatLng(viaPoint), "Via Point", "Via Point of the route", BitmapDescriptorFactory.HUE_YELLOW);
        }
    }
    private void addMarker(LatLng markerLoc, String title, String poiDescription, float color){
        mMap.addMarker(new MarkerOptions()
                .position(markerLoc)
                .title(title)
                .snippet(poiDescription)
                .icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    private void startNavigation(){
        String url = "https://www.google.com/maps/dir/Current+Location/";
        url += route.getStart().getLatitude()+","+route.getStart().getLongitude() + "/";
        url += route.getEnd().getLatitude()+","+route.getEnd().getLongitude();

        if( route.getViaPoints().size() > 0 ){
            url += "/";
            for(int i = 0; i < route.getViaPoints().size(); i++ ){
                Location viaPoint = route.getViaPoints().get(i);
                url += viaPoint.getLatitude()+","+viaPoint.getLongitude();
                if( i+1 < route.getViaPoints().size()){
                    url += "/";
                }
            }
        }

        logger.e("Starting with url: %s", url);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private Route createTestRoute() {
        Route route = new Route();
        route.setName("Test Route");
        route.setStart(LocationUtil.createLocation(37.778845, -122.414722));
        route.setEnd(LocationUtil.createLocation(37.779795, -122.407201));


        List<Location> viaPoints = new ArrayList<Location>();
        viaPoints.add(LocationUtil.createLocation(37.778794, -122.410495));
        viaPoints.add(LocationUtil.createLocation(37.783254, -122.402706));

        route.setViaPoints(viaPoints);

        return route;

    }

}
