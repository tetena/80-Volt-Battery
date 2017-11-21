package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent lastIntent = getIntent();

        final String currentUsername = lastIntent.getStringExtra("username");
        final int id = lastIntent.getIntExtra("ARCADE_ID", 0);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);
        final ListView lv = (ListView) findViewById(R.id.lv);
        final List<Comment> comments = new ArrayList<Comment>();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        comments.clear();

                        JSONArray jsonCommentIDs = jsonResponse.getJSONArray("Comment_ID");
                        JSONArray jsonComments = jsonResponse.getJSONArray("Comment");
                        JSONArray jsonUsernames = jsonResponse.getJSONArray("Username");

                        for(int i = 0; i < jsonComments.length(); i ++){
                            JSONObject jsonCommentID = jsonCommentIDs.getJSONObject(i);
                            JSONObject jsonComment = jsonComments.getJSONObject(i);
                            JSONObject jsonUsername = jsonUsernames.getJSONObject(i);

                            int commentID = jsonCommentID.getInt("Comment_ID");
                            String comment = jsonComment.getString("Comment");
                            String username = jsonUsername.getString("Username");

                            final Comment commentEntry = new Comment(commentID, comment, username);
                            comments.add(commentEntry);
                        }

                        List<String> commentsList = new ArrayList<String>();
                        Collections.sort(comments, new Comparator<Comment>() {

                            @Override
                            public int compare(Comment comment1, Comment comment2) {
                                if(comment1.getCommentId() < comment2.getCommentId()) {
                                    return 1;
                                }
                                return -1;
                            }
                        });

                        for(Comment c : comments) {
                            commentsList.add(c.getComment() + "\nPosted by " + c.getUserName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CommentActivity.this, android.R.layout.simple_list_item_1, commentsList);
                        lv.setAdapter(arrayAdapter);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                        builder.setMessage("Error communicating with server, try again later.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetCommentsRequest getCommentsRequest = new GetCommentsRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(getCommentsRequest);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Response.Listener<String> listener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                GetCommentsRequest getCommentsRequest = new GetCommentsRequest(id, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
                                queue.add(getCommentsRequest);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                                builder.setMessage("Error communicating with server, try again later.")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddCommentRequest addCommentRequest = new AddCommentRequest(id, currentUsername, editText.getText().toString(), listener);
                RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
                queue.add(addCommentRequest);
            }
        });
    }
}
