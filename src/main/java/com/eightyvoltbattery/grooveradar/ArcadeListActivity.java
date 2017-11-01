package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArcadeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_list);

        final ListView lvArcadeList = (ListView) findViewById(R.id.lvArcadeList);
        final List<ArcadeEntry> arcades = new ArrayList<ArcadeEntry>();
        final List<String> addresses = new ArrayList<String>();

        Intent lastIntent = getIntent();
        String selectedGame = lastIntent.getStringExtra("SELECTED_GAME_KEY");

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        JSONArray jsonArcades = jsonResponse.getJSONArray("arcade_name");
                        JSONArray jsonAddresses = jsonResponse.getJSONArray("address");

                        for(int i = 0; i < jsonArcades.length(); i ++) {
                            JSONObject jsonArcade = jsonArcades.getJSONObject(i);
                            JSONObject jsonAddress = jsonAddresses.getJSONObject(i);

                            String arcadeName = jsonArcade.getString("arcade_name");
                            if(!arcadeName.equals("CLOSED")) {
                                String address = jsonAddress.getString("address");
                                arcades.add(new ArcadeEntry(arcadeName, address));
                            }
                        }

                        ArrayList<String> arcadeList = new ArrayList<String>();
                        for(ArcadeEntry arcadeEntry : arcades) {
                            arcadeList.add(arcadeEntry.getName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ArcadeListActivity.this, android.R.layout.simple_list_item_1, arcadeList);
                        lvArcadeList.setAdapter(arrayAdapter);
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
}