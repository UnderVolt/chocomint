package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;

import java.awt.*;

public class Circle extends Drawable {
    protected Color color;
    protected float radius;
    protected Drawable child;

    public Circle(Color color, float radius, Drawable child) {
        this.color = color;
        this.radius = radius;
        this.child = child;
    }

    public Circle(Color color, float radius) {
        this(color, radius, null);
    }

    @Override
    public void init() {
        child.init();
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = radius * 2;
        this.height = radius * 2;
        ui.chocomint.getRenderUtils().drawFilledCircle(x + (int) radius, y + (int) radius, radius, color.getRGB());

        if(child != null) this.child.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }
}
