package com.example.mohamed.akelnyresturant.Service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.example.mohamed.akelnyresturant.OrderStatus_c;
import com.example.mohamed.akelnyresturant.R;
import com.example.mohamed.akelnyresturant.model.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Requests");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        databaseReference.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        Request request=dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(),request);

    }
///////////////////////////////////////////////////////////////////////////////////////////
    private void showNotification(String key, Request request) {

        Intent intent=new Intent(getBaseContext() ,OrderStatus_c.class);
        intent.putExtra("userPhone",request.getPhone());
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("EDMTDev")
                .setContentInfo("Your Order was updated")
                .setContentText("Order #"+key+"was update status to "+convertCodeToStatus(request.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.drawable.ic_restaurant_black_24dp);

        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

///////////////////////////////////////////////////////////////////////////////////////////////
    public static String convertCodeToStatus(String code) {

        if (code.equals("0"))
            return "Placed";

        else if (code.equals("1"))
            return "On the way";
        else
            return "Shipping";

    }
///////////////////////////////////////////////////////////////////////////////////////////////
}
