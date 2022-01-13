package com.veercreation.twitttwitt.user;

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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.veercreation.twitttwitt.FeedActivity;
import com.veercreation.twitttwitt.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter adapter;
    ListView userListListView;

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
                                Toast.makeText(UsersListActivity.this, "Twitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UsersListActivity.this, "twitt failed", Toast.LENGTH_SHORT).show();
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

            case R.id.view_feed:{
                Intent intent = new Intent(getApplicationContext() , FeedActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.log_out:{
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext() ,LogInSignInActivity.class);
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
        setContentView(R.layout.activity_users_list);

        setTitle("Users on Twitt-Twitt");

        userListListView = findViewById(R.id.userListListView);
        userListListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter(this , android.R.layout.simple_list_item_checked , users);
        userListListView.setAdapter(adapter);

        userListListView.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckedTextView checkedTextView = (CheckedTextView) view;
            if(checkedTextView.isChecked()){
                Log.i("Info" , "Checked!");
                ParseUser.getCurrentUser().add("isFollowing" , users.get(i));
            } else {
                Log.i("Info" , "Not checked!");
                ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                ParseUser.getCurrentUser().remove("isFollowing");
                ParseUser.getCurrentUser().put("isFollowing" , tempUsers);
            }
            ParseUser.getCurrentUser().saveInBackground();
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects.size() >0 && e == null){
                    for(ParseUser  usersList : objects ){
                        users.add(usersList.getUsername());
                    }
                    adapter.notifyDataSetChanged();

                    for(String userName : users){
                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(userName)){
                            userListListView.setItemChecked(users.indexOf(userName), true);
                        }
                    }
                }
            }
        });
    }
}