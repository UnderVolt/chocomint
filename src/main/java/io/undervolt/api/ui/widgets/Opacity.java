package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import org.lwjgl.opengl.GL11;

public class Opacity extends IWidget {

    protected float opacity;
    protected IWidget child;

    public Opacity(float opacity, IWidget child) {
        this.opacity = opacity;
        this.child = child;
        this.child.parent = this;
    }

    public Opacity setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    @Override
    public void init() {
        this.child.init();
    }

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        if(this.opacity > 0){
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, this.opacity);
            this.child.draw(ui, x, y, mouseX, mouseY, deltaTime);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onPress(int x, int y, int button) {
        if(this.opacity > 0) this.child.onPress(x, y, button);
    }

    @Override
    public void onRelease(int x, int y, int button) {
        if(this.opacity > 0) this.child.onRelease(x, y, button);
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        if(this.opacity > 0) this.child.onDrag(x, y, button, timeSinceLastClick);
    }
}
