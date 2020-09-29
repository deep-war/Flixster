package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.MovieActivity;
import com.example.flixster.MovieDetailActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;
    public static final String ADAPTER_TAG = "MovieAdapter";

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(ADAPTER_TAG, "onCreateViewHolder");
        // create a new view
       View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(ADAPTER_TAG, "onBindViewHolder"+position);
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the viewholder
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        RelativeLayout movieContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            movieContainer = itemView.findViewById(R.id.movieContainer);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getMovieName());
            tvOverview.setText(movie.getMovieDesc());
            String imageUrl;
            //if phone is in landscape,
            // then imageurl = backdropimage
            //else imageurl = poster image(default)
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            } else{
                imageUrl = movie.getPosterPath();
            }

           Glide.with(context).load(imageUrl).into(ivPoster);

            // 1. Register the click listener on the whole container
            movieContainer.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    //2. Navigate to a new activity on tap
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    Pair<View, String> p1 = Pair.create((View)tvTitle, "tnTitle");
                    Pair<View, String> p2 = Pair.create((View)tvOverview, "tnOverview");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, p1, p2);
                    context.startActivity(intent, options.toBundle());
                }
            });
        }
    }
}
