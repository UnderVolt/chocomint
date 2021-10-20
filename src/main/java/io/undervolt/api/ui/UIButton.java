package io.undervolt.api.ui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class UIButton extends GuiButton {

    private final Chocomint chocomint;

    private UIAction action;
    private Color bg;

    public UIButton(int x, int y, String txt, Color bg, UIAction act) {
        super(-1, x, y, txt);
        this.bg = bg;
        this.action = act;
        this.chocomint = GameBridge.getChocomint();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.chocomint.getRenderUtils().drawRoundedRect(this.xPosition, this.yPosition, 60, 20, 5, this.bg.getRGB());
        this.hovered = mouseX > this.xPosition && mouseY > this.yPosition && mouseX < this.xPosition + 60 && mouseY < this.yPosition + 20;
        this.displayString = this.hovered ? "Hovering" : "No Hovering";
        this.drawCenteredString(mc.fontRendererObj, this.displayString, this.xPosition + 30, this.yPosition + 5, -1);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(this.hovered) this.action.onDispath(this);
        return this.hovered;
    }

    public interface UIAction{
        void onDispath(UIButton btn);
    }
}
