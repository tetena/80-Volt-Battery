package com.eightyvoltbattery.grooveradar;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with Google Maps by listening to the response of the
 * search URL constructed by the coordinates of the user and their selection.
 */
public class GoogleMapsRequest extends StringRequest {

    /** Parameters used for the Google Maps request (in this case, the search URL) */
    private Map<String, String> params;

    /**
     * Creates a new GoogleMapsRequest that holds the search url and a response listener that listens
     * for a response from Google Maps
     *
     * @param url The Google search URL
     * @param listener The response listener to be carried by the GoogleMapsRequest
     */
    public GoogleMapsRequest(String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}