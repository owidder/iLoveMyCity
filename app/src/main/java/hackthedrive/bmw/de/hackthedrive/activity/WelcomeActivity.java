package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        findViewById(R.id.create_by_driving).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RouteCreationMapActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.create_via_pois).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.create_from_poi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SearchPoiActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.use_existing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SearchRouteActivity.class);
                startActivity(intent);
            }
        });
    }
}
