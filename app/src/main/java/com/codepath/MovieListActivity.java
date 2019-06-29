package com.codepath;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.models.Config;
import com.codepath.models.MovieData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //Constants
    //The base url for the API
    public final static String BASE_URL_API = "https://api.themoviedb.org/3";
    //the parameter name for the api key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // Instance Variables
    AsyncHttpClient client;
    // the base url for loading images
    ArrayList<MovieData> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the adapter wired to the recycler view
    MovieAdapter adapter;
    //image config
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // Initializing the client
        client = new AsyncHttpClient();
        // Initialize list of movies
        movies = new ArrayList<>();
        // initialize the adapter - movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and the adapter
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get the configuration on app creation
        getConfiguration();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9c1f44")));
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /*
     Get the list of currently playing movies from the API
     */
     private void getNowPlaying() {
         //Create the URl
         String url = BASE_URL_API + "/movie/now_playing";
         //Set the request parameters
         RequestParams params = new RequestParams();
         params.put(API_KEY_PARAM,getString(R.string.api_key)); // API key always required
         //Executing a GET request expecting a JSON object response
         client.get(url,params, new JsonHttpResponseHandler() {


             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 //load the results into the data structure i.e the movie list
                 try {
                     JSONArray results = response.getJSONArray("results");
                     // iterate through result set and create Movie objects
                     for(int i = 0; i< results.length(); i++) {
                         MovieData movie = new MovieData(results.getJSONObject(i));
                         movies.add(movie);
                         // notify adapter that a row was added
                         adapter.notifyItemInserted(movies.size() - 1);
                     }
                     Log.i(TAG, String.format("Loaded %s movies", results.length()));
                 } catch (JSONException e) {
                     logError("Failed to parse now playing movies", e , true);
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 logError("Failed to get data from now_playing", throwable,true);
             }
         });

     }

    // get the configuration from the API
    private void getConfiguration () {
        //Create the URl
        String url = BASE_URL_API + "/configuration";
        //Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM,getString(R.string.api_key)); // API key always required
        //Executing a GET request expecting a JSON object response
        client.get(url,params, new JsonHttpResponseHandler(){

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed getting configuration", throwable,true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImgBaseUrl(), config.getPosterSize()));
                    //pass the config to the adapter
                    adapter.setConfig(config);
                    // get the list of currently playing movies.
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true );
                }
            }
        });




    }

    //handle errors, log and alert user
    protected void logError(String message, Throwable error, Boolean alertUser) {
        // Always log the error
        Log.e(TAG, message, error);
        // Also alert the user to prevent silent errors
        if (alertUser) {
            // show a long toast
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
        }

    }
}
