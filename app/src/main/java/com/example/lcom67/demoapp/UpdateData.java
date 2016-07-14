package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UpdateData extends AppCompatActivity {

    DBConnection helper = new DBConnection(this);
    Contacts contact = new Contacts();
    long noteId = -1;
    private FrameLayout flMain;
    private CanvasView customCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        flMain = (FrameLayout) findViewById(R.id.fl_main);
        noteId = getIntent().getLongExtra("noteId", -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        TextView Description = (TextView) findViewById(R.id.tDescription);
        ImageView ShowImage = (ImageView) findViewById(R.id.showImage);

        toolbar.setTitle(helper.getTitle(noteId));
        Description.setText(helper.getDescription(noteId));

        if (helper.getImageName(noteId) != null) {
            toolbar.setTitle(helper.getImageName(noteId));

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
        } else if (helper.getCameraImageName(noteId) != null) {
            toolbar.setTitle("SnapShot");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (helper.getImageName(noteId) != null) {
                Intent ii = new Intent(UpdateData.this, DrawInCanvasActivity.class);
                ii.putExtra("noteId", noteId);

                startActivity(ii);
            } else if (helper.getCameraImageName(noteId) != null) {
                Intent ii = new Intent(UpdateData.this, CameraActivity.class);
                ii.putExtra("noteId", noteId);

                startActivity(ii);
            } else {

                Intent ii = new Intent(UpdateData.this, AddNotes.class);
                ii.putExtra("noteId", noteId);
                startActivity(ii);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
