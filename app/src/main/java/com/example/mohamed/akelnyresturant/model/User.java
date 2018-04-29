package com.example.mohamed.akelnyresturant.model;

/**
 * Created by mohamed on 3/21/18.
 */

public class User {

    private String name, phone, password, isStaff, secureCode;

    //we remove phone from request because may be it will be null

    public User(String name, String password, String SecureCode) {
        this.name = name;
        this.password = password;
        this.isStaff = "false";
        this.secureCode = SecureCode;
    }

    public User() {
    }


    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
