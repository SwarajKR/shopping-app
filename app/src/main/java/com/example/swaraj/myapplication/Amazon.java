package com.example.swaraj.myapplication;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;


public class Amazon {

    private ArrayList<Product> contents;

    /**
     * Constructor performs webscraping and place results in contents list
     *
     * @param search
     */
    public Amazon(String search) {

        //BUILD URL

        String url = "http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=" + search;
        contents = new ArrayList<>();

        try {

            //REQUEST  AND GET CONTENTS

            Document doc = Jsoup.connect(url).get();
            int count = 0;

            while(count < 5) {

                // FETCH DATA FROM CONTENT
                Element block = doc.select("#result_" + count).first();
                if(block != null){

                    String name, price, link , image,rating;
                     try {
                         name = block.select("h2").first().text();
                         if(name.equals("Shop by Category")){
                             count++;
                            continue;
                         }
                     } catch (NullPointerException e){
                         name = "";
                     }
                     try {  price = "Price: "+ block.select("span.s-price").first().text(); } catch (NullPointerException e){price = "Nill"; }
                     try {  link = block.select("a.s-access-detail-page").first().attr("href"); } catch (NullPointerException e){ link = ""; }
                     try {  image = block.select("img.s-access-image").first().attr("src"); } catch (NullPointerException e){ image = ""; }
                     try { rating = "Rating: "+block.select("a > i > span.a-icon-alt").first().text().split(" ")[0]; } catch (NullPointerException e){ rating = "Rating: Not Avalible"; }

                    Product item = new Product(0, name, price, rating, image, link);
                    contents.add(item);

                }
                else { break; }
                ++count;
            }

        }
        catch (IOException e) {

        } catch (IllegalArgumentException e){

        }

    }

    /**
     * Getter fro Contents
     *
     * @return
     */
    public ArrayList<Product> getContents() {
        return contents;
    }

    /**
     * used for view moew scarpping
     *
     * @param url
     * @return
     */
    public static String getFeatures(String url){
        String text= "No Details Found...";
        try{
            Document doc = Jsoup.connect(url).get();
            Element block = doc.select("#feature-bullets").first();
            text =  block.text();
        }
        catch (IOException e) {

        }
        catch (IllegalArgumentException e){

        }
        catch (NullPointerException e){

        }
        return text;
    }

    /**
     * To Fetch Reviews from the product page
     */
    public static ArrayList<String> reviews(String url) {
        ArrayList<String> arrayList =  new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Element container = doc.select("#revMH").first();
            Elements blocks = container.select("#revMHRL > div");
            for (Element block : blocks) {
                try {
                    arrayList.add(block.select(".a-spacing-small > div.a-section").first().text());

                } catch (NullPointerException e) {
                }
            }
        }
        catch(NullPointerException e){
            Elements reviews = doc.select("div.review");
            for (Element review : reviews) {
                Elements datas = review.select("div.review-data");
                String reviewString = "";
                for (Element data : datas) {
                    reviewString = reviewString + data.text()+"\n";
                }
                arrayList.add(reviewString);
            }
        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
        return arrayList;
    }

}
