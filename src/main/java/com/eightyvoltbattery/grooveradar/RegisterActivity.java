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

public class RegisterActivity extends AppCompatActivity {

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

                if(email.length() == 0 || username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
                    tvRegisterError.setText("One or more fields have been left blank! Please fill all fields.");
                } else if(username.length() > 16) {
                    tvRegisterError.setText("Username exceeds 16 characters!");
                } else if(password.length() < 8) {
                    tvRegisterError.setText("Password must be at least 8 characters!");
                } else if(!password.equals(confirmPassword)) {
                    tvRegisterError.setText("Password fields do not match!");
                } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvRegisterError.setText("The e-mail address you have provided is not valid!");
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Registration failed!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(email, username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}