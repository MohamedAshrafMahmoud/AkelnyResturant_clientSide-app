package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;

/**
 * Created by mohamed on 3/20/18.
 */

  public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
                   ,View.OnCreateContextMenuListener{

    public TextView txt_cart_name, txt_price;
    public ElegantNumberButton elegantNumberButton;
    public ImageView image_cart;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;


    private ItemClickListner itemClickListner;


    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.number_button2);
        image_cart = (ImageView) itemView.findViewById(R.id.imagedata);
        view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
        view_foreground=(LinearLayout) itemView.findViewById(R.id.view_forground);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle(" (choose one) ");

        menu.add(0,0,getAdapterPosition(), Common.Delete);

    }
}
