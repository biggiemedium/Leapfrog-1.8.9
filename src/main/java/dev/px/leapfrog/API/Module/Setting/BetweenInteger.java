package dev.px.leapfrog.API.Module.Setting;

public class BetweenInteger<T extends Number & Comparable<T>> {

    private T min;
    private T max;

    public BetweenInteger(T min, T max) {
        this.min = min;
        this.max = max;
        adjustMinMax();
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
        adjustMinMax();
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
        adjustMinMax();
    }

    public void adjustMinMax() {
        if (min.compareTo(max) > 0) {
            T temp = min;
            min = max;
            max = temp;
        }
    }
}
