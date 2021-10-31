package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import org.checkerframework.checker.units.qual.A;

public class Absolute extends Drawable {

    protected int x;
    protected int y;
    protected boolean relative;
    protected Drawable child;

    public Absolute(int x, int y, Drawable child) {
        this.x = x;
        this.y = y;
        this.child = child;
        this.child.parent = this;
    }

    public Absolute setRelative(boolean relative) {
        this.relative = relative;
        return this;
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = child.width;
        this.height = child.height;

        child.draw(ui, this.relative ? x + this.x : this.x, this.relative ? y + this.y : this.y, mouseX, mouseY, deltaTime);
        super.draw(ui, this.relative ? x + this.x : this.x, this.relative ? y + this.y : this.y, mouseX, mouseY, deltaTime);
    }

    @Override
    public void onPress(int x, int y, int button) {
        child.onPress(x, y, button);
    }

    @Override
    public void onRelease(int x, int y, int button) {
        child.onRelease(x, y, button);
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        child.onDrag(x, y, button, timeSinceLastClick);
    }

    public Absolute setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.child.x = x;
        this.child.y = y;
        return this;
    }

    public Absolute setX(int x) {
        this.x = x;
        return this;
    }

    public Absolute setY(int y) {
        this.y = y;
        return this;
    }

    public Drawable getChild() {
        return child;
    }
}
