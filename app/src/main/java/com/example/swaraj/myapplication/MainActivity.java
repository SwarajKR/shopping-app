package com.example.swaraj.myapplication;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private ArrayList<Product>  items;
    private RecyclerView rView;
    private RecyclerView.Adapter adapter;
    public SearchView searchView;
    private ProgressBar circle;
    private ImageView notice;
    private ArrayList<Product> resultOne;
    private ArrayList<Product> resultTwo;
    private ArrayList<Product> resultThree;
    private TextView noticeText;


    /**
     *Handler called when Search is complete to hide progressbar and show recyclerview
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(notice.getVisibility() == View.VISIBLE){
                notice.setVisibility(View.GONE);
                noticeText.setVisibility(View.GONE);
            }
            circle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = (ProgressBar) findViewById(R.id.loading);
        circle.setVisibility(View.GONE);
        notice = (ImageView) findViewById(R.id.placeholder);
        noticeText = (TextView) findViewById(R.id.notice_text);

        items = new ArrayList<>();

        resultOne = new ArrayList<>();
        resultTwo = new ArrayList<>();
        resultThree = new ArrayList<>();

        rView = (RecyclerView) findViewById(R.id.recycler);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(items, getApplicationContext(), this);
        rView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);

        et.setHintTextColor(Color.WHITE);

        /**
         * Searchview controls webscrapp is called on submit
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (query != null){
                    query = query.replaceAll("\\s", "");
                    webScrap(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    /**
     * Search Through all the websites using keyword  and add those results to arraylist
     *
     * @param keyword
     */
    public void webScrap(final String keyword){

        items.clear();
        resultOne.clear();
        resultTwo.clear();
        resultThree.clear();

        adapter.notifyDataSetChanged();
        if(notice.getVisibility() == View.GONE){
            notice.setVisibility(View.VISIBLE);
            noticeText.setVisibility(View.VISIBLE);
        } else {
            noticeText.setText("Searching");
        }
        circle.setVisibility(View.VISIBLE);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                Runnable rOne = new Runnable() {
                    @Override
                    public void run() {
                        Flipkart flipkartResults = new Flipkart(keyword);
                        resultTwo.addAll(flipkartResults.getContents());

                    }
                };

                Thread thOne = new Thread(rOne);
                thOne.start();

                Runnable rTwo = new Runnable() {
                    @Override
                    public void run() {
                        Amazon amazonResults = new Amazon(keyword);
                        resultOne.addAll(amazonResults.getContents());
                    }
                };

                Thread thTwo = new Thread(rTwo);
                thTwo.start();

                Runnable rThree = new Runnable() {
                    @Override
                    public void run() {
                        Ebay ebayResults = new Ebay(keyword);
                        resultThree.addAll(ebayResults.getContents());
                    }
                };

                Thread thThree = new Thread(rThree);
                thThree.start();

                try {
                    thOne.join();
                    thTwo.join();
                    thThree.join();
                } catch (InterruptedException e){

                }
                //Sorting
                for (int i=0; i<5; i++){
                    try{ items.add(resultOne.get(i)); }catch (IndexOutOfBoundsException e){ }
                    try{ items.add(resultTwo.get(i)); }catch (IndexOutOfBoundsException e){ }
                    try{ items.add(resultThree.get(i)); }catch (IndexOutOfBoundsException e){ }
                }
                handler.sendEmptyMessage(0);
            }
        };


        Thread th = new Thread(r);
        th.start();

    }

}
