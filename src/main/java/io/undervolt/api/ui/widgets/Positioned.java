package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;

public class Positioned extends Drawable {

    protected Alignment alignment;
    protected Drawable child;
    protected Alignment childAlign = Alignment.TOP_LEFT;

    public Positioned(Alignment alignment, Drawable child) {
        this.alignment = alignment;
        this.child = child;
        this.child.parent = this;
    }

    public Positioned setChildAlign(Alignment alignment) {
        this.childAlign = alignment;
        return this;
    }

    @Override
    public void init() {
        child.init();
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

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = this.child.width;
        this.height = this.child.height;

        if(this.parent != null){
            if(this.childAlign != null){
                child.draw(ui, (int) ((int) ((this.parent.width * this.alignment.xModifier)) - child.width * alignment.xModifier), (int) ((int) ((this.parent.height * this.alignment.yModifier)) - child.height * alignment.yModifier), mouseX, mouseY, deltaTime);
            }else {
                this.child.draw(ui, (int)(this.parent.width * this.alignment.xModifier), (int)(this.parent.height * this.alignment.yModifier), mouseX, mouseY, deltaTime);
            }
        }
    }
}
