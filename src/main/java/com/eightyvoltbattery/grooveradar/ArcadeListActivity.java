package com.eightyvoltbattery.grooveradar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

public class ArcadeListActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_list);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();
                final ListView lvArcadeList = (ListView) findViewById(R.id.lvArcadeList);
                final List<ArcadeEntry> arcades = new ArrayList<ArcadeEntry>();

                Intent lastIntent = getIntent();
                String selectedGame = lastIntent.getStringExtra("SELECTED_GAME_KEY");

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                final JSONArray jsonArcades = jsonResponse.getJSONArray("arcade_name");
                                JSONArray jsonAddresses = jsonResponse.getJSONArray("address");
                                RequestQueue queue = Volley.newRequestQueue(ArcadeListActivity.this);
                                for(int i = 0; i < jsonArcades.length(); i ++) {
                                    JSONObject jsonArcade = jsonArcades.getJSONObject(i);
                                    JSONObject jsonAddress = jsonAddresses.getJSONObject(i);

                                    String arcadeName = jsonArcade.getString("arcade_name");
                                    if(!arcadeName.equals("CLOSED")) {
                                        String address = jsonAddress.getString("address");
                                        final ArcadeEntry entry = new ArcadeEntry(arcadeName, address);

                                        Response.Listener<String> googleMapsListener = new Response.Listener<String>() {
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    String status = jsonResponse.getString("status");

                                                    if(!status.equals("ZERO_RESULTS")) {
                                                        double sum = 0;
                                                        JSONArray routes = jsonResponse.getJSONArray("routes");

                                                        JSONObject route = routes.getJSONObject(0);
                                                        JSONArray legs = route.getJSONArray("legs");
                                                        for (int i = 0; i < legs.length(); i++) {
                                                            JSONObject leg = legs.getJSONObject(i);
                                                            JSONArray steps = leg.getJSONArray("steps");
                                                            for (int k = 0; k < steps.length(); k++) {
                                                                JSONObject step = steps.getJSONObject(k);
                                                                JSONObject distance = step.getJSONObject("distance");
                                                                sum += Integer.parseInt(distance.getString("value"));
                                                            }
                                                        }
                                                        sum /= 1609.34;
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
                                                        arcadeList.add("No results found");
                                                    }
                                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArcadeListActivity.this, android.R.layout.simple_list_item_1, arcadeList);
                                                    lvArcadeList.setAdapter(arrayAdapter);
                                                }catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        String url;
                                        String s1 = "https://maps.googleapis.com/maps/api/directions/json?origin=";
                                        String s2 = "&destination=";
                                        String s3 = "&key=AIzaSyC9JhQO9SMcWF6HTYvkBNRoNP9rvD2gnNo";
                                        String origin = latitude + "," + longitude;
                                        String destination = address;
                                        destination = destination.replace("\n", "");
                                        destination = destination.replace(",", "");
                                        destination = destination.replace(" ", "+");
                                        url = s1 + origin + s2 + destination + s3;
                                        TestRequest2 testRequest2 = new TestRequest2(googleMapsListener, url);
                                        queue.add(testRequest2);
                                    }
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ArcadeListActivity.this);
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

                GetArcadeListRequest getArcadeListRequest = new GetArcadeListRequest(selectedGame, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ArcadeListActivity.this);
                queue.add(getArcadeListRequest);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        config();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                config();
                break;
            default:
                break;
        }
    }
    private void config() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 2000, 10, locationListener);
    }
}