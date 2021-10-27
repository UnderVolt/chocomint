package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Drawable implements Cloneable{

    protected float width;
    protected float height;
    protected Drawable parent;
    protected final Minecraft mc = Minecraft.getMinecraft();

    protected Drawable[] internal;
    public void load(){
        if(internal != null)
            Arrays.asList(internal).forEach(Drawable::load);
    }
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime){
        if(internal != null)
            Arrays.asList(internal).forEach(drawable -> drawable.draw(ui, x, y, mouseX, mouseY, deltaTime));
    }
    public void onPress(int x, int y, int button){
        if(internal != null)
            Arrays.asList(internal).forEach(drawable -> drawable.onPress(x, y, button));
    }
    public void onRelease(int x, int y, int button){
        if(internal != null)
            Arrays.asList(internal).forEach(drawable -> drawable.onRelease(x, y, button));
    }
    public void onDrag(int x, int y, int button, long timeSinceLastClick){
        if(internal != null)
            Arrays.asList(internal).forEach(drawable -> drawable.onDrag(x, y, button, timeSinceLastClick));
    }

    public Drawable getParent() {
        return parent;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Drawable copyWidget(){
        try {
            return (Drawable) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Drawable();
    }
}
