package com.niz.android.training.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.niz.android.training.R;
import com.niz.android.training.adapter.ImageAdapter;
import com.niz.android.training.api.model.Image;
import com.niz.android.training.service.ImageService;
import com.niz.android.training.service.ImageType;
import com.niz.android.training.util.Animations;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
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
    @InjectView(R.id.fullscreenImageView) ImageView fullscreenImageView;

    @OnClick(R.id.fullscreenImageView)
    public void onFullscreenImageClicked() {
        hideFullscreenImage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ButterKnife.inject(this);

        setActionBarTitle(getIntent());

        imageService = new ImageService();

        setupImageList();
    }

    @Override
    public void onBackPressed() {
        if (fullscreenImageView.getVisibility() == View.VISIBLE) {
            hideFullscreenImage();
        } else {
            super.onBackPressed();
        }
    }

    public static Intent getIntent(Context context, ImageType imageType) {
        Intent intent = new Intent(context, ImageListActivity.class);
        intent.putExtra(KEY_IMAGE_TYPE, imageType.getType());
        return intent;
    }

    private void setActionBarTitle(Intent intent) {
        String typeString = intent.getStringExtra(KEY_IMAGE_TYPE);
        imageType = ImageType.fromType(typeString);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(imageType.getTitle(this));
        }
    }

    private void setupImageList() {
        layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);

        imageAdapter = new ImageAdapter(ImageListActivity.this, new ImageAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadImagesWithDelay();
            }
        });
        imageAdapter.setImageClickListener(new ImageAdapter.ImageClickListener() {
            @Override
            public void onImageClicked(Image image, ImageView imageView) {
                showFullscreenImage(imageView);
            }
        });

        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setItemAnimator(new SlideInUpAnimator());
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
                imageAdapter.addImages(images, layoutManager);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void showFullscreenImage(ImageView listItemImageView) {
        fullscreenImageView.setImageDrawable(listItemImageView.getDrawable());
        Animations.slideInFromBottom(this, fullscreenImageView);
    }

    private void hideFullscreenImage() {
        Animations.slideOutToBottom(this, fullscreenImageView);
    }
}
