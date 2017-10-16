package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the user table database by listening to the response of the Login.php
 * file specified within the class.
 */
public class LoginRequest extends StringRequest {

    /** URL of the .php file that runs the login query on the user table database */
    private static final String LOGIN_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/Login.php";

    /** Strings used in the program */
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    /** Parameters used for the login request (in this case, username and password) */
    private Map<String, String> params;

    /**
     * Creates a new LoginRequest that holds a username, password, and a response listener that
     * listens for a response from the user table database
     *
     * @param username The username entered by the user at the login screen
     * @param password The password entered by the user at the login screen
     * @param listener The response listener to be carried by the login request
     */
    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_USERNAME, username);
        params.put(TAG_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}