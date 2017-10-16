package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * This class interacts with the user table database by listening to the response of the
 * GetExistingUserInfo.php file specified within the class.
 */
public class GetExistingUserInfoRequest extends StringRequest {

    /** URL of the .php file that runs the user info query on the user table database */
    private static final String GET_EXISTING_USER_INFO_REQUEST_URL =
            "http://pharmacopoeial-game.000webhostapp.com/GetExistingUserInfo.php";

    /**
     * Creates a new GetExistingUserInfoRequest that holds a response listener that listens for a
     * response from the user table database
     *
     * @param listener The response listener to be carried by the user info request
     */
    public GetExistingUserInfoRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, GET_EXISTING_USER_INFO_REQUEST_URL, listener, null);
    }
}