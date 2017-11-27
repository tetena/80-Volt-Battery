package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the comments database by listening to the response of the AddComment.php
 * file specified within the class.
 */
public class AddCommentRequest extends StringRequest {

    /** URL of the .php file that runs the add comment query on the comment database */
    private static final String ADD_COMMENT_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/AddComment.php";

    /** Strings used in the program */
    private static final String TAG_ARCADE_ID = "arcade_id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_COMMENT = "comment";

    /** Parameters used for the add comment request (in this case, arcade ID, username, and password) */
    private Map<String, String> params;

    /**
     * Creates a new AddCommentRequest that holds an arcadeID, username, comment, and a response listener
     * that listens for a response from the comment database
     *
     * @param arcadeID The ID number of the arcade that the user is leaving a comment on
     * @param username The username of the user
     * @param comment The comment that the user has submitted
     * @param listener The response listener to be carried by the AddCommentRequest
     */
    public AddCommentRequest(int arcadeID, String username, String comment, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_COMMENT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_ARCADE_ID, (arcadeID + ""));
        params.put(TAG_USERNAME, username);
        params.put(TAG_COMMENT, comment);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
