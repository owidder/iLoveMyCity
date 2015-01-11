package hackthedrive.bmw.de.hackthedrive.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import hackthedrive.bmw.de.hackthedrive.BaseActivity;
import hackthedrive.bmw.de.hackthedrive.R;
import hackthedrive.bmw.de.hackthedrive.domain.Route;
import hackthedrive.bmw.de.hackthedrive.service.RouteService;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by dst on 10.01.2015.
 */
public class SaveNewRouteActivity extends BaseActivity {

    LogUtil log = LogUtil.getLogger();

    private Route route;
    private RouteService routeService;

    private static int RESULT_LOAD_IMAGE = 1;
    Uri myPicture = null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_save_new_route);
        Bundle b = getIntent().getExtras();
        route = (Route) b.getSerializable("route");
        routeService = new RouteService();

        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Save new route");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveNewRouteActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onClickSave(View v){
        EditText nameField = (EditText)findViewById(R.id.editTextName);
        EditText descField = (EditText)findViewById(R.id.editTextDescr);
        route.setName(nameField.getText().toString());
        route.setDescription(descField.getText().toString());
        routeService.save(route);

        log.d("Save new route: %s", route.toString());
        finish();
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
            route.addImage(BitmapFactory.decodeFile(picturePath));
        }
    }


}
