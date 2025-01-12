package com.jpmorgan.positionbook.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeEvent {
    private int id;
    private String type; // BUY, SELL, CANCEL
    private String account;
    private String security;
    private int quantity;

    @JsonCreator
    public TradeEvent(@JsonProperty("id") int id, @JsonProperty("tradeType") String tradeType, @JsonProperty("account") String account, @JsonProperty("security") String security, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.type = tradeType;
        this.account = account;
        this.security = security;
        this.quantity = quantity;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public String getAccount(){
        return account;
    }

    public void setSecurity(String security){
        this.security=security;
    }

    public String getSecurity(){
        return security;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

}
