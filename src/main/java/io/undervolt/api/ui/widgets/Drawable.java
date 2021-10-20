package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.UIView;
import net.minecraft.client.Minecraft;

public class  implements Cloneable{

    protected float width;
    protected float height;
    protected  parent;
    protected final Minecraft mc = Minecraft.getMinecraft();
    public void init(){}
    public void draw(UIView ui, int x, int y, int mouseX, int mouseY, float deltaTime){}
    public void onPress(int x, int y, int button){}
    public void onRelease(int x, int y, int button){}
    public void onDrag(int x, int y, int button, long timeSinceLastClick){}

    public  getParent() {
        return parent;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public  copyWidget(){
        try {
            return () super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ();
    }
}
