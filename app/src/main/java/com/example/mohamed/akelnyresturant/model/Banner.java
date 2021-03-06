package com.example.mohamed.akelnyresturant.model;

/**
 * Created by mohamed on 4/8/18.
 */

public class Banner {

    private String id;
    private String name;
    private String image;

    public Banner(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Banner() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
