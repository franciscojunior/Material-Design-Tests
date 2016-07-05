package com.example.fxjr.testetabs;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CryptCardsListViewAdapter cryptCardsListViewAdapter;
    private LibraryCardsListViewAdapter libraryCardsListViewAdapter;

    private List<CardsListFragment> fragmentsToFilter2 = new ArrayList<>();

    private FrameLayout search_container;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private DrawerArrowDrawable drawerArrowDrawable;

    private boolean searchShown = false;
    private TextView search_bar_text_view;

    String filter = "";

    FilterModel2 filterModel = new FilterModel2();

    boolean restoring = false;


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


                AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
                appbar.setExpanded(true);
                search_bar_text_view.requestFocus();

                // TODO: 07/06/2016 Check a way to show the softkeybard when the appbar animation is enabled.
                // Currently, the keyboard is shown but immediately after it is hidden again because the edittext is not visible yet.

                // Reference: http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(search_bar_text_view, InputMethodManager.SHOW_IMPLICIT);


            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        drawerArrowDrawable = new DrawerArrowDrawable(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        search_container = (FrameLayout) getLayoutInflater().inflate(R.layout.persistent_search_bar, null);


        setupSearchContainter(search_container);

        toolbar.addView(search_container);


        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    float appbarHeight = appBarLayout.getHeight();
//                    float tabbarHeight = tabLayout.getHeight();
//
//
////                    Log.d(TAG, "onOffsetChanged: tabbar" + tabbarHeight);
////
////                    Log.d(TAG, "onOffsetChanged: appbar" + appbarHeight);
////
////                    Log.d(TAG, "onOffsetChanged: " + verticalOffset);
//
//                    if (verticalOffset == 0) {
//                        tabLayout.setAlpha(1);
////                        fab.show();
//                    }
//                    else if (appbarHeight + verticalOffset <= tabbarHeight ){
//                        tabLayout.setAlpha((appbarHeight + verticalOffset)/tabbarHeight);
////                        fab.hide();
//                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);
//                    }
//                }

            }
        });


        // TODO: 11/06/16 Check how to make those initializations off the main thread.
        setupSearchFilterNavigation();




    }

    private void setupSearchFilterNavigation() {
        SeekBar seekBarMin = (SeekBar) findViewById(R.id.seekBarCapacityMin);
        SeekBar seekBarMax = (SeekBar) findViewById(R.id.seekBarCapacityMax);

//        Reference: http://stackoverflow.com/questions/18400910/seekbar-in-a-navigationdrawer

        View.OnTouchListener seekBarDisallowDrawerInterceptTouchEvent = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle seekbar touch events.
                v.onTouchEvent(event);
                return true;

            }
        };

        seekBarMin.setOnTouchListener(seekBarDisallowDrawerInterceptTouchEvent);
        seekBarMax.setOnTouchListener(seekBarDisallowDrawerInterceptTouchEvent);


        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                switch (seekBar.getId()) {
//
//                    case R.id.seekBarCapacityMin:
//                        filterModel.setCapacityMin(progress);
//
//                        break;
//                    case R.id.seekBarCapacityMax:
//                        filterModel.setCapacityMax(progress);
//                        break;
//
//                }
//
//                filterCards();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                switch (seekBar.getId()) {

                    case R.id.seekBarCapacityMin:
                        filterModel.setCapacityMin(seekBar.getProgress());

                        break;
                    case R.id.seekBarCapacityMax:
                        filterModel.setCapacityMax(seekBar.getProgress());
                        break;

                }

                filterCards();

            }
        };


        seekBarMin.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarMax.setOnSeekBarChangeListener(seekBarChangeListener);


        final View disciplinesHeader = findViewById(R.id.disciplinesHeader);
        final View disciplinesLayout = findViewById(R.id.disciplinesLayout);

        final ImageView imgDisciplinesLayoutArrow = (ImageView) findViewById(R.id.imgDisciplinesLayoutArrow);


        setupExpandLayout(disciplinesHeader, disciplinesLayout, imgDisciplinesLayoutArrow);


        final View clansHeader = findViewById(R.id.clansHeader);
        final View clansLayout = findViewById(R.id.clansLayout);

        final ImageView imgClansLayoutArrow = (ImageView) findViewById(R.id.imgClansLayoutArrow);


        setupExpandLayout(clansHeader, clansLayout, imgClansLayoutArrow);


        final View cardTypesHeader = findViewById(R.id.cardTypesHeader);
        final View cardTypesLayout = findViewById(R.id.cardTypesLayout);

        final ImageView imgCardTypesLayoutArrow = (ImageView) findViewById(R.id.imgCardTypesLayoutArrow);


        setupExpandLayout(cardTypesHeader, cardTypesLayout, imgCardTypesLayoutArrow);

