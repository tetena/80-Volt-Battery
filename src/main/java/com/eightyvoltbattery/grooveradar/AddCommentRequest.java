package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddCommentRequest extends StringRequest {

    private static final String ADD_COMMENT_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/AddComment.php";

    private Map<String, String> params;

    public AddCommentRequest(int arcadeID, String username, String comment, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_COMMENT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("arcade_id", (arcadeID + ""));
        params.put("username", username);
        params.put("comment", comment);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
