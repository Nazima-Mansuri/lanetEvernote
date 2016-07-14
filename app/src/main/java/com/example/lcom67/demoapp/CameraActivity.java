package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lcom67.demoapp.Connection.DBConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    ImageView image;
    private String ImagePath;
    private long noteId;
    DBConnection helper = new DBConnection(this);
    private FrameLayout fl_main;
    private static final int CAMERA_REQUEST = 1888;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object

        toolbar.setTitle("SnapShot");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        image = (ImageView) findViewById(R.id.imagecamera);

        noteId = getIntent().getLongExtra("noteId", -1);
//        toolbar.setTitle("SnapShot");

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        if (noteId != -1) {
            try {
                File f = new File(directory, helper.getCameraImageName(noteId));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addCamera) {
            Intent ii = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(ii, CAMERA_REQUEST);

        }
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            if (noteId != -1) {
                //update
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String path = saveToInternalStorage(photo);
                    helper.updateCameraImage(noteId, path);

                    Intent ii = new Intent(CameraActivity.this, RecyclerViewActivity.class);
                    startActivity(ii);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

    }
}
