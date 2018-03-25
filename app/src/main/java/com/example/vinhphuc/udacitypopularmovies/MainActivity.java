package com.example.vinhphuc.udacitypopularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinhphuc.udacitypopularmovies.adapters.MovieAdapter;
import com.example.vinhphuc.udacitypopularmovies.api.MovieApiCallback;
import com.example.vinhphuc.udacitypopularmovies.api.MovieApiManager;
import com.example.vinhphuc.udacitypopularmovies.models.Movies;
import com.example.vinhphuc.udacitypopularmovies.utilities.EndlessRecyclerViewScrollListener;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;
    @InjectView(R.id.noDataContainer)
    private View mNoDataContainer;
    @InjectView(R.id.tvNoDataMsg)
    private TextView mNoDataContainerMsg;
    @InjectView(R.id.rvMovieList)
    private RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    private SwipyRefreshLayout mSwipeRefreshLayout;

    private EndlessRecyclerViewScrollListener mScrollListener;

    private Menu mMenu;

    private final String parcel_movie = "PARCEL_MOVIE";
    private final String pref_sort_method_key = "SORT_METHOD";
    private final String tmdb_sort_pop_desc = "popularity.desc";
    private final String tmdb_sort_vote_avg_desc = "vote_average.desc";
    private final String tmdb_sort_fav_desc = "favourite.desc";

    private MovieApiManager.SortBy sortBy = MovieApiManager.SortBy.MostPopular;

    public static final String BUNDLE_MOVIES_KEY = "movies";
    public static final String BUNDLE_RECYCLER_POSITION_KEY = "recycler_position";
    public static final int FAVOURITES_MOVIE_LOADER_ID = 89;

    private Movies mMovies = new Movies();

    //Checking paramether if the activity is in two-pane mode, i.e. running on a tablet device
    private boolean mTwoPane;

    // Receivers
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Movies never loaded. Get them! (Entry Point)
            if (mRecyclerView.getAdapter() == null) {
                if (isNetworkAvailable()) {
                    loadMovies();
                } else {
                    if (sortBy == MovieApiManager.SortBy.Favourite) {
                        // We can load favourite movies even without connection
                        loadMovies();
                    }
                    mNoDataContainerMsg.setText(R.string.error_need_internet);
                }
                toggleNoDataContainer();
            }

            // Internet connectivity changed so there is no chance data is loading anymore!
            if (mScrollListener != null) {
                mScrollListener.setLoading(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        loadSortSelected();

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movieDetailContainer) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setupRecyclerView();
        setupSwipeRefreshLayout();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mScrollListener != null && !mMovies.getResults().isEmpty()) {
            outState.putInt(BUNDLE_RECYCLER_POSITION_KEY, mScrollListener.getFirstCompletelyVisibleItemPosition());
            outState.putParcelable(BUNDLE_MOVIES_KEY, mMovies);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Movies tempMovie = savedInstanceState.getParcelable(BUNDLE_MOVIES_KEY);
        int position = savedInstanceState.getInt(BUNDLE_RECYCLER_POSITION_KEY);
        if (tempMovie != null) {
            mMovies = tempMovie;

            setRecyclerViewAdapter(new MovieAdapter(this, mMovies, mTwoPane));
            toggleNoDataContainer();
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        super.onResume();
        try {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Restored Instance State
        if (sortBy == MovieApiManager.SortBy.TopRated)
            menu.findItem(R.id.menu_sort_top_rated).setChecked(true);
        else if (sortBy == MovieApiManager.SortBy.Favourite)
            menu.findItem(R.id.menu_sort_favourite).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sort_popularity:
                if (sortBy != MovieApiManager.SortBy.MostPopular) {
                    if (isNetworkAvailable()) {
                        sortBy = MovieApiManager.SortBy.MostPopular;
                        loadMovies();
                        item.setChecked(true);
                        saveSortSelected();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_need_internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_sort_top_rated:
                if (sortBy != MovieApiManager.SortBy.TopRated) {
                    if (isNetworkAvailable()) {
                        sortBy = MovieApiManager.SortBy.TopRated;
                        loadMovies();
                        item.setChecked(true);
                        saveSortSelected();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_need_internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_sort_favourite:
                if (sortBy != MovieApiManager.SortBy.Favourite) {
                    sortBy = MovieApiManager.SortBy.Favourite;
                    loadMovies();
                    item.setChecked(true);
                    saveSortSelected();
                }
                break;
        }

        setTitleAccordingSort();
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        int spanCount = getHandySpanCount();

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.movie_list_items_margin), SpacingItemDecoration.GRID));
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                // We don't want to display "error_need_internet" message on Endless Scroll so check if network is available
                if (isNetworkAvailable()) {
                    loadMoreMovies();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    private void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.clearOnScrollListeners();

        if (adapter != null) {
            if (adapter instanceof MovieAdapter) {
                mRecyclerView.addOnScrollListener(mScrollListener);
                mSwipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
            } else if (adapter instanceof FavouriteMovieAdapter) {
                mSwipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
            }
        }
        mRecyclerView.setAdapter(adapter);
        toggleNoDataContainer();
        mScrollListener.resetState();
        mSwipeRefreshLayout.setRefreshing(false);

        // If on two pane mode clear movie details fragment
        if (adapter == null) {
            ViewGroup view = findViewById(R.id.movieDetailContainer);
            if (view != null) {
                view.removeAllViews();
            }
        }
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadMovies();
                } else {
                    loadMoreMovies();
                }
            }
        });
    }

    private void loadMovies() {
        // Get the movies
        getMovies(1);
    }

    private void loadMoreMovies() {
        getMovies(mMovies.getPage() + 1);
    }

    private void getMovies(final int page) {
        if (sortBy != MovieApiManager.SortBy.Favourite) {
            if (isNetworkAvailable()) {
                getSupportLoaderManager().destroyLoader(FAVOURITES_MOVIE_LOADER_ID);

                MovieApiManager.getInstance().getMovies(sortBy, page, new MovieApiCallback<Movies>() {
                    @Override
                    public void onResponse(Movies result) {
                        if (result != null) {
                            if (page == 1) { // Refreshing movies
                                mMovies = result;
                                setRecyclerViewAdapter(new MovieAdapter(MainActivity.this, mMovies, mTwoPane));
                            } else {
                                if (mRecyclerView.getAdapter() instanceof MovieAdapter) {
                                    ((MovieAdapter) mRecyclerView.getAdapter()).updateMovies(result);
                                }
                            }
                        } else {
                            mNoDataContainerMsg.setText(R.string.movie_detail_error_message);
                            Toast.makeText(getApplicationContext(), R.string.movie_detail_error_message, Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                mNoDataContainerMsg.setText(R.string.error_need_internet);
                Toast.makeText(getApplicationContext(), R.string.error_need_internet, Toast.LENGTH_SHORT).show();
            }
        } else {
            mMovies = new Movies();
            // Reset recycler adapter
            setRecyclerViewAdapter(null);
            getSupportLoaderManager().initLoader(FAVOURITES_MOVIE_LOADER_ID, null, this);
        }

    }

    private void toggleNoDataContainer() {
        if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataContainer.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataContainer.setVisibility(View.VISIBLE);
        }
    }

    private int getHandySpanCount() {
        // Get width regarding ratio 2:3
        double aa = (int) getResources().getDimension(R.dimen.movie_image_height) / 1.5;
        double ab = getResources().getDisplayMetrics().widthPixels;
        if (mTwoPane) {
            // If in Two Pane Mode Recycler View width is Guidelines Percentage
            TypedValue typedValue = new TypedValue();
            getResources().getValue(R.dimen.movies_list_detail_separator_percent, typedValue, true);
            float ac = typedValue.getFloat();
            ab = ac * ab;
        }
        double ac = (ab / aa);
        return (int) Math.round(ac);
    }

    private boolean isNetworkAvailable() {
        return Misc.isNetworkAvailable(getApplicationContext());
    }

    private void saveSortSelected() {
        getPreferences(Context.MODE_PRIVATE).edit().putInt(getString(R.string.saved_sort_by_key), sortBy.ordinal()).commit();
    }

    private void setTitleAccordingSort() {
        switch (sortBy) {
            case MostPopular:
                setTitle(getString(R.string.most_popular) + " " + getString(R.string.movies));
                break;
            case TopRated:
                setTitle(getString(R.string.top_rated) + " " + getString(R.string.movies));
                break;
            case Favourite:
                setTitle(getString(R.string.favourite) + " " + getString(R.string.movies));
                break;
        }
    }

    private void loadSortSelected() {
        sortBy = MovieApiManager.SortBy.fromId(getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.saved_sort_by_key), 0));
        setTitleAccordingSort();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MoviesContract.MoviesEntry._ID);

                } catch (Exception e) {
                    Logger.e("Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            setRecyclerViewAdapter(new FavouriteMovieAdapter(this, data, mTwoPane));
        } else {
            mNoDataContainerMsg.setText(R.string.no_favourite_movies_message);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
