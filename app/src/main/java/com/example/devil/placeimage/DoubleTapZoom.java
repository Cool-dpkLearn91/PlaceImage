package com.example.devil.placeimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class DoubleTapZoom extends Activity implements View.OnClickListener {

    ImageViewTouch mImage;

    Button btnSave;

    Bitmap icon;

    OutputStream output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_tap_zoom);

        mImage = (ImageViewTouch) findViewById(R.id.iv_zoom_image);

        mImage.setImageResource(R.drawable.eye);

        mImage.setDoubleTapListener(new ImageViewTouch.OnImageViewTouchDoubleTapListener() {
            @Override
            public void onDoubleTap() {

            }
        });

        btnSave = (Button) findViewById(R.id.btn_save_to_gallery);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_save_to_gallery:

                icon = BitmapFactory.decodeResource(getResources(), R.drawable.eye);

                File folder = new File("/storage/emulated/0/pichere/");


                if (!folder.exists())
                    folder.mkdir();

                File file = new File("/storage/emulated/0/pichere", "eyeimage.jpg");

                try {
                    output = new FileOutputStream(file);
                    icon.compress(Bitmap.CompressFormat.JPEG, 80, output);

                    output.flush();
                    output.close();

                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            icon, file.getName(), file.getName());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

}
