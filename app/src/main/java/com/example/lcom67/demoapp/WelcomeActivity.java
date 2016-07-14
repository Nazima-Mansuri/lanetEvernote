package com.example.lcom67.demoapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lcom67.demoapp.AdapterClass.CustomAdapter;
import com.example.lcom67.demoapp.Connection.DBConnection;
import com.example.lcom67.demoapp.Beans.Contacts;

import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "WelcomeActivity";
    private Toolbar toolbar;
    DBConnection db = new DBConnection(this);
    Contacts contact = new Contacts();
    CustomAdapter customAdapter;
    HashMap<Integer, Integer> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setTitle("Demo App");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.addNote) {
                    Intent ii = new Intent(WelcomeActivity.this, AddNotes.class);
                    startActivity(ii);
                    return true;
                }
                return false;
            }
        });
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        ListView listview = (ListView) findViewById(R.id.listview1);
//
//        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
//
//        long id = prefs.getLong("SignUp_Id",0);
//
//        Log.d(TAG, "onCreate: SignUp_Id: "+id);
//
//        final List<Contacts> data = db.getAllNotes(id);
//        customAdapter = new CustomAdapter(WelcomeActivity.this,data);
//        listview.setAdapter(customAdapter);
//
//       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           @Override
//           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(WelcomeActivity.this,UpdateData.class);
//               intent.putExtra("noteId",data.get(i).getId());
//               startActivity(intent);
//           }
//       });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Aboutus) {
            Intent intent = new Intent(WelcomeActivity.this, AboutusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Logout) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addNote) {
            Intent ii = new Intent(WelcomeActivity.this, AddNotes.class);
            startActivity(ii);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
