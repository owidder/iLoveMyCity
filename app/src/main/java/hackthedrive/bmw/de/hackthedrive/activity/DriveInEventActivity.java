package hackthedrive.bmw.de.hackthedrive.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Area;
import hackthedrive.bmw.de.hackthedrive.service.VehicleService;
import hackthedrive.bmw.de.hackthedrive.util.GsonDeserializer;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

public class DriveInEventActivity extends BaseActivity {
    private static final LogUtil logger = LogUtil.getLogger(DriveInEventActivity.class);

    private MediaPlayer mp;
    private Area areaNext;

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

        String eventNextStr = getIntent().getStringExtra("next_point");
        if( eventNextStr == null ){
            return;
        }
        areaNext = GsonDeserializer.deserialize(eventNextStr, Area.class);



        TextView txt = (TextView) findViewById(R.id.event_detail);
        txt.setText(area.getText());

    }

    public void onClickPlaySound(View v){
        if(mp != null){
            if(mp.isPlaying()){
                mp.pause();
            } else {
                mp.start();
            }
        } else {
            mp = MediaPlayer.create(DriveInEventActivity.this, R.raw.tourcoffee);
            mp.start();
        }
    }

    public void onClickPassRoute(View v){
        if( areaNext == null ){
            logger.e("Area next is bad");
            return;
        }

        VehicleService s = new VehicleService(getApplicationContext());
        s.sendAddressToVehicle(areaNext.getViaPoint());
        logger.d("Area next sent");
    }
}
