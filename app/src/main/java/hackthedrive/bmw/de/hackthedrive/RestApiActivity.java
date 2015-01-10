package hackthedrive.bmw.de.hackthedrive;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.service.VehicleService;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

public class RestApiActivity extends BaseActivity {
    private static final LogUtil logger = LogUtil.getLogger(RestApiActivity.class);

    private TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_api);
        mainTextView = (TextView) findViewById(R.id.textView);
        mainTextView.setText("Requesting data ...");

        setupToolbar();

        new AsyncTask<Void, Void, Void>(){
            private TestData data;

            @Override
            protected Void doInBackground(Void... params) {
                VehicleService vehicleService = new VehicleService(RestApiActivity.this);
                Vehicle vehicle = vehicleService.getCurrentVehicle();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("REST");
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestApiActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public class TestData  {
        private String testMessage;
        private int testNumber;
        private List<TestData> furtherData;

        public String getTestMessage() {
            return testMessage;
        }

        public int getTestNumber() {
            return testNumber;
        }

        public List<TestData> getFurtherData() {
            return furtherData;
        }

        public void addTestData(TestData data){
            this.furtherData.add(data);
        }

        public void setTestMessage(String testMessage) {
            this.testMessage = testMessage;
        }

        public void setTestNumber(int testNumber) {
            this.testNumber = testNumber;
        }

        public void setFurtherData(List<TestData> furtherData) {
            this.furtherData = furtherData;
        }
    }
}
