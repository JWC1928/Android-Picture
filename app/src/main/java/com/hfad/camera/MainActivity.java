package com.hfad.camera;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int CAMERA_PIC_REQUEST = 22;
    //Uri cameraUri;
    Button BtnSelectImage;
    private ImageView ImgPhoto;
    private TextView outputPathView;
    private TextView storageWritableView;
    private TextView fileExistsView;
    private TextView errorView;
    private TextView listOfFilesInSavedImages;
    //private String Camerapath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorView = findViewById( R.id.error_view );
        outputPathView = findViewById(R.id.outputPathView);
        ImgPhoto = findViewById(R.id.ImgPhoto);
        BtnSelectImage = findViewById(R.id.BtnSelectImg);
        storageWritableView = findViewById(R.id.storageWritableView );
        listOfFilesInSavedImages = findViewById(R.id.listOfFilesInSavedImages);
        listOfFilesInSavedImages.setText(getFilesInSavedImages());
        storageWritableView.setText( "IsStorageWritable: " + (isExternalStorageWritable() ) );
        fileExistsView = findViewById(R.id.fileExistsView );
        File outputFile = new File(getOutputFilePath(getRootPath()));
        fileExistsView.setText( "FileExists: " + ( outputFile.exists() ? true : false ) );
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
                            //SaveImageInternal(photo);
                        } catch (Exception e) {
                            errorView.setText( e.getMessage() );
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
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File externalFilesDir = cw.getExternalFilesDir( "saved_images" );
        //File myDir = new File(getRootPath());
        //if (!myDir.exists()) myDir.mkdirs();
        if (!externalFilesDir.exists()) externalFilesDir.mkdirs();
        //outputPathView.setText( "OutputPath: " + myDir.getAbsolutePath() + "\nExists: " + myDir.exists() );
        outputPathView.setText( "OutputPath: " + externalFilesDir.getAbsolutePath() + "\nExists: " + externalFilesDir.exists() );
        //outputPathView.setText("outputPath: " + myDir.getAbsolutePath());
        //myDir.setWritable( true, false );
//        File file = new File (externalFilesDir, "AnImage.jpg");
        /*
        String fileName = getOutputFilePath(myDir.getAbsolutePath());
        File file = new File (fileName);
        boolean keepGoing = true;
       if (file.exists ()) file.delete();
       try {
           file.createNewFile();
           file = new File(fileName);
       }
       catch (Exception e){
           errorView.setText("Error Creating file: " + e.getMessage());
           keepGoing = false;
       }
         */
//       if( !keepGoing ) return;
//        try {
//            errorView.setText( "FileExists: " + file.exists() +  "\nCan read file: " + file.canRead() + "\nCan write to file: " + file.canWrite() );
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            errorView.setText( "Error"  + e.getMessage() );
//        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public String getRootPath(){
        //String root = "/sdcard/saved_images";
        //File rootDirectory = new File( Environment.getExternalStorageDirectory(), "/saved_images");
        File rootDirectory = new File( Environment.getRootDirectory(), "/scout_saved_images");
        rootDirectory.mkdirs();
        return rootDirectory.getPath();
    }
    public String getOutputFilePath(String rootPath){
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (rootPath, fname);
        String outputPath = file.getAbsolutePath();
        return outputPath;
    }
    public String getFilesInSavedImages(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File externalFilesDir = cw.getExternalFilesDir( "saved_images" );
        File[] files = externalFilesDir.listFiles();
        StringBuilder fileListBuilder = new StringBuilder();
        for( File file : files ){
            fileListBuilder.append( file.getName() + "\n" );
        }
        String textToDisplay = fileListBuilder.toString();
        return textToDisplay;
    }
}