package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class interacts with the games database by listening to the response of the GetArcadeGames.php
 * file specified within the class.
 */
public class ArcadeGameListRequest extends StringRequest {

    /** URL of the .php file that runs the arcade game list query on the games database */
    private static final String ARCADE_GAME_LIST_REQUEST_URL = "http://pharmacopoeial-game.000webhostapp.com/GetArcadeGames.php";

    /** Strings used in the program */
    private static final String TAG_ARCADE_ID = "arcade_id";

    /** Parameters used for the add comment request (in this case, arcade ID) */
    private Map<String, String> params;

    /**
     * Creates a new ArcadeGameListRequest that holds an arcade ID an a response listener that listens
     * for a response from the games database
     *
     * @param id The ID of the arcade
     * @param listener The response listener to be carried by the ArcadeGameListRequest
     */
    public ArcadeGameListRequest(int id, Response.Listener<String> listener) {
        super(Request.Method.POST, ARCADE_GAME_LIST_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(TAG_ARCADE_ID, id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
