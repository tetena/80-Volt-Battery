package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ArcadeGameListRequest extends StringRequest {

    private static final String ARCADE_GAME_LIST_REQUEST_URL =
            "http://pharmacopoeial-game.000webhostapp.com/GetArcadeGames.php";

    private Map<String, String> params;

    public ArcadeGameListRequest(int id, Response.Listener<String> listener) {
        super(Request.Method.POST, ARCADE_GAME_LIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("arcade_id", id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
