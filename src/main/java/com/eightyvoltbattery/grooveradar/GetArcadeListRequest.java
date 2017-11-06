package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the arcade table database by listening to the response of the
 * GetArcadeList.php file specified within the class.
 */
public class GetArcadeListRequest extends StringRequest {

    /** URL of the .php file that runs the user info query on the game table database */
    private static final String GET_ARCADE_LIST_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/GetArcadeList.php";

    /** Strings used in the program */
    private static final String TAG_GAME_NAME = "game_name";

    /** Parameters used for the request (in this case, the name of the game to be searched) */
    private Map<String, String> params;

    /**
     * Creates a new GetArcadeListRequest that holds a game name and a response listener that
     * listens for a response from the arcade table database
     *
     * @param gameName The name of the game to be searched
     * @param listener The response listener to be carried by the request
     */
    public GetArcadeListRequest(String gameName, Response.Listener<String> listener) {
        super(Request.Method.POST, GET_ARCADE_LIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_GAME_NAME, gameName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
