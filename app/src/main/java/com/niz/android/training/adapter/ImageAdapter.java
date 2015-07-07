package com.niz.android.training.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.common.collect.Lists;
import com.niz.android.training.R;
import com.niz.android.training.api.model.Image;
import com.niz.android.training.util.ImageLoaderUtils;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private ImageLoaderUtils imageLoaderUtils;
    private Image loadingMoreImage;
    private List<Image> imageList;
    private LoadMoreCallback loadMoreCallback;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.progressSpinner) ProgressBar progressSpinner;
        @InjectView(R.id.cardView) CardView cardView;
        @InjectView(R.id.imageView) ImageView imageView;
        @InjectView(R.id.title) TextView title;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public interface LoadMoreCallback {
        void onLoadMore();
    }

    public ImageAdapter(Context context, LoadMoreCallback loadMoreCallback) {
        this.context = context;
        this.imageLoaderUtils = new ImageLoaderUtils();
        this.loadingMoreImage = new Image();
        this.imageList = Lists.newArrayList(this.loadingMoreImage);
        this.loadMoreCallback = loadMoreCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = imageList.get(position);
        if (image == loadingMoreImage) {
            holder.progressSpinner.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
            loadMoreCallback.onLoadMore();
        } else {
            holder.progressSpinner.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            imageLoaderUtils.loadImage(context, image, holder.imageView, holder.title);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addImages(Collection<Image> images) {
        int positionToAdd = imageList.size() - 1;
        imageList.addAll(positionToAdd, images);
        if (positionToAdd == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionToAdd, images.size());
        }
    }
}