package com.example.mohamed.akelnyresturant;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.Database.DataBase;
import com.example.mohamed.akelnyresturant.Helper.RecyclerItemTouchHelper;
import com.example.mohamed.akelnyresturant.Interface.RecyclerItemTouchHelperListener;
import com.example.mohamed.akelnyresturant.ViewHolders.CartAdapter;
import com.example.mohamed.akelnyresturant.ViewHolders.CartViewHolder;
import com.example.mohamed.akelnyresturant.model.Order;
import com.example.mohamed.akelnyresturant.model.Request;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart_c extends AppCompatActivity implements RecyclerItemTouchHelperListener {


    public TextView textTotalPrice;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    List<Order> carts = new ArrayList<Order>();
    CartAdapter adapter;
    Button order;
    RecyclerView recyclerView;
    RelativeLayout rootLayout;
    //Place shippingAdress;     for google find location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_c);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Requests");


        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //for swip to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        textTotalPrice = (TextView) findViewById(R.id.total);
        order = (Button) findViewById(R.id.btnplaceorder);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (carts.size() > 0) {
                    showAlertDioalog();
                } else {
                    Toast.makeText(Cart_c.this, "Your bag is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);


        loadListFood();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showAlertDioalog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart_c.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Entre your adress :  ");


        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.orderstatus_comment, null);


        // for google find location
//        PlaceAutocompleteFragment edtAdress=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.places_autocomplete_frag);
//        //hide search icon before fragment
//        edtAdress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
//        //set hint
//        ((EditText)edtAdress.getView().findViewById(R.id.place_autocomplete_search_input))
//                .setHint("Entre your adress");
//        //set text size
//        ((EditText)edtAdress.getView().findViewById(R.id.place_autocomplete_search_input))
//                .setTextSize(14);
//
//        //get adress from place autocomplete
//        edtAdress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                shippingAdress=place;
//            }
//
//            @Override
//            public void onError(Status status) {
//
//                Log.e("Error",status.getStatusMessage());
//            }
//        });

        final EditText adress = (EditText) view.findViewById(R.id.adress);
        final EditText comment = (EditText) view.findViewById(R.id.comment);


        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        "0",
                        // shippingAdress.getAddress().toString(),  for google find location
                        adress.getText().toString(),
                        textTotalPrice.getText().toString(),
                        comment.getText().toString(),
                        // String.format("%s,%s",shippingAdress.getLatLng().latitude,shippingAdress.getLatLng().longitude), for google find location
                        carts
                );


                databaseReference.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                new DataBase((getBaseContext())).deleteFromCart();

                Toast.makeText(Cart_c.this, "Thank you....order placed ", Toast.LENGTH_SHORT).show();
                finish();

                //  for google find location
//                //remove fragment because it will make error in second payment
//                getFragmentManager().beginTransaction()
//                        .remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_frag))
//                        .commit();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //for google find location
//                //remove fragment because it will make error in second payment
//                getFragmentManager().beginTransaction()
//                        .remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_frag))
//                        .commit();
            }
        });
        alertDialog.show();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void loadListFood() {

        carts = new DataBase(this).getAllFromCart();

        adapter = new CartAdapter(carts, this);

        recyclerView.setAdapter(adapter);

        //for refresh data into cart
        adapter.notifyDataSetChanged();


        int total = 0;
        for (Order order : carts)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        textTotalPrice.setText(fmt.format(total));
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.Delete)) {
            deleteFromCart(item.getOrder());

        }
        return true;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //function delete item
    private void deleteFromCart(int position) {

        carts.remove(position);

        //delete old data from sqlite
        new DataBase(this).deleteFromCart();
        Toast.makeText(this, " Item deleted", Toast.LENGTH_SHORT).show();

        //update new data
        for (Order item : carts) {
            new DataBase(this).addToCart(item);
        }

        //for refresh with line 128
        loadListFood();

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //for delete        slider related to line 70
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
         //   new DataBase(getBaseContext()).deleteFromFavorite(deleteItem.getProductId(), Common.currentUser.getPhone());
            new DataBase(getBaseContext()).deleteFromCart();

            //update total

            int total = 0;
            List<Order> orders = new DataBase(getBaseContext()).getAllFromCart();
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            textTotalPrice.setText(fmt.format(total));


            Snackbar snackbar = Snackbar.make(rootLayout, name + "Remove from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new DataBase(getBaseContext()).addToCart(deleteItem);

                    int total = 0;
                    List<Order> orders = new DataBase(getBaseContext()).getAllFromCart();
                    for (Order item : orders)
                        total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en", "US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    textTotalPrice.setText(fmt.format(total));


                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();

        }
    }
}
