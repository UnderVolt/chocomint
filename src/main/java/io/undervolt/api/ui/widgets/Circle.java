package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;

import java.awt.*;

public class Circle extends Drawable {
    protected Color color;
    protected float radius;
    protected Drawable child;
    protected boolean centeredRadius;

    public Circle(Color color, float radius, Drawable child) {
        this.color = color;
        this.radius = radius;
        this.child = child;
        this.centeredRadius = false;
    }

    public Circle(Color color, float radius) {
        this(color, radius, null);
    }

    @Override
    public void load() {
        if(child != null)
            child.load();
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = radius * 2;
        this.height = radius * 2;
        if(centeredRadius)
            ui.chocomint.getRenderUtils().drawFilledCircle(x, y, radius, color.getRGB());
        else
            ui.chocomint.getRenderUtils().drawFilledCircle(x + (int) radius, y + (int) radius, radius, color.getRGB());

        if(child != null) this.child.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }

    public Circle setCenteredRadius(boolean centeredRadius) {
        this.centeredRadius = centeredRadius;
        return this;
    }
}
