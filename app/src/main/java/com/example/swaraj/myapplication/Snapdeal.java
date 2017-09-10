package com.example.swaraj.myapplication;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by oem on 4/9/17.
 */

public class Snapdeal {

    private ArrayList<Product> contents;

    public Snapdeal(String search){

        contents = new ArrayList<>();

        String url  = "https://www.snapdeal.com/search?keyword="+search;
        try {
            Document document = Jsoup.connect(url).get();
            Elements blocks = document.select("div.product-tuple-listing");
            int count = 0;
            for (Element block: blocks) {
                if(count < 5){
                    String name, price, image, link, rating;
                    try{ name = block.select("p.product-title").first().text(); } catch (NullPointerException e){ continue;}
                    try{ price = block.select("span.product-price").first().text(); } catch (NullPointerException e) {price ="";}
                    try{ link = block.select("a.dp-widget-link").first().attr("href"); } catch (NullPointerException e){link = "";}
                    try {
                        image =" https:" +block.select("div.product-img > img").first().attr("src");
                    } catch (NullPointerException e){
                        try{
                            image = block.select("soure.product-image").first().attr("srcset");
                        } catch (NullPointerException exe){
                            image ="";
                        }
                    }
                    try {
                        rating = block.select("div.filled-stars").first().attr("style").split(":")[1];
                        rating = rating.split("%")[0];
                        float temp = Float.parseFloat(rating)/20;
                        rating = "Rating: " + Float.toString(temp);
                    } catch (Exception e){
                        rating = "Rating: Not Found";
                    }
                    contents.add(new Product(4, name, price, rating, image, link));
                    count++;
                } else {
                    break;
                }
            }
        } catch (IOException e){

        } catch (IllegalArgumentException e){

        }
    }

    public ArrayList<Product> getContents() {
        return contents;
    }

    public static String getFeatures(String url){
        StringBuilder result = new StringBuilder();
        try{
            Document document = Jsoup.connect(url).get();
            Elements blocks = document.select("li.dtls-li");
            for (Element block: blocks) {
                result.append(block.text()+"\n");
            }
        } catch (IOException e){

        } catch (NullPointerException e){

        }
        return result.toString();
    }

    public  static  ArrayList<String> reviews(String url){
        ArrayList<String> review = new ArrayList<>();
        review.add("Reviews: Not Found");
        return  review;
    }
}
