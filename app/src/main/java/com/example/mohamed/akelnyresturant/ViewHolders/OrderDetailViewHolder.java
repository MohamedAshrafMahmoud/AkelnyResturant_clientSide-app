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
 * Created by mohamed on 4/13/18.
 */

public class OrderDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView name, quantity, price, discount;

    public OrderDetailViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.productName);
        quantity = (TextView) itemView.findViewById(R.id.productQuantity);
        price = (TextView) itemView.findViewById(R.id.productPrice);
        discount = (TextView) itemView.findViewById(R.id.productDiscount);

    }


}