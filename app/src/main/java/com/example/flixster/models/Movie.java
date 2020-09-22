package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String posterPath;
    String movieName;
    String movieDesc;
    String backdropPath;

    public Movie(JSONObject jsonObj) throws JSONException {
        backdropPath = jsonObj.getString("backdrop_path");
        posterPath = jsonObj.getString("poster_path");
        movieName = jsonObj.getString("title");
        movieDesc = jsonObj.getString("overview");
    }

    public static List<Movie> fromJSONArray(JSONArray movieJSONArray) throws JSONException {
        List<Movie> movieList = new ArrayList<>();
        for(int i=0; i<movieJSONArray.length(); i++){
            movieList.add(new Movie(movieJSONArray.getJSONObject(i)));
        }
        return movieList;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieDesc() {
        return movieDesc;
    }
}
