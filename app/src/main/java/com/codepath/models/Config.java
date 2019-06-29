package com.codepath.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    // the base url for loading images
    String imgBaseUrl;
    //the poster size to fetch
    String posterSize;

    public Config(JSONObject jsonObject) throws JSONException {
        JSONObject images = jsonObject.getJSONObject("images");
        // get the image base url
        imgBaseUrl = images.getString("secure_base_url");
        // get the poster size
        JSONArray posterSizeOptions =  images.getJSONArray("poster_sizes");
        // use the option at index 3 or w342 as the fallback
        posterSize =  posterSizeOptions.optString(3,"w342 ");
    }

    // helper method to construct urls
    public  String getImageUrl(String size, String  path) {
        return String.format("%s%s%s",imgBaseUrl,size,path); // Concatenate all the three



    }
    public String getImgBaseUrl() {
        return imgBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
