package com.example.techspot.Models;

public class LaptopModel  {

    private String laptopTitle; //Represents the title or name of the laptop.

    private String laptopImage; //Represents the image resource ID of the laptop.

    private String laptopPrice; //Represents the price of the laptop.
    //Constructor

    public LaptopModel(String laptopTitle, String laptopImage, String laptopPrice) {
        this.laptopTitle = laptopTitle;
        this.laptopImage = laptopImage;
        this.laptopPrice = laptopPrice;
    }

    public String getLaptopTitle() {
        return laptopTitle;
    }

    public void setLaptopTitle(String laptopTitle) {
        this.laptopTitle = laptopTitle;
    }

    public String getLaptopImage() {
        return laptopImage;
    }

    public void setLaptopImage(String laptopImage) {
        this.laptopImage = laptopImage;
    }

    public String getLaptopPrice() {
        return laptopPrice;
    }

    public void setLaptopPrice(String laptopPrice) {
        this.laptopPrice = laptopPrice;
    }
}

