package com.eightyvoltbattery.grooveradar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ArcadeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_info);

        Intent lastIntent = getIntent();


        String name = lastIntent.getStringExtra("ARCADE_NAME");
        String number = lastIntent.getStringExtra("ARCADE_PHONE_NUMBER").replace(',', ' ');
        String address = lastIntent.getStringExtra("ARCADE_ADDRESS");
        String hours = lastIntent.getStringExtra("ARCADE_HOURS").replace(',', '\n');
        String info = lastIntent.getStringExtra("ARCADE_INFO");

        TextView tvName = (TextView) findViewById(R.id.name);
        TextView tvNumber = (TextView) findViewById(R.id.phoneNumber);
        TextView tvAddress = (TextView) findViewById(R.id.address);
        TextView tvHours = (TextView) findViewById(R.id.hours);
        TextView tvInfo = (TextView) findViewById(R.id.info);

        tvName.setText(name);
        tvNumber.setText(number);
        tvAddress.setText(address);
        tvHours.setText(hours);
        tvInfo.setText(info);
    }
}
