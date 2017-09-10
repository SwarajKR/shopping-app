package com.example.swaraj.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Adapter For The Recycle view
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {


    private ArrayList<Product> listItems;
    private Context context;
    private Context activity;

    /**
     *Constructer  for  productAdapter , saves activity context for later uses
     *
     * @param listItems  List of products
     * @param context Context of application
     */

    public ProductAdapter(ArrayList<Product> listItems, Context context, Context activity) {
        this.listItems= listItems;
        this.context = context;
        this.activity = activity;
    }

    /**
     *Inflating The Cardview created and  returns a viewholder
     *
     * @param parent VireGroup
     * @param viewType and viewtype int
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Bind Data From list object at  position to viewholder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Product item = listItems.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        holder.rating.setText(item.getRating()+" \u2605");
        holder.featureList.setVisibility(View.GONE);

        //Setting Image

        try{
            switch (item.getId()){
                case 1:
                case 2:
                    Picasso.with(context).load(R.drawable.ic_not_interested_black_36dp).into(holder.thumbnail);
                    break;
                default:
                    Picasso.with(context).load(item.getImage()).error(R.drawable.ic_not_interested_black_36dp).into(holder.thumbnail);
                    break;
            }

        }
        catch (IllegalArgumentException e){

        }

        //Setting Website Name

        switch (item.getId()){
            case 0:
                holder.browser.setText("Amazon");
                break;
            case 1:
            case 2:
                holder.browser.setText("Flipkart");
                break;
            case 3:
                holder.browser.setText("Ebay");
                break;
            case 4:
                holder.browser.setText("Snapdeal");
                break;
            default:
                break;
        }



        //WHen CLicked on view more details are  fetched  ,depending on the  object id
        //handler is defined in viewHolder

        holder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.featureList.getVisibility() == View.GONE ) {
                    holder.footer.setVisibility(View.GONE);
                    holder.loading.setVisibility(View.VISIBLE);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                                switch (item.getId()) {
                                    case 0:
                                        String testUrl = item.getLink();
                                        try{
                                            if ( testUrl.charAt(0) != 'h'){
                                                testUrl = "http://amazon.in"+testUrl;
                                            }
                                        } catch (StringIndexOutOfBoundsException e){
                                            Toast.makeText(context, "Failed To Fetch Data.", Toast.LENGTH_SHORT).show();
                                        }
                                        holder.features = Amazon.getFeatures(testUrl);
                                        break;
                                    case 1:
                                    case 2:
                                        holder.features = Flipkart.getFeatures("https://www.flipkart.com"+item.getLink());
                                        break;
                                    case 3:
                                        holder.features = Ebay.getFeatures(item.getLink());
                                        break;
                                    case 4:
                                        holder.features = Snapdeal.getFeatures(item.getLink());
                                        break;
                                    default:
                                        holder.features = "No Details Found...";
                                        break;
                                }
                                holder.handler.sendEmptyMessage(0);
                        }
                    };
                    Thread th = new Thread(r);
                    th.start();
                } else{
                    holder.featureList.setText("");
                    holder.featureList.setVisibility(View.GONE);
                }

            }
        });

         //To open the website

        holder.browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                try {
                    switch (item.getId()){
                        case 0 :
                            url =item.getLink();
                            try{
                                if ( url.charAt(0) != 'h'){
                                    url = "http://amazon.in"+url;
                                }
                            } catch (StringIndexOutOfBoundsException e){
                            }
                            break;
                        case 1 :
                        case 2 :
                            url = "https://www.flipkart.com"+item.getLink();
                            break;
                        default :
                            url =item.getLink();
                            break;
                    }
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browser);
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(context, "Failed To Open Url", Toast.LENGTH_SHORT).show();
                }
            }
        });

         // Fetch the reviews using thread
         // open dialog and closeREview listner in thread handler

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.footer.setVisibility(View.GONE);
                holder.loading.setVisibility(View.VISIBLE);
                Runnable fetchReviews = new Runnable() {
                    @Override
                    public void run() {
                        holder.reviews.clear();
                        switch (item.getId()){
                            case 0:
                                String testUrl = item.getLink();
                                try{
                                    if ( testUrl.charAt(0) != 'h'){
                                        testUrl = "http://amazon.in"+testUrl;
                                    }
                                } catch (StringIndexOutOfBoundsException e){
                                    Toast.makeText(context, "Failed To Fetch Data.", Toast.LENGTH_SHORT).show();
                                }
                                holder.reviews.addAll(Amazon.reviews(testUrl));
                                break;
                            case 1:
                            case 2:
                                holder.reviews.addAll(Flipkart.reviews("https://www.flipkart.com"+item.getLink()));
                                break;
                            case  4:
                                holder.reviews.addAll(Snapdeal.reviews(item.getLink()));
                            default:
                                holder.reviews.addAll(Ebay.reviews(item.getLink()));
                                break;
                        }
                        holder.reviewHandler.sendEmptyMessage(0);
                    }
                };
                Thread thread =new Thread(fetchReviews);
                thread.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView price;
        public TextView rating;
        public ImageView thumbnail;
        public CardView card;
        public TextView featureList;
        public ProgressBar loading;
        public TextView hide;
        public TextView review;
        public TextView browser;
        public LinearLayout footer;
        public String features;
        public ArrayList<String> reviews;

        /**
         * To show the fetched features in the textview
         */
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                loading.setVisibility(View.GONE);
                featureList.setVisibility(View.VISIBLE);
                featureList.setText(features);
                footer.setVisibility(View.VISIBLE);
            }
        };

        /**
         * Runs after reviews are fetched Create and display DIalog Box
         */

        Handler reviewHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                loading.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
                final AlertDialog.Builder builder= new AlertDialog.Builder(activity);
                CharSequence[] charSeq = reviews.toArray(new CharSequence[reviews.size()]);
                builder.setItems(charSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog  alertDialog = builder.create();
                alertDialog.setTitle("Review");
                ListView listView = alertDialog.getListView();
                ColorDrawable line = new ColorDrawable(context.getResources().getColor(R.color.White));
                listView.setDivider(line);
                listView.setDividerHeight(30);
                alertDialog.show();
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            rating = (TextView) itemView.findViewById(R.id.rating);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            card = (CardView) itemView.findViewById(R.id.card);
            featureList = (TextView) itemView.findViewById(R.id.bullet);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
            hide = (TextView) itemView.findViewById(R.id.hide);
            review = (TextView) itemView.findViewById(R.id.review);
            browser =(TextView) itemView.findViewById(R.id.bowser);
            features = "";
            footer = (LinearLayout) itemView.findViewById(R.id.footer);
            reviews= new ArrayList<String>();
        }
    }
}