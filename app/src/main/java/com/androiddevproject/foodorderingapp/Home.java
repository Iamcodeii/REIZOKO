package com.androiddevproject.foodorderingapp;

public class Home {
    String cardImage, cardTitle;

    public Home() {
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public Home(String cardImage, String cardTitle) {
        this.cardImage = cardImage;
        this.cardTitle = cardTitle;
    }
}
