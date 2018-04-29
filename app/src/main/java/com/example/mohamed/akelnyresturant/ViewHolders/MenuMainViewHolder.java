package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;

/**
 * Created by mohamed on 3/12/18.
 */
public class MenuMainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView menuname;
    public ImageView menuimag;

    private ItemClickListner itemClickListner;


    public MenuMainViewHolder(View itemView) {
        super(itemView);

        menuname = (TextView) itemView.findViewById(R.id.menu_name);
        menuimag = (ImageView) itemView.findViewById(R.id.image_menu);

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

