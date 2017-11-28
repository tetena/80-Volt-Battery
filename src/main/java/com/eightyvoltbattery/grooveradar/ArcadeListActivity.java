package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This is where the user can view the full list of arcades that have their selected game,
 * sorted by closest distance to the user's current location. The user can then select any of these
 * arcades to view more information about that arcade.
 */
public class ArcadeListActivity extends AppCompatActivity {

    /** Location management tool to aid in utilizing the user's location */
    private LocationManager locationManager;

    /** String used in the program */
    private static final String TAG_SELECTED_GAME_KEY = "SELECTED_GAME_KEY";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ARCADE_NAME = "arcade_name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_CLOSED = "closed";
    private static final String TAG_STATUS = "status";
    private static final String TAG_ZERO_RESULTS = "ZERO_RESULTS";
    private static final String TAG_ROUTES = "routes";
    private static final String TAG_LEGS = "legs";
    private static final String TAG_STEPS = "steps";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_VALUE = "value";
    private static final String TAG_NO_RESULTS_FOUND = "No results found";
    private static final String TAG_RETRY = "Retry";
    private static final String TAG_ARCADE_ID = "arcade_id";
    private static final String TAG_CONTACT_NUMBER = "contact_number";
    private static final String TAG_OPEN_TIME = "open_time";
    private static final String TAG_INFORMATION = "information";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_ARCADE_ID = "ARCADE_ID";
    private static final String KEY_ARCADE_NAME = "ARCADE_NAME";
    private static final String KEY_ARCADE_PHONE_NUMBER = "ARCADE_PHONE_NUMBER";
    private static final String KEY_ARCADE_ADDRESS = "ARCADE_ADDRESS";
    private static final String KEY_ARCADE_HOURS = "ARCADE_HOURS";
    private static final String KEY_ARCADE_INFO = "ARCADE_INFO";
    private static final String KEY_LATITUDE = "USER_LOCATION_LATITUDE";
    private static final String KEY_LONGITUDE = "USER_LOCATION_LONGITUDE";

    private static final String ERROR_ARCADE_LIST_FAILURE = "Error communicating with server, try again later.";

    private static final String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    private static final String url2 = "&destination=";
    private static final String url3 = "&key=AIzaSyAITBrnJi-444ZWfK7yYeiszkJHOCH-tN8";

