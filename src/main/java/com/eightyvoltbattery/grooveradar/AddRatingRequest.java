package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRatingRequest extends StringRequest {

    private static final String ADD_RATING_REQUEST = "http://pharmacopoeial-game.000webhostapp.com/AddRating.php";

    private Map<String, String> params;

    public AddRatingRequest(int arcadeID, String username, double rating, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_RATING_REQUEST, listener, null);
        params = new HashMap<>();
        params.put("arcade_id", (arcadeID + ""));
        params.put("username", username);
        params.put("rating", (rating + ""));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
