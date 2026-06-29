package com.forever.property.properties;

import com.forever.property.Property;
public class ModeProperty extends Property<Integer> {
    private String[] modes;
    public ModeProperty(String name, int value, String[] modes) {
        super(name, value);
        this.modes = modes;
    }
    public String[] getModes() { return modes; }
    public String getModeString() { return modes[value]; }
}
