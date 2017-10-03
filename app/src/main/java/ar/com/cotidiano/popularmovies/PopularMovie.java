package ar.com.cotidiano.popularmovies;

public class PopularMovie {
    private String mTitle;
    private String mImage;
    private int mId;

    public PopularMovie (String title, String image, int id) {
        mTitle = title;
        mImage = image;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public int getId() {
        return mId;
    }
}
