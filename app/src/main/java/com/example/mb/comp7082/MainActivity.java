package com.example.mb.comp7082;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private String photoPath;
    private ImageView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void searchClick(View view){/*Search Photos*/}
    public void uploadClick(View view){/*Upload Photos*/}
    public void rightClick(View view){/*Navigate Right*/}
    public void leftClick(View view){/*Navigate Left*/}
    public void settingsClick(View view){/*Open Settings*/}

    public void snapClick(View view)
    {
        Intent snapPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (snapPhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("IO", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.mb.comp7082.FileProvider",
                        photoFile);
                Log.d("URI",photoURI.getPath());

                snapPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(snapPhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("DIRECTORY",""+Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = "file:" + image.getAbsolutePath();
        Log.d("PATH",photoPath);
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("TEST","I AM HERE");
        photoView = findViewById(R.id.photoView);
        Log.d("CODES","REQUEST"+requestCode+","+REQUEST_IMAGE_CAPTURE+", Result"+resultCode+","+RESULT_OK);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

          //  try {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                photoView.setImageBitmap(imageBitmap);
           // }
           // catch (IOException ioe)
          //  {
          //      ioe.printStackTrace();
          //  }
        }
    }


    /*public void buttonClick(View view)
    {
        EditText edited = findViewById(R.id.edit_message);
        TextView textView = findViewById(R.id.test_message);
        textView.setText(edited.getText().toString());


    }*/
}
