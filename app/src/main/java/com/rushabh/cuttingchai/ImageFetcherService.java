package com.rushabh.cuttingchai;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by rushabh on 27/10/16.
 */

public interface  ImageFetcherService {

    /*api.php?action=query" +
            "&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50" +
            "& pilimit=50&generator=prefixsearch";*/
    @GET("api.php")
    Call<JsonObject> fetchImages(@QueryMap Map<String, String> params);
}
