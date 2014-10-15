package com.example.vegie.z_home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    // Declare Tab Variables
    ActionBar.Tab Tab1, Tab2, Tab3, Tab4;
    Fragment fragmentTab1 = new FavoritesTab();
    Fragment fragmentTab2 = new RoomsTab();
    Fragment fragmentTab3 = new ScenariosTab();
    Fragment fragmentTab4 = new SummaryTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getActionBar();

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        //Tab1 = actionBar.newTab().setIcon(R.drawable.tab1);//ICON
        Tab1 = actionBar.newTab().setText("Favorites");
        Tab2 = actionBar.newTab().setText("Rooms");
        Tab3 = actionBar.newTab().setText("Scenarios");
        Tab4 = actionBar.newTab().setText("Summary");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab2.setTabListener(new TabListener(fragmentTab2));
        Tab3.setTabListener(new TabListener(fragmentTab3));
        Tab4.setTabListener(new TabListener(fragmentTab4));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
        actionBar.addTab(Tab4);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            DialogFragment myFragment = new MyDialogFragment();

            myFragment.show(getFragmentManager(), "theDialog");

            return true;
        } else if (id == R.id.exit_the_app) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
