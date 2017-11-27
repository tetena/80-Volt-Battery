package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is where the user can view different information about the selected arcade, such as its phone number,
 * hours of operation, address, and a short summary about the selected arcade.
 *
 * There is also a rating bar, as well a view that displays the current average rating from users of the app.
 *
 * From this activity, the user can leave a rating, view comments by selecting the comments link, or tap on the
 * address of the arcade to get Google Maps directions to the selected arcade.
 */
public class ArcadeInfoActivity extends AppCompatActivity {

    /** Strings used in the program */
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ARCADE_ID = "ARCADE_ID";
    private static final String KEY_ARCADE_NAME = "ARCADE_NAME";
    private static final String KEY_ARCADE_PHONE_NUMBER = "ARCADE_PHONE_NUMBER";
    private static final String KEY_ARCADE_ADDRESS = "ARCADE_ADDRESS";
    private static final String KEY_ARCADE_HOURS = "ARCADE_HOURS";
    private static final String KEY_ARCADE_INFO = "ARCADE_INFO";
    private static final String KEY_LATITUDE = "USER_LOCATION_LATITUDE";
    private static final String KEY_LONGITUDE = "USER_LOCATION_LONGITUDE";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_RATING = "rating";
    private static final String TAG_RETRY = "Retry";

    private static final String ERROR_SERVER = "Error communicating with server, try again later.";

    private static final String url1 = "https://www.google.com/maps/dir/?api=1&origin=";
    private static final String url2 = "&destination=";
    private static final String url3 = "&travelmode=driving";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_info);

        Intent lastIntent = getIntent();

        final String currentUsername = lastIntent.getStringExtra(KEY_USERNAME);

        final int id = lastIntent.getIntExtra(KEY_ARCADE_ID, 0);
        final String name = lastIntent.getStringExtra(KEY_ARCADE_NAME);
        String number = lastIntent.getStringExtra(KEY_ARCADE_PHONE_NUMBER).replace(',', ' ');
        final String address = lastIntent.getStringExtra(KEY_ARCADE_ADDRESS);
        String hours = lastIntent.getStringExtra(KEY_ARCADE_HOURS).replace(',', '\n');
        String info = lastIntent.getStringExtra(KEY_ARCADE_INFO);
        final String latitude = lastIntent.getStringExtra(KEY_LATITUDE);
        final String longitude = lastIntent.getStringExtra(KEY_LONGITUDE);

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
                    boolean success = jsonResponse.getBoolean(TAG_SUCCESS);
                    if(success) {
                        JSONArray jsonUsernames = jsonResponse.getJSONArray(TAG_USERNAME);
                        JSONArray jsonRatings = jsonResponse.getJSONArray(TAG_RATING);

                        int numRatings = jsonRatings.length();
                        double sum = 0;

                        for(int i = 0; i < numRatings; i ++) {
                            JSONObject jsonUsername = jsonUsernames.getJSONObject(i);
                            JSONObject jsonRating = jsonRatings.getJSONObject(i);

                            String username = jsonUsername.getString(TAG_USERNAME);
                            double rating = jsonRating.getDouble(TAG_RATING);

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
                        builder.setMessage(ERROR_SERVER)
                                .setNegativeButton(TAG_RETRY, null)
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
                                    builder.setMessage(ERROR_SERVER)
                                            .setNegativeButton(TAG_RETRY, null)
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
                intent.putExtra(KEY_ARCADE_ID, id);
                intent.putExtra(KEY_USERNAME, currentUsername);
                ArcadeInfoActivity.this.startActivity(intent);
            }
        });

        seeGames.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArcadeInfoActivity.this, ArcadeGameListActivity.class);
                intent.putExtra(KEY_ARCADE_ID, id);
                intent.putExtra(KEY_ARCADE_NAME, name);
                ArcadeInfoActivity.this.startActivity(intent);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String origin = latitude + "," + longitude;
                String destination = address;
                destination = destination.replace("\n", "");
                destination = destination.replace(",", " ");
                destination = destination.replace(" ", "+");
                String url = url1 + origin + url2 + destination + url3;
                Intent openGoogleMaps = new Intent(Intent.ACTION_VIEW);
                openGoogleMaps.setData(Uri.parse(url));
                startActivity(openGoogleMaps);
            }
        });
    }
}