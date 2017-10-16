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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * This is where the user can create a new account that they can use to log in at the login activity
 * to proceed using the rest of the application.
 */
public class RegisterActivity extends AppCompatActivity {

    /** Important values used in the program */
    private static final int MAX_USERNAME_LENGTH = 16;
    private static final int MIN_PASSWORD_LENGTH = 8;

    /** Strings used in the program */
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_EMAILS = "emails";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_USERNAMES = "usernames";
    private static final String TAG_RETRY = "Retry";

    private static final String ERROR_BLANK_FIELD = "One or more fields have been left blank! Please fill all fields.";
    private static final String ERROR_USERNAME_LENGTH = "Username exceeds 16 characters!";
    private static final String ERROR_PASSWORD_LENGTH = "Password must be at least 8 characters!";
    private static final String ERROR_MISMATCHED_PASSWORDS = "Password fields do not match!";
    private static final String ERROR_INVALID_EMAIL = "The e-mail address you have provided is not valid!";
    private static final String ERROR_FAILED_REGISTRATION = "Registration failed!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        final EditText etRegisterUsername = (EditText) findViewById(R.id.etRegisterUsername);
        final EditText etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        final EditText etRegisterConfirmPassword = (EditText) findViewById(R.id.etRegisterConfirmPassword);
        final TextView tvRegisterError = (TextView) findViewById(R.id.tvRegisterError);
        Button btRegister = (Button) findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRegisterError.setText("");

                final String email = etRegisterEmail.getText().toString();
                final String username = etRegisterUsername.getText().toString();
                final String password = etRegisterPassword.getText().toString();
                final String confirmPassword = etRegisterConfirmPassword.getText().toString();

                if (email.length() == 0 || username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
                    tvRegisterError.setText(ERROR_BLANK_FIELD);
                } else if (username.length() > MAX_USERNAME_LENGTH) {
                    tvRegisterError.setText(ERROR_USERNAME_LENGTH);
                } else if (password.length() < MIN_PASSWORD_LENGTH) {
                    tvRegisterError.setText(ERROR_PASSWORD_LENGTH);
                } else if (!password.equals(confirmPassword)) {
                    tvRegisterError.setText(ERROR_MISMATCHED_PASSWORDS);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvRegisterError.setText(ERROR_INVALID_EMAIL);
                } else {
                    final Set<String> existingEmails = new HashSet<String>();
                    final Set<String> existingUsernames = new HashSet<String>();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                                if (success) {
                                    JSONArray jsonExistingEmails = jsonResponse.getJSONArray(TAG_EMAILS);
                                    JSONArray jsonExistingUsernames = jsonResponse.getJSONArray(TAG_USERNAMES);

                                    for (int i = 0; i < jsonExistingUsernames.length(); i++) {
                                        JSONObject jsonExistingEmail = jsonExistingEmails.getJSONObject(i);
                                        JSONObject jsonExistingUsername = jsonExistingUsernames.getJSONObject(i);

                                        existingEmails.add(jsonExistingEmail.getString(TAG_EMAIL));
                                        existingUsernames.add(jsonExistingUsername.getString(TAG_USERNAME));
                                    }

                                    if(existingEmails.contains(email)) {
                                        final String ERROR_DUPLICATE_EMAIL = "Email " + email + " is already in use!";
                                        tvRegisterError.setText(ERROR_DUPLICATE_EMAIL);
                                    } else if(existingUsernames.contains(username)) {
                                        final String ERROR_DUPLICATE_USERNAME = "Username " + username + " is already in use!";
                                        tvRegisterError.setText(ERROR_DUPLICATE_USERNAME);
                                    } else {
                                        Response.Listener<String> registrationResponseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                                                    if (success) {
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        RegisterActivity.this.startActivity(intent);
                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                        builder.setMessage(ERROR_FAILED_REGISTRATION)
                                                                .setNegativeButton(TAG_RETRY, null)
                                                                .create()
                                                                .show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        RegisterRequest registerRequest = new RegisterRequest(email, username, password, registrationResponseListener);
                                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                        queue.add(registerRequest);
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage(ERROR_FAILED_REGISTRATION)
                                            .setNegativeButton(TAG_RETRY, null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    GetExistingUserInfoRequest getExistingUserInfoRequest = new GetExistingUserInfoRequest(responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(getExistingUserInfoRequest);
                }
            }
        });
    }
}