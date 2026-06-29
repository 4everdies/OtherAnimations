package com.forever.config;

import com.forever.module.modules.Animations;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.Properties;

public class Config {
    private static File getFile() {
        return new File(Minecraft.getMinecraft().mcDataDir, "config" + File.separator + "otheranimation.cfg");
    }

    public static void load(Animations anim) {
        File file = getFile();
        if (!file.exists()) return;
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
            anim.mode.setValue(Integer.parseInt(props.getProperty("mode", "1")));
            anim.scale.setValue(Float.parseFloat(props.getProperty("scale", "100.0")));
            anim.swingSpeed.setValue(Float.parseFloat(props.getProperty("swing-speed", "6.0")));
            anim.posX.setValue(Float.parseFloat(props.getProperty("pos-x", "0.0")));
            anim.posY.setValue(Float.parseFloat(props.getProperty("pos-y", "0.0")));
            anim.posZ.setValue(Float.parseFloat(props.getProperty("pos-z", "0.0")));
            anim.rotX.setValue(Float.parseFloat(props.getProperty("rot-x", "0.0")));
            anim.rotY.setValue(Float.parseFloat(props.getProperty("rot-y", "0.0")));
            anim.rotZ.setValue(Float.parseFloat(props.getProperty("rot-z", "0.0")));
            anim.disableHandSway.setValue(Boolean.parseBoolean(props.getProperty("disable-hand-sway", "false")));
            anim.itemSwitchMode.setValue(Integer.parseInt(props.getProperty("item-switch-mode", "1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(Animations anim) {
        Properties props = new Properties();
        props.setProperty("mode", String.valueOf(anim.mode.getValue()));
        props.setProperty("scale", String.valueOf(anim.scale.getValue()));
        props.setProperty("swing-speed", String.valueOf(anim.swingSpeed.getValue()));
        props.setProperty("pos-x", String.valueOf(anim.posX.getValue()));
        props.setProperty("pos-y", String.valueOf(anim.posY.getValue()));
        props.setProperty("pos-z", String.valueOf(anim.posZ.getValue()));
        props.setProperty("rot-x", String.valueOf(anim.rotX.getValue()));
        props.setProperty("rot-y", String.valueOf(anim.rotY.getValue()));
        props.setProperty("rot-z", String.valueOf(anim.rotZ.getValue()));
        props.setProperty("disable-hand-sway", String.valueOf(anim.disableHandSway.getValue()));
        props.setProperty("item-switch-mode", String.valueOf(anim.itemSwitchMode.getValue()));
        File file = getFile();
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "OtherAnimations Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
