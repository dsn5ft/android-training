package com.niz.android.training.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.niz.android.training.R;

public class Animations {

    public static void slideInFromBottom(Context context, final View view) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeInAnimation);
    }

    public static void slideOutToBottom(Context context, final View view) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_to_bottom);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeOutAnimation);
    }
}
