package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import io.undervolt.utils.DrawUtil;
import io.undervolt.utils.GFXUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class DrawableContainer extends IWidget{

    protected Color backgroundColor;
    protected int radius;
    protected Alignment alignment;
    protected boolean overflowHidden;
    protected BorderBox box;

    protected Drawable child;

    public DrawableContainer(float width, float height, Drawable child) {
        this.width = width;
        this.height = height;
        this.child = child;
    }

    public DrawableContainer(float width, float height) {
        this.width = width;
        this.height = height;
        this.child = null;
    }

    public DrawableContainer setWidth(int width) {
        this.width = width;
        return this;
    }

    public DrawableContainer setHeight(int height) {
        this.height = height;
        return this;
    }

    public DrawableContainer setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DrawableContainer setBorderRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public DrawableContainer setChild(Drawable child) {
        this.child = child;
        return this;
    }

    public DrawableContainer setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public DrawableContainer setOverflowHidden(boolean overflowHidden) {
        this.overflowHidden = overflowHidden;
        return this;
    }

    public DrawableContainer setBorder(BorderBox box) {
        this.box = box;
        return this;
    }

    private Box tmp = new Box(0, 0);

    @Override
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        if(this.backgroundColor != null){
            if(this.radius > 0){
                if(this.box != null) {
                    DrawUtil.drawRoundedRect(x, y, width, height, radius, this.box.color);
                    DrawUtil.drawRoundedRect(x + this.box.lineWidth, y + this.box.lineWidth, width - this.box.lineWidth * 2, height - this.box.lineWidth * 2, radius, this.backgroundColor);
                }else{
                    DrawUtil.drawRoundedRect(x, y, width, height, radius, this.backgroundColor);
                }
            }else{
                if(this.box != null) {
                    GL11.glColor3f(1, 1, 1);
                    DrawUtil.drawRect(x, y, width, height, this.box.color);
                    DrawUtil.drawRect(x + this.box.lineWidth, y + this.box.lineWidth, width - this.box.lineWidth * 2, height - this.box.lineWidth * 2, this.backgroundColor);
                }else{
                    DrawUtil.drawRect(x, y, width, height, this.backgroundColor);
                }
            }
        }

        if(child != null){
            if(alignment != null){
                if(this.overflowHidden){
                    GFXUtil.enableScissor();

                    GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);

                    this.tmp = this.child.onDraw(this, ui, (int) ((int) (x + (width * alignment.xModifier)) - this.width * alignment.xModifier), (int) ((int) (y + (height * alignment.yModifier)) - this.height * alignment.yModifier), mouseX, mouseY, deltaTime);

                    GFXUtil.disableScissor();
                }else {
                    this.tmp = this.child.onDraw(this, ui, (int) ((int) (x + (width * alignment.xModifier)) - this.width * alignment.xModifier), (int) ((int) (y + (height * alignment.yModifier)) - this.height * alignment.yModifier), mouseX, mouseY, deltaTime);
                }
            }else{
                if(this.overflowHidden){
                    GFXUtil.enableScissor();
                    GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);
                    this.child.onDraw(this, ui, x, y, mouseX, mouseY, deltaTime);
                    GFXUtil.disableScissor();
                }else{
                    this.child.onDraw(this, ui, x, y, mouseX, mouseY, deltaTime);
                }
            }
        }
    }

    public interface Drawable{
        Box onDraw(IWidget parent, UIView ui, int parentX, int parentY, int mouseX, int mouseY, float deltaTime);
    }

    public static class Box{
        int width = 0;
        int height = 0;

        public Box(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

}
