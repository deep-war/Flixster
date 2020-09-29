package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailActivity extends YouTubeBaseActivity {

    // Removed the Youtube API KEY, since its a public repository
    private static final String YOUTUBE_API_KEY = "Your YOUTUBE API KEY";
    private static final String TAG = "MovieDetailActivity";
    private static final String VIDEO_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    RatingBar ratingBar;
    TextView tvOverview;
    YouTubePlayerView youTubePlayerView;
    TextView tvPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        tvTitle = findViewById(R.id.tvTitle);
        ratingBar = findViewById(R.id.ratingBar);
        tvOverview = findViewById(R.id.tvOverview);
        youTubePlayerView = findViewById(R.id.youtubeplayer);
        tvPopularity = findViewById(R.id.tvPopularity);

        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getMovieName());
        tvOverview.setText(movie.getMovieDesc());

        final float rating = (float)movie.getMovieRating();
        ratingBar.setRating(rating);

        double popularity = movie.getPopularity();
        tvPopularity.setText("Popularity : "+ popularity);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_API, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "ASyncHttpClient On Success");
                JSONObject jsonObj = json.jsonObject;
                try {
                    JSONArray results = jsonObj.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d(TAG, "YouTube Key:"+youtubeKey);
                    
                    initialiseYouTubeKey(youtubeKey, rating);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to Parse JSON");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "ASyncHttpClient On Failure");
            }
        });
    }

    private void initialiseYouTubeKey(final String youtubeKey, final float rating) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        Log.d(TAG, "onInitializationSuccess");

                        if(rating > 7){
                            // Bonus option: If the movie rating is greater than 7 then play the video
                            youTubePlayer.loadVideo(youtubeKey);
                        } else{
                            // Else: just load the video image and then provide an option to play
                            youTubePlayer.cueVideo(youtubeKey);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d(TAG, "onInitializationFailure");
                    }
                });
    }
}
