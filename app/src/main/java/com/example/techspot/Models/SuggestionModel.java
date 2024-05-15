package com.example.techspot.Models;

public class SuggestionModel {
    private String userName;

    private int userImage;

    private float userRating;

    private String userText;

    //Constructor with parameters.
    public SuggestionModel(String userName, int userImage, float userRating, String userText) {
        this.userName = userName;
        this.userImage = userImage;
        this.userRating = userRating;
        this.userText = userText;
    }
    // Constructor to save the edit text in the suggestion activity
    public SuggestionModel(String userText) {

    }
    //Getters And Setters.
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }
}

