package com.forever.property.properties;

import com.forever.property.Property;
public class FloatProperty extends Property<Float> {
    private float min, max;
    public FloatProperty(String name, float value, float min, float max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }
    public float getMin() { return min; }
    public float getMax() { return max; }
}
