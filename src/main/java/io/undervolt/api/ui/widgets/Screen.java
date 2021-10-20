package io.undervolt.api.ui.widgets;

public class Screen extends Drawable {

    protected Drawable child;

    public Screen(Drawable child) {
        this.child = child;
        this.child.parent = this;
    }

    @Override
    public void init() {
        this.child.init();
    }

    @Override
    public void draw(io.undervolt.api.ui.Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
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
