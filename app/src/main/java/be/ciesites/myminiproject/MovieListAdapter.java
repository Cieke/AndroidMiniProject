package be.ciesites.myminiproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.movie.MovieBasic;

import java.net.URL;
import java.util.List;

/**
 * Created by tywinlannister on 24/10/16.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Context context;
    private List<MovieBasic> movieList;


    public MovieListAdapter(Context context, List<MovieBasic> movieList) {
        this.context = context;
        this.movieList = movieList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieBasic currentMovie = movieList.get(position);
        holder.getTitleTextView().setText(currentMovie.getTitle());
        holder.getDescriptionTextView().setText(currentMovie.getOverview());

        URL imageUrl = null;
        imageUrl = AppController.getInstance().createImageUrl(currentMovie.getPosterPath(), "w185");
        ImageLoader.getInstance().displayImage(imageUrl.toString(), holder.getImageView());


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
