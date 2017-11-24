package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArcadeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_info);

        Intent lastIntent = getIntent();

        final String currentUsername = lastIntent.getStringExtra("username");

        final int id = lastIntent.getIntExtra("ARCADE_ID", 0);
        final String name = lastIntent.getStringExtra("ARCADE_NAME");
        String number = lastIntent.getStringExtra("ARCADE_PHONE_NUMBER").replace(',', ' ');
        String address = lastIntent.getStringExtra("ARCADE_ADDRESS");
        String hours = lastIntent.getStringExtra("ARCADE_HOURS").replace(',', '\n');
        String info = lastIntent.getStringExtra("ARCADE_INFO");

        final TextView tvName = (TextView) findViewById(R.id.name);
        final TextView tvNumber = (TextView) findViewById(R.id.phoneNumber);
        final TextView tvAddress = (TextView) findViewById(R.id.address);
        final TextView tvHours = (TextView) findViewById(R.id.hours);
        final TextView tvInfo = (TextView) findViewById(R.id.info);
        final TextView tvAvgRating = (TextView) findViewById(R.id.avgRating);
        final TextView tvCommentsLink = (TextView) findViewById(R.id.comments);
        final Button btnSubmit = (Button) findViewById(R.id.button2);
        final RatingBar rbRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView seeGames = (TextView) findViewById(R.id.seeGames);

        tvName.setText(name);
        tvNumber.setText(number);
        tvAddress.setText(address);
        tvHours.setText(hours);
        tvInfo.setText(info);

        final ArrayList<Boolean> alreadyRated = new ArrayList<Boolean>();
        alreadyRated.add(false);

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        JSONArray jsonUsernames = jsonResponse.getJSONArray("username");
                        JSONArray jsonRatings = jsonResponse.getJSONArray("rating");

                        int numRatings = jsonRatings.length();
                        double sum = 0;

                        for(int i = 0; i < numRatings; i ++) {
                            JSONObject jsonUsername = jsonUsernames.getJSONObject(i);
                            JSONObject jsonRating = jsonRatings.getJSONObject(i);

                            String username = jsonUsername.getString("username");
                            double rating = jsonRating.getDouble("rating");

                            sum += rating;

                            if(username.equals(currentUsername)) {
                                alreadyRated.clear();
                                alreadyRated.add(true);
                            }
                        }

                        if(numRatings != 0) {
                            double avgRating = sum / numRatings;
                            avgRating = Math.round(avgRating * 10) / 10.0;
                            tvAvgRating.setText("Average rating of " + avgRating + " out of " + numRatings + " review(s).");
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArcadeInfoActivity.this);
                        builder.setMessage("Error communicating with server, try again later.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        GetRatingsRequest getRatingsRequest = new GetRatingsRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ArcadeInfoActivity.this);
        queue.add(getRatingsRequest);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!alreadyRated.get(0)) {
                    alreadyRated.clear();
                    alreadyRated.add(true);
                    Response.Listener<String> listener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    GetRatingsRequest getRatingsRequest = new GetRatingsRequest(id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(ArcadeInfoActivity.this);
                                    queue.add(getRatingsRequest);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ArcadeInfoActivity.this);
                                    builder.setMessage("Error communicating with server, try again later.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AddRatingRequest addRatingRequest = new AddRatingRequest(id, currentUsername, rbRatingBar.getRating(), listener);
                    RequestQueue queue = Volley.newRequestQueue(ArcadeInfoActivity.this);
                    queue.add(addRatingRequest);
                }
            }
        });

        tvCommentsLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArcadeInfoActivity.this, CommentActivity.class);
                intent.putExtra("ARCADE_ID", id);
                intent.putExtra("username", currentUsername);
                ArcadeInfoActivity.this.startActivity(intent);
            }
        });

        seeGames.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArcadeInfoActivity.this, ArcadeGameListActivity.class);
                intent.putExtra("ARCADE_ID", id);
                intent.putExtra("ARCADE_NAME", name);
                ArcadeInfoActivity.this.startActivity(intent);
            }
        });
    }
}