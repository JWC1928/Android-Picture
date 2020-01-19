package com.hfad.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.camera.R;


public class MainActivity extends Activity {

    private static final int CAMERA_PIC_REQUEST = 22;

    Uri cameraUri;

    Button BtnSelectImage;
    private TextView textView;
    private ImageView ImgPhoto;
    private String Camerapath ;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        ImgPhoto = (ImageView) findViewById(R.id.ImgPhoto);

        BtnSelectImage = (Button) findViewById(R.id.BtnSelectImg);

        textView2 = (TextView) findViewById(R.id.textView2);

        textView3 = (TextView) findViewById(R.id.textView3);

        textView4 = (TextView) findViewById(R.id.textView4);

        File outputFile = new File(getOutputFilePath(getRootPath()));
        if (outputFile.exists ()){
            textView4.setText(""+true);
        } else {
            textView4.setText(""+false);
        }

        BtnSelectImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Couldn't load photo", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case CAMERA_PIC_REQUEST:
                    if (resultCode == RESULT_OK) {
                        try {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");

                            ImgPhoto.setImageBitmap(photo);
                            SaveImage(photo);
                        } catch (Exception e) {
                            Toast.makeText(this, "Couldn't load photo", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }
    private void SaveImage(Bitmap finalBitmap) {


        File myDir = new File(getRootPath());
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = getOutputFilePath(myDir.getAbsolutePath());

        File file = new File (fname);

        textView.setText(myDir.getAbsolutePath());
        String outputStorageWritable = ""+isExternalStorageWritable();
        textView2.setText(outputStorageWritable);
        if (file.exists ())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public String getRootPath(){
        String root = "/sdcard/saved_images";
        return root;
    }
    public String getOutputFilePath(String rootPath){
        String fname = "Image_test.jpg";
        File file = new File (rootPath, fname);
        String outputPath = file.getAbsolutePath();
        return outputPath;
    }




    public String doesNotExist() {
        String doesNotExist = "Does not exist";
        return doesNotExist();
    }

}