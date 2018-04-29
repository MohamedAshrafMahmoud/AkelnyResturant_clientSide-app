package com.example.mohamed.akelnyresturant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    EditText phone, password, username ,secureCode;
    Button btnsignup;
    TextView font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        phone = (EditText) findViewById(R.id.etphone);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etpass);
        secureCode = (EditText) findViewById(R.id.etsecure);
        btnsignup = (Button) findViewById(R.id.button3);
        font = (TextView) findViewById(R.id.textt);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/fat.ttf");
        font.setTypeface(tf);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phone.getText().toString().length() == 0) {
                    phone.setError("phone not entered");
                    phone.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password not entered");
                    password.requestFocus();

                } else if (username.getText().toString().length() == 0) {
                    username.setError("username not entered");
                    username.requestFocus();
                } else {


                    final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setMessage("please wait ....");
                    progressDialog.show();


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Phone alredy registered ", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                User user = new User(username.getText().toString(), password.getText().toString(),secureCode.getText().toString());
                                databaseReference.child(phone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "SignUp sucessfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,MainMenu_c.class));
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
        });


    }
}
