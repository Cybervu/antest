package com.home.vod.model;

/**
 * Created by Android on 10/31/2017.
 */

public class NotificationModel {

    private String notification = "";

    public NotificationModel(String notification) {
        this.notification = notification;

    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

}
