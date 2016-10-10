package fanvu.easygoer;

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

import fanvu.easygoer.fragment.HelpFragment;
import fanvu.easygoer.fragment.HomeFragment;
import fanvu.easygoer.fragment.PostripFragment;
import fanvu.easygoer.gcm.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ListTripActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trip_main);
        //init();
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
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
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
        // name, website
        txtName.setText("Ravi Tamada");
        txtWebsite.setText("www.androidhive.info");
        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgNavHeaderBg);
        // Loading profile image
        Glide.with(this).load(urlProfileImg)
            .crossFade()
            .thumbnail(0.5f)
            .bitmapTransform(new CropCircleTransformation(this))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
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
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    //Check to see which item was being clicked and perform appropriate action
                    switch (menuItem.getItemId()) {
                        //Replacing the main content with ContentFragment Which is our Inbox View;
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
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_LOGOUT;
                            break;
                        default:
                            navItemIndex = 0;
                    }
                    //Checking if the item is in checked state or not, if not make it in checked state
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
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    super.onDrawerOpened(drawerView);
                }
            };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.menu_list_trip, menu);
          return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
          if (id == R.id.action_post_trip) {
              Intent intent = new Intent(ListTripActivity.this, PostTripActivity.class);
              intent.putExtra("UserName", mUserName);
              intent.putExtra("Password", mPassword);
              intent.putExtra("TypeOfUser", mTypeOfUser);
              startActivity(intent);
              return true;
          }
          if (id == R.id.action_log_out) {
              SharedPreferences mPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
              SharedPreferences.Editor edit = mPreferences.edit();
              edit.putBoolean("isLogin", false);
              edit.commit();
              Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
              startActivity(intent);
              return true;
          }
          return super.onOptionsItemSelected(item);
      }
  */
}
