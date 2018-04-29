package com.example.mohamed.akelnyresturant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.ViewHolders.OrderDetailAdapter;

public class OrderDetails_c extends AppCompatActivity {

    TextView order_id,order_phone,order_adress,order_total,order_comment;
    String order_id_value="";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

       // order_id=(TextView)findViewById(R.id.ordeId);
        order_phone=(TextView)findViewById(R.id.ordephone);
        order_adress=(TextView)findViewById(R.id.ordeadress);
        order_total=(TextView)findViewById(R.id.ordetotal);
        order_comment=(TextView)findViewById(R.id.ordecomment);


        recyclerView = (RecyclerView) findViewById(R.id.lstFoods);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent()!=null){
            order_id_value=getIntent().getStringExtra("OrderId");


           // order_id.setText("Id :"+order_id_value);
            order_phone.setText("Phone  :   "+Common.currentRequest.getPhone());
            order_total.setText("Total  :   "+Common.currentRequest.getTotal());
            order_adress.setText("Adress  :   "+Common.currentRequest.getAdress());
            order_comment.setText("Comment  :   "+Common.currentRequest.getComment());


            OrderDetailAdapter adapter=new OrderDetailAdapter(Common.currentRequest.getFoods());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }


    }
}