    /** Other important values */
    private static final double numMetersInMile = 1609.34;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_list);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(locationProvider);

        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();

        /** Hard-coded values for Rensselaer Student Union for testing on emulator */
        //final double latitude = 42.7300;
        //final double longitude = -73.6767;

        final ListView lvArcadeList = (ListView) findViewById(R.id.lvArcadeList);
        final List<ArcadeEntry> arcades = new ArrayList<ArcadeEntry>();

        Intent lastIntent = getIntent();
        String selectedGame = lastIntent.getStringExtra(TAG_SELECTED_GAME_KEY);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean(TAG_SUCCESS);
                    if(success) {
                        JSONArray jsonIds = jsonResponse.getJSONArray(TAG_ARCADE_ID);
                        final JSONArray jsonArcades = jsonResponse.getJSONArray(TAG_ARCADE_NAME);
                        JSONArray jsonPhoneNumbers = jsonResponse.getJSONArray(TAG_CONTACT_NUMBER);
                        JSONArray jsonAddresses = jsonResponse.getJSONArray(TAG_ADDRESS);
                        JSONArray jsonHours = jsonResponse.getJSONArray(TAG_OPEN_TIME);
                        JSONArray jsonInfos = jsonResponse.getJSONArray(TAG_INFORMATION);
                        RequestQueue queue = Volley.newRequestQueue(ArcadeListActivity.this);

                        for(int i = 0; i < jsonArcades.length(); i ++) {
                            JSONObject jsonId = jsonIds.getJSONObject(i);
                            JSONObject jsonArcade = jsonArcades.getJSONObject(i);
                            JSONObject jsonPhoneNumber = jsonPhoneNumbers.getJSONObject(i);
                            JSONObject jsonAddress = jsonAddresses.getJSONObject(i);
                            JSONObject jsonHour = jsonHours.getJSONObject(i);
                            JSONObject jsonInfo = jsonInfos.getJSONObject(i);

                            String arcadeName = jsonArcade.getString(TAG_ARCADE_NAME);

                            if(!arcadeName.equals(TAG_CLOSED)) {
                                int id = jsonId.getInt(TAG_ARCADE_ID);
                                String phoneNumber = jsonPhoneNumber.getString(TAG_CONTACT_NUMBER);
                                String address = jsonAddress.getString(TAG_ADDRESS);
                                String hour = jsonHour.getString(TAG_OPEN_TIME);
                                String info = jsonInfo.getString(TAG_INFORMATION);
                                final ArcadeEntry entry = new ArcadeEntry(id, arcadeName, phoneNumber, address, hour, info);

                                Response.Listener<String> googleMapsListener = new Response.Listener<String>() {

                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);

                                            String status = jsonResponse.getString(TAG_STATUS);

                                            if(!status.equals(TAG_ZERO_RESULTS)) {
                                                double sum = 0;
                                                JSONArray routes = jsonResponse.getJSONArray(TAG_ROUTES);

                                                JSONObject route = routes.getJSONObject(0);
                                                JSONArray legs = route.getJSONArray(TAG_LEGS);

                                                for (int i = 0; i < legs.length(); i++) {
                                                    JSONObject leg = legs.getJSONObject(i);
                                                    JSONArray steps = leg.getJSONArray(TAG_STEPS);

                                                    for (int k = 0; k < steps.length(); k++) {
                                                        JSONObject step = steps.getJSONObject(k);
                                                        JSONObject distance = step.getJSONObject(TAG_DISTANCE);
                                                        sum += Integer.parseInt(distance.getString(TAG_VALUE));
                                                    }
                                                }
                                                sum /= numMetersInMile;
                                                sum = Math.round(sum * 10) / 10.0;
                                                entry.setDistanceFromUser(sum);
                                                arcades.add(entry);
                                            }
                                            ArrayList<String> arcadeList = new ArrayList<String>();
                                            Collections.sort(arcades, new Comparator<ArcadeEntry>() {

                                                @Override
                                                public int compare(ArcadeEntry arcade1, ArcadeEntry arcade2) {
                                                    if(arcade1.getDistanceFromUser() > arcade2.getDistanceFromUser()) {
                                                        return 1;
                                                    }
                                                    else {
                                                        return -1;
                                                    }
                                                }
                                            });

                                            for (ArcadeEntry arcadeEntry : arcades) {
                                                arcadeList.add(arcadeEntry.getName() + "\n" + arcadeEntry.getDistanceFromUser() + " miles");
                                            }
                                            if(arcades.size() == 0) {
                                                arcadeList.add(TAG_NO_RESULTS_FOUND);
                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArcadeListActivity.this, android.R.layout.simple_list_item_1, arcadeList);
                                            lvArcadeList.setAdapter(arrayAdapter);
                                            lvArcadeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    String selectedArcadeEntry = (String) parent.getItemAtPosition(position);
                                                    if(!selectedArcadeEntry.equals(TAG_NO_RESULTS_FOUND)) {
                                                        String[] data = selectedArcadeEntry.split("\n", 0);
                                                        String name = data[0];
                                                        double distance = Double.parseDouble(data[1].replace(" miles", ""));
                                                        for (int i = 0; i < arcades.size(); i++) {
                                                            if (arcades.get(i).getName().equals(name) && arcades.get(i).getDistanceFromUser() == distance) {
                                                                Intent intent = new Intent(ArcadeListActivity.this, ArcadeInfoActivity.class);
                                                                intent.putExtra(KEY_ARCADE_ID, arcades.get(i).getId());
                                                                intent.putExtra(KEY_ARCADE_NAME, arcades.get(i).getName());
                                                                intent.putExtra(KEY_ARCADE_PHONE_NUMBER, arcades.get(i).getPhoneNumber());
                                                                intent.putExtra(KEY_ARCADE_ADDRESS, arcades.get(i).getAddress());
                                                                intent.putExtra(KEY_ARCADE_HOURS, arcades.get(i).getHours());
                                                                intent.putExtra(KEY_ARCADE_INFO, arcades.get(i).getInfo());
                                                                intent.putExtra(KEY_USERNAME, getIntent().getStringExtra(KEY_USERNAME));
                                                                intent.putExtra(KEY_LATITUDE, Double.toString(latitude));
                                                                intent.putExtra(KEY_LONGITUDE, Double.toString(longitude));
                                                                ArcadeListActivity.this.startActivity(intent);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                String url;
                                String origin = latitude + "," + longitude;
                                String destination = address;
                                destination = destination.replace("\n", "");
                                destination = destination.replace(",", "");
                                destination = destination.replace(" ", "+");
                                url = url1 + origin + url2 + destination + url3;
                                GoogleMapsRequest googleMapsRequest = new GoogleMapsRequest(url, googleMapsListener);
                                queue.add(googleMapsRequest);
                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArcadeListActivity.this);
                        builder.setMessage(ERROR_ARCADE_LIST_FAILURE)
                                .setNegativeButton(TAG_RETRY, null)
                                .create()
                                .show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetArcadeListRequest getArcadeListRequest = new GetArcadeListRequest(selectedGame, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ArcadeListActivity.this);
        queue.add(getArcadeListRequest);
    }
}