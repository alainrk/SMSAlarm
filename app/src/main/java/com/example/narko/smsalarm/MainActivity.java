package com.example.narko.smsalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button armbut;
    Button disarmBut;
    Button statusBut;
    TextView tvmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        armbut = (Button) findViewById(R.id.armBut);
        disarmBut = (Button) findViewById(R.id.disarmBut);
        statusBut = (Button) findViewById(R.id.statusBut);

        armbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(Constants.ARM_CODE);
            }
        });

        disarmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(Constants.DISARM_CODE);
            }
        });

        statusBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(Constants.STATUS_CODE);
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        String t = "Central alarm: " + sharedPref.getString(Constants.PREF_ALARMNUMBER, "NOT SET");
        tvmain = (TextView) findViewById(R.id.tvmain);
        tvmain.setText(t);
    }

    private void sendMessage(String code) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);

        //String sim_number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("sim_number", "XXXXXX");
        String sim_number = sharedPref.getString(Constants.PREF_ALARMNUMBER, "");
        String user_password = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("user_passwd", "XXXXXX");

        if (sim_number.equals("XXXXXX") || user_password.equals("XXXXXX")) {
            Toast.makeText(getApplicationContext(), "Telephone number or password not valid!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Sending code   "+code+"\nTo:   "+sim_number, Toast.LENGTH_LONG).show();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sim_number, null, user_password + code, null, null);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
