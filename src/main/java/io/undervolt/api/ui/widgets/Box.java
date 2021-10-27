package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.GFXUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Box extends Drawable {

    protected Color backgroundColor;
    protected EdgeInsets radius;
    protected Alignment alignment;
    protected boolean overflowHidden;
    protected BorderBox box;
    protected boolean adjustToChildSize = true;

    protected Drawable child;

    public Box(float width, float height, Drawable child) {
        this.width = width;
        this.height = height;
        this.child = child;
        this.child.parent = this;
        this.adjustToChildSize = false;
    }

    public Box(Drawable child) {
        this.width = child.width;
        this.height = child.height;
        this.child = child;
        this.child.parent = this;
    }

    public Box(float width, float height) {
        this.width = width;
        this.height = height;
        this.child = null;
        this.adjustToChildSize = false;
    }

    public Box(float width, float height, Color colour, Drawable child) {
        this.width = width;
        this.height = height;
        this.child = child;
        this.backgroundColor = colour;
        this.adjustToChildSize = false;
    }

    public Box(float width, float height, Color colour) {
        this.width = width;
        this.height = height;
        this.backgroundColor = colour;
        this.child = null;
        this.adjustToChildSize = false;
    }

    public Box(Color colour, Drawable child) {
        this.width = child.width;
        this.height = child.height;
        this.backgroundColor = colour;
        this.child = child;
        this.child.parent = this;
    }

    public Box setAdjustToChildSize(boolean adjustToChildSize) {
        this.adjustToChildSize = adjustToChildSize;
        return this;
    }

    public Box setWidth(int width) {
        this.width = width;
        return this;
    }

    public Box setHeight(int height) {
        this.height = height;
        return this;
    }

    public Box setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Box setBorderRadius(EdgeInsets radius) {
        this.radius = radius;
        return this;
    }

    public Box setChild(Drawable child) {
        this.child = child;
        this.child.parent = this;
        child.load();
        return this;
    }

    public Box setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Box setOverflowHidden(boolean overflowHidden) {
        this.overflowHidden = overflowHidden;
        return this;
    }

    public Box setBorder(BorderBox box) {
        this.box = box;
        return this;
    }

    public Box limitWidthToParent() {
        this.width = parent.width;
        return this;
    }

    public Box limitHeightToParent() {
        this.height = parent.height;
        return this;
    }

    @Override
    public void load() {
        if(child instanceof Scrollable) {
            this.setAdjustToChildSize(false);
        }
        if(child != null){
            child.load();
        }
    }

    @Override
    public void onPress(int x, int y, int button) {
        if(child != null) {
            child.onPress(x, y, button);
        }
    }

    @Override
    public void onRelease(int x, int y, int button) {
        if(child != null) {
            child.onRelease(x, y, button);
        }
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        if(child != null){
            child.onDrag(x, y, button, timeSinceLastClick);
        }
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1, 1, 1);
        if(this.backgroundColor != null){
             if(this.radius != null){
                 if(this.box != null) {
                     GameBridge.getChocomint().getRenderUtils().drawRoundedRect(x, y, x + width,  y +height, radius.top, radius.left, radius.right, radius.bottom, this.box.color.getRGB());
                     GameBridge.getChocomint().getRenderUtils().drawRoundedRect(x + this.box.lineWidth, y + this.box.lineWidth, x + width - this.box.lineWidth * 2, y + height - this.box.lineWidth * 2, radius.top, radius.left, radius.right, radius.bottom, this.backgroundColor.getRGB());
                 }else{
                     GameBridge.getChocomint().getRenderUtils().drawRoundedRect(x, y, x + width, y + height, radius.top, radius.left, radius.right, radius.bottom, this.backgroundColor.getRGB());
                 }
             }else{
                 if(this.box != null) {
                     GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x + width, y, this.box.lineWidth, this.box.color.getRGB());
                     GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x, y + height, this.box.lineWidth, this.box.color.getRGB());
                     GameBridge.getChocomint().getRenderUtils().drawLine(x + width, y, x + width, y + height, this.box.lineWidth, this.box.color.getRGB());
                     GameBridge.getChocomint().getRenderUtils().drawLine(x, y + height, x + width, y + height, this.box.lineWidth, this.box.color.getRGB());
                     GameBridge.getChocomint().getRenderUtils().drawRect(x, y, width, height, this.backgroundColor);
                 }else{
                     GameBridge.getChocomint().getRenderUtils().drawRect(x, y, width, height, this.backgroundColor);
                 }
             }
        }

        if(child != null){
            if(adjustToChildSize) {
                this.width = child.width;
                this.height = child.height;
            }
            if(alignment != null){
                if(this.overflowHidden && !(child instanceof IgnoreOverflow)){
                    GFXUtil.enableScissor();
                    GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);
                    if(adjustToChildSize)
                        child.draw(ui, (int) ((int) (x + width * alignment.xModifier)), (int) ((int) (y + height * alignment.yModifier)), mouseX, mouseY, deltaTime);
                    else
                        child.draw(ui, (int) ((int) (x + (width * alignment.xModifier)) - child.width * alignment.xModifier), (int) ((int) (y + (height * alignment.yModifier)) - child.height * alignment.yModifier), mouseX, mouseY, deltaTime);
                    GFXUtil.disableScissor();
                }else {
                    if(adjustToChildSize)
                        child.draw(ui, (int) ((int) (x + width * alignment.xModifier)), (int) ((int) (y + height * alignment.yModifier)), mouseX, mouseY, deltaTime);
                    else
                        child.draw(ui, (int) ((int) (x + (width * alignment.xModifier)) - child.width * alignment.xModifier), (int) ((int) (y + (height * alignment.yModifier)) - child.height * alignment.yModifier), mouseX, mouseY, deltaTime);
                }
            }else{
                if(this.overflowHidden && !(child instanceof IgnoreOverflow)){
                    GFXUtil.enableScissor();
                    GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);
                    child.draw(ui, x, y, mouseX, mouseY, deltaTime);
                    GFXUtil.disableScissor();
                }else{
                    child.draw(ui, x, y, mouseX, mouseY, deltaTime);
                }
            }
        }
    }
}
