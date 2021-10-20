package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;

public class Screen extends IWidget {

    protected IWidget child;

    public Screen(IWidget child) {
        this.child = child;
        this.child.parent = this;
    }

    @Override
    public void init() {
        this.child.init();
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = ui.getWidth();
        this.height = ui.getHeight();
        this.child.draw(ui, x, y, mouseX, mouseY, deltaTime);
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
