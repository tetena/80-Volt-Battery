package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class GetExistingUserInfoRequest extends StringRequest {

    private static final String GET_EXISTING_USER_INFO_REQUEST_URL =
            "http://pharmacopoeial-game.000webhostapp.com/GetExistingUserInfo.php";

    public GetExistingUserInfoRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, GET_EXISTING_USER_INFO_REQUEST_URL, listener, null);
    }
}