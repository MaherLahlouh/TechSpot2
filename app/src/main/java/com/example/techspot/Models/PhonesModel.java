package com.example.techspot.Models;

public class PhonesModel {
    private String phoneTitle; //Represents the title or name of the phone.

    private String phoneImage; //Represents the image resource ID of the phone.

    private String phonePrice; //Represents the price of the phone.
    //Constructor

    public PhonesModel(String phoneTitle, String phoneImage, String phonePrice) {
        this.phoneTitle = phoneTitle;
        this.phoneImage = phoneImage;
        this.phonePrice = phonePrice;
    }

    public String getPhoneTitle() {
        return phoneTitle;
    }

    public void setPhoneTitle(String phoneTitle) {
        this.phoneTitle = phoneTitle;
    }

    public String getPhoneImage() {
        return phoneImage;
    }

    public void setPhoneImage(String phoneImage) {
        this.phoneImage = phoneImage;
    }

    public String getPhonePrice() {
        return phonePrice;
    }

    public void setPhonePrice(String phonePrice) {
        this.phonePrice = phonePrice;
    }
}

