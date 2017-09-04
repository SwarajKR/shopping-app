package com.example.swaraj.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

/**
 * Created by oem on 19/8/17.
 */

public class Ebay {

    private ArrayList<Product> contents;

    /**
     *The functio that searches and make list
     *
     * @param search
     */
    public Ebay(String search) {
        contents = new ArrayList<>();
        String url = "https://www.ebay.in/sch/i.html?_nkw="+search;
        try {
            Document doc = Jsoup.connect(url).get();
            int count = 0;
            Elements reviews = doc.select(" ul#ListViewInner > li");
            for (Element review : reviews) {
                if (count < 5){
                    String name, price, link , image ;
                    try{
                        name = review.select("h3").first().text();
                        link = review.select("h3 > a").first().attr("href");
                    } catch(NullPointerException e){
                        continue;
                    }
                    try{
                        image = review.select("img").first().attr("src");
                    } catch (NullPointerException e){
                        image = "";
                    }
                    try{
                        price = review.select("li.lvprice").first().text();
                    } catch(NullPointerException e){
                        price="";
                    }
                    contents.add(new Product(3, name, price, "Rating: Check The Reviews", image, link));

                } else {
                    break;
                }
                count++;
            }
        } catch (NullPointerException e){

        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
    }

    public ArrayList<Product> getContents() {
        return contents;
    }

    public  static  String getFeatures(String url){
        String text= "No Details Found...";
        try {
            Document doc = Jsoup.connect(url).get();
            try {
                Elements blocks = doc.select(" div.itemAttr > div > table");
                StringBuilder result = new StringBuilder();
                for (Element var : blocks) {
                    if (!var.attr("id").equals("#itmSellerDesc")) {
                        int flag = 0;
                        int count = 0;
                        Elements cols = var.select("td");
                        for (Element col: cols) {
                            if (col.text().equals("Condition:")){
                                flag = 1;
                            }
                            else{
                                if (flag == 1){
                                    flag = 0;
                                    continue;
                                }
                                if (count %2 == 0){
                                    result.append(result+col.text());
                                }
                                else{
                                    result.append(col.text()+"\n");
                                }
                                count++;
                            }
                        }
                    }

                }
                return result.toString();

            } catch (NullPointerException e) {

            }
        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
        return text;
    }

    public static ArrayList<String> reviews(String url){
        ArrayList<String> results = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(url).get();
            String result = doc.select("div.mbg").first().text();
            result += "\n" + doc.select("div#si-fb").first().text();
            results.add(result);
        } catch (NullPointerException e) {

        } catch (IOException e) {

        } catch (IllegalArgumentException e){

        }
        return  results;
    }

}