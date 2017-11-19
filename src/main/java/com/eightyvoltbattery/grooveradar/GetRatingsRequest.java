package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetRatingsRequest extends StringRequest {

    private static final String GET_RATINGS_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/GetRating.php";

    private Map<String, String> params;

    public GetRatingsRequest(int arcadeID, Response.Listener<String> listener) {
        super(Request.Method.POST, GET_RATINGS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("arcade_id", (arcadeID + ""));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
