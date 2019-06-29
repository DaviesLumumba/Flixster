package com.codepath.models;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieData {

    //Variables tracking values coming from API
    private String title;
    private String overview;
    private String posterPath; //the API only returns a partial image path, not the full url




    /*
    Constructor, initialized from JSON data
    */
    public MovieData(JSONObject jsonObject) throws JSONException {
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
