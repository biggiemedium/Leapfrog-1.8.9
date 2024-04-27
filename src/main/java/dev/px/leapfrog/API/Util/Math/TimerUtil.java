package dev.px.leapfrog.API.Util.Math;

public class TimerUtil {

    private long time;

    public TimerUtil() {
        this.time = -1L;
    }

    public boolean passed(long ms) {
        return this.getTime(System.nanoTime() - this.time) >= ms;
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public long getTime(long time) {
        return time / 1000000L;
    }

    public long getTime() {
        return System.currentTimeMillis() - time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
