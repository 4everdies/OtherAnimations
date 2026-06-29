package com.forever.gui;

import com.forever.OtherAnimations;
import com.forever.config.Config;
import com.forever.module.modules.Animations;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

public class GuiAnimations extends GuiScreen {
    private Animations animations;
    private GuiSlider sliderScale;
    private GuiSlider sliderSwingSpeed;
    private GuiSlider sliderPosX;
    private GuiSlider sliderPosY;
    private GuiSlider sliderPosZ;
    private GuiSlider sliderRotX;
    private GuiSlider sliderRotY;
    private GuiSlider sliderRotZ;
    private GuiButton buttonMode;
    private GuiButton buttonItemSwitch;
    private GuiButton buttonSway;

    public GuiAnimations() {
        this.animations = OtherAnimations.animations;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        int center = this.width / 2;
        int rowStart = this.height / 4 - 24;
        this.buttonList.add(this.buttonMode = new GuiButton(0, center - 160, rowStart, 150, 20, "Mode: " + animations.mode.getModeString()));
        this.buttonList.add(this.sliderScale = new GuiSlider(1, center - 160, rowStart + 24, 150, 20, "Scale: ", "%", animations.scale.getMin(), animations.scale.getMax(), animations.scale.getValue(), false, true));
        this.buttonList.add(this.sliderSwingSpeed = new GuiSlider(2, center - 160, rowStart + 48, 150, 20, "Swing Speed: ", "", animations.swingSpeed.getMin(), animations.swingSpeed.getMax(), animations.swingSpeed.getValue(), true, true));
        this.buttonList.add(this.buttonItemSwitch = new GuiButton(3, center - 160, rowStart + 72, 150, 20, "Item Switch: " + animations.itemSwitchMode.getModeString()));
        this.buttonList.add(this.buttonSway = new GuiButton(4, center - 160, rowStart + 96, 150, 20, "Hand Sway: " + getSwayText()));
        this.buttonList.add(this.sliderPosX = new GuiSlider(5, center + 10, rowStart, 150, 20, "Pos X: ", "", animations.posX.getMin(), animations.posX.getMax(), animations.posX.getValue(), true, true));
        this.buttonList.add(this.sliderPosY = new GuiSlider(6, center + 10, rowStart + 24, 150, 20, "Pos Y: ", "", animations.posY.getMin(), animations.posY.getMax(), animations.posY.getValue(), true, true));
        this.buttonList.add(this.sliderPosZ = new GuiSlider(7, center + 10, rowStart + 48, 150, 20, "Pos Z: ", "", animations.posZ.getMin(), animations.posZ.getMax(), animations.posZ.getValue(), true, true));
        this.buttonList.add(this.sliderRotX = new GuiSlider(8, center + 10, rowStart + 72, 150, 20, "Rot X: ", "", animations.rotX.getMin(), animations.rotX.getMax(), animations.rotX.getValue(), true, true));
        this.buttonList.add(this.sliderRotY = new GuiSlider(9, center + 10, rowStart + 96, 150, 20, "Rot Y: ", "", animations.rotY.getMin(), animations.rotY.getMax(), animations.rotY.getValue(), true, true));
        this.buttonList.add(this.sliderRotZ = new GuiSlider(10, center + 10, rowStart + 120, 150, 20, "Rot Z: ", "", animations.rotZ.getMin(), animations.rotZ.getMax(), animations.rotZ.getValue(), true, true));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (sliderScale.dragging) animations.scale.setValue((float) sliderScale.getValue());
        if (sliderSwingSpeed.dragging) animations.swingSpeed.setValue((float) sliderSwingSpeed.getValue());
        if (sliderPosX.dragging) animations.posX.setValue((float) sliderPosX.getValue());
        if (sliderPosY.dragging) animations.posY.setValue((float) sliderPosY.getValue());
        if (sliderPosZ.dragging) animations.posZ.setValue((float) sliderPosZ.getValue());
        if (sliderRotX.dragging) animations.rotX.setValue((float) sliderRotX.getValue());
        if (sliderRotY.dragging) animations.rotY.setValue((float) sliderRotY.getValue());
        if (sliderRotZ.dragging) animations.rotZ.setValue((float) sliderRotZ.getValue());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                cycleMode(animations.mode); 
                button.displayString = "Mode: " + animations.mode.getModeString();
                break;
            case 3:
                cycleMode(animations.itemSwitchMode);
                button.displayString = "Item Switch: " + animations.itemSwitchMode.getModeString();
                break;
            case 4:
                animations.disableHandSway.setValue(!animations.disableHandSway.getValue());
                button.displayString = "Hand Sway: " + getSwayText();
                break;
        }
        Config.save(animations);
    }

    @Override
    public void onGuiClosed() {
        Config.save(animations);
    }
    
    private void cycleMode(com.forever.property.properties.ModeProperty modeProp) {
        int currentIndex = java.util.Arrays.asList(modeProp.getModes()).indexOf(modeProp.getModeString());
        int nextIndex = (currentIndex + 1) % modeProp.getModes().length;
        modeProp.setValue(nextIndex);
    }

    private String getSwayText() {
        return animations.disableHandSway.getValue() ? (EnumChatFormatting.RED + "Disabled") : (EnumChatFormatting.GREEN + "Enabled");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }
}
