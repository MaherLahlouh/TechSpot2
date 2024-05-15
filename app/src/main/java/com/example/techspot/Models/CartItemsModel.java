package com.example.techspot.Models;

public class CartItemsModel {
    private String cartItemQuantity;
    private String cartItemTitle;

    private String cartItemPrice;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private int cartItemDeleteImage;

    public CartItemsModel(String cartItemQuantity, String cartItemTitle, String cartItemPrice,int cartItemDeleteImage,String key) {
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemTitle = cartItemTitle;
        this.cartItemPrice = cartItemPrice;
        this.cartItemDeleteImage = cartItemDeleteImage;
        this.key = key;
    }

    public String getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(String cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public String getCartItemTitle() {
        return cartItemTitle;
    }

    public void setCartItemTitle(String cartItemTitle) {
        this.cartItemTitle = cartItemTitle;
    }

    public String getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(String cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public int getCartItemDeleteImage() {
        return cartItemDeleteImage;
    }

    public void setCartItemDeleteImage(int cartItemDeleteImage) {
        this.cartItemDeleteImage = cartItemDeleteImage;
    }
}
