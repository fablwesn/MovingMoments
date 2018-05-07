/*
 * Copyright (C) 2018 Darijo Barucic, Seventoes
 *
 *  Licensed under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fablwesn.education.movingmoments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fablwesn.education.movingmoments.fragments.SectionFavFragment;
import com.fablwesn.education.movingmoments.fragments.SectionMovFragment;
import com.fablwesn.education.movingmoments.utility.PrefUtils;

import java.util.Locale;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Hosts several fragments and a Navigation Drawer.
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Bindings */

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindString(R.string.pref_key_enable_adult)
    String enableAdultKey;
    @BindBool(R.bool.pref_default_adult)
    boolean defaultAdultPref;
    @BindString(R.string.act_label_home_mov)
    String sectionLabelMovies;
    @BindString(R.string.act_label_home_fav)
    String sectionLabelFavs;

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    /*
     * Public */

    public final static String PREF_KEY_SECTION_DISPLAYED = "current_section";

    // Runtime data for creating the request url
    public static float screenWidthPx;
    public static String deviceLanguage;
    public static String enableAdultResults;

    /*
     * Private */

    private SharedPreferences sharedPreferences;
    private static String sectionSelected;
    private FragmentManager fragmentManager;


    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Fundamentals */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);
        fragmentManager = getSupportFragmentManager();

        initSettingValues();
        prepareDisplay(savedInstanceState);
        prepareNavDrawer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the section title we are currently in
        outState.putString(PREF_KEY_SECTION_DISPLAYED, sectionSelected);
    }

    @Override
    public void onBackPressed() {
        // If the drawer is open close it, else behave as usual.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Toolbar and Drawer-Related Methods */

    /**
     * Loads the default fragment and sets up the toolbar
     * and drawer accordingly.
     *
     * @param savedInstanceState of the activity
     */
    private void prepareDisplay(Bundle savedInstanceState) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Check the default section and fragment to load on startup
        if (savedInstanceState == null) {
            sectionSelected = sharedPreferences.getString(getString(R.string.pref_key_default_section),
                    sectionLabelMovies);

            // Checks the drawer item corresponding to the loaded fragment on application-start.
            navigationView.setCheckedItem(PrefUtils.setNavDefaultSection(this, sectionSelected));
            if (findViewById(R.id.fragment_section) != null) {
                loadDefaultFragment();
            }
        } else {
            sectionSelected = savedInstanceState.getString(PREF_KEY_SECTION_DISPLAYED);
        }

        // Set the correct title
        toolbar.setTitle(sectionSelected);
    }

    /**
     * Enables the use of the navigation drawer.
     */
    private void prepareNavDrawer() {
        ActionBarDrawerToggle abDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(abDrawerToggle);
        abDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Replace the current fragment if it's a section or start a new activity if not
        // Do nothing if user selects already displayed section
        int itemId = item.getItemId();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Sections load a new fragment while a non section starts a new activity
        boolean isSection = false;

        switch (itemId) {
            case R.id.nav_movies:
                if (sectionSelected.equals(sectionLabelMovies)) {
                    return true;
                }
                sectionSelected = sectionLabelMovies;
                isSection = true;

                //Load Fragment
                SectionMovFragment movFragment = new SectionMovFragment();
                transaction.replace(R.id.fragment_section, movFragment);
                break;
            case R.id.nav_tv_shows:
                return true;
            case R.id.nav_people:
                return true;
            case R.id.nav_adv_search:
                return true;
            case R.id.nav_favorites:
                if (sectionSelected.equals(sectionLabelFavs)) {
                    return true;
                }
                sectionSelected = sectionLabelFavs;
                isSection = true;

                //Load Fragment
                SectionFavFragment favFragment = new SectionFavFragment();
                transaction.replace(R.id.fragment_section, favFragment);
                break;
            case R.id.nav_feedback:
                return true;
            case R.id.nav_about:
                return true;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        // If it was a section, set the new title for the activity, close the drawer
        // and load the new fragment
        if (isSection) {
            sharedPreferences.edit().putString(PREF_KEY_SECTION_DISPLAYED, sectionSelected).apply();
            toolbar.setTitle(sectionSelected);
            drawer.closeDrawer(GravityCompat.START);

            transaction.commit();
        }
        // If starting a new activity, change the transition-animation
        else {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return true;
    }

    /**
     * Checks which section to load when the app is started.
     */
    private void loadDefaultFragment() {
        // Favorites Fragment
        if (sectionSelected.equals(sectionLabelFavs)) {
            SectionFavFragment fragmentToDisplay = new SectionFavFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_section, fragmentToDisplay).commit();
        }
        // Movie Fragment, also as default
        else {
            SectionMovFragment fragmentToDisplay = new SectionMovFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_section, fragmentToDisplay).commit();
        }
        // Save the section for the search activity
        sharedPreferences.edit().putString(PREF_KEY_SECTION_DISPLAYED, sectionSelected).apply();
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        // Get search item and search manager
        MenuItem searchItem = menu.findItem(R.id.menu_action_search);
        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);

        // Setup our search interface and icon
        if (searchManager != null) {
            SearchView searchView = null;
            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

                // Use custom icon
                ImageView searchButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
                searchButton.setImageResource(R.drawable.ic_search);
            }


        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Save section we are currently in and initiate search
            case R.id.menu_action_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Settings */

    /**
     * SharedPreference-Listener watching out for runtime relevant changes
     */
    final SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals(enableAdultKey)) {
                enableAdultResults = String.valueOf(sharedPreferences.getBoolean(enableAdultKey, defaultAdultPref));
            }
        }
    };

    /**
     * Loads values needed to display the data properly.
     */
    private void initSettingValues() {
        // load default preference values
        PreferenceManager.setDefaultValues(this, R.xml.pref_main, false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidthPx = displayMetrics.widthPixels;
        deviceLanguage = Locale.getDefault().getLanguage();
        enableAdultResults = String.valueOf(sharedPreferences.getBoolean(enableAdultKey, defaultAdultPref));
    }

    /*____________________________________________________________________________________________*/
}
