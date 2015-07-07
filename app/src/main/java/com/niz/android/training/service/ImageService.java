package com.niz.android.training.service;

import java.util.List;

import com.niz.android.training.api.TrainingApi;
import com.niz.android.training.api.model.Image;
import retrofit.Callback;
import retrofit.RestAdapter;

public class ImageService {

    private final TrainingApi trainingApi = new RestAdapter.Builder()
            .setEndpoint("http://niz-training.ngrok.io")
//            .setEndpoint("http://10.0.3.2:8080")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()
            .create(TrainingApi.class);

    public void getImage(ImageType imageType, Callback<Image> callback) {
        trainingApi.getImage(imageType.getType(), callback);
    }

    public void getImages(ImageType imageType, Integer count, Callback<List<Image>> callback) {
        trainingApi.getImages(imageType.getType(), count, callback);
    }
}
