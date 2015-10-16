package com.x.coin.coinx2.model;

public class CardInfo {

    String name;
    String cardNumber;
    String expiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public CardInfo(String name, String cardNumber, String expiry)
    {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
    }
}