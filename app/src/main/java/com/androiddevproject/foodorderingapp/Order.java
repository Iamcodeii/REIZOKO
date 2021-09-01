package com.androiddevproject.foodorderingapp;

public class Order {
    private String productName, productImage,productId;
    private float  productPrice,  priceTotal;
    private long productQty;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public float getPriceTotal() {
        return priceTotal;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductQty() {
        return productQty;
    }

    public void setProductQty(long productQty) {
        this.productQty = productQty;
    }


    public void setPriceTotal(float priceTotal) {
        this.priceTotal = priceTotal;

    }

    public Order(String productName, String productImage, String productId, float productPrice, long productQty, float priceTotal) {
        this.productName = productName;
        this.productImage = productImage;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.priceTotal = priceTotal;
    }

    public Order() {
    }
}
