package com.example.techspot.Models;

public class AccessoriesModel {

    //Represent the title of accessories
    private String accessoriesTitle;
    //Represent the image resource id of accessories
    private String accessoriesImage;
    //Represent the price of accessories
    private String accessoriesPrice;
    //Constructor

    public AccessoriesModel(String accessoriesTitle, String accessoriesImage, String accessoriesPrice) {
        this.accessoriesTitle = accessoriesTitle;
        this.accessoriesImage = accessoriesImage;
        this.accessoriesPrice = accessoriesPrice;
    }

    public String getAccessoriesTitle() {
        return accessoriesTitle;
    }

    public void setAccessoriesTitle(String accessoriesTitle) {
        this.accessoriesTitle = accessoriesTitle;
    }

    public String getAccessoriesImage() {
        return accessoriesImage;
    }

    public void setAccessoriesImage(String accessoriesImage) {
        this.accessoriesImage = accessoriesImage;
    }

    public String getAccessoriesPrice() {
        return accessoriesPrice;
    }

    public void setAccessoriesPrice(String accessoriesPrice) {
        this.accessoriesPrice = accessoriesPrice;
    }
}

