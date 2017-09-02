package com.example.swaraj.myapplication;

/**
 *
 * Data Structure To STORE Details Of a Product
 */

public class Product {

    private int id;
    private String name;
    private String price;
    private String rating;
    private String image;
    private String link;

    public Product(int id,String name, String price, String rating, String image, String link) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.image = image;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }
}
