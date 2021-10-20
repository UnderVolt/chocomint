package io.undervolt.api.ui;

import com.google.common.collect.Lists;
import io.undervolt.api.ui.widgets.Container;
import io.undervolt.api.ui.widgets.IWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public abstract class UIView extends GuiScreen {

    protected List<IWidget> widgets = Lists.newArrayList();

    public final Minecraft minecraft = Minecraft.getMinecraft();
    public float oldTime;
    public float newTime;
    public float deltaTime;
    protected ScaledResolution sr;
    protected boolean forceUseScaleFactor = false;
    protected int forcedScaleFactor = 2;
    private boolean startedDrawing = false;
    private long time = 0;
    private int tmpMouseX = 0;
    private int tmpMouseY = 0;

    public void initUI(){}
    public void drawUI(int mouseX, int mouseY, float deltaTime){}
    public void closeUI(){}

    @Override
    public void initGui() {
        this.oldTime = this.newTime;
        this.newTime = Minecraft.getSystemTime() / 1000.0f;
        this.initUI();
        this.deltaTime = this.newTime - this.oldTime;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.buttonList.clear();
        this.widgets.clear();
        this.mc = mc;
        this.sr = new ScaledResolution();
        if(this.forceUseScaleFactor){
            this.width = (int)Math.floor(Display.getWidth() / (float)this.forcedScaleFactor);
            this.height = (int)Math.floor(Display.getHeight() / (float)this.forcedScaleFactor);
        }else{
            this.width = this.sr.getScaledWidth();
            this.height = this.sr.getScaledHeight();
        }
        this.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.oldTime = this.newTime;
        this.newTime = Minecraft.getSystemTime() / 1000.0f;

        this.sr = new ScaledResolution();

        if(this.forceUseScaleFactor){
            GL11.glPushMatrix();

            float scaleWidthFactor = this.sr.getScaledWidth() / (Display.getWidth() / (float)this.forcedScaleFactor);
            float scaleHeightFactor = this.sr.getScaledHeight() / (Display.getHeight() / (float)this.forcedScaleFactor);

            GL11.glScalef(scaleWidthFactor, scaleHeightFactor, 1);

            this.tmpMouseX = (int)Math.floor(mouseX / scaleWidthFactor);
            this.tmpMouseY = (int)Math.floor(mouseY / scaleHeightFactor);

            if(!this.startedDrawing){
                this.time = System.currentTimeMillis();
                this.startedDrawing = true;
            }

            super.drawScreen(this.tmpMouseX , this.tmpMouseY, partialTicks);
            this.widgets.forEach(w -> w.draw(this, 0, 0, this.tmpMouseX, this.tmpMouseY, deltaTime));
            this.drawUI(this.tmpMouseX, this.tmpMouseY, this.deltaTime);
            this.deltaTime = this.newTime - this.oldTime;
            GL11.glPopMatrix();
        }else{
            if(!this.startedDrawing){
                this.time = System.currentTimeMillis();
                this.startedDrawing = true;
            }

            this.tmpMouseX = mouseX;
            this.tmpMouseY = mouseY;

            super.drawScreen(mouseX, mouseY, partialTicks);
            this.widgets.forEach(w -> w.draw(this, 0, 0, mouseX, mouseY, deltaTime));
            this.drawUI(mouseX, mouseY, this.deltaTime);
            this.deltaTime = this.newTime - this.oldTime;
        }
    }

    public void addWidgets(IWidget... widgets){
        for (IWidget w : widgets) {
            this.widgets.add(new Container(this.getWidth(), this.getHeight()).setChild(w));
            w.init();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.widgets.forEach(w -> w.onPress(this.tmpMouseX, this.tmpMouseY, mouseButton));

        super.mouseClicked(this.tmpMouseX, this.tmpMouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {

        this.widgets.forEach(w -> w.onRelease(this.tmpMouseX, this.tmpMouseY, state));

        super.mouseReleased(this.tmpMouseX, this.tmpMouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        this.widgets.forEach(w -> w.onDrag(this.tmpMouseX, this.tmpMouseY, clickedMouseButton, timeSinceLastClick));

        super.mouseClickMove(this.tmpMouseX, this.tmpMouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void onGuiClosed() {
        this.closeUI();
    }

    public void forceScaleFactor(int newScaleFactor){
        this.forceUseScaleFactor = true;
        this.forcedScaleFactor = newScaleFactor;
    }

    public boolean forcingUseScaleFactor() {
        return forceUseScaleFactor;
    }

    public int getWidth(){
        return Math.round(this.forceUseScaleFactor ? (Display.getWidth() / (float)this.forcedScaleFactor) : this.sr.getScaledWidth());
    }

    public int getHeight(){
        return Math.round(this.forceUseScaleFactor ? (Display.getHeight() / (float)this.forcedScaleFactor) : this.sr.getScaledHeight());
    }

    public float getWidthA(){
        return this.forceUseScaleFactor ? Display.getHeight() : this.sr.getScaledWidth();
    }

    public float getHeightA(){
        return this.forceUseScaleFactor ? Display.getWidth() : this.sr.getScaledHeight();
    }

    public int getScaleFactor(){
        return this.forceUseScaleFactor ? this.forcedScaleFactor : this.sr.getScaleFactor();
    }

    public float getFixedWidth(float originalWidth){
        return this.getWidth() * (1.0f / (this.getWidth() / (originalWidth / (this.forceUseScaleFactor ? this.forcedScaleFactor : sr.getScaleFactor()))));
    }

    public float getFixedHeight(float originalWidth){
        return this.getHeight() * (1.0f / (this.getHeight() / (originalWidth / (this.forceUseScaleFactor ? this.forcedScaleFactor : sr.getScaleFactor()))));
    }

    /**
     * Returns the time in seconds since the UI Init
     *
     * @return      The time in seconds
     */

    public float getTime(){
        return (float) ((System.currentTimeMillis() - this.time) / 1000.0);
    }

}

