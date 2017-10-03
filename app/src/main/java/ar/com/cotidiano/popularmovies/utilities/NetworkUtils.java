package ar.com.cotidiano.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String API_KEY = "api_key";
    private final static String PERSONAL_API_KEY = "PERSONAL_API_KEY";
    private final static String DEFAULT_SORT = "popular";
    private final static String PARAM_SORT = "sort_by";

    /**
     * Builds the URL used to query The Movie Database.
     *
     * @return The URL to use to query the The Movie Database.
     */
    public static URL buildUrl(String sortBy) {
        Uri builtUri;

            builtUri = Uri.parse(TMDB_BASE_URL + sortBy).buildUpon()
                    .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                    .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("URL", builtUri.toString());

        return url;
    }

    public static URL buildMovieUrl(String id) {
        Uri builtUri;

        builtUri = Uri.parse(TMDB_BASE_URL + id).buildUpon()
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getSortByDefault() {
        return DEFAULT_SORT;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
