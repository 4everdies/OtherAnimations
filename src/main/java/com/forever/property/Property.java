package com.forever.property;

public class Property<T> {
    public String name;
    public T value;
    public Property(String name, T value) {
        this.name = name;
        this.value = value;
    }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}
