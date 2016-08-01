package com.example.lcom67.demoapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * This is About Us Activity..
 */

public class AboutusActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("About Us");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tSetTitle = (TextView) findViewById(R.id.tSetTitle);
        TextView tDesc = (TextView) findViewById(R.id.tDesc);
        TextView VersionName = (TextView) findViewById(R.id.tVersionname);
        TextView PackageName = (TextView) findViewById(R.id.packageName);

        tSetTitle.setText("Welcome Here");
        tDesc.setText("This is created by Nazima Mansoor..");
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            String packageName = info.packageName;
//            int versionCode = info.versionCode;
            String versionName = info.versionName;

            VersionName.setText(versionName);
            PackageName.setText(packageName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
}
