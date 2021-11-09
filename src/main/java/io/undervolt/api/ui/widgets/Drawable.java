package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.DebugOptions;
import io.undervolt.api.ui.Screen;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Drawable implements Cloneable{

    protected float width, height;
    protected int x, y;
    protected Drawable parent;
    protected final Minecraft mc = Minecraft.getMinecraft();

    protected Drawable[] internal;
    public void load(){
        if(internal != null)
            Arrays.asList(internal).forEach(Drawable::load);
    }
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime){
        this.x = x;
        this.y = y;
        if(internal != null) {
            Arrays.asList(internal).forEach(drawable -> drawable.draw(ui, x, y, mouseX, mouseY, deltaTime));
            this.width = (int) Arrays.stream(internal).sorted(Comparator.comparing(Drawable::getWidth))
                 .collect(Collectors.toList()).get(0).getWidth();
            this.height = (int) Arrays.stream(internal).sorted(Comparator.comparing(Drawable::getWidth))
                 .collect(Collectors.toList()).get(0).getHeight();
        }
        if(DebugOptions.showBounds)
        {
            GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x + width, y, 1, Colour.Magenta.getRGB());
            GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x, y + height, 1, Colour.Magenta.getRGB());
            GameBridge.getChocomint().getRenderUtils().drawLine(x + width, y, x + width, y + height, 1, Colour.Magenta.getRGB());
            GameBridge.getChocomint().getRenderUtils().drawLine(x, y + height, x + width, y + height, 1, Colour.Magenta.getRGB());
        }
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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
