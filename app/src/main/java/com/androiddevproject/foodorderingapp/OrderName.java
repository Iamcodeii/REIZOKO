package com.androiddevproject.foodorderingapp;

public class OrderName {
    String orderStatus;
    String orderState;
    String orderId;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public float getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(long grossTotal) {
        this.grossTotal = grossTotal;
    }

    public OrderName(String orderStatus, String orderState, String orderId, String orderPhone, String orderTime, long totalItems, float grossTotal) {
        this.orderStatus = orderStatus;
        this.orderState = orderState;
        this.orderId = orderId;
        this.orderPhone = orderPhone;
        this.orderTime = orderTime;
        this.totalItems = totalItems;
        this.grossTotal = grossTotal;
    }

    String orderPhone;

    String orderTime;
    long totalItems;
    float grossTotal;

    public OrderName() {
    }
}
