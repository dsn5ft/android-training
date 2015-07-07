package com.niz.android.training.api.model;

public class Image {

    private String url;
    private String title;
    private int width;
    private int height;

    public Image() {}

    public Image(String url, String title, int width, int height) {
        this.url = url;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
