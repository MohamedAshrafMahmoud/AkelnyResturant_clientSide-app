package com.example.mohamed.akelnyresturant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohamed.akelnyresturant.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Manage_c extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);

    }
//////////////////////////////////////////////////////////////////////////////////////////

    public void goSignIn(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manage_c.this);
        alertDialog.setTitle("Are you sure logout");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                Intent intent = new Intent(Manage_c.this, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
/////////////////////////////////////////////////////////////////////////////////////////

    public void changePassword(View view) {
        AlertDialog.Builder alertdDialog = new AlertDialog.Builder(Manage_c.this);
        alertdDialog.setTitle("Change Password");
        alertdDialog.setMessage("please fill all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view_pwd = inflater.inflate(R.layout.changepassword, null);

        final EditText old_password = (EditText) view_pwd.findViewById(R.id.oldpassword);
        final EditText new_password = (EditText) view_pwd.findViewById(R.id.newpassword);
        final EditText repeat_password = (EditText) view_pwd.findViewById(R.id.repeatpassword);

        alertdDialog.setView(view_pwd);

        alertdDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final android.app.AlertDialog watingDialog = new SpotsDialog(Manage_c.this);
                watingDialog.show();

                //check old password
                if (old_password.getText().toString().equals(Common.currentUser.getPassword())) {
                    //check new password
                    if (new_password.getText().toString().equals(repeat_password.getText().toString())) {
                        Map<String, Object> password_update = new HashMap<>();
                        password_update.put("Password", new_password.getText().toString());

                        //make update
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getPhone()).updateChildren(password_update).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                watingDialog.dismiss();
                                Toast.makeText(Manage_c.this, "Password was updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        watingDialog.dismiss();
                        Toast.makeText(Manage_c.this, "New Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    watingDialog.dismiss();
                    Toast.makeText(Manage_c.this, "Old Password is wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertdDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertdDialog.show();
    }
}
