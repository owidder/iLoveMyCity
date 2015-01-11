package hackthedrive.bmw.de.hackthedrive;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends ActionBarActivity {
    public static final String ROUTE_INTENT_EXTRA = "ROUTE";
    public static final String DRIVE_IN_EVENT_INTENT_EXTRA = "DRIVE_IN_EVENT";

    private Toolbar mActionBarToolbar;

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                //setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

}
