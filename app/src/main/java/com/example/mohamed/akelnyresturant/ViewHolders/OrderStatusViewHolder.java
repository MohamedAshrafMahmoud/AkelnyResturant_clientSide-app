package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.R;

/**
 * Created by mohamed on 4/11/18.
 */

public class OrderStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderPhone ,txtOrderStatus,txtOrderAdress,txtOrderDate;
    public TextView btnedit ,btnremove,btndetails;

    private ItemClickListner itemClickListner;


    public OrderStatusViewHolder(View itemView) {
        super(itemView);

        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderAdress = (TextView) itemView.findViewById(R.id.order_adress);
        txtOrderDate = (TextView) itemView.findViewById(R.id.order_date);



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

