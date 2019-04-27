package com.jo.android.onlineshopping.model;

public class Product  {
    String pName,description,price,image,catagory,pID,date,time;

    public Product() {
    }

    public Product(String pName, String description, String price, String image, String catagory, String pID, String date, String time) {
        this.pName = pName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.catagory = catagory;
        this.pID = pID;
        this.date = date;
        this.time = time;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
