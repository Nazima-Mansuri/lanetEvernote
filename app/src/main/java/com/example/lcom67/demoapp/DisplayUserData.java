package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom67.demoapp.Connection.DBConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserData extends AppCompatActivity {
    CircleImageView ProfileImage;
    TextView Username, EmailId;
    EditText etUserName;
    DBConnection helper = new DBConnection(this);
    String Text;
    private long id;
    private static int RESULT_LOAD_IMAGE = 1;
    private String ImagePath;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Edite Profile ");
        setSupportActionBar(toolbar);



        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        Username = (TextView) findViewById(R.id.txtUsername);
        EmailId = (TextView) findViewById(R.id.txtEmailId);
        etUserName = (EditText) findViewById(R.id.etUserName);

        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);

        id = prefs.getLong("SignUp_Id", 0);

        if(id != -1)
        {
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

            Username.setText(helper.getUserName(id));
            Log.d("","User name : " + Username);

            EmailId.setText(helper.getEmailId(id));
            Log.d("","Email Id : " + EmailId);

            etUserName.setText(helper.getUserName(id));
            Text = etUserName.getText().toString();

        }

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                Text = etUserName.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                Username.setText(Text);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(DisplayUserData.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Save or Discard Profile");
                alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        helper.updateUserName(id,Text);
                        dialogInterface.dismiss();
                        finish();

                    }
                });
                alert.setNegativeButton("DISCARD", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                alert.show();

            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            ProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        String name = helper.getUserName(id);
        // Create imageDir
        File mypath = new File(directory, name + "_" + id + ".jpg");

        ImagePath = mypath.getName();

        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            fos.close();
        }
        return ImagePath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menuSave)
        {
            try
            {
                helper.updateUserName(id,Text);

                if(picturePath!=null)
                {
                    saveToInternalStorage(BitmapFactory.decodeFile(picturePath));
                }
                else
                {
                    ProfileImage.setImageResource(R.drawable.login);
                }

                finish();
                return true;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
