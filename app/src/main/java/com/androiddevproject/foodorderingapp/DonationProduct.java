package com.androiddevproject.foodorderingapp;

public class DonationProduct {
    String name;
    String latStr;
    String longtStr;
    String image;
    String id;
    String categ;
    String user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatStr() {
        return latStr;
    }

    public void setLatStr(String latStr) {
        this.latStr = latStr;
    }

    public String getLongtStr() {
        return longtStr;
    }

    public void setLongtStr(String longtStr) {
        this.longtStr = longtStr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DonationProduct(String name, String latStr, String longtStr, String image, String id, String categ, String user, int quantity) {
        this.name = name;
        this.latStr = latStr;
        this.longtStr = longtStr;
        this.image = image;
        this.id = id;
        this.categ = categ;
        this.user = user;
        this.quantity = quantity;
    }

    int quantity;
}
