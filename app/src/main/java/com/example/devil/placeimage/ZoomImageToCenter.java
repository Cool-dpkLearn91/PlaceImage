package com.example.devil.placeimage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.gesture.GestureOverlayView;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ZoomImageToCenter extends FragmentActivity implements View.OnClickListener {

    Animator animator;

    Animation animation, animation2;

    ImageView shortImage;
    ImageViewTouch expandedImage;

    FrameLayout layout;

    RelativeLayout rl2;

    ImageView back_arrow;

    private int mShortAnimationDuration;

    float startScaleFinal;

    Rect startBounds;
    Rect finalBounds;
    Point globalOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_to_center);

        shortImage = (ImageView) findViewById(R.id.iv_original);
        shortImage.setOnClickListener(this);

        expandedImage = (ImageViewTouch) findViewById(R.id.iv_expanded);
        expandedImage.setOnClickListener(this);

        expandedImage.setDoubleTapListener(new ImageViewTouch.OnImageViewTouchDoubleTapListener() {
            @Override
            public void onDoubleTap() {

            }
        });

        expandedImage.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                rl2.clearAnimation();
                rl2.startAnimation(animation);
                rl2.setVisibility(View.VISIBLE);
            }
        });

        layout = (FrameLayout) findViewById(R.id.container);

        rl2 = (RelativeLayout)findViewById(R.id.rl2);

        back_arrow = (ImageView)findViewById(R.id.iv_back_arrow);
        back_arrow.setOnClickListener(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_original:

                zoomImageTocenter(shortImage, R.drawable.eye);
                break;

            case R.id.iv_expanded:

                rl2.clearAnimation();
                rl2.startAnimation(animation);
                rl2.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_back_arrow:

                clickOnExpandedImage();
                break;
        }
    }


    private void clickOnExpandedImage() {

        if (animator != null) {
            animator.cancel();
        }

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(expandedImage, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(expandedImage,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImage,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImage,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shortImage.setAlpha(1f);
                expandedImage.setVisibility(View.GONE);
                animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                shortImage.setAlpha(1f);
                expandedImage.setVisibility(View.GONE);
                animator = null;
            }
        });
        set.start();
        animator = set;

        rl2.setVisibility(View.GONE);
    }

    private void zoomImageTocenter(final ImageView shortImage, int imageID) {

        if (animator != null) {
            animator.cancel();
        }

        expandedImage.setImageResource(imageID);

        startBounds = new Rect();
        finalBounds = new Rect();
        globalOffset = new Point();

        shortImage.getGlobalVisibleRect(startBounds);

        layout.getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        shortImage.setAlpha(0f);
        expandedImage.setVisibility(View.VISIBLE);

        expandedImage.setPivotX(0f);
        expandedImage.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImage,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animator = null;
            }
        });

        set.start();
        animator = set;

        startScaleFinal = startScale;

        rl2.setVisibility(View.VISIBLE);
        animation  = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        animation2  = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
        rl2.clearAnimation();
        rl2.startAnimation(animation);

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl2.setVisibility(View.GONE);
                rl2.clearAnimation();
                rl2.startAnimation(animation2);
            }
        });

    }
}
