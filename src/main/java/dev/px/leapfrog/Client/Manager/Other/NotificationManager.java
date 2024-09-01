package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

    private CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public NotificationManager() {

    }

    public void post(String title, String description, Notification.NotificationType type) {
        this.post(new Notification(title, description, type, 2));
    }

    public void post(String title, String description, Notification.NotificationType type, int time) {
        this.post(new Notification(title, description, type, time));
    }

    private void post(Notification notification) {
        notifications.add(notification);
    }

    public CopyOnWriteArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(CopyOnWriteArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {

    });
}
