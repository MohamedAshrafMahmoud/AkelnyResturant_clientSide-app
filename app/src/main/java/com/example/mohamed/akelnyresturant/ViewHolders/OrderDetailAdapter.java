package com.example.mohamed.akelnyresturant.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mohamed.akelnyresturant.Cart_c;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.R;
import com.example.mohamed.akelnyresturant.model.Order;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mohamed on 4/13/18.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    private List<Order> orders;


    public OrderDetailAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_order_details, parent, false);
        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderDetailViewHolder holder, final int position) {

        Order order = orders.get(position);

        holder.name.setText(String.format("Name :   %s", order.getProductName()));
        holder.quantity.setText(String.format("Quantity :   %s", order.getQuantity()));
        holder.price.setText(String.format("Price :   $%s", order.getPrice()));
        holder.discount.setText(String.format("Discount :   %s", order.getDiscount()));

    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

}