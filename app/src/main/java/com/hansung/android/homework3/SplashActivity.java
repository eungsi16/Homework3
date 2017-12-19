package com.hansung.android.homework3;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;



public class SplashActivity extends AppCompatActivity {

    final String TAG = "AnimationTest";
    FrameLayout mFrame;
    ImageView mand;
    ImageView mtitle;
    ImageView mCountDown;
    int mScreenHeight;
    int mScreenwidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mFrame = (FrameLayout)findViewById(R.id.activity_splash);
        mCountDown = (ImageView) findViewById(R.id.countdown);
        mtitle = (ImageView) findViewById(R.id.title);
        mand = (ImageView) findViewById(R.id.image);



    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        mScreenwidth = displaymetrics.widthPixels;


        startCountDownFrameAnimation();
        startandObjectPropertyAnimation();

    }

    private void startCountDownFrameAnimation() {
        mCountDown.setBackgroundResource(R.drawable.frame_anim);
        AnimationDrawable countdownAnim = (AnimationDrawable) mCountDown.getBackground();
        countdownAnim.start();
    }

    private void startandObjectPropertyAnimation() {
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(mand, "translationY", 0, -mScreenHeight/2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(positionAnimator);

        animatorSet.setDuration(2000);
        animatorSet.setStartDelay(2000);
        animatorSet.start();
        animatorSet.addListener(animatorListener);

    }


    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), RestaurantMapActivity.class));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), RestaurantMapActivity.class));
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.i(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };
}
