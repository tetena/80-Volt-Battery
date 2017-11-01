package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetArcadeListRequest extends StringRequest {

    private static final String GET_ARCADE_LIST_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/GetArcadeList.php";

    private static final String TAG_GAME_NAME = "game_name";

    private Map<String, String> params;

    public GetArcadeListRequest(String gameName, Response.Listener<String> listener) {
        super(Request.Method.POST, GET_ARCADE_LIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_GAME_NAME, gameName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
