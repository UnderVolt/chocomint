package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;

public class Expanded extends IWidget {

    protected IWidget child;
    protected ExpandedMode mode;

    public Expanded(ExpandedMode mode, IWidget child) {
        this.child = child;
        this.child.parent = this;
        this.mode = mode;
    }

    @Override
    public void init() {
        this.child.init();
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {

        switch (this.mode){
            case WIDTH: {
                this.width = this.parent.width;
                this.child.width = this.parent.width;
                this.height = this.child.height;
                break;
            }
            case HEIGHT: {
                this.width = this.child.width;
                this.height = this.parent.height;
                this.child.height = this.parent.height;
                break;
            }
            case BOTH: {
                this.width = this.parent.width;
                this.child.width = this.parent.width;
                this.height = this.parent.height;
                this.child.height = this.parent.height;
            }
        }

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
