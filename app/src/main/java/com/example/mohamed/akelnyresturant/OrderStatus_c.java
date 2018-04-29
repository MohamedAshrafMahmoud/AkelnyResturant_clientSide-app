package com.example.mohamed.akelnyresturant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Interface.ItemClickListner;
import com.example.mohamed.akelnyresturant.ViewHolders.OrderStatusViewHolder;
import com.example.mohamed.akelnyresturant.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus_c extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Request, OrderStatusViewHolder> adapter;

    RecyclerView recycler_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status_c);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Requests");

        recycler_Menu = (RecyclerView) findViewById(R.id.orderStatus);
        recycler_Menu.setHasFixedSize(true);
        recycler_Menu.setLayoutManager(new LinearLayoutManager(this));



        if (getIntent()==null){
            loadOrders(Common.currentUser.getPhone());

        }else {
            //when click in notification it will pass login so phone will be null and will make error
            loadOrders(getIntent().getStringExtra("userPhone"));

        }

    }
///////////////////////////////////////////////////////////////////
    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderStatusViewHolder>(Request.class, R.layout.item_orderstatus_c, OrderStatusViewHolder.class
                , databaseReference) {

     //////////       .orderByChild("phone").equalTo(phone)

            @Override
            protected void populateViewHolder(final OrderStatusViewHolder viewHolder, final Request model, final int position) {

              //  viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText("Status :   "+convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText("Phone :   "+model.getPhone());
                viewHolder.txtOrderAdress.setText("Adress :   "+model.getAdress());
                viewHolder.txtOrderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));

                viewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(OrderStatus_c.this, OrderDetails_c.class);
                        Common.currentRequest=model;
                        intent.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });




            }
        };
        recycler_Menu.setAdapter(adapter);

    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String convertCodeToStatus(String status) {

        if (status.equals("0")){
            return "Placed";
        }else if(status.equals("1")){
            return "On my way";
        }else {
            return "Shipped";
        }

    }


}

