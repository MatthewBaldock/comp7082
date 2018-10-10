package com.example.mb.comp7082;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_IMAGE = 2;
    protected Bitmap imageBitmap;
    protected String photoPath;
    private ImageView photoView;
    protected ArrayList<String> imageList;
    protected int currentIndex;
    protected String currentPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        photoView = findViewById(R.id.photoView);
        resetList();
    }
    public void searchClick(View view){
        Intent intent = new Intent(MainActivity.this, Search.class);
        startActivityForResult(intent, SEARCH_IMAGE);
    }
    public void uploadClick(View view)
    {
        Intent shareIntent = new Intent();
        shareIntent.setType("image/*");
        shareIntent.setAction(Intent.ACTION_SEND);
        File imageFileToShare = new File(currentPhoto);

        Uri uri = Uri.fromFile(imageFileToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Upload Image To: "));
    }
    public void rightClick(View view)
    {
        if(indexCheck((1)))
        {
            currentIndex++;
            displayList(currentIndex);
        }

    }
    public void leftClick(View view){
        if(indexCheck((-1)))
        {
            currentIndex--;
            displayList(currentIndex);
        }
    }

    public boolean indexCheck(int operator)
    {
        int checkLow = currentIndex - 1;
        int checkHigh = currentIndex + 1;
        boolean oprtnCheck = false;
        switch (operator)
        {
            case (-1):
                oprtnCheck = (checkLow >= 0);
                break;
            case (1):
                oprtnCheck = (checkHigh < imageList.size());
                break;
        }
        return oprtnCheck;
    }
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
    private void getList()
    {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.mb.comp7082/files/Pictures");
        imageList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                imageList.add(f.getPath());
            }
        }
    }
    private void resetList()
    {
        getList();
        Log.d("LIST_SIZE",""+imageList.size());
        if (imageList.size() > 0 ){
            displayList(0);
        }
    }
    private void displayList(int listIndex)
    {
        currentIndex = listIndex;
        currentPhoto = imageList.get(currentIndex);
        photoView.setImageBitmap(BitmapFactory.decodeFile(currentPhoto));
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
        Log.d("CODES","REQUEST"+requestCode+","+REQUEST_IMAGE_CAPTURE+", Result"+resultCode+","+RESULT_OK);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                photoView.setImageBitmap(imageBitmap);
        }
        if (requestCode == SEARCH_IMAGE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            sortByDate(extras.get("STARTDATE").toString(),extras.get("ENDDATE").toString());
            sortByLocation(extras.get("LATITUDE").toString()
                            ,extras.get("LONGITUDE").toString()
                            ,extras.get("RADIUS").toString());

        }
    }
    private void sortByLocation(String latParm,String lngParm,String radius)
    {
        String lat;
        String lng;
        ArrayList<String> sortedList = new ArrayList<>();
        int initialSize = imageList.size();
        double distance;
        if (radius.equals(""))
        {
            radius = "0";
        }
        if(!latParm.equals("") && !lngParm.equals(""))
        {
            String[] latList;
            String[] lngList;
            for (int index = 0; index < initialSize; index++)
            {


                ExifInterface exifData;
                try {
                    exifData = new ExifInterface(imageList.get(index));
                } catch (IOException ex) {
                    break;
                }
                lat = exifData.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                lng = exifData.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

                if (lat !=null && lng!=null && !lat.equals(null) && !lng.equals(null))
                {
                    latList = lat.split(",");
                    lngList = lng.split(",");
                    double latDbl = Double.parseDouble(latList[0].replace("/1",""))+
                            (Double.parseDouble(latList[1].replace("/1",""))/60) +
                            (Double.parseDouble(latList[2].replace("/1000",""))/3600/1000);
                    double lngDbl = Double.parseDouble(lngList[0].replace("/1",""))+
                            (Double.parseDouble(lngList[1].replace("/1",""))/60) +
                            (Double.parseDouble(lngList[2].replace("/1000",""))/3600/1000);
                    distance = Math.sqrt(Math.pow(latDbl - Double.parseDouble(latParm), 2) +
                        Math.pow(lngDbl - Double.parseDouble(lngParm), 2));

                    if (Double.compare(distance, Double.parseDouble(radius)) <= 0)
                    {
                        sortedList.add(imageList.get(index));
                    }
                }
            }
        }
        if(sortedList.size() >0)
        {
            imageList = sortedList;
            displayList(0);
        }
        else
        {
            Toast.makeText(this, "No Results By Location", Toast.LENGTH_SHORT).show();
        }
    }
    private void sortByDate(String startDate, String endDate)
    {
        Log.d("DATE","Start DAte "+startDate);
        Log.d("DATE","End date "+endDate);
        getList();
        ArrayList<String> sortedList = new ArrayList<>();
        int initialSize = imageList.size();
        String[] timestamp;
        String path = "";
        Date start;
        Date end;
        if(startDate.equals("") )
        {
            start = new Date(0);
        }
        else
        {
            start = new Date(startDate);
        }
        if(endDate.equals(""))
        {
            end = new Date();
        }
        else
        {
            end = new Date(endDate);
        }
        Date tempDate;


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd:HHmmss");
        for(int index = 0;index< initialSize;index++)
        {


            timestamp = getNameFromPath(imageList.get(index));
            Log.d("TIME",timestamp.toString());
            try{
            tempDate = format.parse(timestamp[0]+":"+timestamp[1]);
            }
            catch (ParseException ex)
            {
                break;
            }

            if(tempDate.after(start) && tempDate.before(end))
            {
                sortedList.add(imageList.get(index));
            }

            Log.d("FILES",timestamp[0]+":"+timestamp[1]);
        }
        if(sortedList.size() >0)
        {
            imageList = sortedList;
            displayList(0);
        }
        else
        {
            Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show();
            resetList();
        }

    }
    public String[] getNameFromPath(String filepath)
    {
         filepath = filepath.substring(filepath.lastIndexOf("/")+1);
         filepath = filepath.substring(filepath.indexOf("_")+1);
         filepath = filepath.replaceAll("_-[0-9]+.[a-z]+","");
         return filepath.split("_");
    }
    /*public void buttonClick(View view)
    {
        EditText edited = findViewById(R.id.edit_message);
        TextView textView = findViewById(R.id.test_message);
        textView.setText(edited.getText().toString());


    }*/
}
