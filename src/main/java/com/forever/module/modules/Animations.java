package com.forever.module.modules;

import com.forever.property.properties.BooleanProperty;
import com.forever.property.properties.FloatProperty;
import com.forever.property.properties.ModeProperty;

public class Animations {
    public static boolean forceSwing = false;
    public String name;
    public boolean enabled;
    public final ModeProperty mode = new ModeProperty("mode", 1, new String[]{
        "Vanilla",
        "1.7",
        "Exhibition",
        "ETB",
        "Sigma",
        "Spin",
        "Slide",
        "Swong",
        "Swang",
        "Punch"
    });
    public final FloatProperty scale = new FloatProperty("scale", 100.0F, 15.0F, 200.0F);
    public final FloatProperty swingSpeed = new FloatProperty("swing-speed", 6.0F, 1.0F, 20.0F);
    public final FloatProperty posX = new FloatProperty("pos-x", 0.0F, -2.0F, 2.0F);
    public final FloatProperty posY = new FloatProperty("pos-y", 0.0F, -2.0F, 2.0F);
    public final FloatProperty posZ = new FloatProperty("pos-z", 0.0F, -2.0F, 2.0F);
    public final FloatProperty rotX = new FloatProperty("rot-x", 0.0F, -180.0F, 180.0F);
    public final FloatProperty rotY = new FloatProperty("rot-y", 0.0F, -180.0F, 180.0F);
    public final FloatProperty rotZ = new FloatProperty("rot-z", 0.0F, -180.0F, 180.0F);
    public final BooleanProperty disableHandSway = new BooleanProperty("disable-hand-sway", false);
    public final ModeProperty itemSwitchMode = new ModeProperty("item-switch-mode", 1, new String[]{"Disabled", "1.7", "1.8"});
    public Animations() {
        this.name = "Animations";
        this.enabled = false;
    }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getCurrentMode() {
        return this.mode.getModeString().toUpperCase();
    }
    public String[] getSuffix() {
        return new String[]{this.getCurrentMode()};
    }
}
