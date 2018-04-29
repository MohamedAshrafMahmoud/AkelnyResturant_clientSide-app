package com.example.mohamed.akelnyresturant;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.ViewHolders.MenuMainViewHolder;
import com.example.mohamed.akelnyresturant.ViewHolders.ShowCommentViewHolder;
import com.example.mohamed.akelnyresturant.model.Category;
import com.example.mohamed.akelnyresturant.model.Rating;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ShowComment_c extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    RecyclerView recycler;
    SwipeRefreshLayout swipeRefreshLayout;


    String foodId = "";
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (adapter!=null)
//            adapter.stopListening();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_comment_c);

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder())
//                .setDefaultFontPath("fonts/fat.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//                .setContentView(R.layout.show_comment_c);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Rating");

        recycler = (RecyclerView) findViewById(R.id.recyclerComment);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadComments(foodId);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadComments(foodId);

            }
        });


    }

    private void loadComments(String foodId) {

        if (getIntent()!=null)
            foodId=getIntent().getStringExtra(Common.Intent_Food_ID);
        if (!foodId.isEmpty()&&foodId!=null){

            //create request query
            Query query=databaseReference.orderByChild("foodId").equalTo(foodId);

            adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(Rating.class, R.layout.item_showcomment_c, ShowCommentViewHolder.class, databaseReference.orderByChild("foodId").equalTo(foodId)) {
                @Override
                protected void populateViewHolder(ShowCommentViewHolder viewHolder, Rating rating, int position) {

                    viewHolder.phone.setText(rating.getUserPhone());
                    viewHolder.comment.setText(rating.getComment());
                    viewHolder.rate.setRating(Float.parseFloat(rating.getRateValue()));

                }
            };
            recycler.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
