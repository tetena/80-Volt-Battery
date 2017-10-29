package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Also the main activity of the application, this is the login screen.
 * Here, the user has the option of entering a username and password to login, or to go to the register
 * activity.
 */
public class LoginActivity extends AppCompatActivity {

    /** Strings used in the program */
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_RETRY = "Retry";

    private static final String ERROR_FAILED_LOGIN = "Login failed! Please make sure username/password info is correct.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);
        tvRegisterHere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        final EditText etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        final EditText etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String username = etLoginUsername.getText().toString();
                final String password = etLoginPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                            if (success) {
                                String username = jsonResponse.getString(TAG_USERNAME);

                                Intent intent = new Intent(LoginActivity.this, GameListActivity.class);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(ERROR_FAILED_LOGIN)
                                        .setNegativeButton(TAG_RETRY, null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}