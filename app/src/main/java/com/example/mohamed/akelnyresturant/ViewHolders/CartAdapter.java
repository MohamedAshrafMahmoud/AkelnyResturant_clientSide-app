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


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

     private List<Order> listdata=new ArrayList<>();
     private Cart_c cart;

    public CartAdapter(List<Order> listdata, Cart_c cart) {
        this.listdata = listdata;
        this.cart = cart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(cart);
        View itemView=inflater.inflate(R.layout.item_cart_c,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {

        Picasso.with(cart.getBaseContext())
                .load(listdata.get(position).getImage())
                .resize(120,90)
                .centerCrop()
                .into(holder.image_cart);


        holder.elegantNumberButton.setNumber(listdata.get(position).getQuantity());
        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order=listdata.get(position);
                order.setQuantity(String.valueOf(newValue));
                new DataBase(cart).updateCart(order);

                int total = 0;
                List<Order> orders=new DataBase(cart).getAllFromCart();
                for (Order item : orders)
                    total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

               cart.textTotalPrice.setText(fmt.format(total));
            }
        });

        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        int price =(Integer.parseInt(listdata.get(position).getPrice()))*(Integer.parseInt(listdata.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listdata.get(position).getProductName());


    }

    public Order getItem(int position){
        return listdata.get(position);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void removeItem(int position){
        listdata.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position) {
        listdata.add(position,item);
        notifyItemInserted(position);
    }
}

