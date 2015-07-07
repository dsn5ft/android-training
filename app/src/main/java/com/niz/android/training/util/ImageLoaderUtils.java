package com.niz.android.training.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import com.niz.android.training.api.model.Image;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageLoaderUtils {

    public void loadImage(Context context, final Image image, final ImageView imageView, final TextView titleView) {
        titleView.setText("...");

        Bitmap placeholderBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        placeholderBitmap.eraseColor(Color.DKGRAY);
        Drawable placeholder = new BitmapDrawable(context.getResources(), placeholderBitmap);

        Picasso.with(context)
                .load(image.getUrl())
                .placeholder(placeholder)
                .fit()
                .centerInside()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        titleView.setText(image.getTitle());
                    }

                    @Override
                    public void onError() {
                        titleView.setText("Failed");
                    }
                });
    }
}
