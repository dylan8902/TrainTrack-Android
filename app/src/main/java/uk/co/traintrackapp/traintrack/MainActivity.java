package uk.co.traintrackapp.traintrack;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import uk.co.traintrackapp.traintrack.fragment.AccountManagerFragment;
import uk.co.traintrackapp.traintrack.fragment.DashboardFragment;
import uk.co.traintrackapp.traintrack.fragment.JourneysFragment;
import uk.co.traintrackapp.traintrack.fragment.NavigationDrawerFragment;
import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private CharSequence title;
    private Fragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
        title = getString(R.string.app_name);
        newFragment = DashboardFragment.newInstance();
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case -1:
                newFragment = AccountManagerFragment.newInstance();
                break;
            case 0:
                newFragment = DashboardFragment.newInstance();
                break;
            case 1:
                newFragment = StationsFragment.newInstance();
                break;
            case 2:
                newFragment = JourneysFragment.newInstance();
                break;
            case 3:
                newFragment = SettingsFragment.newInstance();
                break;
        }
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerClosed() {
        if (newFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, newFragment).commit();
           newFragment = null;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
    }

}
