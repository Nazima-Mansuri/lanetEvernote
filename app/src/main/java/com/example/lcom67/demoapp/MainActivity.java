package com.example.lcom67.demoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

/*
this is SIGNUP activity..
 */
public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DBConnection helper = new DBConnection(this);
    EditText name, email, pass, mobile, address;
    Button submit;
    String Name, mail, passwd, mobileno, adrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        toolbar.setTitle("Sign Up ");

        name = (EditText) findViewById(R.id.eName);
        email = (EditText) findViewById(R.id.eEmail);
        pass = (EditText) findViewById(R.id.ePasswd);
        mobile = (EditText) findViewById(R.id.eMobile);
        address = (EditText) findViewById(R.id.eAddres);
        submit = (Button) findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = name.getText().toString();
                mail = email.getText().toString();
                passwd = pass.getText().toString();
                mobileno = mobile.getText().toString();
                adrs = address.getText().toString();

                if (Name.equals("")) {
                    name.setError("Must Enter Name");
                } else if (mail.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("Please Enter Valid Email Address");
                } else if (passwd.equals("") || passwd.length() < 8) {
                    pass.setError("You must more characters in your password");
                } else if (mobileno.equals("") || mobile.length() < 10) {
                    mobile.setError("Please Enter 10 digit Mobile No.");
                } else if (adrs.equals("")) {
                    address.setError("Must Enter Address");
                } else {
                    Contacts contact = new Contacts();

                    contact.setName(Name);
                    contact.setEmail(mail);
                    contact.setPasswd(passwd);
                    contact.setMobile(mobileno);
                    contact.setAddress(adrs);

                    long transactId = helper.AddData(contact);
                    Log.d("TransacId", " : " + transactId);
                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putLong("SignUp_Id", transactId);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
