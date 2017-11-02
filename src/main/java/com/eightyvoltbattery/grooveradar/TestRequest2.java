package com.eightyvoltbattery.grooveradar;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TestRequest2 extends StringRequest {

    private Map<String, String> params;

    public TestRequest2(Response.Listener<String> listener, String url) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}