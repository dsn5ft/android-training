package com.niz.android.training.api;

import java.util.List;

import com.niz.android.training.api.model.Image;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TrainingApi {

    @GET("/api/v1/image")
    void getImage(@Query("type") String type, Callback<Image> callback);

    @GET("/api/v1/images")
    void getImages(@Query("type") String type, @Query("count") Integer count, Callback<List<Image>> callback);
}
