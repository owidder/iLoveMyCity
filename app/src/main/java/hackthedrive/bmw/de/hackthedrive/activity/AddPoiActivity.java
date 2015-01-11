package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.service.VehicleServiceAsyncWrapper;
import hackthedrive.bmw.de.hackthedrive.util.GsonSerializer;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 10.01.2015.
 */
public class AddPoiActivity extends BaseActivity {

    private static final LogUtil logger = LogUtil.getLogger(AddPoiActivity.class);

    private Poi poi;

    private ImageButton startSoundRecordButton;
    private ImageButton stopSoundRecordButton;

    private static int RESULT_LOAD_IMAGE = 1;
    private MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_new_poi);
        poi = new Poi();
        setupToolbar();

        startSoundRecordButton = (ImageButton)findViewById(R.id.startSoundRecordButton);
        stopSoundRecordButton = (ImageButton)findViewById(R.id.stopSoundRecordButton);
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Add Poi");
    }

    public void onClickSave(View v){
        fillPoi();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("poi", GsonSerializer.serialize(poi));
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void onClickDismiss(View v){
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    private void fillPoi(){
        EditText nameField = (EditText)findViewById(R.id.editTextName);
        EditText descField = (EditText)findViewById(R.id.editTextDescr);
        poi.setName(nameField.getText().toString());
        poi.setDesc(descField.getText().toString());

        SeekBar rangeBar = (SeekBar)findViewById(R.id.rangeBar);
        logger.d("Range: %s", rangeBar.getProgress());

        poi.setRadius(rangeBar.getProgress());
        poi.setLocation(VehicleServiceAsyncWrapper.instance(getApplicationContext()).getCurrentLocation());
    }

    public void onClickAddImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    public void onClickStartRecording(View v){
        startSoundRecordButton.setVisibility(View.GONE);
        stopSoundRecordButton.setVisibility(View.VISIBLE);

        /**
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("");
        try {
            recorder.prepare();
        } catch (IOException e) {
            logger.w(e, "Problem with sound recording. %s", e.getMessage());
        }
        recorder.start();   // Recording is now started
         */
    }

    public void onClickStopRecording(View v) {
        startSoundRecordButton.setVisibility(View.VISIBLE);
        stopSoundRecordButton.setVisibility(View.GONE);
        /**
        if(recorder != null) {
            recorder.stop();
            recorder.reset();   // You can reuse the object by going back to setAudioSource() step
            recorder.release(); // Now the object cannot be reused
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            logger.d("Adding image to route: %s", picturePath.toString());
            //poi.addImage(BitmapFactory.decodeFile(picturePath));
        }
    }
}
