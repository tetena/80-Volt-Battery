package com.eightyvoltbattery.grooveradar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * This class interacts with the game table database by listening to the response of the
 * GameList.php file specified within the class.
 */
public class GameListRequest extends StringRequest {

    /** URL of the .php file that runs the user info query on the game table database */
    private static final String GAME_LIST_REQUEST_URL =
            "http://pharmacopoeial-game.000webhostapp.com/GameList.php";

    /**
     * Creates a new GameListRequest that holds a response listener that listens for a
     * response from the game table database
     *
     * @param listener The response listener to be carried by the user info request
     */
    public GameListRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, GAME_LIST_REQUEST_URL, listener, null);
    }
}