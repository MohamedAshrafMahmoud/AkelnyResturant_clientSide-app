package com.example.mohamed.akelnyresturant.Common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mohamed.akelnyresturant.model.Request;
import com.example.mohamed.akelnyresturant.model.User;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mohamed on 3/21/18.
 */

public class Common {

    public static User currentUser;
    public static Request currentRequest;

    public static final String Delete="delete";

    //for check remember me
    public static final String USER_KEY="user";
    public static final String PASSWOED_KEY="password";



    public static final String Intent_Food_ID="FoodId";



    public static boolean isConnectToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static String getDate(long time){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date=new StringBuilder(android.text.format.DateFormat.format("dd/MM/yyyy     HH:mm",calendar).toString());
        return date.toString();
    }



}
