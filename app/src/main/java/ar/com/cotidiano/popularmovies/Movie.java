package ar.com.cotidiano.popularmovies;

/**
 * Created by gonza on 02/10/2017.

 original title
 movie poster image thumbnail
 A plot synopsis (called overview in the api)
 user rating (called vote_average in the api)
 release date
 */

public class Movie {
    private String mTitle;
    private String mOriginalTitle;
    private String mBackDropPath;
    private String mOverview;
    private String mReleaseDate;
    private Double mVoteAverage;
    private int mId;

    public Movie(int id, String title, String originalTitle, String backDropPath, String overview, String releaseDate, Double average) {
        mTitle = title;
        mBackDropPath = backDropPath;
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = average;
        mId = id;
    }

    public Movie() {

    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setOriginalTitle(String title) {
        mOriginalTitle = title;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setBackDropPath(String backDropPath) {
        mBackDropPath = backDropPath;
    }

    public String getBackDropPath() {
        return mBackDropPath;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setVoteAverage(Double average) {
        mVoteAverage = average;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }



}
