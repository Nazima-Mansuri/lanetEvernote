package com.example.lcom67.demoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    DBConnection helper = new DBConnection(this);
    EditText EmailId, Password;
    TextView Register;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Login ");

        EmailId = (EditText) findViewById(R.id.eEmailId);
        Password = (EditText) findViewById(R.id.ePassword);
        Login = (Button) findViewById(R.id.btnLogin);
        Register = (TextView) findViewById(R.id.txtRegister);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ii);
            }
        });
        Login.setOnClickListener(new View.OnClickListener()
        {
            SharedPreferences sharedpreference;

            @Override
            public void onClick(View view) {
                String username = EmailId.getText().toString();
                String Passwd = Password.getText().toString();
                if (username.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    EmailId.setError("Enter Valid Email Address");
                }
                List<Contacts> data = helper.getAllUsers(username);

                if (data.size() > 0) {
                    Contacts contact = data.get(0);
                    if (contact.getEmail().equals(username) && contact.getPasswd().equals(Passwd))
                    {
                        SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                        editor.putLong("SignUp_Id", contact.getId());
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, RecyclerViewActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Email or Password does not Match", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No User Exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isTablet(Context ctx)
    {
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
