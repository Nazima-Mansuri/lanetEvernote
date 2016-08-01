package com.example.lcom67.demoapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

import java.security.spec.PSSParameterSpec;
import java.util.List;

/**
 * This is Login Activity..
 */

public class LoginActivity extends AppCompatActivity {

    DBConnection helper = new DBConnection(this);
    EditText EmailId, Password;
    TextView forgotPassword;
    Button Login;
    CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        toolbar.setTitle("Login ");

        EmailId = (EditText) findViewById(R.id.eEmailId);
        Password = (EditText) findViewById(R.id.ePassword);
        Login = (Button) findViewById(R.id.btnLogin);
        showPassword = (CheckBox) findViewById(R.id.cb_show_pasword);
        forgotPassword = (TextView) findViewById(R.id.tv_forgot_password);

        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Forgot Password...");
                dialog.getWindow().setTitleColor(getResources().getColor(R.color.Blue));

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_reset_password);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(showPassword.isChecked())
                {
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    Password.setInputType(129);

                    // Actual 129 is
                    // password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });
    }

    public static boolean isTablet(Context ctx)
    {
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
