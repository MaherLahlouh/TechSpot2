package com.example.techspot.Models;

public class CommentModel {
    private String name; //Represents the name of the commenter.
    private String comment; // Represents the content of the comment.

    public CommentModel(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
