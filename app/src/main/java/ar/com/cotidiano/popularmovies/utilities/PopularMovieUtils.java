package ar.com.cotidiano.popularmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.HttpURLConnection;

import ar.com.cotidiano.popularmovies.Movie;
import ar.com.cotidiano.popularmovies.PopularMovie;

public class PopularMovieUtils {
    private final static String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    private final static String BACKDROP_PATH = "http://image.tmdb.org/t/p/w780/";

    public static PopularMovie[] getMovieListFromJson(String popularMoviesJsonStr)
        throws JSONException {

        final String MOVIE_LIST = "results";
        final String TITLE = "title";
        final String POSTER = "poster_path";
        final String OWM_MESSAGE_CODE = "status_code";

        PopularMovie[] popularMovie = null;

        JSONObject popularMovieJson = new JSONObject(popularMoviesJsonStr);

        JSONArray moviesArray = popularMovieJson.getJSONArray(MOVIE_LIST);

        if(moviesArray != null) {
            popularMovie = new PopularMovie[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                String title;
                String poster_path;
                int id;

                JSONObject movie = moviesArray.getJSONObject(i);

                title = movie.getString(TITLE);
                poster_path = POSTER_PATH + movie.getString(POSTER);
                id = movie.getInt("id");

                popularMovie[i] = new PopularMovie(title, poster_path, id);
            }
        }

        return popularMovie;
    }

    public static Movie getMovieFromJson(String movieJsonStr)
        throws JSONException {
        Movie movie = null;

        if(movieJsonStr != "") {
            String json = "{\"movie\": " + movieJsonStr + "}";
            JSONObject movieJson = new JSONObject(json);
            JSONObject movieInfo = movieJson.getJSONObject("movie");

            movie = new Movie();
            movie.setId(movieInfo.getInt("id"));
            movie.setTitle(movieInfo.getString("title"));
            movie.setOriginalTitle(movieInfo.getString("original_title"));
            movie.setOverview(movieInfo.getString("overview"));
            movie.setBackDropPath(BACKDROP_PATH + movieInfo.getString("backdrop_path"));
            movie.setVoteAverage(movieInfo.getDouble("vote_average"));
            movie.setReleaseDate(movieInfo.getString("release_date"));
        }

        return movie;
    }
}
