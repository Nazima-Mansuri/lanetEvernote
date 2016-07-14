package com.example.lcom67.demoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

public class AddNotes extends AppCompatActivity {

    EditText title, description;
    Button Submit;
    Contacts contact;
    DBConnection db = new DBConnection(this);
    private long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = (EditText) findViewById(R.id.eTitle_notes);
        description = (EditText) findViewById(R.id.eDescription);
        Submit = (Button) findViewById(R.id.btn_submit_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Add Notes");
        setSupportActionBar(toolbar);

        noteId = getIntent().getLongExtra("noteId", -1);
        if (noteId != -1) {
            Submit.setText("Update");
            title.setText(db.getTitle(noteId));
            description.setText(db.getDescription(noteId));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
                String str_title, str_description;
                long id;
                str_title = title.getText().toString();
                str_description = description.getText().toString();
                id = prefs.getLong("SignUp_Id", 0);
                contact = new Contacts();

                if (noteId != -1) {
                    db.updateData(noteId, str_title, str_description);
                    Toast.makeText(AddNotes.this, "Notes Updated..", Toast.LENGTH_SHORT).show();
                    Intent ii = new Intent(AddNotes.this, RecyclerViewActivity.class);
                    startActivity(ii);
                } else {
                    contact.setNotes_Title(str_title);
                    contact.setNotes_Description(str_description);
                    contact.setId(id);
                    db.AddNotes(contact);
                    Toast.makeText(AddNotes.this, "Notes Saved..", Toast.LENGTH_SHORT).show();
                    Intent ii = new Intent(AddNotes.this, RecyclerViewActivity.class);
                    startActivity(ii);

                }


            }
        });

    }
}
