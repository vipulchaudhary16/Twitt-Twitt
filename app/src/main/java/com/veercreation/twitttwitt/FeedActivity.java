package com.veercreation.twitttwitt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.veercreation.twitttwitt.user.LogInSignInActivity;
import com.veercreation.twitttwitt.user.UsersListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    ListView feedListView;
    List<Map<String , String>> tweetData = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tweet:{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Send a Twitt");

                EditText tweetEditText = new EditText(this);

                builder.setView(tweetEditText);

                builder.setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("tweet" , tweetEditText.getText().toString());
                        ParseObject tweet = new ParseObject("Tweet");
                        tweet.put("tweet" ,tweetEditText.getText().toString());
                        tweet.put("username" , ParseUser.getCurrentUser().getUsername());
                        tweet.saveInBackground(e -> {
                            if(e==null){
                                Toast.makeText(FeedActivity.this, "Twitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FeedActivity.this, "twitt failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("tweet" , "Canceled");
                        dialogInterface.cancel();
                    }
                });

                builder.show();
                return true;
            }

            case R.id.users_list:{
                Intent intent = new Intent(getApplicationContext() , UsersListActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.log_out:{
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext() , LogInSignInActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        feedListView = findViewById(R.id.feedListView);
        setTitle(ParseUser.getCurrentUser().getUsername());
    }

    public void loadFeed(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username" , ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject tweet : objects){
                        Map<String , String> tweetInfo = new HashMap<>();
                        tweetInfo.put("content" , tweet.getString("tweet"));
                        tweetInfo.put("username" , tweet.getString("username"));
                        tweetData.add(tweetInfo);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(FeedActivity.this ,tweetData , android.R.layout.simple_list_item_2 , new String[] {"content" , "username"} , new int[]{android.R.id.text1 , android.R.id.text2 });

                    feedListView.setAdapter(simpleAdapter);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFeed();
    }
}