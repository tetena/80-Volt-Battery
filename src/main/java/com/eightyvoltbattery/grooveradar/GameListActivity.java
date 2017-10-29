package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GameListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        final ListView lvGameList = (ListView) findViewById(R.id.lvGameList);
        final Set<String> games = new TreeSet<String>(new Comparator<String>() {

            @Override
            public int compare(String game1, String game2) {
                String formattedGame1 = game1.replaceAll("\\s+", "").toLowerCase();
                String formattedGame2 = game2.replaceAll("\\s+", "").toLowerCase();
                return formattedGame1.compareTo(formattedGame2);
            };
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        JSONArray jsonGames = jsonResponse.getJSONArray("game_names");
                        for(int i = 0; i < jsonGames.length(); i ++) {
                            JSONObject jsonGame = jsonGames.getJSONObject(i);
                            games.add(jsonGame.getString("game_name"));
                        }
                        final List<String> gameList = new ArrayList<String>();
                        gameList.addAll(games);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GameListActivity.this, android.R.layout.simple_list_item_1, gameList);
                        lvGameList.setAdapter(arrayAdapter);
                        lvGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(GameListActivity.this, ArcadeListActivity.class);
                                GameListActivity.this.startActivity(intent);
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameListActivity.this);
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

        GameListRequest gameListRequest = new GameListRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(GameListActivity.this);
        queue.add(gameListRequest);
    }
}