package com.example.mohamed.akelnyresturant.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.FoodDetails_c;
import com.example.mohamed.akelnyresturant.FoodList_c;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;
import com.example.mohamed.akelnyresturant.model.Favorites;
import com.example.mohamed.akelnyresturant.model.Order;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mohamed on 4/24/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(context).inflate(R.layout.item_favorites_list, parent, false);
        return new FavoritesViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final FavoritesViewHolder viewHolder, final int position) {


        viewHolder.foodname.setText(favoritesList.get(position).getFoodName().toString());
        viewHolder.foodprice.setText(favoritesList.get(position).getFoodPrice().toString());
        Picasso.with(context).load(favoritesList.get(position).getFoodImge()).into(viewHolder.foodimage);


        //to quickly cart
        viewHolder.cartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new DataBase(context).addToCart(new Order(
                        Common.currentUser.getPhone(),
                        favoritesList.get(position).getFoodId(),
                        favoritesList.get(position).getFoodName(),
                        "1",
                        favoritesList.get(position).getFoodPrice(),
                        favoritesList.get(position).getFoodDiscount(),
                        favoritesList.get(position).getFoodImge()
                ));

                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        if (new DataBase(context).isFavorite(favoritesList.get(position).getFoodId(), Common.currentUser.getPhone()))
            viewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        viewHolder.favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  favoritesList.remove(position);
                new DataBase(context).deleteFromFavorite(favoritesList.get(position).getFoodId(), Common.currentUser.getPhone());
             //   favoritesList.notify();
                viewHolder.favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();

            }
        });


        viewHolder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, FoodDetails_c.class);
                intent.putExtra("FoodId", favoritesList.get(position).getFoodId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int position) {
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorites item, int position) {
        favoritesList.add(position, item);
        notifyItemInserted(position);
    }

    public Favorites getItem(int position) {
        return favoritesList.get(position);
    }

}