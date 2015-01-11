package hackthedrive.bmw.de.hackthedrive;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

import java.lang.reflect.Method;

import hackthedrive.bmw.de.hackthedrive.adapter.DbHelper;
import hackthedrive.bmw.de.hackthedrive.domain.Vehicle;
import hackthedrive.bmw.de.hackthedrive.service.LocationMockService;
import hackthedrive.bmw.de.hackthedrive.service.VehicleServiceAsyncWrapper;
import hackthedrive.bmw.de.hackthedrive.util.LocationUtil;
import hackthedrive.bmw.de.hackthedrive.util.LogUtil;

/**
 * Created by ewa on 10.01.2015.
 */
public class HtdApplication extends Application {
    private static final LogUtil logger = LogUtil.getLogger(HtdApplication.class);


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        LocationMockService.getInstance(this);
        DbHelper.createInstance(this);

    }
}
