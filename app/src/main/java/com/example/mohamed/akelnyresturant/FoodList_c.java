package com.example.mohamed.akelnyresturant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.ViewHolders.FoodListViewHolder;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.model.Favorites;
import com.example.mohamed.akelnyresturant.model.Foods;
import com.example.mohamed.akelnyresturant.model.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList_c extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Foods, FoodListViewHolder> adapter;

    FirebaseRecyclerAdapter<Foods, FoodListViewHolder> searchadapter;

    List<String> suggestlist = new ArrayList<>();

    DataBase db = new DataBase(this);


    RecyclerView recycler_Menu;
    MaterialSearchBar materialSearchBar;
    SwipeRefreshLayout swipeRefreshLayout;

    String categoryId = "";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list_c);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Foods");


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_Layout);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (getIntent() != null) {
                    categoryId = getIntent().getStringExtra("categoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null) {
                    loadListFood(categoryId);
                }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (getIntent() != null) {
                    categoryId = getIntent().getStringExtra("categoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null) {
                    loadListFood(categoryId);
                }

            }
        });


        recycler_Menu = (RecyclerView) findViewById(R.id.recyclerFoods);
        recycler_Menu.setHasFixedSize(true);
        recycler_Menu.setLayoutManager(new LinearLayoutManager(this));


        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Entre your food");


        suggestFood();


        materialSearchBar.setLastSuggestions(suggestlist);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();
                for (String search : suggestlist) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search is closed
                //restore orignal adapter
                if (!enabled) {
                    recycler_Menu.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //when search finish
                //show result
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void startSearch(CharSequence text) {
        //for   materialSearchBar.setOnSearchActionListener   function    onSearchConfirmed

        searchadapter = new FirebaseRecyclerAdapter<Foods, FoodListViewHolder>(
                Foods.class,
                R.layout.item_food_list_c,
                FoodListViewHolder.class,
                databaseReference.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodListViewHolder viewHolder, Foods model, int position) {
                viewHolder.foodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodimage);

                final Foods foods = model;
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodList_c.this, FoodDetails_c.class);
                        intent.putExtra("FoodId", searchadapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };

        recycler_Menu.setAdapter(searchadapter);

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void suggestFood() {
        databaseReference.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Foods item = postSnapshot.getValue(Foods.class);
                            suggestlist.add(item.getName());
                        }
                        materialSearchBar.setLastSuggestions(suggestlist);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Foods, FoodListViewHolder>(Foods.class, R.layout.item_food_list_c, FoodListViewHolder.class
                , databaseReference.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final FoodListViewHolder viewHolder, final Foods model, final int position) {

                viewHolder.foodname.setText(model.getName());
                viewHolder.foodprice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.foodimage);


                //to quickly cart
                viewHolder.cartimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addToCart(new Order(
                                Common.currentUser.getPhone(),
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount(),
                                model.getImage()
                        ));

                        Toast.makeText(FoodList_c.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });







                //Add favorite
                if (db.isFavorite(adapter.getRef(position).getKey(), Common.currentUser.getPhone()))
                    viewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);

                //change state of favorite
                viewHolder.favImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites=new Favorites();
                        favorites.setFoodId(adapter.getRef(position).getKey());
                        favorites.setFoodName(model.getName());
                        favorites.setFoodPrice(model.getPrice());
                        favorites.setUserPhone(Common.currentUser.getPhone());
                        favorites.setFoodImge(model.getImage());
                        favorites.setFoodMenuId(model.getMenuId());
                        favorites.setFoodDiscription(model.getDescription());
                        favorites.setFoodDiscount(model.getDiscount());

                        if (!db.isFavorite(adapter.getRef(position).getKey(),Common.currentUser.getPhone())){
                            db.addToFavorite(favorites);
                            viewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList_c.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        }else {
                            db.deleteFromFavorite(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                            viewHolder.favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(FoodList_c.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                //      final Foods foods = model;
                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodList_c.this, FoodDetails_c.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        recycler_Menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }
}
