package com.androiddevproject.foodorderingapp;

public class Products {
    private String productName, productBrand,  productImage, productId;
    float productPrice;

    public Products() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public Products(String productName, String productBrand, String productImage, String productId, float productPrice) {
        this.productName = productName;
        this.productBrand = productBrand;
        this.productImage = productImage;
        this.productId = productId;
        this.productPrice = productPrice;
    }
}
