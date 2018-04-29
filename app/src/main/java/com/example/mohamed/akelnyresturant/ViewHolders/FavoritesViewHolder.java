package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;

/**
 * Created by mohamed on 4/24/18.
 */

public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodname ,foodprice;
    public ImageView foodimage , favImage ,cartimage;

    private ItemClickListner itemClickListner;


    public FavoritesViewHolder(View itemView) {
        super(itemView);

        foodname = (TextView) itemView.findViewById(R.id.foods_name);
        foodprice = (TextView) itemView.findViewById(R.id.foods_price);
        foodimage = (ImageView) itemView.findViewById(R.id.image_foods);
        favImage= (ImageView) itemView.findViewById(R.id.fav);
        cartimage= (ImageView) itemView.findViewById(R.id.tocart);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
}

