package com.example.swaraj.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;


public class Flipkart {

    private ArrayList<Product> contents;


    /**
     * Two types of views in flipkart , no images in first view
     *
     * @param search
     */
    public Flipkart(String search){

        contents = new ArrayList<>();
        String url = "https://www.flipkart.com/search?q=" + search;

        try {

            //First layout

            Document doc = Jsoup.connect(url).get();
            Elements blocks = doc.select("div.MP_3W3");
            if (blocks.first() != null) {
                int count = 0;
                for (Element block : blocks) {
                    if (count < 5) {
                        String name, link, image, price, rating;
                        try {
                            Element title = block.select("a._2cLu-l").first();
                            name = title.text();
                            link = title.attr("href");
                        } catch (NullPointerException e) {
                            continue;
                        }
                        try {
                            price = block.select("div._1vC4OE").first().text();
                        } catch (NullPointerException e) {
                            price = "";
                        }
                        try {
                            image = block.select("img").first().attr("src");
                        } catch (NullPointerException e) {
                            image = "";
                        }
                        try {
                            rating = "Rating: "+block.select("div.hGSR34").first().text().split(" ")[0];
                        } catch (Exception e) {
                            rating = "Rating: Not Avalible";
                        }
                        contents.add(new Product(1, name, price, rating, image,link));

                    } else {
                        break;
                    }
                    count++;
                }
            } else {
                //SECOND LAYOUT

                blocks = doc.select("div._2-gKeQ");
                if (blocks.first() != null) {
                    int count = 0;
                    for (Element block : blocks) {
                        if ( count < 5) {
                            String name, link, price, image, rating;
                            try {
                                link = block.select("a").first().attr("href");
                                name = block.select("div._3wU53n").first().text();
                            } catch (NullPointerException e) {
                                continue;
                            }
                            try{
                                price = block.select("div._1vC4OE").first().text();
                            }
                            catch(NullPointerException e){
                                price ="";
                            }
                            try{
                                rating = "Rating: "+block.select("div.hGSR34").first().text().split(" ")[0];
                            }
                            catch(Exception e){
                                rating = "Rating: Not Avalible";
                            }
                            try{
                                image = doc.select("img").first().attr("src");
                            }
                            catch(NullPointerException e){
                                image = "";
                            }
                            contents.add(new Product(2, name, price, rating, image, link));
                        }
                        else{
                            break;
                        }
                        count++;
                    }
                }
            }
        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
    }

    public ArrayList<Product> getContents() {
        return contents;
    }

    public static String getFeatures(String url){
        String text = "No Details Found...";
        try{
            Document doc = Jsoup.connect(url).get();
            Element block = doc.select("div._3WHvuP").first();
            text = block.text();
        }
        catch (IOException e) {

        }
        catch(IllegalArgumentException e){

        }
        catch (NullPointerException e){

        }
        return text;
    }

    /**
     * To Fetch Reviews from the product page
     */
    public static ArrayList<String> reviews(String url) {
        ArrayList<String> arrayList= new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements blocks = doc.select("div._3nrCtb");
            for (Element block : blocks) {
                try {
                    String review = block.select("div.qwjRop > div > div").text();
                    if(review != null){
                        arrayList.add(review);
                    }

                } catch (NullPointerException e) {

                }
            }
        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
        return arrayList;
    }
}
