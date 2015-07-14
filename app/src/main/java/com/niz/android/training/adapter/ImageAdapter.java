package com.niz.android.training.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.niz.android.training.R;
import com.niz.android.training.api.model.Image;
import com.niz.android.training.util.ImageLoaderUtils;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ImageLoaderUtils imageLoaderUtils;
    private List<Image> imageList;
    private LoadMoreListener loadMoreListener;

    public ImageAdapter(Context context, LoadMoreListener loadMoreListener) {
        this.context = context;
        this.imageLoaderUtils = new ImageLoaderUtils();
        this.imageList = new ArrayList<>();
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public int getItemCount() {
        // add one for loading item
        return imageList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewHolderType(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderType viewHolderType = ViewHolderType.fromViewType(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(viewHolderType.getLayoutResId(), parent, false);
        return viewHolderType.getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getViewHolderType(position)) {
            case IMAGE:
                Image image = imageList.get(position);
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageLoaderUtils.loadImage(context, image, imageViewHolder.imageView, imageViewHolder.title);
                break;
            case LOADING:
                loadMoreListener.onLoadMore();
                break;
        }
    }

    public void addImages(Collection<Image> imagesToAdd, RecyclerView.LayoutManager layoutManager) {
        int initialSize = imageList.size();
        imageList.addAll(imagesToAdd);
        notifyItemInserted(initialSize);

        // workaround for known issue: https://code.google.com/p/android/issues/detail?id=174227
        if (initialSize == 0) {
            layoutManager.scrollToPosition(0);
        }
    }

    private ViewHolderType getViewHolderType(int position) {
        if (position < imageList.size()) {
            return ViewHolderType.IMAGE;
        } else {
            return ViewHolderType.LOADING;
        }
    }

    /*
     * Helper classes
     */
    private enum ViewHolderType {
        IMAGE(R.layout.image_list_item) {
            @Override
            RecyclerView.ViewHolder getViewHolder(View view) {
                return new ImageViewHolder(view);
            }
        },
        LOADING(R.layout.loading_list_item) {
            @Override
            RecyclerView.ViewHolder getViewHolder(View view) {
                return new LoadingViewHolder(view);
            }
        };

        int layoutResId;

        ViewHolderType(int layoutResId) {
            this.layoutResId = layoutResId;
        }

        public int getLayoutResId() {
            return layoutResId;
        }

        public int getViewType() {
            return ordinal();
        }

        public static ViewHolderType fromViewType(int viewType) {
            return values()[viewType];
        }

        abstract RecyclerView.ViewHolder getViewHolder(View view);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.imageView) ImageView imageView;
        @InjectView(R.id.title) TextView title;

        public ImageViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}