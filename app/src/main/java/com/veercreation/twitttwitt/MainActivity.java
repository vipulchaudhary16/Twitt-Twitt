package com.veercreation.twitttwitt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.veercreation.twitttwitt.user.LogInSignInActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer("Timer");;
        timer.schedule(task , 1400L);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
    TimerTask task = new TimerTask() {
        public void run() {
            Intent intent = new Intent(MainActivity.this, LogInSignInActivity.class);
            startActivity(intent);
            finish();
        }
    };
}