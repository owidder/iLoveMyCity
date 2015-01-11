package hackthedrive.bmw.de.hackthedrive.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.ListActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;

public class DriveInEventActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drive_in);

        findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String eventStr = getIntent().getStringExtra(DRIVE_IN_EVENT_INTENT_EXTRA);
        if( eventStr == null ){
            return;
        }

        Area area = GsonDeserializer.deserialize(eventStr, Area.class);
        if( area == null ){
            return;
        }

        TextView txt = (TextView) findViewById(R.id.event_detail);
        txt.setText(area.getText());

    }
}
