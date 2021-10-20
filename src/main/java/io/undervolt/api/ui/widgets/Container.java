package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.GFXUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Container extends Drawable {

    protected Color backgroundColor;
    protected EdgeInsets radius;
    protected Alignment alignment;
    protected boolean overflowHidden;
    protected BorderBox box;

    protected Drawable child;

    public Container(float width, float height, Drawable child) {
        this.width = width;
        this.height = height;
        this.child = child;
        this.child.parent = this;
    }

    public Container(Drawable child) {
        this.child = child;
        this.child.parent = this;
    }

    public Container(float width, float height) {
        this.width = width;
        this.height = height;
        this.child = null;
    }

    public Container setWidth(int width) {
        this.width = width;
        return this;
    }

    public Container setHeight(int height) {
        this.height = height;
        return this;
    }

    public Container setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Container setBorderRadius(EdgeInsets radius) {
        this.radius = radius;
        return this;
    }

    public Container setChild(Drawable child) {
        this.child = child;
        this.child.parent = this;
        child.init();
        return this;
    }

    public Container setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public Container setOverflowHidden(boolean overflowHidden) {
        this.overflowHidden = overflowHidden;
        return this;
    }

    public Container setBorder(BorderBox box) {
        this.box = box;
        return this;
    }

    public Container limitWidthToParent() {
        this.width = parent.width;
        return this;
    }

    public Container limitHeightToParent() {
        this.height = parent.height;
        return this;
    }

    @Override
    public void init() {
        if(child != null){
            child.init();
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
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
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
                     GameBridge.getChocomint().getRenderUtils().drawRect(x, y, width, height, this.box.color);
                     GameBridge.getChocomint().getRenderUtils().drawRect(x + this.box.lineWidth, y + this.box.lineWidth, width - this.box.lineWidth * 2, height - this.box.lineWidth * 2, this.backgroundColor);
                 }else{
                     GameBridge.getChocomint().getRenderUtils().drawRect(x, y, width, height, this.backgroundColor);
                 }
             }
        }

        if(child != null){
            if(alignment != null){
                if(this.overflowHidden && !(child instanceof IgnoreOverflow)){
                    GFXUtil.enableScissor();
                    GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);
                    child.draw(ui, (int) ((int) (x + (width * alignment.xModifier)) - child.width * alignment.xModifier), (int) ((int) (y + (height * alignment.yModifier)) - child.height * alignment.yModifier), mouseX, mouseY, deltaTime);
                    GFXUtil.disableScissor();
                }else {
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
