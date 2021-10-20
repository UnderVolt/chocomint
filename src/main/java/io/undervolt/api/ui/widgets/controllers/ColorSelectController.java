package io.undervolt.api.ui.widgets.controllers;

import java.awt.*;

public class ColorSelectController {

    protected boolean toggleContainer = false;
    protected boolean rainbow = false;
    protected Color color = Color.white;
    protected SelectColorCallback changeCallback;
    protected boolean enableRainbowButton = true;

    public boolean drawContainer() {
        return toggleContainer;
    }

    public Color getColor() {
        return color;
    }

    public ColorSelectController setChangeCallback(SelectColorCallback changeCallback) {
        this.changeCallback = changeCallback;
        return this;
    }

    public ColorSelectController setToggleContainer(boolean toggleContainer) {
        this.toggleContainer = toggleContainer;
        return this;
    }

    public ColorSelectController setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
        return this;
    }

    public ColorSelectController enableRainbowButton(boolean enableRainbowButton) {
        this.enableRainbowButton = enableRainbowButton;
        return this;
    }

    public boolean drawRainbowButton() {
        return enableRainbowButton;
    }

    public void toggleContainer() {
        this.toggleContainer = !this.toggleContainer;
    }

    public void toggleRainbow(){
        this.rainbow = !this.rainbow;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public ColorSelectController setColor(Color color) {
        this.color = color;
        if(this.changeCallback != null){
            this.changeCallback.onChange(color);
        }
        return this;
    }

    public interface SelectColorCallback{
        void onChange(Color c);
    }
}
