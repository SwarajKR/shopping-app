package com.example.swaraj.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Data Structure To STORE Details Of a Product
 */

public class Product implements Parcelable {

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

    public Product(Parcel in){
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        rating = in.readString();
        image = in.readString();
        link = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(rating);
        dest.writeString(image);
        dest.writeString(link);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
