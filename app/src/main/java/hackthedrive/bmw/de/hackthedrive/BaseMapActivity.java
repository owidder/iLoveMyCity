package hackthedrive.bmw.de.hackthedrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public abstract class BaseMapActivity extends BaseActivity {
    protected GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateMapView(savedInstanceState);
        setUpMapIfNeeded();
    }

    protected abstract void onCreateMapView(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    protected abstract void setUpMap();

}
