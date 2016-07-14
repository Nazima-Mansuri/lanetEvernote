package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

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
    FloatingActionButton addNote, handwriting, camera;
    private TextView HeaderTitle;
    private TextView HeaderId;
    long id;
    private CircleImageView ProfileImage;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setTitle("Demo App");

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        DesignFAB = (FloatingActionMenu) findViewById(R.id.displayMenus);
        DesignFAB.setClosedOnTouchOutside(true);
        addNote = (FloatingActionButton) findViewById(R.id.imgNote);
        handwriting = (FloatingActionButton) findViewById(R.id.imgHandwriring);
        camera = (FloatingActionButton) findViewById(R.id.imgCamera);

        if(isTablet(this))
        {

            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        else
        {
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
            public void onClick(View view)
            {
                Intent intent = new Intent(RecyclerViewActivity.this,DisplayUserData.class);
                startActivity(intent);
            }
        });

        final List<Contacts> data = db.getAllNotes(id);
        myAdapter = new MyAdapter(RecyclerViewActivity.this, data);
        recyclerView.setAdapter(myAdapter);

        Log.d("", "List Data : " + data);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Intent intent = new Intent(RecyclerViewActivity.this, UpdateData.class);
                intent.putExtra("noteId", data.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position)
            {
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



    }

    public static boolean isTablet(Context ctx)
    {
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        HeaderTitle.setText(helper.getUserName(id));
        HeaderId.setText(helper.getEmailId(id));

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, helper.getUserName(id)+ "_" + id+ ".jpg");

        if(f.exists())
        {
                try
                {
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    ProfileImage.setImageBitmap(b);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
        }
        else
        {
            ProfileImage.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                String path = saveToInternalStorage(photo);

                Log.d("", "Image Path is : " + path);

                SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
                long id;
                id = prefs.getLong("SignUp_Id", 0);
                contact.setCameraImage(path);
                contact.setId(id);

                helper.AddNotes(contact);

                final List<Contacts> data1 = db.getAllNotes(id);
                myAdapter = new MyAdapter(RecyclerViewActivity.this, data1);
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
            Intent intent = new Intent(RecyclerViewActivity.this, LoginActivity.class);
            startActivity(intent);
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
