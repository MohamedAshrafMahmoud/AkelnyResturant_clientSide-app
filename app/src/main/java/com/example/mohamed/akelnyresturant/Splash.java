package com.example.mohamed.akelnyresturant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Butcher on 28/08/2017.
 */

public class Splash extends Activity {

    int timeSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //  new Handler().postDelayed(new Runnable() {
        Thread splash_screen = new Thread() {
            //for not moving to any activity after 3000
            @Override
            public void run() {
                // This method will be executed once the timer is over
                timeSec = 500;
                // Start your app main activity
                try {
                    sleep(timeSec);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                    // it must return to main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences share = getSharedPreferences("da", Context.MODE_PRIVATE);
                            String User = share.getString("phone", null);
                            String pass = share.getString("password", null);

                            if (!TextUtils.isEmpty(User)) {
                                Login(User, pass);

                            } else {
                                Intent intent = new Intent(Splash.this, Main.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        };

        splash_screen.start();


    }


    private void Login(final String phone, final String password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User");

        if (Common.isConnectToInternet(getBaseContext())) {

            final ProgressDialog progressDialog = new ProgressDialog(Splash.this);
            progressDialog.setMessage("please wait ....");
            progressDialog.show();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phone).exists()) {
                        progressDialog.dismiss();

                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);


                        if (user.getPassword().equals(password)) {
                            Intent intent = new Intent(Splash.this, MainMenu_c.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Splash.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Splash.this, "phone not exist", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(Splash.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
        }

    }


}
