package com.example.lcom67.demoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lcom67.AlarmReceiver;
import com.example.lcom67.demoapp.AdapterClass.MyAdapter;
import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    private Toolbar toolbar;
    DBConnection db = new DBConnection(this);
    Contacts contact = new Contacts();
    MyAdapter myAdapter;
    private static final int CAMERA_REQUEST = 1888;
    private String ImagePath;
    DBConnection helper = new DBConnection(this);
    long noteId;
    FloatingActionMenu DesignFAB;
    FloatingActionButton addNote, handwriting, camera, reminder;
    private TextView HeaderTitle;
    private TextView HeaderId;
    long id;
    private CircleImageView ProfileImage;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    List<Contacts> data;
    private int mYear, mMonth, mDay;
    private Date date;
    private EditText textReminder;
    private ImageView reminderimage;
    private String date1, time, datetime;
    private int hour, minute;
    private Calendar c;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setTitle("Demo App");

        linearLayout = (LinearLayout) findViewById(R.id.bottom_wrapper_2);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        DesignFAB = (FloatingActionMenu) findViewById(R.id.displayMenus);
        DesignFAB.setClosedOnTouchOutside(true);
        addNote = (FloatingActionButton) findViewById(R.id.imgNote);
        handwriting = (FloatingActionButton) findViewById(R.id.imgHandwriring);
        camera = (FloatingActionButton) findViewById(R.id.imgCamera);
        reminder = (FloatingActionButton) findViewById(R.id.imgReminder);

        Intent currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();


        if (isTablet(this)) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);

        id = prefs.getLong("SignUp_Id", 0);

        View headerView = navigationView.inflateHeaderView(R.layout.header);

        HeaderTitle = (TextView) headerView.findViewById(R.id.headerTitle);
        HeaderId = (TextView) headerView.findViewById(R.id.headerId);
        ProfileImage = (CircleImageView) headerView.findViewById(R.id.profile_image);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecyclerViewActivity.this, DisplayUserData.class);
                startActivity(intent);
            }
        });

        data = db.getAllNotes(id);
        myAdapter = new MyAdapter(RecyclerViewActivity.this, data);
        recyclerView.setAdapter(myAdapter);

        recyclerView.setOnScrollListener(onScrollListener);
        Log.d("", "List Data : " + data);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
//                if(view.getId() == R.id.bottom_wrapper_2)
//                {
//                    noteId = data.get(position).getId();
//                    helper.deleteNote(noteId);
//                    data.remove(position);
//                    myAdapter.notifyDataSetChanged();
//
//
//                }
//                else
//                {
//                    Intent intent = new Intent(RecyclerViewActivity.this, UpdateData.class);
//                    intent.putExtra("noteId", data.get(position).getId());
//                    startActivity(intent);
//                }
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RecyclerViewActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete Note ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noteId = data.get(position).getId();
                        helper.deleteNote(noteId);
                        data.remove(position);
                        dialogInterface.dismiss();
                        myAdapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        }));


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DesignFAB.close(false);
                Intent intent = new Intent(RecyclerViewActivity.this, AddNotes.class);
                startActivity(intent);
            }
        });

        handwriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DesignFAB.close(false);
                Intent intent = new Intent(RecyclerViewActivity.this, DrawInCanvasActivity.class);
                startActivity(intent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DesignFAB.close(false);
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DesignFAB.close(false);
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(RecyclerViewActivity.this);
                View promptsView = li.inflate(R.layout.content_reminder, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecyclerViewActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                textReminder = (EditText) promptsView.findViewById(R.id.et_addReminder);
                reminderimage = (ImageView) promptsView.findViewById(R.id.calenderimage);

                reminderimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        // Get Current Date
                        c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        final DatePickerDialog datePickerDialog = new DatePickerDialog(RecyclerViewActivity.this,
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        if (monthOfYear == mMonth && dayOfMonth < mDay && year <= mYear)
                                        {
                                            Toast.makeText(RecyclerViewActivity.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();
                                        }
//                                        else if (monthOfYear <= mMonth && dayOfMonth == mDay && year <= mYear)
//                                        {
//                                            Toast.makeText(RecyclerViewActivity.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();
//
//                                        }
                                        else if (monthOfYear <= mMonth && dayOfMonth <= mDay && year < mYear)
                                        {
                                            Toast.makeText(RecyclerViewActivity.this, "Invalid Date" + " Please Set Comming Date", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            date1 = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                            final Calendar mcurrentTime = Calendar.getInstance();
                                            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                            minute = mcurrentTime.get(Calendar.MINUTE);

                                            TimePickerDialog mTimePicker;
                                            mTimePicker = new TimePickerDialog(RecyclerViewActivity.this, new TimePickerDialog.OnTimeSetListener()
                                            {
                                                @Override
                                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                                                {
                                                    String AM_PM = "";

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
                                                }
                                            }, hour, minute, true);//Yes 24 hour time
                                            mTimePicker.setTitle("Select Time");
                                            mTimePicker.show();
                                        }
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });



                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }

                                })
                        .setPositiveButton("SET",
                                new DialogInterface.OnClickListener()
                                {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    public void onClick(DialogInterface dialog, int id)
                                    {

                                        String reminder_title = textReminder.getText().toString();
                                        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
                                        long signupid;
                                        signupid = prefs.getLong("SignUp_Id", 0);
                                        contact = new Contacts();
                                        contact.setNotes_Title(reminder_title);

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                                        datetime = date1 + " " + time;
                                        try
                                        {
                                            date = dateFormat.parse(datetime);
                                        }
                                        catch (ParseException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        contact.setReminder_date(date);

                                        c = Calendar.getInstance();
                                        long different = date.getTime() - c.getTimeInMillis();

                                        scheduleNotification(getNotification("Alarm.."),different);
                                        Log.d("", "Date Is :  " + date);

                                        contact.setId(signupid);

                                        helper.AddNotes(contact);

                                        data = db.getAllNotes(signupid);
                                        myAdapter = new MyAdapter(RecyclerViewActivity.this, data);
                                        recyclerView.setAdapter(myAdapter);

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

    }


    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };
    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification(String content)
    {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo);
        return builder.build();
    }

    public static boolean isTablet(Context ctx) {
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onResume() {
        super.onResume();

        HeaderTitle.setText(helper.getUserName(id));
        HeaderId.setText(helper.getEmailId(id));

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, helper.getUserName(id) + "_" + id + ".jpg");

        if (f.exists()) {
            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ProfileImage.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            ProfileImage.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentdata) {

        try {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) intentdata.getExtras().get("data");
                String path = saveToInternalStorage(photo);

                Log.d("", "Image Path is : " + path);

                SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
                long id;
                id = prefs.getLong("SignUp_Id", 0);
                contact = new Contacts();
                contact.setCameraImage(path);
                contact.setId(id);

                helper.AddNotes(contact);

                data = db.getAllNotes(id);
                myAdapter = new MyAdapter(RecyclerViewActivity.this, data);
                recyclerView.setAdapter(myAdapter);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        // Create imageDir
        File mypath = new File(directory, "Snapshot_" + ts + ".jpg");

        ImagePath = mypath.getName();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return ImagePath;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Aboutus) {
            Intent intent = new Intent(RecyclerViewActivity.this, AboutusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            Intent intent = new Intent(RecyclerViewActivity.this, TabActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}

class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private RecyclerViewActivity.ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewActivity.ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
