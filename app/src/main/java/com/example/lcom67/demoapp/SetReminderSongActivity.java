package com.example.lcom67.demoapp;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SetReminderSongActivity extends AppCompatActivity {

    MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder_song);

        m=MediaPlayer.create(getApplicationContext(), R.raw.alarmringtone);
        //create a folder raw inside res folder. and put 1 .mp3 song
        m.start();

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SetReminderSongActivity.this);
        dlgAlert.setTitle("Remainder !");
        dlgAlert.setMessage("Your time is up !");
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                m.stop();
                dialog.cancel();
            }
        });
        dlgAlert.show();
    }

    }
