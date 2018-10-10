package com.example.mb.comp7082;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    public void search(final View v) {
        Intent intent = new Intent();
        String start;
        String end;

        EditText startDate = findViewById(R.id.startDate);
        EditText endDate = findViewById(R.id.endDate);
        EditText lat = findViewById(R.id.latSearch);
        EditText lng = findViewById(R.id.lngSearch);
        EditText radius = findViewById(R.id.radSearch);
        start = formatDate(startDate.getText().toString());
        end = formatDate(endDate.getText().toString());
        if(start.equals("INVALID"))
        {
            start = "";
        }
        if(end.equals("INVALID"))
        {
            end = "";
        }
        Log.d("COORDS","Long"+lng.getText().toString()+" Lat "+ lat.getText().toString()+
                " rad"+ radius.getText().toString());
        intent.putExtra("LONGITUDE",lng.getText().toString());
        intent.putExtra("LATITUDE",lat.getText().toString());
        intent.putExtra("RADIUS",radius.getText().toString());
        intent.putExtra("STARTDATE", start);
        intent.putExtra("ENDDATE", end);
        setResult(RESULT_OK, intent);
        finish();
    }
    public String formatDate(String date)
    {
        String newDate = "";
        date = date.replace("/","");
        date = date.replace(":","");
        date = date.replace("-","");
        Log.d("DATE_TEST",date.length()+"");
        for(int index = 0; index < date.length();index++)
        {
            newDate += date.charAt(index);
            Log.d("DATE_TEST",newDate);
            if(index == 3 || index==5)
            {
                newDate+="/";
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (!newDate.equals(""))
        {
            try
            {
                Date testDate = format.parse(newDate);

            }
            catch (ParseException ex)
            {
                Toast.makeText(this,"Invalid Date Entered",Toast.LENGTH_LONG).show();
                newDate = "INVALID";
            }
        }
        return newDate;
    }
    public void exit(final View v) {
        finish();
    }
}
