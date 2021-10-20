package io.undervolt.api.ui;

import io.undervolt.api.ui.widgets.Container;
import io.undervolt.api.ui.widgets.Drawable;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class Overlay extends Screen {

    public OverlayPosition overlayPosition;

    protected GuiScreen parentScreen;

    public Overlay(GuiScreen parentScreen, OverlayPosition overlayPosition) {
        this.parentScreen = parentScreen;
        this.overlayPosition = overlayPosition;
    }

    public enum OverlayPosition {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
