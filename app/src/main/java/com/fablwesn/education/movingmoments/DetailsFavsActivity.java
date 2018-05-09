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

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fablwesn.education.movingmoments.adapters.BackdropPagerAdapter;
import com.fablwesn.education.movingmoments.adapters.ReviewsRecyclerAdapter;
import com.fablwesn.education.movingmoments.adapters.VideosRecyclerAdapter;
import com.fablwesn.education.movingmoments.database.MoviesContract;
import com.fablwesn.education.movingmoments.models.details.MovieDetailsModel;
import com.fablwesn.education.movingmoments.models.details.reviews.ReviewModelDetails;
import com.fablwesn.education.movingmoments.models.details.reviews.ReviewsContainerDetails;
import com.fablwesn.education.movingmoments.models.details.videos.VideoModelDetails;
import com.fablwesn.education.movingmoments.models.details.videos.VideosContainerDetails;
import com.fablwesn.education.movingmoments.remote.TMDbMoviesService;
import com.fablwesn.education.movingmoments.utility.ApiUtils;
import com.fablwesn.education.movingmoments.utility.ConverterUtils;
import com.fablwesn.education.movingmoments.utility.MiscUtils;
import com.fablwesn.education.movingmoments.utility.PrefUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFavsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Bindings */

    @BindView(R.id.details_collapsing_tb)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.details_app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.details_poster_img)
    ImageView posterImg;
    @BindView(R.id.details_backdrop_pager)
    ViewPager backdropPager;
    @BindView(R.id.details_backdrop_tablayout)
    TabLayout backdropTabLay;
    @BindView(R.id.details_tb_title_txt)
    TextView titleText;
    @BindView(R.id.details_tb_genre_txt)
    TextView genreText;
    @BindView(R.id.details_toolbar_runtime_txt)
    TextView runtimeText;
    @BindView(R.id.details_toolbar_release_txt)
    TextView releaseText;
    @BindView(R.id.details_toolbar_countries_iso_txt)
    TextView countriesIsoTxt;

    @BindView(R.id.details_fab_favorites)
    FloatingActionButton favoritesFab;

    @BindView(R.id.details_tagline_txt)
    TextView taglineText;

    @BindView(R.id.details_overview_txt)
    TextView overviewText;

    @BindView(R.id.details_votes_txt)
    TextView voteCount;
    @BindView(R.id.details_vote_rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.recycler_loading)
    RecyclerView videosRecycler;
    @BindView(R.id.txt_empty_loading)
    TextView emptyVideosText;

    @BindView(R.id.details_recycler_view_reviews)
    RecyclerView reviewsRecycler;
    @BindView(R.id.details_empty_text_reviews)
    TextView emptyReviewTxt;

    @BindView(R.id.details_text_rel_date)
    TextView detRelDateText;
    @BindView(R.id.details_text_rel_countries)
    TextView detRelCountriesText;
    @BindView(R.id.details_text_orig_title)
    TextView detOrigTitleText;
    @BindView(R.id.details_text_orig_lang)
    TextView detOrigLangText;
    @BindView(R.id.details_text_budget)
    TextView detBudgetText;
    @BindView(R.id.details_text_revenue)
    TextView detRevenueText;

    /*____________________________________________________________________________________________*/

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Resources */

    @BindDrawable(R.drawable.ic_favorite_full)
    Drawable drawFavFull;
    @BindDrawable(R.drawable.ic_favorite_empty)
    Drawable drawFavEmpty;

    /* __________________________________________________________________________________________ */

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Globals */

    /*
     * Public */

    public final static String KEY_MOVIE_URI = "tmdb_uri_to_load";

    /*
     * Private */

    private final static String KEY_MOVIE_ID = "movie_tmdbid";
    private final static String KEY_MOVIE_TITLE = "movie_title";
    private final static String KEY_MOVIE_POSTER = "movie_poster";
    private final static String KEY_MOVIE_RELEASE = "movie_release";
    private final static String KEY_MOVIE_GENRE = "movie_genre";
    private final static String KEY_MOVIE_OVERVIEW = "movie_overview";
    private final static String KEY_MOVIE_OrigTitle = "movie_ortit";
    private final static String KEY_MOVIE_OrigLang = "movie_orlang";
    private final static String KEY_MOVIE_VOTES = "movie_votes";
    private final static String KEY_MOVIE_RATE = "movie_rate";

    private SharedPreferences sharedPreferences;
    private TMDbMoviesService tmDbMoviesService;
    private MenuItem favoriteMenuItem;
    private Uri selectedMovie;
    private MovieDetailsModel detailsModel;
    private Timer backdropTimer;

    private boolean isFav;

    private int movieId = -1, movieVotes;
    private String movieTmdbLink, movieTitle, moviePoster, movieRelease, movieGenre, movieOverview, movieOrigTitle, movieOrigLang;
    private float movieRate;

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Fundamentals */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (!MiscUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.fav_det_no_conn), Toast.LENGTH_LONG).show();
        }

        // Get our remote service
        tmDbMoviesService = ApiUtils.getTMDbMoviesService();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedInstanceState == null) {
            selectedMovie = getIntent().getData();
            //Start loader
            getLoaderManager().initLoader(8045, null, this);
        } else {
            movieId = savedInstanceState.getInt(KEY_MOVIE_ID);
            movieVotes = savedInstanceState.getInt(KEY_MOVIE_VOTES);
            movieRate = savedInstanceState.getFloat(KEY_MOVIE_RATE);
            movieTitle = savedInstanceState.getString(KEY_MOVIE_TITLE);
            moviePoster = savedInstanceState.getString(KEY_MOVIE_POSTER);
            movieRelease = savedInstanceState.getString(KEY_MOVIE_RELEASE);
            movieGenre = savedInstanceState.getString(KEY_MOVIE_GENRE);
            movieOverview = savedInstanceState.getString(KEY_MOVIE_OVERVIEW);
            movieOrigTitle = savedInstanceState.getString(KEY_MOVIE_OrigTitle);
            movieOrigLang = savedInstanceState.getString(KEY_MOVIE_OrigLang);
            displayBaseData(savedInstanceState);
        }

        isFav = sharedPreferences.getBoolean(String.valueOf(movieId), true);
        prepareToolbar();

        // Constructs the String url for the movie's tmdb-link.
        movieTmdbLink =
                ApiUtils.MOVIE_TMDB_LINK_BASE +
                        movieId +
                        ApiUtils.MOVIE_TMDB_LINK_LANG +
                        HomeActivity.deviceLanguage;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cancel the backdrop timer when pausing
        if (backdropTimer != null) {
            backdropTimer.cancel();
            backdropTimer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume pager if already loaded
        if (detailsModel != null) {
            setupBackdropTimer();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Add a custom Animation when returning
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (movieId != -1) {
            outState.putInt(KEY_MOVIE_ID, movieId);
            outState.putInt(KEY_MOVIE_VOTES, movieVotes);
            outState.putFloat(KEY_MOVIE_RATE, movieRate);
            outState.putString(KEY_MOVIE_TITLE, movieTitle);
            outState.putString(KEY_MOVIE_POSTER, moviePoster);
            outState.putString(KEY_MOVIE_RELEASE, movieRelease);
            outState.putString(KEY_MOVIE_GENRE, movieGenre);
            outState.putString(KEY_MOVIE_OVERVIEW, movieOverview);
            outState.putString(KEY_MOVIE_OrigTitle, movieOrigTitle);
            outState.putString(KEY_MOVIE_OrigLang, movieOrigLang);
        }
        if (detailsModel != null) {
            outState.putParcelable(DetailsActivity.KEY_MOVIE_DETAILS, detailsModel);
        }
    }

    /*____________________________________________________________________________________________*/

     /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Menu & Toolbar */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        // Get the favorite action and set it's icon according to the favorite state of the movie
        favoriteMenuItem = menu.findItem(R.id.menu_action_favorite);
        if (isFav) {
            favoriteMenuItem.setIcon(drawFavFull);
        } else {
            favoriteMenuItem.setIcon(drawFavEmpty);
        }
        // Hide the action initially as we start with an expanded toolbar
        favoriteMenuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When the home button is pressed, take the user back
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // Menu actions
        switch (item.getItemId()) {
            // Add or remove the movie to/from the favorites list
            case R.id.menu_action_favorite:
                toggleFavorites();
                return true;
            // Start a share intent to share the tmdb link of the movie
            case R.id.menu_action_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, movieTitle);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_intent_movie_body, movieTmdbLink));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharing_intent_movie_title)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets up the toolbar and a listener for the expansion of the tb.
     */
    private void prepareToolbar() {
        if (isFav) {
            favoritesFab.setImageDrawable(drawFavFull);
        } else {
            favoritesFab.setImageDrawable(drawFavEmpty);
        }
        toolbar.setTitle("");
        titleText.setText(movieTitle);
        // Listen for changes in the toolbar extension
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isCollapsed = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Initialize on first run
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                // When completely collapsed:
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(movieTitle);
                    titleText.setText("");
                    isCollapsed = true;
                    if (favoriteMenuItem != null) {
                        favoriteMenuItem.setVisible(true);
                    }
                }
                // When expanded.
                else if (isCollapsed) {
                    collapsingToolbar.setTitle(" ");
                    titleText.setText(movieTitle);
                    isCollapsed = false;
                    if (favoriteMenuItem != null) {
                        favoriteMenuItem.setVisible(false);
                    }
                }
            }
        });

        // Set Toolbar and the up button.
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /**
     * Toggle's between a movies "isFavorited" state
     * adding it to the local DB or removing it from there
     */
    @OnClick(R.id.details_fab_favorites)
    public void toggleFavorites() {
        // If not inside our DB (default case), add it
        if (!isFav) {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.MovieEntry.COLUMN_TMDB_ID, movieId);
            values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, moviePoster);
            values.put(MoviesContract.MovieEntry.COLUMN_RELEASE, movieRelease);
            values.put(MoviesContract.MovieEntry.COLUMN_TITLE, movieTitle);
            values.put(MoviesContract.MovieEntry.COLUMN_GENRE, movieGenre);
            values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movieOverview);
            values.put(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT, movieVotes);
            values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVG, movieRate);
            values.put(MoviesContract.MovieEntry.COLUMN_ORIG_TITLE, movieOrigTitle);
            values.put(MoviesContract.MovieEntry.COLUMN_ORIG_LANG, movieOrigLang);

            Uri uri = getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);

            if (uri != null) {
                Toast.makeText(this, getString(R.string.added_fav_toast), Toast.LENGTH_SHORT).show();
                isFav = true;
                favoriteMenuItem.setIcon(drawFavFull);
                favoritesFab.setImageDrawable(drawFavFull);
                sharedPreferences.edit().putBoolean(String.valueOf(movieId), isFav).apply();
            } else {
                Toast.makeText(this, getString(R.string.add_fav_error_toast), Toast.LENGTH_LONG).show();
            }


        }
        // If already inside our DB, remove it
        else {
            int numRows = getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry.DELETE_CLAUSE_ID, new String[]{Integer.toString(movieId)});

            if (numRows > 0) {
                Toast.makeText(getApplicationContext(), R.string.removed_fav_toast, Toast.LENGTH_SHORT).show();
                isFav = false;
                favoriteMenuItem.setIcon(drawFavEmpty);
                favoritesFab.setImageDrawable(drawFavEmpty);
                sharedPreferences.edit().putBoolean(String.valueOf(movieId), isFav).apply();
            } else {
                Toast.makeText(getApplicationContext(), R.string.rem_fav_error_toast, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Movie Content */

    /**
     * Display all the data we have stored inside our DB
     */
    private void displayBaseData(Bundle savedInstanceState) {
        // load posterImg
        Glide.with(this)
                .load(ApiUtils.BASE_IMAGES_URL + ApiUtils.getPosterSizePath(this) + moviePoster)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.vector_img_loading)
                        .error(R.drawable.vector_no_image))
                .into(posterImg);

        titleText.setText(movieTitle);
        genreText.setText(movieGenre);
        overviewText.setText(movieOverview);
        voteCount.setText(String.valueOf(movieVotes));
        ratingBar.setRating(movieRate);
        detOrigLangText.setText(movieOrigLang);
        if (movieOrigTitle != null) {
            detOrigTitleText.setText(movieOrigTitle);
        }

        // Reformat the dates format before displaying
        try {
            releaseText.setText(ConverterUtils.changeDateFormat(ConverterUtils.RELEASE_DATE_SHORT, movieRelease));
        } catch (ParseException e) {
            releaseText.setText(movieRelease);
        }

        try {
            detRelDateText.setText(ConverterUtils.changeDateFormat(ConverterUtils.RELEASE_DATE_FULL, movieRelease));
        } catch (ParseException e) {
            detRelDateText.setText(movieRelease);
        }

        loadDetails(savedInstanceState);
    }

    /**
     * Loads the details of the movie, either from the web api or from the
     * savedInstanceState, if available
     *
     * @param savedInstanceState of this activity
     */
    private void loadDetails(Bundle savedInstanceState) {
        // If we already have loaded a list, display it instead of querying again
        if (savedInstanceState != null) {
            detailsModel = savedInstanceState.getParcelable(DetailsActivity.KEY_MOVIE_DETAILS);
            displayDetails();
        } else {
            if (!MiscUtils.isNetworkAvailable(this)) {
                emptyReviewTxt.setText(R.string.no_connection_error);
                emptyVideosText.setText(R.string.no_connection_error);
                return;
            }
            // Get our remote service
            TMDbMoviesService tmdbService = ApiUtils.getTMDbMoviesService();
            Call<MovieDetailsModel> detailsCall = tmdbService.getMovieDetails(
                    movieId,
                    ApiUtils.API_KEY,
                    HomeActivity.deviceLanguage,
                    ApiUtils.URL_QUERY_VALUE_TO_APPEND,
                    ApiUtils.URL_QUERY_VALUE_IMG_LANG
            );
            detailsCall.enqueue(new Callback<MovieDetailsModel>() {
                @Override
                public void onResponse(@NonNull Call<MovieDetailsModel> call, @NonNull Response<MovieDetailsModel> response) {
                    detailsModel = response.body();
                    if (detailsModel != null) {
                        displayDetails();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_data_fetched), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieDetailsModel> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data_fetched), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Displays the additional movie details loaded in this activity.
     */
    private void displayDetails() {
        // Get the backdrop-images if available and set them to the adapter
        // with an timer iterating through the entries.
        if (detailsModel.getImages().getBackdrops() != null) {
            BackdropPagerAdapter backdropAdapter = new BackdropPagerAdapter(this, detailsModel.getImages().getBackdrops());
            backdropPager.setAdapter(backdropAdapter);
            backdropTabLay.setupWithViewPager(backdropPager, true);
            setupBackdropTimer();
        }

        loadMovieVideos();
        loadMovieReviews();

        // Set runtime
        if (detailsModel.getRuntime() == null || detailsModel.getRuntime() == 0) {
            runtimeText.setText(R.string.details_default_empty);
        } else {
            runtimeText.setText(getString(R.string.details_runtime_appen, detailsModel.getRuntime()));
        }

        // Set Tagline if available, hide if not
        if (detailsModel.getTagline().isEmpty()) {
            taglineText.setVisibility(View.GONE);
        } else {
            taglineText.setText(getString(R.string.details_quote_frame, detailsModel.getTagline()));
        }

        // Set Countries in two differently formatted ways.
        if (detailsModel.getProductionCountries() != null) {
            countriesIsoTxt.setText(ConverterUtils.formatProductionCountries(detailsModel.getProductionCountries(), ConverterUtils.ID_COUNTRY_CONVERTER_ISO));
            detRelCountriesText.setText(ConverterUtils.formatProductionCountries(detailsModel.getProductionCountries(), ConverterUtils.ID_COUNTRY_CONVERTER_FULL));
        }

        // Get the financial info
        int budget = detailsModel.getBudget();
        int revenue = detailsModel.getRevenue();

        // Color the revenue text if we have enough
        // info to calculate if the movie made any profit (green) ir loss (red)
        if (revenue != 0 && budget != 0 && revenue - budget > 0) {
            detRevenueText.setTextColor(Color.GREEN);
        } else if (revenue != 0 && budget != 0 && revenue - budget < 0) {
            detRevenueText.setTextColor(Color.RED);
        }
        // Reformat the display of the numbers
        NumberFormat numberFormat = NumberFormat.getInstance(MiscUtils.getCurrentLocale(this));
        if (budget != 0) {
            detBudgetText.setText(getString(R.string.details_dollar_appendix, numberFormat.format(budget)));
        }
        if (revenue != 0) {
            detRevenueText.setText(getString(R.string.details_dollar_appendix, numberFormat.format(revenue)));
        }

    }

    /**
     * Set up the pager timer displaying the backdrop images in a loop from start to end
     */
    private void setupBackdropTimer() {
        // Creates a timer that iterates through the pagers entries automatically, if there are more than 1
        if (detailsModel.getImages().getBackdrops().size() > 1 && backdropTimer == null) {
            backdropTimer = new Timer();
            backdropTimer.scheduleAtFixedRate(new DetailsFavsActivity.BackdropSliderTimer(), PrefUtils.BACKDROP_PAGER_INIT_TIME, PrefUtils.BACKDROP_PAGER_PERIOD_TIME);
        }
    }

    /**
     * Displays a list of videos for the movie.
     * If none are available, display an error
     */
    private void loadMovieVideos() {
        // Get our remote service
        Call<VideosContainerDetails> videoCall = tmDbMoviesService.getMovieVideos(
                movieId,
                ApiUtils.API_KEY,
                HomeActivity.deviceLanguage
        );

        videoCall.enqueue(new Callback<VideosContainerDetails>() {
            @Override
            public void onResponse(@NonNull Call<VideosContainerDetails> call, @NonNull Response<VideosContainerDetails> response) {
                VideosContainerDetails videosContainerDetails = response.body();
                List<VideoModelDetails> videosList = new ArrayList<>();

                if (videosContainerDetails != null) {
                    videosList = videosContainerDetails.getResults();
                }

                if (videosList == null || videosList.isEmpty()) {
                    emptyVideosText.setText(R.string.no_videos_found);
                } else {
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(videosRecycler.getContext(),
                            layoutManager.getOrientation());

                    VideosRecyclerAdapter videoAdapter = new VideosRecyclerAdapter(videosList);

                    videosRecycler.setLayoutManager(layoutManager);
                    videosRecycler.addItemDecoration(dividerItemDecoration);
                    videosRecycler.setAdapter(videoAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosContainerDetails> call, @NonNull Throwable t) {
                emptyVideosText.setText(R.string.no_data_fetched);
            }
        });
    }

    /**
     * Displays a list of reviews for the movie.
     * If none are available, display an error
     */
    private void loadMovieReviews() {
        // Get our remote service
        Call<ReviewsContainerDetails> reviewCall = tmDbMoviesService.getMovieReviews(
                movieId,
                ApiUtils.API_KEY,
                HomeActivity.deviceLanguage
        );

        reviewCall.enqueue(new Callback<ReviewsContainerDetails>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsContainerDetails> call, @NonNull Response<ReviewsContainerDetails> response) {
                ReviewsContainerDetails reviewsContainerDetails = response.body();
                List<ReviewModelDetails> reviewList = new ArrayList<>();

                if (reviewsContainerDetails != null) {
                    reviewList = reviewsContainerDetails.getResults();
                }

                if (reviewList == null || reviewList.isEmpty()) {
                    emptyReviewTxt.setText(R.string.no_reviews_found);
                } else {
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reviewsRecycler.getContext(),
                            layoutManager.getOrientation());

                    ReviewsRecyclerAdapter reviewAdapter = new ReviewsRecyclerAdapter(getApplicationContext(), reviewList);

                    reviewsRecycler.setLayoutManager(layoutManager);
                    reviewsRecycler.addItemDecoration(dividerItemDecoration);
                    reviewsRecycler.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsContainerDetails> call, @NonNull Throwable t) {
                emptyReviewTxt.setText(R.string.no_data_fetched);
            }
        });
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       DB Query Loader */

    /**
     * onCreateLoader:
     * <p>
     * - creates a projection String[] containing the query params we want and starts loading it from the db
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // We want all columns
        String[] projection = {
                MoviesContract.MovieEntry.DB_ID,
                MoviesContract.MovieEntry.COLUMN_TMDB_ID,
                MoviesContract.MovieEntry.COLUMN_ADULT,
                MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MoviesContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesContract.MovieEntry.COLUMN_RELEASE,
                MoviesContract.MovieEntry.COLUMN_GENRE,
                MoviesContract.MovieEntry.COLUMN_ORIG_TITLE,
                MoviesContract.MovieEntry.COLUMN_ORIG_LANG,
                MoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
                MoviesContract.MovieEntry.COLUMN_VOTE_AVG
        };
        // start loading with set uri
        return new CursorLoader(this, selectedMovie, projection, null, null, null);
    }

    /**
     * onLoadFinished:
     * <p>
     * - fill in the views with correct data from the db query
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // return early if there's nothing to show
        if (cursor == null || cursor.getCount() < 1)
            return;

        //If the cursor has some data, move through the entries and set all the views accordingly
        if (cursor.moveToFirst()) {
            movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TMDB_ID));
            movieTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
            movieVotes = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT));
            movieRate = cursor.getFloat(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVG));
            moviePoster = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));
            movieRelease = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE));
            movieGenre = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_GENRE));
            movieOverview = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW));
            movieOrigTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIG_TITLE));
            movieOrigLang = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIG_LANG));
        }

        displayBaseData(null);
    }

    /**
     * onLoaderReset:
     * empty
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /*____________________________________________________________________________________________*/

    /*¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
       Misc. OnClicks */

    /**
     * Open the movie's original homepage, display an error toast if no link is available.
     */
    @OnClick(R.id.details_poster_img)
    public void openMovieHomepageWeb() {
        if (detailsModel != null) {
            if (detailsModel.getHomepage() == null || detailsModel.getHomepage().isEmpty()) {
                Toast.makeText(this, getString(R.string.details_no_homepage), Toast.LENGTH_SHORT).show();
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailsModel.getHomepage()));
                startActivity(browserIntent);
            }
        }
    }

    /**
     * Open the movies TMDb-Website
     */
    @OnClick(R.id.details_collapsing_tb)
    public void openMovieTmdbWeb() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieTmdbLink));
        startActivity(browserIntent);
    }

    /*____________________________________________________________________________________________*/

    /* ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
      Inner classes */

    /**
     * Timer going through every pager entry, starts again from the beginning
     * after reaching the last entry
     */
    private class BackdropSliderTimer extends TimerTask {

        @Override
        public void run() {
            DetailsFavsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (detailsModel.getImages().getBackdrops().size() > getResources().getInteger(R.integer.details_indicator_max_size) - 1) {
                        if (backdropPager.getCurrentItem() < getResources().getInteger(R.integer.details_indicator_max_size) - 1) {
                            backdropPager.setCurrentItem(backdropPager.getCurrentItem() + 1);
                        } else {
                            backdropPager.setCurrentItem(0);
                        }
                    } else {
                        if (backdropPager.getCurrentItem() < detailsModel.getImages().getBackdrops().size() - 1) {
                            backdropPager.setCurrentItem(backdropPager.getCurrentItem() + 1);
                        } else {
                            backdropPager.setCurrentItem(0);
                        }
                    }
                }
            });
        }
    }

    /* __________________________________________________________________________________________ */
}
