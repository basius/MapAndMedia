package com.example.basius.mapandmedia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private File f;
    static final int REQUEST_TAKE_PHOTO = 1;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    //private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.camera){
            dispatchTakePictureIntent();
        }else if(id == R.id.video){
            dispatchTakeVideoIntent();
        }else if(id == R.id.galeria){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == RESULT_OK){
                // executem l'insert
                String lat = null;
                String lon = null;
                Media media = new Media(mStorageRef.getName(), (requestCode == REQUEST_TAKE_PHOTO ? "photo" : "video"), f.getAbsolutePath(), lat, lon);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.push().setValue(media);
            }
        }
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = createImageFile(".jpg",storageDir);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                f = photoFile;
            }
        }
    }

    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                videoFile = createImageFile(".mp4",storageDir);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(videoFile));
                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        }
    }
    String mCurrentPhotoPath;
    public File createImageFile(String extencio, File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                extencio,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("DEBUG",mCurrentPhotoPath);
        return image;
    }


}
