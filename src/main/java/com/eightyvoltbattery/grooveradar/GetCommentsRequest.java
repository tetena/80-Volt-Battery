package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the comment table database by listening to the response of the
 * GetComment.php file specified within the class.
 */
public class GetCommentsRequest extends StringRequest {

    /** URL of the .php file that runs the get comments query on the comment table database */
    private static final String GET_COMMENTS_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/GetComment.php";

    /** Strings used in the program */
    private static final String TAG_ARCADE_ID = "arcade_id";

    /** Parameters used for the request (in this case, the arcade ID) */
    private Map<String, String> params;

    /**
     * Creates a new GetCommentsRequest that holds an arcade ID and a response listener that
     * listens for a response from the comment table database
     *
     * @param arcadeID The arcade's ID
     * @param listener The response listener to be carried by the GetRatingsRequest
     */
    public GetCommentsRequest(int arcadeID, Response.Listener<String> listener) {
        super(Request.Method.POST, GET_COMMENTS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_ARCADE_ID, (arcadeID + ""));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
