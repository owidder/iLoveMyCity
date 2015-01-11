package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Poi;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleService;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 10.01.2015.
 */
public class AddPoiActivity extends BaseActivity {

    private Poi poi;

    LogUtil log = LogUtil.getLogger();

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_new_poi);
    }

    public void onClickSave(View v){
        fillPoi();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("poi",poi);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void onClickDismiss(View v){
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    private void fillPoi(){
        poi = new Poi();
        EditText nameField = (EditText)findViewById(R.id.editTextName);
        EditText descField = (EditText)findViewById(R.id.editTextDescr);
        poi.setName(nameField.getText().toString());
        poi.setDesc(descField.getText().toString());

        VehicleService vehicleService = new VehicleService(getApplication());
        poi.setLocation(vehicleService.getCurrentLocation());
    }

    public void onClickAddImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
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
            log.d("Adding image to route: %s", picturePath.toString());
            poi.addImage(BitmapFactory.decodeFile(picturePath));
        }
    }
}
