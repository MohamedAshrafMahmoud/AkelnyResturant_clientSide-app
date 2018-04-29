package com.example.mohamed.akelnyresturant.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.mohamed.akelnyresturant.model.Favorites;
import com.example.mohamed.akelnyresturant.model.Order;

import java.util.ArrayList;

/**
 * Created by mohamed on 4/9/18.
 */

public class DataBase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "akelnyDB";

    private static final String TABLE_ORDERS = "Orders";
    private static final String TABLE_FAVORITES = "Favorite";


    private static final String KEY_ID = "Id";
    private static final String userPhone = "UserPhone";
    private static final String Id = "ProductId";
    private static final String Name = "ProductName";
    private static final String Quantity = "Quantity";
    private static final String Price = "Price";
    private static final String Discount = "Discount";
    private static final String Image = "Image";

    private static final String ID = "FoodId";
    private static final String UserPhone = "UserPhone";
    private static final String FoodName = "FoodName";
    private static final String FoodPrice = "FoodPrice";
    private static final String FoodMenuId = "FoodMenuId";
    private static final String FoodImage = "FoodImage";
    private static final String FoodDiscount = "FoodDiscount";
    private static final String FoodDescription = "FoodDescription";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String Table_orders = "CREATE TABLE " + TABLE_ORDERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + userPhone + " TEXT,"+ Id + " TEXT," + Name + " TEXT," + Quantity + " TEXT," + Price + " TEXT,"
                + Discount + " TEXT," + Image + " TEXT);";

        String Table_favorites = "CREATE TABLE " + TABLE_FAVORITES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FoodName + " TEXT,"
                + FoodPrice + " TEXT," + FoodMenuId + " TEXT," + FoodImage + " TEXT," + FoodDiscount + " TEXT,"
                + FoodDescription + " TEXT," + UserPhone + " TEXT);";


        db.execSQL(Table_orders);
        db.execSQL(Table_favorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        onCreate(db);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addToCart(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, order.getId());    // values.put(KEY_ID, 1);
        values.put(userPhone, order.getUserPhone());
        values.put(Id, order.getProductId());
        values.put(Name, order.getProductName());
        values.put(Quantity, order.getQuantity());
        values.put(Price, order.getPrice());
        values.put(Discount, order.getDiscount());
        values.put(Image, order.getImage());

        db.insert(TABLE_ORDERS, null, values);    ///rerturn long
        //return data;
        db.close();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Order> getAllFromCart() {
        ArrayList<Order> contactList = new ArrayList<Order>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(0));
                order.setUserPhone(cursor.getString(1));
                order.setProductId(cursor.getString(2));
                order.setProductName(cursor.getString(3));
                order.setQuantity(cursor.getString(4));
                order.setPrice(cursor.getString(5));
                order.setDiscount(cursor.getString(6));
                order.setImage(cursor.getString(7));
                contactList.add(order);
            } while (cursor.moveToNext());
        }

        return contactList;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteFromCart() {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("DELETE FROM " + TABLE_ORDERS);
        db.execSQL(query);
    }

    public int deleteFromCart2(String id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String[] whereargs = {id};

        int count = sqLiteDatabase.delete(TABLE_ORDERS, Id + " =? ", whereargs);
        return count;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public int getCountCart() {

        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = String.format("SELECT COUNT(*) FROM Orders ;");
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;
    }



    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ORDERS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateCart(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = String.format("UPDATE Orders SET " + Quantity + "= %s WHERE Id =%d", order.getQuantity(), order.getId());
        db.execSQL(selectQuery);

    }


    public int updateCart2(String oldid, String newid) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Id, newid);

        String[] whereargs = {oldid};

        int count = sqLiteDatabase.update(TABLE_ORDERS, contentValues, Id + " =? ", whereargs);
        return count;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addToFavorite(Favorites favorites) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("INSERT INTO Favorite (FoodId,FoodName,FoodPrice,FoodMenuId,FoodImage,FoodDiscount,FoodDescription,UserPhone) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');",
                favorites.getFoodId()
                , favorites.getFoodName()
                , favorites.getFoodPrice()
                , favorites.getFoodMenuId()
                , favorites.getFoodImge()
                , favorites.getFoodDiscount()
                , favorites.getFoodDiscription()
                , favorites.getUserPhone());
        db.execSQL(query);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isFavorite(String foodId, String userPhone) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM Favorite Where FoodId='%s' and UserPhone='%s';", foodId, userPhone);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteFromFavorite(String foodId, String userPhone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("DELETE FROM Favorite Where FoodId ='%s' and UserPhone='%s';", foodId, userPhone);
        db.execSQL(query);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Favorites> getAllFromFavorites() {
        ArrayList<Favorites> contactList = new ArrayList<Favorites>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Favorites favorites = new Favorites();
                favorites.setFoodId(cursor.getString(0));
                favorites.setFoodName(cursor.getString(1));
                favorites.setFoodPrice(cursor.getString(2));
                favorites.setFoodMenuId(cursor.getString(3));
                favorites.setFoodImge(cursor.getString(4));
                favorites.setFoodDiscount(cursor.getString(5));
                favorites.setFoodDiscription(cursor.getString(6));
                favorites.setUserPhone(cursor.getString(7));

                contactList.add(favorites);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

}


