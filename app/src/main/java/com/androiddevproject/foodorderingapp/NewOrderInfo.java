package com.androiddevproject.foodorderingapp;

public class NewOrderInfo {
    float grossTotal;
    String orderId;
    String orderState;
    String orderStatus;
    String orderTime;
    long totalItems;

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String str) {
        this.orderStatus = str;
    }

    public String getOrderState() {
        return this.orderState;
    }

    public void setOrderState(String str) {
        this.orderState = str;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String str) {
        this.orderId = str;
    }

    public String getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(String str) {
        this.orderTime = str;
    }

    public long getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(long j) {
        this.totalItems = j;
    }

    public float getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(float j) {
        this.grossTotal = j;
    }

    public NewOrderInfo(String str, String str2, String str3, String str4, long j, float j2) {
        this.orderStatus = str;
        this.orderState = str2;
        this.orderId = str3;
        this.orderTime = str4;
        this.totalItems = j;
        this.grossTotal = j2;
    }

    public NewOrderInfo() {
    }
}
