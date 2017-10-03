package ar.com.cotidiano.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import ar.com.cotidiano.popularmovies.utilities.NetworkUtils;

import static ar.com.cotidiano.popularmovies.utilities.PopularMovieUtils.getMovieListFromJson;

public class MainActivity
        extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PopularMovieAdapter popularMovieAdapter;
    private PopularMovie[] popularMovie;

    private GridView gridView;
    private ProgressBar mLoadingIndicator;
    private Button mTryAgain;
    private LinearLayout mNoInternetConnectionLinearLayout;

    private Button mTryAgainNoData;
    private LinearLayout mNoDataAvailableLinearLayout;

    private SharedPreferences preferences;
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        gridView = (GridView) findViewById(R.id.popular_movies_grid);
        mNoInternetConnectionLinearLayout = (LinearLayout) findViewById(R.id.ll_no_connection);
        mTryAgain = (Button) findViewById(R.id.btn_try_again);

        mNoDataAvailableLinearLayout = (LinearLayout) findViewById(R.id.ll_no_data);
        mTryAgainNoData = (Button) findViewById(R.id.btn_try_again_no_data);

        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTMDBSearch();
            }
        });

        mTryAgainNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTMDBSearch();
            }
        });

        setupSharedPreferences();

        makeTMDBSearch();
    }

    private void startLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    private void endLoading() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    private void makeTMDBSearch() {
        mNoInternetConnectionLinearLayout.setVisibility(View.INVISIBLE);
        mNoDataAvailableLinearLayout.setVisibility(View.INVISIBLE);

        URL tmdbSearchUrl = NetworkUtils.buildUrl(mSortBy);
        new TMDBQueryTask().execute(tmdbSearchUrl);
    }

    private void populateMovie() {
        //Adapter
        endLoading();

        popularMovieAdapter = new PopularMovieAdapter(MainActivity.this, Arrays.asList(popularMovie));

        gridView.setAdapter(popularMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = popularMovie[position].getTitle();
                int movieId = popularMovie[position].getId();

                Context context = MainActivity.this;
                Class destinationClass = PopularMovieDetail.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, String.valueOf(movieId));

                startActivity(intentToStartDetailActivity);
            }
        });
    }

    private class TMDBQueryTask extends AsyncTask<URL, Void, String> {
        private boolean internet;

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            internet = netInfo != null && netInfo.isConnectedOrConnecting();

            return internet;
        }

        @Override
        protected void onPreExecute() {
            startLoading();
            Log.d("INTERNET", String.valueOf(isOnline()));
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String tmdbSearchResultsJson = null;

            if(isOnline()) {
                try {
                    tmdbSearchResultsJson = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return tmdbSearchResultsJson;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String tmdbResults) {

            if(tmdbResults != "" && tmdbResults != null) {
                try {
                    popularMovie = getMovieListFromJson(tmdbResults);

                    populateMovie();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                if(!internet) {
                    showNoConnectionMessage();
                } else {
                    showJsonEmptyMessage();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNoConnectionMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mNoInternetConnectionLinearLayout.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void showJsonEmptyMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mNoDataAvailableLinearLayout.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, getString(R.string.no_data_available), Toast.LENGTH_SHORT).show();
    }

    private void setupSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSortBy = preferences.getString(getString(R.string.pref_sort_by_list), NetworkUtils.getSortByDefault());

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sort_by_list))) {
//            Toast.makeText(this, sharedPreferences.getString(getString(R.string.pref_sort_by_list), NetworkUtils.getSortByDefault()), Toast.LENGTH_SHORT).show();
            mSortBy = sharedPreferences.getString(getString(R.string.pref_sort_by_list), NetworkUtils.getSortByDefault());

            makeTMDBSearch();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}