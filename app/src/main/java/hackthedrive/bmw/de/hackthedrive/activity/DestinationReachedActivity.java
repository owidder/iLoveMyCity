package hackthedrive.bmw.de.hackthedrive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;

public class DestinationReachedActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_destination_reached);

        findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String routeStr = getIntent().getStringExtra(ROUTE_INTENT_EXTRA);
        if( routeStr == null ){
            return;
        }

        Route route = GsonDeserializer.deserialize(routeStr, Route.class);
        if( route == null ){
            return;
        }

    }
}
