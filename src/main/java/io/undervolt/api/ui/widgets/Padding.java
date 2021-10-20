package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;

public class Padding extends Drawable {

    protected Drawable child;
    protected EdgeInsets padding;

    public Padding(EdgeInsets padding, Drawable child) {
        this.padding = padding;
        this.child = child;
        this.child.parent = this;
    }

    @Override
    public void init() {
        this.child.init();
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = this.child.getWidth() + this.padding.right + this.padding.left;
        this.height = this.child.getHeight() + this.padding.bottom + this.padding.top;
        this.child.draw(ui, x + this.padding.left, y + this.padding.top, mouseX, mouseY, deltaTime);
    }

    @Override
    public void onPress(int x, int y, int button) {
        this.child.onPress(x, y, button);
    }

    @Override
    public void onRelease(int x, int y, int button) {
        this.child.onRelease(x, y, button);
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        this.child.onDrag(x, y, button, timeSinceLastClick);
    }
}
