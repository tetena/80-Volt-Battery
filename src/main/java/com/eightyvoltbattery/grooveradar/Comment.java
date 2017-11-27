package com.eightyvoltbattery.grooveradar;

/**
 * A Comment object stores the data related to a single comment.
 */
public class Comment {

    private int commentID_;
    private String comment_;
    private String userName_;

    /**
     * Creates a new Comment with the given name and address.
     *
     * @param commentID The comment's ID.
     * @param comment The comment's content.
     * @param userName The comment's author.
     */
    public Comment(int commentID, String comment, String userName) {
        commentID_ = commentID;
        comment_ = comment;
        userName_ = userName;
    }

    /**
     * Returns the id of this comment.
     *
     * @return The id of this comment.
     */
    int getCommentId() {
        return commentID_;
    }

    /**
     * Returns the content of this comment.
     * @return The content of this comment.
     */
    String getComment() {
        return comment_;
    }

    /**
     * Returns the username of the user who posted this comment.
     *
     * @return The username of the user who posted this comment.
     */
    String getUserName() {
        return userName_;
    }

}