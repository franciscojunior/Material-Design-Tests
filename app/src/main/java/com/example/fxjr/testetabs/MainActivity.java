package com.example.fxjr.testetabs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<CardsListFragment> fragmentsToFilter2 = new ArrayList<>();

    private FrameLayout search_container;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerArrowDrawable drawerArrowDrawable;

    private boolean searchShown = false;
    private MultiAutoCompleteTextView search_bar_text_view;
    private ArrayAdapter<String> adapterLibrary;

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


                AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
                appbar.setExpanded(true);
                search_bar_text_view.requestFocus();

                // Reference: http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(search_bar_text_view, InputMethodManager.SHOW_IMPLICIT);


                //toggleSearchView();


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


        // Reference: http://stackoverflow.com/questions/30824324/clicking-hamburger-icon-on-toolbar-does-not-open-navigation-drawer?lq=1

//        actionBarDrawerToggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//
//
//        actionBarDrawerToggle.syncState();



        drawerArrowDrawable = new DrawerArrowDrawable(this);
//        drawerArrowDrawable.setVerticalMirror(false);


        //toolbar.setNavigationIcon(drawerArrowDrawable);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        search_container = (FrameLayout) getLayoutInflater().inflate(R.layout.persistent_search_bar, null);


        setupSearchContainter(search_container);

        toolbar.addView(search_container);


        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    float appbarHeight = appBarLayout.getHeight();

//                    Log.d(TAG, "onOffsetChanged: " + appBarLayout.getHeight());
//
//                    Log.d(TAG, "onOffsetChanged: " + verticalOffset);

                    if (verticalOffset == 0) {
                        tabLayout.setAlpha(1);
                    }
                    else {
                        tabLayout.setAlpha((appbarHeight + verticalOffset)/appbarHeight);
                    }
                }

            }
        });



    }

    private void setupSearchContainter(FrameLayout search_container) {

        final ImageView imageViewLeftAction = (ImageView) search_container.findViewById(R.id.left_action);
        search_bar_text_view = (MultiAutoCompleteTextView) search_container.findViewById(R.id.search_bar_text);
        final ImageView imageViewCloseButton = (ImageView) search_container.findViewById(R.id.clear_btn);
        final ImageView imageViewSearchSettingsButton = (ImageView) search_container.findViewById(R.id.search_settings);


        imageViewLeftAction.setImageDrawable(drawerArrowDrawable);
        imageViewLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_bar_text_view.getText().length() > 0) {
                    search_bar_text_view.setText("");
                } else if (drawerArrowDrawable.getProgress() != 0){
                    playDrawerToggleAnim(drawerArrowDrawable);
                    // Reference: http://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus/16477251#16477251
                    search_bar_text_view.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }





            }
        });




        search_bar_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG, "onQueryTextChange... ");

                String newText = "%" + s.toString().toLowerCase() + "%";



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



            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    //imageViewCloseButton.setImageResource(R.drawable.ic_close);
                    imageViewSearchSettingsButton.setVisibility(View.VISIBLE);
                    imageViewCloseButton.setVisibility(View.VISIBLE);
                } else {
                    imageViewCloseButton.setVisibility(View.GONE);
                    imageViewSearchSettingsButton.setVisibility(View.GONE);
                }



            }
        });


        imageViewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar_text_view.setText("");
            }
        });


        imageViewSearchSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Show search filter settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        search_bar_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    playDrawerToggleAnim(drawerArrowDrawable);
                }

            }
        });

//        ArrayAdapter<String> adapterClans =   new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.clans));
//
//        search_bar_text_view.setAdapter(adapterClans);
//        search_bar_text_view.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    private void toggleSearchView() {
        //playDrawerToggleAnim((DrawerArrowDrawable)toolbar.getNavigationIcon());

        if (searchShown) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            toolbar.removeView(search_container);

        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.addView(search_container);
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


//                Log.d(TAG, "onQueryTextChange... ");

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
        } else if (drawerArrowDrawable.getProgress() != 0){
            playDrawerToggleAnim(drawerArrowDrawable);
            // Reference: http://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus/16477251#16477251
            search_bar_text_view.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);

        } else if (searchShown) {
            toggleSearchView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//
//        // Sets searchable configuration defined in searchable.xml for this SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//
//            setupSearchView(searchView);
//
//        }


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
