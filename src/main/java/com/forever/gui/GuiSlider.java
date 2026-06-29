package com.forever.gui;

import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiButtonExt {
    public double sliderValue;
    public String dispString;
    public boolean dragging;
    public boolean showDecimal;
    public double minValue;
    public double maxValue;
    public int precision;
    public String suffix;
    public boolean drawString;
    public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
        super(id, xPos, yPos, width, height, prefix);
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (currentVal - this.minValue) / (this.maxValue - this.minValue);
        this.dispString = prefix;
        this.suffix = suf;
        this.showDecimal = showDec;
        this.drawString = drawStr;
        this.precision = this.showDecimal ? 1 : 0;
        updateSlider();
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                updateSlider();
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            updateSlider();
            this.dragging = true;
            return true;
        }
        return false;
    }

    public void updateSlider() {
        if (this.sliderValue < 0.0D) this.sliderValue = 0.0D;
        if (this.sliderValue > 1.0D) this.sliderValue = 1.0D;
        double val = this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
        double factor = Math.pow(10, this.precision);
        val = Math.round(val * factor) / factor;
        String displayVal;
        if (this.showDecimal) {
            displayVal = String.format(Locale.US, "%." + this.precision + "f", val);
        } else {
            displayVal = Integer.toString((int) Math.round(val));
        }
        if (this.drawString) {
            this.displayString = this.dispString + displayVal + this.suffix;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    public double getValue() {
        double raw = this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
        double factor = Math.pow(10, this.precision);
        return Math.round(raw * factor) / factor;
    }

    public int getValueInt() {
        return (int) Math.round(getValue());
    }

    public void setValue(double d) {
        this.sliderValue = (d - this.minValue) / (this.maxValue - this.minValue);
        updateSlider();
    }
}
