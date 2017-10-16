package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the user table database by listening to the response of the Register.php
 * file specified within the class.
 */
public class RegisterRequest extends StringRequest {

    /** URL of the .php file that runs the register query on the user table database */
    private static final String REGISTER_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/Register.php";

    /** Strings used in the program */
    private static final String TAG_EMAIL = "email";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    /** Parameters used for the login request (in this case, email, username, and password) */
    private Map<String, String> params;

    /**
     * Creates a new RegisterRequest that holds an email, a username, a password, and a response
     * listener that listens for a response from the user table database
     *
     * @param email The email address entered by the user at the registration screen
     * @param username The username entered by the user at the registration screen
     * @param password The password entered by the user at the registration screen
     * @param listener The response listener to be carried by the register request
     */
    public RegisterRequest(String email, String username, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_EMAIL, email);
        params.put(TAG_USERNAME, username);
        params.put(TAG_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}