package com.androiddevproject.foodorderingapp;

public class OrderPerson {
    String orderPersonPhone;
    String orderPersonName;

    public String getOrderPersonPhone() {
        return orderPersonPhone;
    }

    public void setOrderPersonPhone(String orderPersonPhone) {
        this.orderPersonPhone = orderPersonPhone;
    }

    public String getOrderPersonName() {
        return orderPersonName;
    }

    public void setOrderPersonName(String orderPersonName) {
        this.orderPersonName = orderPersonName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public OrderPerson(String orderPersonPhone, String orderPersonName, String orderState) {
        this.orderPersonPhone = orderPersonPhone;
        this.orderPersonName = orderPersonName;
        this.orderState = orderState;
    }

    public OrderPerson(String orderState) {
        this.orderState = orderState;
    }

    String orderState;


    public OrderPerson() {
    }
}
