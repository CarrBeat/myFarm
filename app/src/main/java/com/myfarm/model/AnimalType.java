package com.myfarm.model;

public class AnimalType {
    int id, category;
    String img, title, backgroundColor;

    public AnimalType(int id, String img, String title, String backgroundColor, int category) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
