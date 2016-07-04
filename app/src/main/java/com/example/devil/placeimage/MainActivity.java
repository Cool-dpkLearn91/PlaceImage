package com.example.devil.placeimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = (ImageView) findViewById(R.id.activity_main_iv_eye);

        imageview.setOnClickListener(this);
    }

    private void makeItToCenter() {

        RelativeLayout root = (RelativeLayout) findViewById(R.id.rl1);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        imageview.getLocationOnScreen(originalPos);

        int xDest = dm.widthPixels / 2;
        xDest -= (imageview.getMeasuredWidth() / 2);
        int yDest = dm.heightPixels / 2 /*- (imageview.getMeasuredHeight() / 2) - statusBarOffset*/;

        TranslateAnimation anim = new TranslateAnimation(0, xDest - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(500);
        anim.setFillAfter(true);
        imageview.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        makeItToCenter();
    }
}
