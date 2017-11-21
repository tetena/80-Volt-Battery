package com.eightyvoltbattery.grooveradar;

/**
 * Created by Yi (Robin) Fan on 11/19/2017.
 */

public class Comment {
    private int commentID_;
    private String comment_;
    private String userName_;
    /**
     * Creates a new Comment with the given name and address.
     *
     * @param commentID The comment's ID.
     * @param comment The commnet's content.
     * @param userName The commnet's author.
     */
    public Comment(int commentID, String comment, String userName) {
        commentID_ = commentID;
        comment_ = comment;
        userName_ = userName;
    }

    /**
     * Gets the id of this comment.
     */
    int getCommentId() { return commentID_; }

    /**
     * Gets the content of this comment.
     */
    String getComment() { return comment_; }

    /**
     * Gets the user name of this comment
     */
    String getUserName() { return userName_; }

}