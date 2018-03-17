package com.example.vinhphuc.udacitypopularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private Menu mMenu;

    private final String parcel_movie = "PARCEL_MOVIE";
    private final String pref_sort_method_key = "SORT_METHOD";
    private final String tmdb_sort_pop_desc = "popularity.desc";
    private final String tmdb_sort_vote_avg_desc = "vote_average.desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridview_movie);
        mGridView.setOnItemClickListener(moviePosterClickListener);

        if (savedInstanceState == null) {
            getMoviesFromTMDb(getSortMethod());
        } else {
            Parcelable[] parcelable = savedInstanceState
                    .getParcelableArray(parcel_movie);

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                mGridView.setAdapter(new MovieAdapter(this, movies));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, mMenu);

        //Make menu items accessible
        mMenu = menu;

        //Add menu items
        mMenu.add(Menu.NONE, R.string.pref_sort_pop_desc_key, Menu.NONE, null)
                .setVisible(false)
                .setIcon(R.drawable.ic_action_whatshot)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mMenu.add(Menu.NONE, R.string.pref_sort_vote_avg_desc_key, Menu.NONE, null)
                .setVisible(false)
                .setIcon(R.drawable.ic_action_poll)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        //Update menu to show relevant items
        updateMenu();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            //Get Movie objects from gridView
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) mGridView.getItemAtPosition(i);
            }

            //Save Movie objects to bundle
            outState.putParcelableArray(parcel_movie, movies);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.string.pref_sort_pop_desc_key:
                updateSharedPreferences(tmdb_sort_pop_desc);
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            case R.string.pref_sort_vote_avg_desc_key:
                updateSharedPreferences(tmdb_sort_vote_avg_desc);
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            default:
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private final GridView.OnItemClickListener moviePosterClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(parcel_movie, movie);

            startActivity(intent);
        }
    };

    private void getMoviesFromTMDb(String sortMethod) {
        if (isNetworkAvailable()) {
            //Listener for AsyncTask when it is ready to update UI
            OnTaskCompleted taskCompleted = new OnTaskCompleted() {
                @Override
                public void onFetchMovieTaskCompleted(Movie[] movies) {
                    mGridView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                }
            };

            //Execute task
            FetchMovieTask movieTask = new FetchMovieTask(taskCompleted);
            movieTask.execute(sortMethod);
        } else {
            Toast.makeText(this, getString(R.string.error_need_internet), Toast.LENGTH_LONG).show();
        }
    }

    //Check if Internet is accessible. Return true if yes & false otherwise
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Update menu based on method found set in SharedPreferences
    private void updateMenu() {
        String sortMethod = getSortMethod();

        if (sortMethod.equals(tmdb_sort_pop_desc)) {
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(true);
        } else {
            mMenu.findItem(R.string.pref_sort_vote_avg_desc_key).setVisible(false);
            mMenu.findItem(R.string.pref_sort_pop_desc_key).setVisible(true);
        }
    }

    /**
     * Gets the sort method set by user from SharedPreferences. If no sort method is defined it will
     * default to sorting by popularity (tmdb_sort_pop_desc).
     *
     * @return Sort method from SharedPreferences
     */
    private String getSortMethod() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        return preferences.getString(pref_sort_method_key, tmdb_sort_pop_desc);
    }

    private void updateSharedPreferences(String sortMethod) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pref_sort_method_key, sortMethod);
        editor.apply();
    }
}
