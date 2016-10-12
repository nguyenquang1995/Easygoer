package fanvu.easygoer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fanvu.easygoer.Constant;
import fanvu.easygoer.fragment.HelpFragment;
import fanvu.easygoer.fragment.HomeFragment;
import fanvu.easygoer.fragment.PostripFragment;
import fanvu.easygoer.fragment.SettingFragment;
import fanvu.easygoer.gcm.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ListTripActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName;
    private Toolbar toolbar;
    private static final String urlNavHeaderBg =
        "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg =
        "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_POST_TRIP = "pos trip";
    private static final String TAG_HELP = "help";
    private static final String TAG_SETTING = "setting";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_HOME;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private String mUserName;
    private String mPassword;
    private String mTypeOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trip_main);
        initMain(savedInstanceState);
    }

    private void initMain(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARE_PREFERENCE
            , Context.MODE_PRIVATE);
        mUserName = sharedPreferences.getString("username", "");
        mPassword = sharedPreferences.getString("password", "");
        mTypeOfUser = sharedPreferences.getString("typeOfUser", "");
        // load nav menu header data
        loadNavHeader();
        // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        txtName.setText(mTypeOfUser);
        Glide.with(this).load(R.drawable.nav_bg_menu)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgNavHeaderBg);
        Glide.with(this).load(urlProfileImg)
            .crossFade()
            .thumbnail(0.5f)
            .bitmapTransform(new CropCircleTransformation(this))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile);
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        drawer.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                PostripFragment postripFragment = new PostripFragment();
                return postripFragment;
            case 2:
                HelpFragment helpFragment = new HelpFragment();
                return helpFragment;
            case 3:
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Intent intent;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME;
                            break;
                        case R.id.nav_post_trip:
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_POST_TRIP;
                            break;
                        case R.id.nav_help:
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_HELP;
                            break;
                        case R.id.nav_setting:
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_SETTING;
                            break;
                        case R.id.nav_logout:
                            SharedPreferences
                                mPreferences =
                                getSharedPreferences(Constant.SHARE_PREFERENCE, MODE_PRIVATE);
                            SharedPreferences.Editor edit = mPreferences.edit();
                            edit.putBoolean(Constant.IS_LOGIN, false);
                            edit.commit();
                            intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_LOGOUT;
                            break;
                        default:
                            navItemIndex = 0;
                    }
                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    } else {
                        menuItem.setChecked(true);
                    }
                    menuItem.setChecked(true);
                    loadHomeFragment();
                    return true;
                }
            });
        ActionBarDrawerToggle
            actionBarDrawerToggle =
            new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer,
                R.string.closeDrawer) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }
}
