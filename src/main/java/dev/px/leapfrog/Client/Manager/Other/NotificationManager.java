package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.util.PriorityQueue;
import java.util.Queue;

public class NotificationManager {

    private Queue<Notification> notifications = new PriorityQueue<>();

    public NotificationManager() {

    }

    public void pushNotification(String title, String message, Notification.NotificationType type, int time) {
       // notifications.add(new Notification(title, message, type, time));
    }

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {
        for(Notification n : notifications) {

        }
    });


}
