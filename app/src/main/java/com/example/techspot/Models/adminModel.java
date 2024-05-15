package com.example.techspot.Models;

public class adminModel {
    String ImageURL;
    //Constructor without parameters
    public adminModel() {

    }
    //Constructor with parameters
    public adminModel(String imageURL) {
        ImageURL = imageURL;
    }
    //Getter And Setter
    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
