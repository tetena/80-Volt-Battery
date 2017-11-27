package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is where the user can view the full list of arcade games and select one to find all
 * arcades near him/her that have that game.
 */
public class GameListActivity extends AppCompatActivity {

    /** Strings used in the program */
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GAME_NAME = "game_name";
    private static final String TAG_GAME_NAMES = "game_names";
    private static final String TAG_RETRY = "Retry";

    private static final String KEY_USERNAME = "username";

    private static final String ERROR_GAME_LIST_FAILURE = "Error communicating with server, try again later.";
    private static final String SELECTED_GAME_KEY = "SELECTED_GAME_KEY";

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
                    boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                    if(success) {
                        JSONArray jsonGames = jsonResponse.getJSONArray(TAG_GAME_NAMES);

                        for(int i = 0; i < jsonGames.length(); i ++) {
                            JSONObject jsonGame = jsonGames.getJSONObject(i);
                            games.add(jsonGame.getString(TAG_GAME_NAME));
                        }

                        final List<String> gameList = new ArrayList<String>();
                        gameList.addAll(games);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GameListActivity.this, android.R.layout.simple_list_item_1, gameList);
                        lvGameList.setAdapter(arrayAdapter);
                        lvGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedGame = (String) parent.getItemAtPosition(position);
                                Intent intent = new Intent(GameListActivity.this, ArcadeListActivity.class);
                                intent.putExtra(SELECTED_GAME_KEY, selectedGame);
                                intent.putExtra(KEY_USERNAME, getIntent().getStringExtra(KEY_USERNAME));
                                GameListActivity.this.startActivity(intent);
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameListActivity.this);
                        builder.setMessage(ERROR_GAME_LIST_FAILURE)
                                .setNegativeButton(TAG_RETRY, null)
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