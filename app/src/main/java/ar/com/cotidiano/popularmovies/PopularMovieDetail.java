package ar.com.cotidiano.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import ar.com.cotidiano.popularmovies.utilities.NetworkUtils;

import static ar.com.cotidiano.popularmovies.utilities.PopularMovieUtils.getMovieFromJson;

public class PopularMovieDetail extends AppCompatActivity {
    private Movie movie;

    private LinearLayout mInfoLinearLayout;
    private ProgressBar mLoadingIndicator;

    private ImageView mBackdropImageView;
    private TextView mOriginalTitleTextView;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;

    private Button mTryAgain;
    private LinearLayout mNoInternetConnectionLinearLayout;

    private String movieId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_detail);

        mInfoLinearLayout = (LinearLayout) findViewById(R.id.ll_movie_info);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_original_title);
        mOverviewTextView = (TextView) findViewById(R.id.tv_movie_overview);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        mVoteAverageTextView = (TextView) findViewById(R.id.tv_movie_vote_average);
        mBackdropImageView = (ImageView) findViewById(R.id.iv_movies_backdrop);

        mNoInternetConnectionLinearLayout = (LinearLayout) findViewById(R.id.ll_no_connection);
        mTryAgain = (Button) findViewById(R.id.btn_try_again);

        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMovieData();
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieData();
        }
    }

    private void startLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mInfoLinearLayout.setVisibility(View.GONE);
    }

    private void endLoading() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mInfoLinearLayout.setVisibility(View.VISIBLE);
    }

    private void getMovieData() {
        mNoInternetConnectionLinearLayout.setVisibility(View.INVISIBLE);

        if(movieId != "") {
            URL tmdbSearchUrl = NetworkUtils.buildMovieUrl(movieId);
            new MovieQueryTask().execute(tmdbSearchUrl);
        } else {
            showJsonEmptyMessage();
        }
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {
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
            Log.d("MOVIE_ID", movieId);

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
        protected void onPostExecute(String movieJson) {
            if(movieJson != "") {
                try {
                    movie = getMovieFromJson(movieJson);

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

    private void populateMovie() {
        mTitleTextView.setText(movie.getTitle());
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mOverviewTextView.setText(movie.getOverview());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mVoteAverageTextView.setText(String.valueOf(movie.getVoteAverage()));

        Picasso.with(this).load(movie.getBackDropPath()).into(mBackdropImageView);

        endLoading();
    }

    private void showNoConnectionMessage() {
        mNoInternetConnectionLinearLayout.setVisibility(View.VISIBLE);
        Toast.makeText(PopularMovieDetail.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private void showJsonEmptyMessage() {
        Toast.makeText(PopularMovieDetail.this, "No Data Available", Toast.LENGTH_SHORT).show();
    }
}
