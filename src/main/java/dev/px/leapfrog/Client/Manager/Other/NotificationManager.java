package dev.px.leapfrog.Client.Manager.Other;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.Client.GUI.Notifications.Notification;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class NotificationManager {

    private Queue<Notification> notifications = new PriorityQueue<>(Comparator.comparingLong(Notification::getEnd));

    public NotificationManager() {

    }

    public void pushNotification(String title, String message, Notification.NotificationType type, int length) {
        Notification notification = new Notification(message, length);
        notifications.add(notification);
        notification.show();
    }

    @EventHandler
    private Listener<Render2DEvent> render2DEventListener = new Listener<>(event -> {
        notifications.removeIf(notification -> !notification.isShown());
        int yOffset = 10;
        for (Notification notification : notifications) {
            notification.render();
            yOffset += notification.getHeight() + 5;
        }
    });


}
