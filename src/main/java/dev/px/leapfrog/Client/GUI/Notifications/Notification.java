package dev.px.leapfrog.Client.GUI.Notifications;

import dev.px.leapfrog.API.Util.Math.TimerUtil;

public class Notification {

    private String title, message;
    private int x, y;
    private int width, height;
    private float time;
    private TimerUtil timer;
    private NotificationType type;

    public Notification(String title, String message, NotificationType type, float time) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timer = new TimerUtil();
        this.time = (float) (time * 1000);
    }

    public void render() {
        switch (type) {
            case INFO:

                break;

            case Warning:

                break;

            case Error:

                break;
        }
    }

    public enum NotificationType {
        INFO,
        Warning,
        Error
    }
}
