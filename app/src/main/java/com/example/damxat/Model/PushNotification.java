package com.example.damxat.Model;

public class PushNotification {
    public PushNotification(String to, NotificationModel notification) {
        this.to = to;
        this.notification = notification;
    }

    String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }

    NotificationModel notification;
}
