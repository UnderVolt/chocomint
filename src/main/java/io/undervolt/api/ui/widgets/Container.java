package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import io.undervolt.utils.DrawUtil;
import io.undervolt.utils.GFXUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Container extends IWidget{

    protected Color backgroundColor;
    protected EdgeInsets radius;
    protected Alignment alignment;
    protected boolean overflowHidden;
    protected BorderBox box;
    protected boolean limitWidthToParent = false;
    protected boolean limitHeightToParent = false;

    protected IWidget child;

    public Container(float width, float height, IWidget child) {
        this.width = width;
        this.height = height;
        this.child = child;
        this.child.parent = this;
    }

    public Container(IWidget child) {
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

    public Container setChild(IWidget child) {
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

    public Container limitWidthToParent(boolean limitWidthToParent) {
        this.limitWidthToParent = limitWidthToParent;
        return this;
    }

    public Container limitHeightToParent(boolean limitHeightToParent) {
        this.limitHeightToParent = limitHeightToParent;
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

        if(this.backgroundColor != null){
             if(this.radius != null){
                 if(this.box != null) {
                     DrawUtil.drawRoundedRect(x, y, x + width,  y +height, radius.top, radius.left, radius.right, radius.bottom, this.box.color.getRGB());
                     DrawUtil.drawRoundedRect(x + this.box.lineWidth, y + this.box.lineWidth, x + width - this.box.lineWidth * 2, y + height - this.box.lineWidth * 2, radius.top, radius.left, radius.right, radius.bottom, this.backgroundColor.getRGB());
                 }else{
                     DrawUtil.drawRoundedRect(x, y, x + width, y + height, radius.top, radius.left, radius.right, radius.bottom, this.backgroundColor.getRGB());
                 }
             }else{
                 if(this.box != null) {
                     DrawUtil.drawRect(x, y, width, height, this.box.color);
                     DrawUtil.drawRect(x + this.box.lineWidth, y + this.box.lineWidth, width - this.box.lineWidth * 2, height - this.box.lineWidth * 2, this.backgroundColor);
                 }else{
                     DrawUtil.drawRect(x, y, width, height, this.backgroundColor);
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
