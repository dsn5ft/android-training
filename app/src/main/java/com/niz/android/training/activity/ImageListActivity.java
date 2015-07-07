package com.niz.android.training.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.niz.android.training.R;
import com.niz.android.training.adapter.ImageAdapter;
import com.niz.android.training.api.model.Image;
import com.niz.android.training.service.ImageService;
import com.niz.android.training.service.ImageType;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ImageListActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_TYPE = "image_type";
    private static final int SPAN_COUNT = 2;
    private static final int IMAGE_BATCH_SIZE = 10;

    private ImageType imageType = ImageType.RANDOM;
    private ImageService imageService;
    private StaggeredGridLayoutManager layoutManager;
    private ImageAdapter imageAdapter;

    @InjectView(R.id.imageRecyclerView) RecyclerView imageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ButterKnife.inject(this);

        String typeString = getIntent().getStringExtra(KEY_IMAGE_TYPE);
        imageType = ImageType.fromType(typeString);
        getSupportActionBar().setTitle(imageType.getTitle(this));

        imageService = new ImageService();

        layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);

        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(layoutManager);

        imageAdapter = new ImageAdapter(ImageListActivity.this, new ImageAdapter.LoadMoreCallback() {
            @Override
            public void onLoadMore() {
                loadImagesWithDelay();
            }
        });
        imageRecyclerView.setAdapter(imageAdapter);
    }

    public static Intent getIntent(Context context, ImageType imageType) {
        Intent intent = new Intent(context, ImageListActivity.class);
        intent.putExtra(KEY_IMAGE_TYPE, imageType.getType());
        return intent;
    }

    private void loadImagesWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadImages();
            }
        }, 2000);
    }

    private void loadImages() {
        imageService.getImages(imageType, IMAGE_BATCH_SIZE, new Callback<List<Image>>() {
            @Override
            public void success(List<Image> images, Response response) {
                imageAdapter.addImages(images);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO: launch settings page
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
