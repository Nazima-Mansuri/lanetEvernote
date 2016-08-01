package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This activity is used for to DRAW in CANVAS and store that canvas as image in DATABASE..
 * And UPDATE canvas
 */
public class DrawInCanvasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DBConnection helper = new DBConnection(this);
    private CanvasView customCanvas;
    private LinearLayout root;
    Spinner spinner;
    private Button btnReset;
    public static int selectedColor = -1;
    private static final String COLOR_PREFERENCE_KEY = "color";
    Contacts contact = new Contacts();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private FrameLayout fl_main;
    String Thumbpath;
    public String ImagePath;
    ImageView imageView;
    private long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_in_canvas);
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        customCanvas = new CanvasView(this);
        fl_main.addView(customCanvas);

        spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("HandWriting");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        noteId = getIntent().getLongExtra("noteId", -1);


        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        if (noteId != -1)
        {
            try
            {
                toolbar.setTitle(helper.getImageName(noteId));
                File f = new File(directory, helper.getImageName(noteId));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        btnReset = (Button) findViewById(R.id.button1);
        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fl_main.removeView(customCanvas);
                customCanvas = new CanvasView(DrawInCanvasActivity.this);
                customCanvas.setPaintStrokeColor(selectedColor);
                fl_main.addView(customCanvas);
                imageView.setImageBitmap(null);

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        long id = spinner.getSelectedItemId();
        Log.d("", " i : " + i);

        switch (i) {
            case 0:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.BLACK).commit();
                selectedColor = Color.BLACK;


                Log.d("", "Selected Color : " + selectedColor);
                break;
            case 1:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.BLUE).commit();
                selectedColor = Color.BLUE;
                break;
            case 2:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.RED).commit();
                selectedColor = Color.RED;
                break;
            case 3:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.YELLOW).commit();
                selectedColor = Color.YELLOW;
                break;
            case 4:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.GREEN).commit();
                selectedColor = Color.GREEN;
                break;
            case 5:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.CYAN).commit();
                selectedColor = Color.CYAN;
                break;
            case 6:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.MAGENTA).commit();
                selectedColor = Color.MAGENTA;
                break;
            case 7:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.GRAY).commit();
                selectedColor = Color.GRAY;
                break;
            default:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(COLOR_PREFERENCE_KEY, Color.RED).commit();
                selectedColor = Color.RED;
                break;
        }
        if (selectedColor != -1 && customCanvas != null) {
            customCanvas.setPaintStrokeColor(selectedColor);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DrawInCanvas Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        // Create imageDir
        File mypath = new File(directory, tsLong + ".jpg");

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

    public Bitmap screenShot(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.Undo:
                customCanvas.undo();
                return true;
            case R.id.Redo:
                customCanvas.redo();
                return true;
            case R.id.save:
                if (noteId != -1)
                {
                    //update
                    try
                    {
                        String path = saveToInternalStorage(screenShot(fl_main));
                        helper.updateImage(noteId, path);

                        Intent ii = new Intent(DrawInCanvasActivity.this, RecyclerViewActivity.class);
                        startActivity(ii);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
                else
                {
                    try
                    {
                        //save
                        String path = saveToInternalStorage(customCanvas.getBitmap());

                        Log.d("", "Image Path is : " + path);

                        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
                        long id;
                        id = prefs.getLong("SignUp_Id", 0);
                        contact.setmImagePath(path);
                        contact.setId(id);

                        helper.AddNotes(contact);

                        Intent ii = new Intent(DrawInCanvasActivity.this, RecyclerViewActivity.class);
                        startActivity(ii);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

