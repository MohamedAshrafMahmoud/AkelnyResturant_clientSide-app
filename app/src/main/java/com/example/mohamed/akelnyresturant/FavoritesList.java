package com.example.mohamed.akelnyresturant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.ViewHolders.FavoritesAdapter;
import com.example.mohamed.akelnyresturant.ViewHolders.FavoritesViewHolder;
import com.example.mohamed.akelnyresturant.ViewHolders.FoodListViewHolder;
import com.example.mohamed.akelnyresturant.model.Favorites;
import com.example.mohamed.akelnyresturant.model.Foods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavoritesList extends AppCompatActivity {


    RecyclerView recycler_Favorite;
    RelativeLayout rootLayout;
    FavoritesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_list);

        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);

        recycler_Favorite = (RecyclerView) findViewById(R.id.listfavorites);
        recycler_Favorite.setLayoutManager(new LinearLayoutManager(this));
        LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(recycler_Favorite.getContext(),R.anim.layout_fall_down);
        recycler_Favorite.setLayoutAnimation(controller);


        loadList();
    }

    private void loadList() {
        adapter=new FavoritesAdapter(this,new DataBase(this).getAllFromFavorites());
        recycler_Favorite.setAdapter(adapter);
    }
}
