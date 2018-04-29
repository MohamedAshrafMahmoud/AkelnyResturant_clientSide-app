package com.example.mohamed.akelnyresturant.model;

/**
 * Created by mohamed on 4/24/18.
 */

public class Favorites {

    private String FoodId,FoodName,FoodPrice,FoodMenuId,FoodImge,FoodDiscount,FoodDiscription,UserPhone;

    public Favorites() {
    }

    public Favorites(String foodId, String foodName, String foodPrice, String foodMenuId, String foodImge, String foodDiscount, String foodDiscription, String userPhone) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodMenuId = foodMenuId;
        FoodImge = foodImge;
        FoodDiscount = foodDiscount;
        FoodDiscription = foodDiscription;
        UserPhone = userPhone;

    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(String foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public String getFoodImge() {
        return FoodImge;
    }

    public void setFoodImge(String foodImge) {
        FoodImge = foodImge;
    }

    public String getFoodDiscount() {
        return FoodDiscount;
    }

    public void setFoodDiscount(String foodDiscount) {
        FoodDiscount = foodDiscount;
    }

    public String getFoodDiscription() {
        return FoodDiscription;
    }

    public void setFoodDiscription(String foodDiscription) {
        FoodDiscription = foodDiscription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
