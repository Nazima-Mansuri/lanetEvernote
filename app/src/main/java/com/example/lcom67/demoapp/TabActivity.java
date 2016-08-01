package com.example.lcom67.demoapp;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabActivity extends ActivityGroup
{

    TabHost tabhost;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("LANET EVERNOTE");
//        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.Whitetext));
//        collapsingToolbar.setBackgroundResource(R.color.Blue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LANET EVERNOTE");
        toolbar.setBackgroundResource(R.color.Blue);
        toolbar.setTitleTextColor(Color.WHITE);


        tabhost = (TabHost) findViewById(R.id.tabhost);
        tabhost.setup(this.getLocalActivityManager());

        TabHost.TabSpec logintab = tabhost.newTabSpec("Login Tab");
        TabHost.TabSpec registertab = tabhost.newTabSpec("Register Tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected

        registertab.setIndicator("Create Account");
        registertab.setContent(new Intent(TabActivity.this,MainActivity.class));

        logintab.setIndicator("Login");
        logintab.setContent(new Intent(TabActivity.this, LoginActivity.class));

        tabhost.getTabWidget().setBackgroundResource(R.color.Blue);

        /** Add the tabs  to the TabHost to display. */
        tabhost.addTab(registertab);
        tabhost.addTab(logintab);

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.Whitetext));
        }
    }

}
