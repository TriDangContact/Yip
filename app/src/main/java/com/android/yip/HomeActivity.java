package com.android.yip;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchListFragment.OnSearchListFragmentInteractionListener, MapsFragment.OnMapsFragmentInteractionListener {
    private static final String LOG_TAG = "HomeActivity";
    private static final int REQUEST_REGISTER = 0;
    private static final String EXTRA_ID = "com.android.yip.id";
    private static final String FRAGMENT_1 = "Fragment 1";
    private static final String FRAGMENT_2 = "Fragment 2";
    private static final String FRAGMENT_3 = "Fragment 3";

    private BottomNavigationView mBottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(mToolbar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        displaySearchFragment();

                        // if the user haven't searched anything, we display popup
                        ContentList list = ContentList.get(getApplicationContext());
                        List<Business> businessList = list.getBusinesses();
                        if (businessList.size() <= 0) {
                            displayPopUpWindow();
                        }
                        return true;
                    case R.id.navigation_map:
                        displayMapFragment();
                        return true;
                }
                return false;
            }
        });

        displaySearchFragment();
        displayPopUpWindow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                displaySearchActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchListFragmentInteraction(Business item) {
        displayBusinessDetail(item.mId);
    }

    // Doesn't need
    @Override
    public void onMapsFragmentInteraction(String string) {}

    private void displayPopUpWindow() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_home_popup);
        dialog.setTitle(getString(R.string.popup_title));
        Button dialogButton = (Button) dialog.findViewById(R.id.popup_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
        Fragment fragment = MapsFragment.newInstance();
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_3)
//                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    // TODO: new intent to start activity, passing in the id
    private void displayBusinessDetail(String id) {
        Intent intent = new Intent(getApplicationContext(), BusinessDetailActivity.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    private void displaySearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, REQUEST_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
//                Bundle extras = data.getExtras();
//                mName = extras.getString(EXTRA_NAME);

                displaySearchFragment();
                updateNavigation();
            }
        }
    }

    // make sure that the currently selected navigation matches current fragment displayed
    private void updateNavigation() {
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
