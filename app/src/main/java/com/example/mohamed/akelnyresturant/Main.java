package com.example.mohamed.akelnyresturant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.example.mohamed.akelnyresturant.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Main extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    EditText edtphone, edtpassword;
    Button btnsignin;
    TextView font;
    CheckBox check_remember;
    ImageView forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edtphone = (EditText) findViewById(R.id.etphone);
        edtpassword = (EditText) findViewById(R.id.etpass);
        btnsignin = (Button) findViewById(R.id.button3);
        font = (TextView) findViewById(R.id.textt);
        check_remember = (CheckBox) findViewById(R.id.checkremember);
        forgetPassword = (ImageView) findViewById(R.id.forgetT);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/fat.ttf");
        font.setTypeface(tf);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");


        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtphone.getText().toString().length() == 0) {
                    edtphone.setError("phone required");
                    edtphone.requestFocus();
                } else if (edtpassword.getText().toString().length() == 0) {
                    edtpassword.setError("password required");
                    edtpassword.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {


                    SignUser(edtphone.getText().toString(), edtpassword.getText().toString());

                } else {
                    Toast.makeText(Main.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectToInternet(getBaseContext())) {
                    showForgetPassDialog();
                } else {
                    Toast.makeText(Main.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void SignUser(final String phone, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(Main.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                    progressDialog.dismiss();

                    User user = dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);
                    user.setPhone(edtphone.getText().toString());


                    if (user.getPassword().equals(edtpassword.getText().toString())) {
                        Intent intent = new Intent(Main.this, MainMenu_c.class);
                        Common.currentUser = user;

                        //save data when checkbox
                        if (check_remember.isChecked()) {
                            SharedPreferences.Editor edit = getSharedPreferences("da", Context.MODE_PRIVATE).edit();
                            edit.putString("phone", edtphone.getText().toString());
                            edit.putString("password", edtpassword.getText().toString());
                            edit.commit();
                        }

                        startActivity(intent);
                        finish();

                        databaseReference.removeEventListener(this);
                    } else {
                        Toast.makeText(Main.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Main.this, "phone not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void goSignUp(View view) {


        if (Common.isConnectToInternet(getBaseContext())) {
            Intent intent = new Intent(Main.this, SignUp.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showForgetPassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("     Forget Password");
        builder.setMessage("Entre Your secure code");
        builder.setIcon(R.drawable.secure);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.forgetpassword, null);

        builder.setView(view);

        final EditText phone = (EditText) view.findViewById(R.id.fogetphone);
        final EditText secure_code = (EditText) view.findViewById(R.id.forgetsecure);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(phone.getText().toString()).exists()) {

                            User user = dataSnapshot.child(phone.getText().toString()).getValue(User.class);

                            if (user.getSecureCode().equals(secure_code.getText().toString())) {
                                Toast.makeText(Main.this, "Your Password is :  " + user.getPassword(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Main.this, "Wrong secure code !!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Main.this, " Oops without password and securecode you must signup again ", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Main.this, "Phone Not Exist !!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

}
