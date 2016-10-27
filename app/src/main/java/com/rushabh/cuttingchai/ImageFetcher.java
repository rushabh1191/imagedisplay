package com.rushabh.cuttingchai;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rushabh on 27/10/16.
 */


//https://en.wikipedia.org/w/api.php?prop=pageimages&piprop=thumbnail&generator=prefixsearch&pithumbsize=50&action=query&format=json&gpssearch=food
//https://en.wikipedia.org/w/api.php?prop=pageimages&piprop=thumbnail&generator=prefixsearch&pithumbsize=50&action=query&format=json&pilimit=50&gpssearch=food

public class ImageFetcher {



    public interface ImageFetcherListener{
        void imageFetched(ArrayList<String> imageList);
        void failed();
    }

    ImageFetcherListener imageFetcherListener;
    public ImageFetcher(ImageFetcherListener imageFetcherListener){
        this.imageFetcherListener=imageFetcherListener;
    }


    public void fetchImage(String query,Context context){

        Retrofit retrofit=getRetrofitInstance(context);

        ImageFetcherService imageFetcherService=retrofit.create(ImageFetcherService.class);

        /*api.php?action=query" +
            "&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50" +
            "& pilimit=50&generator=prefixsearch";*/
        HashMap<String,String> map=new HashMap<>();
        map.put("action","query");
        map.put("prop","pageimages");
        map.put("format","json");
        map.put("piprop","thumbnail");
        map.put("pithumbsize","300");
        map.put("pilimit","50");
        map.put("generator","prefixsearch");
        map.put("gpssearch",query);

        final Call<JsonObject> imageFetched=imageFetcherService.fetchImages(map);

        imageFetched.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("beta","SUUCCESS");


                if(response.code()==200) {
                    try {
                        JsonObject responseFromServer=response.body();


                        JSONObject imageObjects
                                = new JSONObject(responseFromServer.toString()).getJSONObject("query").getJSONObject("pages");
                        Iterator keys = imageObjects.keys();

                        Log.d("beta","**** "+responseFromServer+"**");
                        ArrayList<String> listOfImages=new ArrayList<String>();
                        while (keys.hasNext()) {

                            String key= (String) keys.next();


                            JSONObject imageInfor=imageObjects.getJSONObject(key);
                            if(imageInfor.has("thumbnail")) {
                                String image = imageObjects.getJSONObject(key).
                                        getJSONObject("thumbnail").getString("source");
                                listOfImages.add(image);
                            }


                        }
                        imageFetcherListener.imageFetched(listOfImages);
                    } catch (Exception e) {
                        e.printStackTrace();
                        imageFetcherListener.failed();
                    }
                }

//                    imageList.
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("beta","FAILEEDD "+t.getMessage() );
                imageFetcherListener.failed();
            }
        });
       /* imageFetched.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200){

                    JsonObject responseFromServer=response.body();

                    JsonObject imageList
                    =responseFromServer.getAsJsonObject("query").getAsJsonObject("pages");

//                    imageList.

                }
                else{
                    imageFetcherListener.failed();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                i
            }
        });*/
    }

    public static Retrofit getRetrofitInstance(Context context) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        String url = "https://en.wikipedia.org/w/";
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}
