package com.example.techspot.Models;

public class ComputerModel {

    private String computerTitle; //Represents the title or name of the computer.

    private String computerImage; //Represents the image resource ID of the computer.

    private String computerPrice; //Represents the price of the computer.
    //Constructor

    public ComputerModel(String computerTitle, String computerImage, String computerPrice) {
        this.computerTitle = computerTitle;
        this.computerImage = computerImage;
        this.computerPrice = computerPrice;
    }

    public String getComputerTitle() {
        return computerTitle;
    }

    public void setComputerTitle(String computerTitle) {
        this.computerTitle = computerTitle;
    }

    public String getComputerImage() {
        return computerImage;
    }

    public void setComputerImage(String computerImage) {
        this.computerImage = computerImage;
    }

    public String getComputerPrice() {
        return computerPrice;
    }

    public void setComputerPrice(String computerPrice) {
        this.computerPrice = computerPrice;
    }
}

