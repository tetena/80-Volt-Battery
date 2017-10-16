package com.eightyvoltbattery.grooveradar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * For now, this activity simply prints the user's username on the screen upon login.
 *
 * TO BE IMPLEMENTED
 */
public class GameListActivity extends AppCompatActivity {

    /** Strings used in the program */
    private static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Intent intent = getIntent();
        String username = intent.getStringExtra(TAG_USERNAME);

        TextView tvDisplayUsername = (TextView) findViewById(R.id.tvDisplayUsername);
        tvDisplayUsername.setText(username);
    }
}