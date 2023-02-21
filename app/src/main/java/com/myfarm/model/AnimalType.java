package com.myfarm.model;

public class AnimalType {
    int id;
    String img, title, backgroundColor;

    public AnimalType(int id, String img, String title, String backgroundColor) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.backgroundColor = backgroundColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
