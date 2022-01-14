package com.veercreation.twitttwitt.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.veercreation.twitttwitt.FeedActivity;
import com.veercreation.twitttwitt.R;

import java.util.Objects;

public class LogInSignInActivity extends AppCompatActivity {
    private String username;
    private String password;

    Boolean isSigningUp = true;

    ProgressBar progressBar;
    EditText usernameEditText;
    EditText passwordEditText;
    Button signIn_signUp_button;
    TextView logInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        logInTextView = findViewById(R.id.logInText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        signIn_signUp_button = findViewById(R.id.signUpButton);
    }


    public void signUp(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (isSigningUp) {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(70);
            user.signUpInBackground(e -> {
                if(e==null ) {
                    progressBar.setProgress(100, true);
                    progressBar.setVisibility(View.INVISIBLE);
                    redirectUser();
                } else {
                    progressBar.setProgress(0);
                    usernameEditText.setError(e.getMessage());
                }
            });
        } else {
            isSigningUp = false;
            ParseUser.logInInBackground(username, password, (user, e) -> redirectUser());
        }
    }

    public void changeToLogIn(View view) {
        TextView alreadyText = findViewById(R.id.alreadyHaveAccountText);
        if (isSigningUp) {
            //user want to login
        signIn_signUp_button.setText(getString(R.string.log_in));
        isSigningUp = false;

        alreadyText.setText(getString(R.string.already_text_2));
        logInTextView.setText(R.string.sign_up);

        } else {
            //user want to signUp
            isSigningUp=true;
            signIn_signUp_button.setText(getString(R.string.sign_up));
            alreadyText.setText(getString(R.string.already_have_account));
            logInTextView.setText(getString(R.string.log_in));

        }
    }

    public  void redirectUser(){
        Intent intent = new Intent(getApplicationContext() , FeedActivity.class);
        startActivity(intent);
        finish();
    }
}