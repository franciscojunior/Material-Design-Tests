package com.example.fxjr.testetabs;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CryptCardsListViewAdapter cryptCardsListViewAdapter;
    private LibraryCardsListViewAdapter libraryCardsListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate... ");

        new UpdateDatabaseOperation().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Reference: ﻿https://www.raywenderlich.com/103367/material-design
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    int cx = viewPager.getRight() - 30;
                    int cy = viewPager.getBottom() - 60;
                    int finalRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(viewPager, cx, cy, 0, finalRadius);
                    //view.setVisibility(View.VISIBLE);
                    anim.start();

                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
//                        ValueAnimator
//                                colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", *//*Red*//*0xFFFF8080, *//*Blue*//*0xFF8080FF);
//                        colorAnim.setDuration(3000);
//                        colorAnim.setEvaluator(new ArgbEvaluator());
//                        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
//                        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
//                        colorAnim.start();
                        break;
                    case 1:
                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });*/


        // Search related
        SearchView searchView = (SearchView) findViewById(R.id.search);
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        setupSearchView(searchView);





    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        Log.d(TAG, "onResumeFragments... ");
//
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume... ");
    }

    private void setupSearchView(final SearchView searchView) {



        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "onClose... ");

                final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setVisibility(View.VISIBLE);
                SearchView searchView = (SearchView) findViewById(R.id.search);
                searchView.setVisibility(View.GONE);

                SQLiteDatabase db = DatabaseHelper.getDatabase();

                Cursor c = db.rawQuery(DatabaseHelper.ALL_FROM_CRYPT_QUERY , null);
                cryptCardsListViewAdapter.changeCursor(c);

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                Log.d(TAG, "onQueryTextChange... ");
                return true;
            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFrag(CardsListFragment.newInstance(0, DatabaseHelper.ALL_FROM_CRYPT_QUERY), "Crypt");
        viewPagerAdapter.addFrag(CardsListFragment.newInstance(1, DatabaseHelper.ALL_FROM_LIBRARY_QUERY), "Library");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            SearchView searchView = (SearchView) findViewById(R.id.search);
            searchView.setVisibility(View.VISIBLE);

        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        /*} else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {*/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent... ");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            // Setup adapters
            cryptCardsListViewAdapter = (CryptCardsListViewAdapter) ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getItem(0)).getCardsAdapter();

            Log.d(TAG, (cryptCardsListViewAdapter == null) ? "true":"false");

            libraryCardsListViewAdapter = (LibraryCardsListViewAdapter) ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getItem(1)).getCardsAdapter();

            String query = intent.getStringExtra(SearchManager.QUERY);

            query = "%" + query + "%";
            SQLiteDatabase db = DatabaseHelper.getDatabase();

            Cursor c = db.rawQuery(DatabaseHelper.ALL_FROM_CRYPT_QUERY + " and lower(name) like ?", new String[] {query});
            cryptCardsListViewAdapter.changeCursor(c);


        }


    }



    private class UpdateDatabaseOperation extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                DatabaseHelper.getDatabase();
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Log.d(TAG, "onPostExecute... ");

            if (result) {

                viewPager = (ViewPager) findViewById(R.id.viewpager);
                setupViewPager(viewPager);

                tabLayout = (TabLayout) findViewById(R.id.tablayout);
                tabLayout.setupWithViewPager(viewPager);


                //theAdapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, "Database Updated", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Database problems. Please, reinstall application", Toast.LENGTH_SHORT).show();
            }


        }
    }


}
