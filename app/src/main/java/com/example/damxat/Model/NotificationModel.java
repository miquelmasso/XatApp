package com.example.damxat.Model;

import android.media.Image;

public class NotificationModel {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public NotificationModel(String title, String body, Image image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }

    String title;
    String body;
    Image image;
}
