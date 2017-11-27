package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the ratings database by listening to the response of the AddRating.php
 * file specified within the class.
 */
public class AddRatingRequest extends StringRequest {

    /** URL of the .php file that runs the add rating query on the comment database */
    private static final String ADD_RATING_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/AddRating.php";

    /** Strings used in the program */
    private static final String TAG_ARCADE_ID = "arcade_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_RATING = "rating";

    /** Parameters used for the add comment request (in this case, arcade ID, username, and rating) */
    private Map<String, String> params;

    /**
     * Creates a new AddRatingRequest that holds an arcadeID, username, rating, and a response listener that
     * listens for a response from the rating database
     *
     * @param arcadeID The ID of the arcade that the user is leaving a rating for
     * @param username The username of the user
     * @param rating The rating that the user has submitted
     * @param listener The response listener to be carried by the AddRatingRequest
     */
    public AddRatingRequest(int arcadeID, String username, double rating, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_RATING_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_ARCADE_ID, (arcadeID + ""));
        params.put(TAG_USERNAME, username);
        params.put(TAG_RATING, (rating + ""));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
