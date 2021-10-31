package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;

public class Gesture extends Drawable {

    protected Function onPress;
    protected Function onDoublePress;
    protected Function onRelease;
    protected DragFunction onDrag;
    protected HoverFunction onHover;
    protected int consecutiveClickPress = 0;
    protected int wX = 0;
    protected int wY = 0;

    protected Drawable child;

    public Gesture(Drawable child) {
        this.child = child;
        this.child.parent = this;
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.width = child.width;
        this.height = child.height;
        this.wX = x;
        this.wY = y;

        if(mouseX > x && mouseY > y && mouseX < x + this.child.width && mouseY < y + this.child.height && this.onHover != null) this.onHover.onRun(child, mouseX, mouseY);

        this.child.draw(ui, x, y, mouseX, mouseY, deltaTime);
        super.draw(ui, x, y, mouseX, mouseY, deltaTime);
    }

    public Gesture onPress(Function onPress) {
        this.onPress = onPress;
        return this;
    }

    public Gesture onDoublePress(Function onDoublePress) {
        this.onDoublePress = onDoublePress;
        return this;
    }

    public Gesture onRelease(Function onRelease) {
        this.onRelease = onRelease;
        return this;
    }

    public Gesture onDrag(DragFunction onDrag) {
        this.onDrag = onDrag;
        return this;
    }

    public Gesture onHover(HoverFunction onHover) {
        this.onHover = onHover;
        return this;
    }

    @Override
    public void load() {
        child.load();
    }

    @Override
    public void onPress(int x, int y, int button) {

        child.onPress(x, y, button);

        if(x > this.wX && y > this.wY && x < this.wX + this.child.width && y < this.wY + this.child.height){
            if(this.onPress != null){
                this.onPress.onRun(this.child, x, y, button);
            }

            if(this.onDoublePress != null){
                if(this.consecutiveClickPress == 2){
                    this.onDoublePress.onRun(this.child, x, y, button);
                    this.consecutiveClickPress = 0;
                }
                this.consecutiveClickPress++;
            }
        }
    }

    @Override
    public void onRelease(int x, int y, int button) {
        child.onRelease(x, y, button);
        if(x > this.wX && y > this.wY && x < this.wX + this.child.width && y < this.wY + this.child.height){
            if(this.onRelease != null){
                this.onRelease.onRun(this.child, x, y, button);
            }
        }
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        child.onDrag(x, y, button, timeSinceLastClick);
        if(x > this.wX && y > this.wY && x < this.wX + this.child.width && y < this.wY + this.child.height){
            if(this.onDrag != null){
                this.onDrag.onRun(this.child, x, y, button, timeSinceLastClick);
            }
        }
    }

    public interface Function{
        void onRun(Drawable child, int mouseX, int mouseY, int button);
    }

    public interface HoverFunction{
        void onRun(Drawable child, int mouseX, int mouseY);
    }

    public interface DragFunction{
        void onRun(Drawable child, int mouseX, int mouseY, int button, long timeSinceLastClick);
    }

    public Drawable getChild() {
        return child;
    }
}
