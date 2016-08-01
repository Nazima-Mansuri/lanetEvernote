package com.example.lcom67.demoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This activioty is used for to display data OR
 * When click recycler view this activity display data..
 */

public class UpdateData extends AppCompatActivity {

    DBConnection helper = new DBConnection(this);
    Contacts contact = new Contacts();
    long noteId = -1;
    private FrameLayout flMain;
    private Calendar c;
    private int mYear,mMonth,mDay,hour,minute;
    private String date1,time,datetime,currentDate,currentTime,AM_PM = "",currentDateTime,newCurDate,newCurTime;
    private Date date,updateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        flMain = (FrameLayout) findViewById(R.id.fl_main);
        noteId = getIntent().getLongExtra("noteId", -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        TextView Description = (TextView) findViewById(R.id.tDescription);
        ImageView ShowImage = (ImageView) findViewById(R.id.showImage);

        final ImageView reminderImage = (ImageView) findViewById(R.id.updatereminder);
        reminderImage.setVisibility(View.GONE);
        reminderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                reminderImage.performLongClick();
            }
        });
        registerForContextMenu(reminderImage);

        FloatingActionButton editData = (FloatingActionButton) findViewById(R.id.editdata);

        toolbar.setTitle(helper.getTitle(noteId));

        Description.setText(helper.getDescription(noteId));

        if (helper.getImageName(noteId) != null)
        {
            toolbar.setTitle(helper.getImageName(noteId));

            reminderImage.setVisibility(View.GONE);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            try {
                File f = new File(directory, helper.getImageName(noteId));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ShowImage.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (helper.getCameraImageName(noteId) != null)
        {
            toolbar.setTitle("SnapShot");

            reminderImage.setVisibility(View.GONE);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            try {
                File f = new File(directory, helper.getCameraImageName(noteId));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ShowImage.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(helper.getDateTime(noteId) != null)
        {
            Description.setText(helper.getDateTime(noteId));
            reminderImage.setVisibility(View.VISIBLE);
            editData.setVisibility(View.GONE);

        }


        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (view.getId() == R.id.editdata)
                {
                    if (helper.getImageName(noteId) != null) {
                        Intent ii = new Intent(UpdateData.this, DrawInCanvasActivity.class);
                        ii.putExtra("noteId", noteId);
                        startActivity(ii);
                    }
                    else if (helper.getCameraImageName(noteId) != null)
                    {
                        Intent ii = new Intent(UpdateData.this, CameraActivity.class);
                        ii.putExtra("noteId", noteId);

                        startActivity(ii);
                    }
                    else
                    {
                        Intent ii = new Intent(UpdateData.this, AddNotes.class);
                        ii.putExtra("noteId", noteId);
                        startActivity(ii);
                    }

                }
            }
        });

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),0,"Set Date");
        menu.add(0,v.getId(),0,"Clear Reminder");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle() == "Set Date" )
        {
            c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateData.this,
                    new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            if (monthOfYear == mMonth && dayOfMonth < mDay && year <= mYear)
                            {
                                Toast.makeText(UpdateData.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();
                            }
                            else if (monthOfYear <= mMonth && dayOfMonth == mDay && year <= mYear)
                            {
                                Toast.makeText(UpdateData.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();

                            }
                            else if (monthOfYear <= mMonth && dayOfMonth <= mDay && year < mYear)
                            {
                                Toast.makeText(UpdateData.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                date1 = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                final Calendar mcurrentTime = Calendar.getInstance();
                                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                minute = mcurrentTime.get(Calendar.MINUTE);

                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(UpdateData.this, new TimePickerDialog.OnTimeSetListener()
                                {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                                    {

                                        if (selectedHour > 12)
                                        {
                                            selectedHour -= 12;
                                            AM_PM = "PM";
                                        }
                                        else if (selectedHour == 0)
                                        {
                                            selectedHour += 12;
                                            AM_PM = "AM";
                                        }
                                        else if (selectedHour == 12)
                                            AM_PM = "PM";
                                        else
                                            AM_PM = "AM";

                                        time = " " + selectedHour + ":" + selectedMinute + " " + AM_PM;
                                        setnewdate();
                                        Toast.makeText(UpdateData.this, "Reminder Updated..", Toast.LENGTH_SHORT).show();
                                        Intent ii = new Intent(UpdateData.this, RecyclerViewActivity.class);
                                        startActivity(ii);

                                    }
                                }, hour, minute, true);//Yes 24 hour time
                                mTimePicker.setTitle("Select Time");
                                mTimePicker.show();
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if(item.getTitle() == "Clear Reminder")
        {
            c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");


            SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
            long signupid;
            signupid = prefs.getLong("SignUp_Id", 0);
            contact = new Contacts();

//            currentDate = " " + mYear + "-" + mMonth + "-" + mDay;

//            currentTime = " " + hour + " : " + minute + " " ;

//            currentDateTime = currentDate + " " + currentTime;
            String getDate =  dateFormat.format(c.getTime());

            try
            {
                updateDate = dateFormat.parse(getDate);
                helper.updateDateTime(noteId,updateDate);

            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            Toast.makeText(UpdateData.this, "Reminder Updated..", Toast.LENGTH_SHORT).show();
            Intent ii = new Intent(UpdateData.this, RecyclerViewActivity.class);
            startActivity(ii);

        }
        return true;
    }

    public void setnewdate()
    {
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        long signupid;
        signupid = prefs.getLong("SignUp_Id", 0);
        contact = new Contacts();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        datetime = date1 + " " + time;
        try
        {
            date = dateFormat.parse(datetime);
            helper.updateDateTime(noteId,date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

}
