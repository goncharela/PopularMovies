package ar.com.cotidiano.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularMovieAdapter extends ArrayAdapter<PopularMovie> {

    public PopularMovieAdapter(Activity context, List<PopularMovie> popularMovies) {
        super(context, 0, popularMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularMovie popularMovie = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.iv_movie_poster);
        Picasso.with(getContext()).load(popularMovie.getImage()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView title = convertView.findViewById(R.id.tv_movie_title);
        title.setText(popularMovie.getTitle());

        return convertView;
    }
}
