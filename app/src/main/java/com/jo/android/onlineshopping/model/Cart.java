package com.jo.android.onlineshopping.model;

public class Cart {
    private  String pID,pname,price,discount,quantity;

    public Cart(String pID, String pname, String price, String discount, String quantity) {
        this.pID = pID;
        this.pname = pname;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
