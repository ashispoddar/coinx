package com.x.coin.coinx2.model;

public class CardInfo {

    String firstName;
    String lastName;
    String cardImage;
    String cardNumber;
    String expiry;

    public String getName() {
        return firstName + " " + lastName ;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }
    public CardInfo(String firstName, String lastName, String cardImage,String cardNumber, String expiry)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.expiry = expiry;

        int pos = cardImage.lastIndexOf("/");
        if(pos != -1) {
            this.cardImage = cardImage.substring(pos+1);
        }
    }

}