package com.veercreation.twitttwitt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.veercreation.twitttwitt.user.LogInSignInActivity;
import com.veercreation.twitttwitt.user.UsersListActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ParseUser currentUser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Timer timer = new Timer("Timer");;
        timer.schedule(task , 1400L);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        currentUser = ParseUser.getCurrentUser();
    }

    TimerTask task = new TimerTask() {
        public void run() {
            if(ParseUser.getCurrentUser().getSessionToken()== null){
                Intent moveToNext;
                moveToNext = new Intent(getApplicationContext(), LogInSignInActivity.class);
                startActivity(moveToNext);
            } else {
                Intent moveToNext;
                moveToNext = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(moveToNext);
            }
            finish();
        }
    };
}