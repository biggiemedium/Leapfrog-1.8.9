package dev.px.leapfrog.API.Util.Math.ADT;

public class Tuple<K, V, O> {

    private K key;
    private V value;
    private O other;

    public Tuple(K key, V value, O other) {
        this.key = key;
        this.value = value;
        this.other = other;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public O getOther() {
        return other;
    }

    public void setOther(O other) {
        this.other = other;
    }
}
