package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.utils.MathUtil;
import org.lwjgl.input.Mouse;

public class Scrollable extends Drawable {

    protected int dWheelVal = 0;
    protected float scrollModifier = 0;
    protected ScrollDirection direction;
    protected Drawable child;

    public Scrollable(ScrollDirection direction, Drawable child) {
        this.direction = direction;
        this.child = child;
        this.child.parent = this;
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        float size = this.direction.equals(ScrollDirection.ROW) ? this.child.width : this.child.height;
        float parentSize = this.direction.equals(ScrollDirection.ROW) ? this.parent.width : this.parent.height;
        float pos = this.direction.equals(ScrollDirection.ROW) ? x : y;

        this.width = this.parent.width;
        this.height = this.parent.height;

        boolean tmp = this.scrollModifier >= -(size - parentSize);
        boolean hovered = mouseX > x && mouseY > y && mouseX < x + this.getWidth() && mouseY < y + this.getHeight();

        if(hovered){
            if(size > parentSize && (pos - this.scrollModifier) >= pos){
                if(tmp){
                    this.dWheelVal += Mouse.getDWheel() / 2;
                    this.scrollModifier = MathUtil.Lerp(this.scrollModifier, this.dWheelVal, 0.15f);
                }else{
                    int tmp2 = (int) -Math.ceil(size - parentSize);
                    this.dWheelVal = tmp2;
                    this.scrollModifier = tmp2;
                }
            }else{
                this.dWheelVal = 0;
                this.scrollModifier = 0;
            }
        }else {
            this.dWheelVal = (int) Math.ceil(this.scrollModifier);
        }

        switch (this.direction){
            case COLUMN:
                child.draw(ui, x, (int) (y + this.scrollModifier), mouseX, mouseY, deltaTime);
                break;
            case ROW:
                child.draw(ui, (int) (x + this.scrollModifier), y, mouseX, mouseY, deltaTime);
                break;
        }
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
}
