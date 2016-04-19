package com.example.fxjr.testetabs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

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
    private SimpleCursorAdapter mAdapter;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerArrowDrawable drawerArrowDrawable;

    private MultiAutoCompleteTextView search_cards;

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate... ");


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);


        final AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        search_cards = (MultiAutoCompleteTextView) getLayoutInflater().inflate(R.layout.search_cards_multiautotext, null);

        setupSearchTextView(search_cards);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Reference: ﻿https://www.raywenderlich.com/103367/material-design
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    int cx = viewPager.getRight() - 30;
                    int cy = viewPager.getBottom() - 60;
                    int finalRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());
                    Animator anim = ViewAnimationUtils.createCircularReveal(viewPager, cx, cy, 0, finalRadius);
                    //view.setVisibility(View.VISIBLE);
                    anim.start();

                }

//                AppCompatAutoCompleteTextView e = new AppCompatAutoCompleteTextView(getSupportActionBar().getThemedContext());
//            AutoCompleteTextView e = new AutoCompleteTextView(MainActivity.this);


                // Reference: http://stackoverflow.com/questions/11710042/expand-and-give-focus-to-searchview-automatically
                // Had to add collapseActionView flag
                //MenuItemCompat.expandActionView(searchMenuItem);

                // Show appbar so user can use the searchview.
                // Reference: http://stackoverflow.com/questions/33958878/hide-show-toolbar-programmatically-on-coordinatorlayout

                appbar.setExpanded(true);

                toggleSearchView();

//                if (!search_cards.isShown()) {
//
//                    getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//                    toolbar.addView(search_cards);
//
//                    search_cards.requestFocus();
//                }
//                else {
//                    toolbar.removeView(search_cards);
//                    getSupportActionBar().setDisplayShowTitleEnabled(true);
//
//                }

            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // Reference: http://stackoverflow.com/questions/30824324/clicking-hamburger-icon-on-toolbar-does-not-open-navigation-drawer?lq=1
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        drawerArrowDrawable = new DrawerArrowDrawable(getSupportActionBar().getThemedContext());
        toolbar.setNavigationIcon(drawerArrowDrawable);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        search_cards.setCompoundDrawables(drawerArrowDrawable, null, null, null);




    }


    private void toggleSearchView() {
//        playDrawerToggleAnim((DrawerArrowDrawable)toolbar.getNavigationIcon());
        playDrawerToggleAnim(drawerArrowDrawable);

        if (search_cards.isShown()) {
            toolbar.removeView(search_cards);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            toolbar.addView(search_cards);

            search_cards.requestFocus();

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }

    }

    // Reference: http://stackoverflow.com/questions/26835209/appcompat-v7-toolbar-up-back-arrow-not-working

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

    private void setupSearchTextView(final MultiAutoCompleteTextView searchView) {


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG, "onQueryTextChange... ");

                String searchConstraint = "%" + s.toString().toLowerCase() + "%";



//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(0)).setFilter(" and lower(name) like ?", new String[] {newText});
//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(1)).setFilter(" and lower(name) like ?", new String[] {newText});


                for (FragmentFilterable fragment:
                        fragmentsToFilter) {

                    fragment.setFilter(" and lower(name) like ?", new String[] {searchConstraint});
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void setupSearchViewold(final SearchView searchView) {

        // Reference: http://stackoverflow.com/questions/23658567/android-actionbar-searchview-suggestions-with-a-simple-string-array
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);



        searchView.setSuggestionsAdapter(mAdapter);






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                populateAdapter(newText);

                Log.d(TAG, "onQueryTextChange... ");

                newText = "%" + newText.toLowerCase() + "%";



//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(0)).setFilter(" and lower(name) like ?", new String[] {newText});
//                ((CardsListFragment)((ViewPagerAdapter)viewPager.getAdapter()).getCachedItem(1)).setFilter(" and lower(name) like ?", new String[] {newText});


                for (FragmentFilterable fragment:
                     fragmentsToFilter) {

                    fragment.setFilter(" and lower(name) like ?", new String[] {newText});
                }

                return true;
            }
        });




    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof FragmentFilterable)
            fragmentsToFilter.add((FragmentFilterable) fragment);
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
        } else if (search_cards.isShown()) {
            if (search_cards.getText().length() > 0) {
                search_cards.setText("");
            } else {
                toggleSearchView();
            }
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

//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            setupSearchView(searchView);
//        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if (search_cards.isShown()) {
                if (search_cards.getText().length() > 0) {
                    search_cards.setText("");
                } else {
                    toggleSearchView();
                }

            }
            else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;

        } else if (id == R.id.action_settings) {  //noinspection SimplifiableIfStatement


            return true;
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


        }


    }


    public interface FragmentFilterable {

        void setFilter(String filter, String[] args);
    }



}
