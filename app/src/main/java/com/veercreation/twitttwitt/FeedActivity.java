package com.veercreation.twitttwitt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    ListView feedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedListView = findViewById(R.id.feedListView);

        setTitle(ParseUser.getCurrentUser().getUsername());

        List<Map<String , String>> tweetData = new ArrayList<>();

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


}