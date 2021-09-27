package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;

import java.awt.*;
import java.util.function.Consumer;

public class CircularGameBarButton extends GameBarButton {

    private final Chocomint chocomint;
    private int radius;

    public CircularGameBarButton(int x, int y, int radius, String buttonText, Consumer consumer) {
        super(x, y, radius * 2, radius * 2, buttonText, consumer);
        this.chocomint = GameBridge.getChocomint();
        this.radius = radius;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        boolean isOverButton = (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (
                mouseY <= this.y + this.height);
        int c = new Color(32,34,37).getRGB();
        if(enabled) {
            if (isOverButton)
                c = new Color(54, 57, 63).getRGB();
        }

        this.chocomint.getRenderUtils().drawFilledCircle(this.x + radius, this.y + radius, this.radius, c);
        drawCenteredString(mc.fontRendererObj, this.buttonText, this.x + (this.width / 2), this.y + (this.height / 3), Color.WHITE.getRGB());
    }
}
