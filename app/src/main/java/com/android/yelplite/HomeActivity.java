package com.android.yelplite;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yelplite.dummy.DummyContent;

public class HomeActivity extends AppCompatActivity implements SearchListFragment.OnSearchListFragmentInteractionListener, MapsFragment.OnMapsFragmentInteractionListener, EmptyFragment.OnEmptyFragmentInteractionListener {
    private static final String HOME_TAG = "HomeActivity";
    private static final String FRAGMENT_1 = "FRAGMENT 1";
    private static final String FRAGMENT_2 = "Fragment 2";
    private static final String FRAGMENT_3 = "Fragment 3";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(mToolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Toast.makeText(HomeActivity.this, getString(R.string.title_home),
                                Toast.LENGTH_SHORT).show();
                        displayHomeFragment();
                        return true;
                    case R.id.navigation_dashboard:
                        Toast.makeText(HomeActivity.this, getString(R.string.title_search),
                                Toast.LENGTH_SHORT).show();
                        displaySearchFragment();
                        return true;
                    case R.id.navigation_notifications:
                        Toast.makeText(HomeActivity.this, getString(R.string.title_map),
                                Toast.LENGTH_SHORT).show();
                        displayMapFragment();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
            case R.id.action_map:
                displayMapFragment();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchListFragmentInteraction(DummyContent.DummyItem item) {
//        getSupportActionBar().setTitle(item.toString());
        Toast.makeText(HomeActivity.this, "Selected: " + item.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapsFragmentInteraction(String string) {
        Toast.makeText(this, "Touching map", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyFragmentInteraction(String string) {
        Toast.makeText(this, "Touching empty", Toast.LENGTH_SHORT).show();
    }

    // TODO: implement various fragments

    // used to test API calls
    private void displayHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = EmptyFragment.newInstance("test1", "test2");
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_1)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displaySearchFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = SearchListFragment.newInstance(1);
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_2)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displayMapFragment() {
//        Intent intent = new Intent(this, MapsActivity.class);
//        startActivity(intent);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = MapsFragment.newInstance("test1", "test2");
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_3)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void displayDetailsFragment() {

    }


}
