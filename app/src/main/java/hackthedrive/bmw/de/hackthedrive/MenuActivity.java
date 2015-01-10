package hackthedrive.bmw.de.hackthedrive;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MenuActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        setupToolbar();
        setupDrawer();
    }

    private void setupToolbar() {
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("Menu");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }
    private void selectDrawerItem(int position)
    {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupDrawer()
    {
        String[] mTitles = new String[4];
        mTitles[0] = "Test 1";
        mTitles[1] = "Test 2";
        mTitles[2] = "Test 3";
        mTitles[3] = "Test 4";

        Integer[] icons = new Integer[4];
        icons[0] = R.drawable.ic_launcher;
        icons[1] = R.drawable.ic_launcher;
        icons[2] = R.drawable.ic_launcher;
        icons[3] = R.drawable.ic_launcher;

        // reference
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_example_layout);
        mDrawerListView = (ListView) findViewById(R.id.activity_example_drawer);

        // set drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerListView.setAdapter(new DrawerAdapter(this, mTitles, icons));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id)
            {
                selectDrawerItem(position);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // open or close the drawer if home button is pressed
        if(mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        // action bar menu behaviour
        switch(item.getItemId())
        {
            case android.R.id.home:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration)
    {
        super.onConfigurationChanged(newConfiguration);
        mDrawerToggle.onConfigurationChanged(newConfiguration);
    }

}
