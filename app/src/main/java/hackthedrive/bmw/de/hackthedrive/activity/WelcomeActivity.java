package hackthedrive.bmw.de.hackthedrive.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import hackthedrive.bmw.de.hackthedrive.ListActivity;
import hackthedrive.bmw.de.hackthedrive.R;

public class WelcomeActivity extends Activity {

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
                // TODO
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
