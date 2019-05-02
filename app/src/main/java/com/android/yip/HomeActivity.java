package com.android.yip;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.yip.dummy.DummyContent;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchListFragment.OnSearchListFragmentInteractionListener, MapsFragment.OnMapsFragmentInteractionListener, EmptyFragment.OnEmptyFragmentInteractionListener {
    private static final String LOG_TAG = "HomeActivity";
    private static final String FRAGMENT_1 = "Fragment 1";
    private static final String FRAGMENT_2 = "Fragment 2";
    private static final String FRAGMENT_3 = "Fragment 3";

    private BottomNavigationView mBottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(mToolbar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        displayHomeFragment();
                        return true;
                    case R.id.navigation_search:

                        // TODO: create a temporary list of business to display
                        ContentList list = ContentList.get(HomeActivity.this);
                        List<Business> businessList = list.getBusinesses();
                        Business business = new Business();
                        businessList.add(business);


                        displaySearchFragment();
                        return true;
                    case R.id.navigation_map:
                        displayMapFragment();
                        return true;
                }
                return false;
            }
        };
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        displayHomeFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchListFragmentInteraction(Business item) {
        Toast.makeText(HomeActivity.this, "Selected: " + item.mName, Toast.LENGTH_SHORT).show();

        // TODO: start a new activity that displays the business details
        displayBusinessDetail(item.mId);
    }

    // Doesn't need
    @Override
    public void onMapsFragmentInteraction(String string) {
        Toast.makeText(this, "Touching map", Toast.LENGTH_SHORT).show();
    }
    // Doesn't need
    @Override
    public void onEmptyFragmentInteraction(String string) {
        Toast.makeText(this, "Touching empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        updateNavigation();
    }


    // TODO: implement various fragments

    // used to test API calls
    private void displayHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = EmptyFragment.newInstance("test1", "test2");
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_1)
//                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displaySearchFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = SearchListFragment.newInstance();
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_2)
//                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displayMapFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = MapsFragment.newInstance("test1", "test2");
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_3)
//                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displayBusinessDetail(String id) {
        // TODO: new intent to start activity, passing in the id
    }


    // make sure that the currently selected navigation matches current fragment displayed
    private void updateNavigation() {
        EmptyFragment emptyFragment =
                (EmptyFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_1);
        if (emptyFragment != null) {
            if (emptyFragment.isVisible()) {
                // set navigation to Home
                mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        }

        SearchListFragment searchListFragment =
                (SearchListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_2);
        if (searchListFragment != null) {
            if (searchListFragment.isVisible()) {
                // set navigation to Search
                mBottomNavigationView.setSelectedItemId(R.id.navigation_search);
            }

        }

        MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(
                FRAGMENT_3);
        if (mapsFragment != null) {
            if (mapsFragment.isVisible()) {
                // set navigation to Maps
                mBottomNavigationView.setSelectedItemId(R.id.navigation_map);
            }
        }
    }


}
