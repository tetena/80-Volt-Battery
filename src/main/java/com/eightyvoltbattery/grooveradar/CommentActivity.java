package com.eightyvoltbattery.grooveradar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Here, the user can view and leave comments for the selected arcade. Comments are listed by most recent first.
 */
public class CommentActivity extends AppCompatActivity {

    /** Strings used in the program */
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ARCADE_ID = "ARCADE_ID";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COMMENT_ID = "Comment_ID";
    private static final String TAG_COMMENT = "Comment";
    private static final String TAG_USERNAME = "Username";
    private static final String TAG_RETRY = "Retry";

    private static final String ERROR_SERVER = "Error communicating with server, try again later.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent lastIntent = getIntent();

        final String currentUsername = lastIntent.getStringExtra(KEY_USERNAME);
        final int id = lastIntent.getIntExtra(KEY_ARCADE_ID, 0);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);
        final ListView lv = (ListView) findViewById(R.id.lv);
        final List<Comment> comments = new ArrayList<Comment>();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                    if(success) {
                        comments.clear();

                        JSONArray jsonCommentIDs = jsonResponse.getJSONArray(TAG_COMMENT_ID);
                        JSONArray jsonComments = jsonResponse.getJSONArray(TAG_COMMENT);
                        JSONArray jsonUsernames = jsonResponse.getJSONArray(TAG_USERNAME);

                        for(int i = 0; i < jsonComments.length(); i ++){
                            JSONObject jsonCommentID = jsonCommentIDs.getJSONObject(i);
                            JSONObject jsonComment = jsonComments.getJSONObject(i);
                            JSONObject jsonUsername = jsonUsernames.getJSONObject(i);

                            int commentID = jsonCommentID.getInt(TAG_COMMENT_ID);
                            String comment = jsonComment.getString(TAG_COMMENT);
                            String username = jsonUsername.getString(TAG_USERNAME);

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
                        builder.setMessage(ERROR_SERVER)
                                .setNegativeButton(TAG_RETRY, null)
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
                            boolean success = jsonResponse.getBoolean(TAG_SUCCESS);

                            if (success) {
                                GetCommentsRequest getCommentsRequest = new GetCommentsRequest(id, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
                                queue.add(getCommentsRequest);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                                builder.setMessage(ERROR_SERVER)
                                        .setNegativeButton(TAG_RETRY, null)
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
