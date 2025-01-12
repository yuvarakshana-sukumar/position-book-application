package com.jpmorgan.positionbook.model;

public class Position {

    private String account;
    private String security;
    private int quantity;

    // Constructor, getters, and setters

    public Position(String account, String security) {
        this.account = account;
        this.security = security;
        this.quantity = 0;
    }

    // Getters and Setters

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity += quantity; // Increment or decrement the position
    }
}
