package com.pdb.zhome.Activities;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;

import com.miz.pdb.R;
import com.pdb.zhome.Adapters.DrawerItemCustomAdapter;
import com.pdb.zhome.Communicator;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.Fragments.FavoritesFragment;
import com.pdb.zhome.Fragments.ObjectDrawerItem;
import com.pdb.zhome.Fragments.RenameScreen;
import com.pdb.zhome.Fragments.RoomsFragment;
import com.pdb.zhome.Fragments.ScenariosFragment;
import com.pdb.zhome.MyApplication;
import com.pdb.zhome.SocketCom;
import com.pdb.zhome.Fragments.SummaryFragment;
import com.pdb.zhome.Fragments.TestFragment;
import com.pdb.zhome.Room;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements Communicator {

	// declare properties
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment summaryFragment;
    private Fragment favoritesFragment;
    private Fragment scenariosFragment;
    private Fragment testFragment;
    private Fragment roomsFragment;
    static private Fragment currentFragment;

    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;

    public static HashMap<String, Device> deviceHashMap = new HashMap<String, Device>();
    public static HashMap<String, Room> roomsHashMap = new HashMap<String, Room>();
    public static JSONArray roomsJsonArray = new JSONArray();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String currentRoomName = "";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// for proper titles
		mTitle = mDrawerTitle = getTitle();
		
		// initialize properties
		mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        preferences = getApplicationContext().getSharedPreferences("ZhomePreferences", MODE_PRIVATE);
        editor = preferences.edit();
        
        // list the drawer items
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
        
        drawerItem[0] = new ObjectDrawerItem(R.drawable.favorites, "Favorites");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.rooms, "Rooms");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.scenarios, "Scenarios");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.summary, "Summary");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.logout, "Logout");
        
        // Pass the folderData to our ListView adapter
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        
        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        
        // set the item click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // for app icon control for nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {
        	
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
        
        if (savedInstanceState == null) {
            // on first time display view for first nav item
        	selectItem(0);
        }

        SocketCom.getInstance().switchContext(this, SocketCom.context.MAIN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    public static void setCurrentFragment(Fragment currentFragment) {
        MainActivity.currentFragment = currentFragment;
    }

    public static Fragment getCurrentFragment() {

        return currentFragment;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
        }

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }

       return super.onOptionsItemSelected(item);
	}

	// to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 1)
            getFragmentManager().popBackStack();
        else{
            moveTaskToBack(true);
        }

    }
	
	// navigation drawer click listener
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	    
	}

    private void selectItem(int position) {
    	
        // update the main content by replacing fragments
        String tag = null;

        switch (position) {
        case 0:
            if (favoritesFragment==null)
                favoritesFragment = new FavoritesFragment();

            currentFragment = favoritesFragment;
            tag = "favoritesFragment";
            break;
        case 1:
            if (roomsFragment==null)
                roomsFragment = new RoomsFragment();

            currentFragment = roomsFragment;
            tag = "roomsFragment";
            break;
        case 2:
            if (scenariosFragment==null)
                scenariosFragment = new ScenariosFragment();

            currentFragment = scenariosFragment;
            tag = "scenariosFragment";
            break;
        case 3:
            if (summaryFragment==null)
                summaryFragment = new SummaryFragment();

            currentFragment = summaryFragment;
            tag = "summaryFragment";
            break;
        case 4:
            if (testFragment==null)
                logout();
            break;

            default:
            break;
        }
        
        if (currentFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, currentFragment, tag).addToBackStack(null).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void update(String[] values){
        if(values[0].equals("Started")){
            if (testFragment!= null) {
                TestFragment fragment = (TestFragment) testFragment;
                fragment.fillUI();
            }
        }else if (values[0].equals("update")){
            Fragment frg = null;
            if (MyApplication.isActivityVisible()) {
                frg = getCurrentFragment();
                if (frg != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                }
            }
        }
    }

    public static HashMap<String,Device> getHashMap(){
        return deviceHashMap;
    }

    public static HashMap<String, Room> getRoomsHashMap(){
        return roomsHashMap;
    }

    private void logout(){
        editor.remove("username");
        editor.remove("password");
        editor.commit();
        try {
            SocketCom.getInstance().closeConnection();
        }catch(IOException e){
            e.printStackTrace();
        }
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent loginActivityIntent =new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    @Override
    public void respond(String data){
        FragmentManager fragmentManager = getFragmentManager();
        RenameScreen renameScreen = (RenameScreen) fragmentManager.findFragmentByTag("Rename");
        renameScreen.changeText(data);
    }

    public String getCurrentRoomName(){
        return currentRoomName;
    }

    public void setCurrentRoomName(String name){
        currentRoomName = name;
    }

    public static JSONArray getRoomsJsonArray(){ return roomsJsonArray;}

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
        Fragment frg = null;
        frg = getCurrentFragment();
        if (frg != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }
}
