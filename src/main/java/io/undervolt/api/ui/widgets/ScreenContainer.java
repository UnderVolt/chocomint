package io.undervolt.api.ui.widgets;

import com.google.common.collect.Lists;
import io.undervolt.api.ui.Screen;
import io.undervolt.utils.GFXUtil;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScreenContainer extends Drawable {

    protected List<Drawable> widgets = Lists.newArrayList();

    protected List<GuiButton> buttonList = Lists.newArrayList();
    protected int wX = 0;
    protected int wY = 0;

    public ScreenContainer(float width, float height){
        this.width = width;
        this.height = height;
    }

    public void initView(){}
    public void drawScreen(Screen ui, int x, int y, float deltaTime){}

    @Override
    public void init() {
        buttonList.clear();
        this.initView();
    }

    @Override
    public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        this.wX = x;
        this.wY = y;

        GL11.glPushMatrix();
        GFXUtil.enableScissor();
        GFXUtil.applyScissor(x, y, (int)(x + this.width), (int)(y + this.height), ui);
        GL11.glTranslatef(x, y, 1);
        this.drawScreen(ui, mouseX - x, mouseY - y, deltaTime);
        this.buttonList.forEach(btn -> btn.drawButton(this.mc, mouseX - x, mouseY - y));
        this.widgets.forEach(w -> w.draw(ui, 0, 0, mouseX - x, mouseY - y, deltaTime));
        GFXUtil.disableScissor();
        GL11.glPopMatrix();
    }

    @Override
    public void onPress(int x, int y, int button) {
        this.widgets.forEach(w -> w.onPress(x - this.wX, y - this.wY, button));
        if(button == 0){
            this.buttonList.forEach(btn -> btn.mousePressed(this.mc, x - this.wX, y - this.wY));
        }
    }

    @Override
    public void onRelease(int x, int y, int button) {
        this.widgets.forEach(w -> w.onRelease(x - this.wX, y - this.wY, button));
    }

    @Override
    public void onDrag(int x, int y, int button, long timeSinceLastClick) {
        this.widgets.forEach(w -> w.onDrag(x - this.wX, y - this.wY, button, timeSinceLastClick));
    }

    public void addWidgets(Drawable... widgets){
        for (Drawable w : widgets) {
            this.widgets.add(new Container(this.width, this.height).setChild(w));
            w.init();
        }
    }

    public int getViewWidth(){
        return (int)this.width;
    }

    public int getViewHeight(){
        return (int)this.height;
    }
}
