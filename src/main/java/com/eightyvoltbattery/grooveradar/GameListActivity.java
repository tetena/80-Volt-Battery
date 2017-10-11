package com.eightyvoltbattery.grooveradar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        TextView tvDisplayUsername = (TextView) findViewById(R.id.tvDisplayUsername);
        tvDisplayUsername.setText(username);
    }
}