//        final CheckedTextView checkedTextView = (CheckedTextView)findViewById(R.id.checktextview);
//        checkedTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkedTextView.toggle();
//            }
//        });





    }

    private void setupExpandLayout(View header, final View layoutToExpand, final ImageView imgArrow) {
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutToExpand.isShown()) {
                    layoutToExpand.setVisibility(View.GONE);
                    imgArrow.setImageResource(R.drawable.ic_expand_more_black_24dp);
                } else {

                    imgArrow.setImageResource(R.drawable.ic_expand_less_black_24dp);


//                    Reference: http://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
                    // Prepare the View for the animation
                    layoutToExpand.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                        layoutToExpand.setAlpha(0.0f);

                        // Start the animation
                        layoutToExpand.animate()
                                .alpha(1.0f);
                    }
                }

            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.d(TAG, "onRestoreInstanceState() called with: " + "savedInstanceState =");

        restoring = true;
        super.onRestoreInstanceState(savedInstanceState);

        restoring = false;

        filterModel.name = savedInstanceState.getCharSequence("name");
        filterModel.groups = savedInstanceState.getBooleanArray("groups");
        filterModel.capacityMin = savedInstanceState.getInt("capacitymin");
        filterModel.capacityMax = savedInstanceState.getInt("capacitymax");

        filterModel.groupsFilterChanged = true;


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = ");

        super.onSaveInstanceState(outState);



        outState.putCharSequence("name", filterModel.name);
        outState.putBooleanArray("groups", filterModel.groups);
        outState.putInt("capacitymin", filterModel.capacityMin);
        outState.putInt("capacitymax", filterModel.capacityMax);



    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume() called with: " + "");



    }

    private void setupSearchContainter(FrameLayout search_container) {

        final ImageView imageViewLeftAction = (ImageView) search_container.findViewById(R.id.left_action);
        search_bar_text_view = (TextView) search_container.findViewById(R.id.search_bar_text);
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


                Log.d(TAG, "onTextChanged() called with: " + "s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");




                // If we are restoring, there is no need to filter now. It will be filtered at the end of activity restoring...
                if (!restoring) {
                    filterModel.setName(s);

                    filterCards();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    imageViewCloseButton.setVisibility(View.VISIBLE);
                } else {
                    imageViewCloseButton.setVisibility(View.GONE);

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
//                Snackbar.make(v, "Show search filter settings", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


//                SearchSettingsFragment searchSettingsFragment = SearchSettingsFragment.newInstance();
//                searchSettingsFragment.show(getSupportFragmentManager(), "search_settings_fragment");

//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//                drawer.openDrawer(GravityCompat.END);

                // If the keyboard is present, hide it.
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);

                // Open right navigation view.
                drawerLayout.openDrawer(GravityCompat.END);


            }
        });

        search_bar_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // Only animate if we are showing the burger icon.
                    if (drawerArrowDrawable.getProgress() == 0.0)
                        playDrawerToggleAnim(drawerArrowDrawable);
                }

            }
        });


    }

    private void filterCards() {

        Log.d(TAG, "filterCards() called with: " + "");

        for (CardsListFragment fragment:
                fragmentsToFilter2) {

            fragment.filterCards(filterModel);
        }
    }


    public void groupsClickHandler (View v) {

        CheckBox checkbox = (CheckBox) v;

        filterModel.setGroup(checkbox.getText(), checkbox.isChecked());

        filterCards();

    }

//    Reference: http://stackoverflow.com/questions/11358121/how-to-handle-the-checkbox-ischecked-and-unchecked-event-in-android
    public void disciplinesClickHandler(View view) {

        int id = view.getId();


    }


    public void cardTypesViewGroupClickHandler(View view) {

        ViewGroup viewGroup = (ViewGroup) view;
        TextView label = (TextView) viewGroup.getChildAt(0);
        CheckBox checkBox = (CheckBox) viewGroup.getChildAt(1);

        checkBox.toggle();

        filterModel.setCardType(label.getText(), checkBox.isChecked());



    }

    public void clansViewGroupClickHandler(View view) {

        ViewGroup viewGroup = (ViewGroup) view;
        TextView label = (TextView) viewGroup.getChildAt(0);
        CheckBox checkBox = (CheckBox) viewGroup.getChildAt(1);

        checkBox.toggle();

        filterModel.setClan(label.getText(), checkBox.isChecked());

        filterCards();

    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

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

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

            drawerLayout.openDrawer(GravityCompat.START);

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



}
