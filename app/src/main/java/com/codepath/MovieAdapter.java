package com.codepath;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.models.Config;
import com.codepath.models.MovieData;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<MovieData> movies;
    //config needed  for image urls
    Config config;
    //context for rendering
    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    /*
        Constructor, initialize with list
         */
    public MovieAdapter(ArrayList<MovieData> movies) {
        this.movies = movies;
    }

    // Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the movie data at the specified position
        MovieData movieData = movies.get(position);
        // Populate the view with the movie data
        holder.tvTitle.setText((movieData.getTitle()));
        holder.tvOverview.setText(movieData.getOverview());

        //build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movieData.getPosterPath());

        // load image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);


    }

    // returnds the size of the entire data set
    @Override
    public int getItemCount() {
        return movies.size();
    }


    // create the viewHolder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //track view layout
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage  = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
