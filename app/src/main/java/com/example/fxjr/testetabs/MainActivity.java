package com.example.fxjr.testetabs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CryptCardsListViewAdapter cryptCardsListViewAdapter;
    private LibraryCardsListViewAdapter libraryCardsListViewAdapter;

    private List<FragmentFilterable> fragmentsToFilter = new ArrayList<>();
    private List<CardsListFragment> fragmentsToFilter2 = new ArrayList<>();

    private LinearLayout search_container;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerArrowDrawable drawerArrowDrawable;

    private boolean searchShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate... ");


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);



        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                // Reference: ﻿https://www.raywenderlich.com/103367/material-design
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    int cx = viewPager.getRight() - 30;
                    int cy = viewPager.getBottom() - 60;
                    int finalRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(viewPager, cx, cy, 0, finalRadius);
                    //view.setVisibility(View.VISIBLE);
                    anim.start();

                }


                toggleSearchView();


//                actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//                actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d(TAG, "onClick: actionbardrawer ");
//                    }
//                });

            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

//        actionBarDrawerToggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//
//
//        actionBarDrawerToggle.syncState();



        drawerArrowDrawable = new DrawerArrowDrawable(getSupportActionBar().getThemedContext());


        toolbar.setNavigationIcon(drawerArrowDrawable);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        search_container = (LinearLayout) findViewById(R.id.search_container);






    }

    private void toggleSearchView() {
        playDrawerToggleAnim((DrawerArrowDrawable)toolbar.getNavigationIcon());

        if (searchShown) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        searchShown = !searchShown;
    }


    private void setupSearchView(final SearchView searchView) {


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                Log.d(TAG, "onQueryTextChange... ");

                newText = "%" + newText.toLowerCase() + "%";



//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(0)).setFilter(" and lower(name) like ?", new String[] {newText});
//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(1)).setFilter(" and lower(name) like ?", new String[] {newText});


//                for (FragmentFilterable fragment:
//                     fragmentsToFilter) {
//
//                    fragment.setFilter(" and lower(name) like ?", new String[] {newText});
//                }

                for (CardsListFragment fragment:
                        fragmentsToFilter2) {

                    //fragment.setFilter(" and lower(name) like ?", new String[] {newText});

                    Log.d(TAG, "onQueryTextChange: Thread Id: " + Thread.currentThread().getId());
                    fragment.getCardsAdapter().getFilter().filter(" and lower(name) like '" + newText + "'");
                }

                return true;
            }
        });


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof FragmentFilterable)
            fragmentsToFilter.add((FragmentFilterable) fragment);

        if (fragment instanceof CardsListFragment)
            fragmentsToFilter2.add((CardsListFragment) fragment);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchShown) {
            toggleSearchView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


            setupSearchView(searchView);

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected: ");

        if (id == android.R.id.home) {
            if (searchShown) {
                toggleSearchView();
            }
            else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;

        } else if (id == R.id.action_settings) {  //noinspection SimplifiableIfStatement


            return true;
        } else if (id == R.id.action_search) {




        }



        return super.onOptionsItemSelected(item);
    }

    public static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
        float start = d.getProgress();
        float end = Math.abs(start - 1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
            offsetAnimator.setDuration(300);
            offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        offset = (Float) animation.getAnimatedValue();
                    }
                    d.setProgress(offset);
                }
            });
            offsetAnimator.start();
        }
        else
            d.setProgress(end);
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


        }


    }


    public interface FragmentFilterable {

        void setFilter(String filter, String[] args);
    }



}
