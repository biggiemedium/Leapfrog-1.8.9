package dev.px.leapfrog.Client.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Setting<T> {

    private String name;

    private T value;
    private T min;
    private T max;

    private Predicate<T> visibility;
    private ArrayList<T> comboBox;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Setting(String name, T value, T... values) {
        this.name = name;
        this.value = value;
        this.comboBox = new ArrayList<>(Arrays.asList(values));
    }

    public Setting(String name, T value, T min, T max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public Setting(String name, T value, Predicate<T> visibility) {
        this.name = name;
        this.value = value;
        this.visibility = visibility;
    }

    public Setting(String name, T value, T min, T max, Predicate<T> visibility) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public boolean isNumberSetting() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Character) && !(this.value instanceof Boolean);
    }

    public boolean isVisible() {
        if (this.visibility == null) {
            return true;
        }
        return this.visibility.test(this.getValue());
    }

}
