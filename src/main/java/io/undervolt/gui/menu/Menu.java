package io.undervolt.gui.menu;

import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class Menu extends AnimationUI {

    private final Chocomint chocomint;
    public GuiScreen previous;
    protected long ftime;
    private final String menuName, menuIcon;

    private double tw = Integer.MAX_VALUE;
    private double layer1Y = Integer.MAX_VALUE;
    protected int scroll = 0;
    private int pageSize;

    protected final MenuColor color;

    private AnimationUI newScreen;

    private boolean backwards = false;

    private final int position = 0;

    private ResourceLocation bracketLayer1;
    private ResourceLocation bracketLayer2;
    private ResourceLocation bracketLayer3;

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final int pageSize) {
        this.menuIcon = null;
        this.menuName = menuName;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
        this.color = MenuColor.DEFAULT;
    }

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final MenuColor color, final int pageSize) {
        this.menuIcon = null;
        this.menuName = menuName;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
        this.color = color;
    }

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final MenuColor color, final String menuIcon, final int pageSize) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
        this.color = color;
    }

    public Menu(final GuiScreen prev, final Chocomint chocomint, final String menuName, final String menuIcon, final int pageSize) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.chocomint = chocomint;
        this.pageSize = pageSize;
        this.previous = prev;
        this.color = MenuColor.DEFAULT;
    }

    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {}

    @Override
    public void initGui() {
        this.ftime = Minecraft.getSystemTime();
        previous.width = width;
        previous.height = height;
        this.chocomint.getGameBar().init(width, height);

        this.bracketLayer1 = this.getBracket(1);
        this.bracketLayer2 = this.getBracket(2);
        this.bracketLayer3 = this.getBracket(3);

        super.initGui();
    }

    protected int getContentWidth() {
        if(this.width < 600) {
            return this.width;
        } else {
            return 600;
        }
    }

    protected int getContentMargin() {
       if(this.getContentWidth() == this.width) {
           return 0;
       } else {
           return (this.width / 2) - 300;
       }
    }

    public void displayNewUI(AnimationUI ui) {
        this.fadeOut();
        this.newScreen = ui;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int hue;

        if(tw != 0) {
            tw = this.getAnimationTime(this.ftime, 3500.0D) * (height + 170);
            if(backwards) tw = this.getAnimationTime(this.ftime, 1600.0D) * height;
        }

        if(layer1Y == this.height + 50)
            if(backwards)
                if(newScreen != null)
                    this.mc.displayGuiScreen(this.newScreen);
                else this.mc.displayGuiScreen(this.previous);


        if(!backwards) {
            hue = 130 - (int)(this.getAnimationTime(this.ftime, 4000.0D) * 130);
        } else {
            hue = (int)(this.getAnimationTime(this.ftime, 4000.0D) * 130);
        }

        if(this.mc.theWorld == null && this.mc.thePlayer == null)
            previous.drawScreen(mouseX, mouseY, partialTicks);

        drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());

        GL11.glPushMatrix();

        GlStateManager.translate(0, backwards ? height - tw : tw,0);
        GL11.glColor3f(1, 1, 1);

        GL11.glPushMatrix();
        GlStateManager.translate(this.getContentMargin(), 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(-60, 0, height, 10, new Color(0, 0, 0, 100).getRGB(), 0);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GlStateManager.translate(this.getContentMargin() + this.getContentWidth() + 10, 0, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(-60, 0, height, 10, 0, new Color(0, 0, 0, 100).getRGB());
        GL11.glPopMatrix();


        // Bracket animation layer 1
        GL11.glPushMatrix();
        layer1Y = (this.getAnimationTime(this.ftime, 3000.0D) * (this.height + 40));
        if(backwards) {
            layer1Y = (this.height + 50) - (this.getAnimationTime(this.ftime, 5500.0D) * (this.height + 50));
        }
        GlStateManager.translate(0, layer1Y, 0);
        this.mc.getTextureManager().bindTexture(this.bracketLayer1);
        drawModalRectWithCustomSizedTexture(this.getContentMargin(),  position - 50, 0, 0, this.getContentWidth(),
                300, this.getContentWidth(), 300);
        GL11.glPopMatrix();

        // Bracket animation layer 2
        GL11.glPushMatrix();
        int layer2Y = (int)(this.getAnimationTime(this.ftime, 3500.0D) * (this.height + 40));
        if(backwards) {
            layer2Y = (this.height + 40) - (int)(this.getAnimationTime(this.ftime, 4000.0D) * (this.height) + 10);
        }
        GlStateManager.translate(0, layer2Y, 0);
        this.mc.getTextureManager().bindTexture(this.bracketLayer2);
        drawModalRectWithCustomSizedTexture(this.getContentMargin(),  position - 30, 0, 0, this.getContentWidth(),
                300, this.getContentWidth(), 300);
        GL11.glPopMatrix();

        // Bracket animation layer 3
        GL11.glPushMatrix();
        int layer3Y = (int)(this.getAnimationTime(this.ftime, 3700.0D) * (this.height + 50));
        if(backwards) {
            layer3Y = (this.height + 50) - (int)(this.getAnimationTime(this.ftime, 3500.0D) * (this.height + 50));
        }
        GlStateManager.translate(0, layer3Y, 0);
        this.mc.getTextureManager().bindTexture(this.bracketLayer3);
        drawModalRectWithCustomSizedTexture(this.getContentMargin(),  position, 0, 0, this.getContentWidth(),
                300, this.getContentWidth(), 300);
        GL11.glPopMatrix();

        drawRect(this.getContentMargin(), position, this.getContentMargin() + this.getContentWidth(), pageSize, this.getMenuContentColor());
        drawRect(this.getContentMargin(), position, this.getContentMargin() + this.getContentWidth(), 50, this.getMenuTitleColor());

        GL11.glColor3f(1, 1, 1);
        if(this.menuIcon != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/icon/" + menuIcon + ".png"));
            drawModalRectWithCustomSizedTexture(this.getContentMargin() + 5, position + 28, 0, 0, 20, 20, 20,20);
            this.fontRendererObj.drawString(this.menuName, this.getContentMargin() + 24, position + 36, Color.white.getRGB());
        } else
            this.fontRendererObj.drawString(this.menuName, this.getContentMargin() + 5, position + 36, Color.white.getRGB());
        drawMenuItems(mouseX, mouseY, partialTicks, this.getContentMargin(), this.scroll);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPopMatrix();

        this.chocomint.getGameBar().draw(mouseX, mouseY, partialTicks, width, height);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i < 0 && !(this.scroll <= 0)) this.scroll -=8;
        else if (i > 0  && (this.scroll <= this.pageSize - this.height)) this.scroll += 8;
    }

    public void fadeOut() {
        if(!this.backwards) {
            this.backwards = true;
            this.ftime = Minecraft.getSystemTime();
            this.tw = 0.1;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(mouseY > 20 && (mouseX < getContentMargin() || mouseX > getContentMargin() + getContentWidth()))
            this.fadeOut();
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.fadeOut();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public enum MenuColor {
        DEFAULT, GREEN, PURPLE, YELLOW;

        public String getName() {
            switch (this) {
                case GREEN:
                    return "green";
                case YELLOW:
                    return "yellow";
                case PURPLE:
                    return "purple";
                default:
                    return "default";
            }
        }
    }

    public int getMenuContentColor() {
        switch(this.color) {
            case YELLOW:
                return new Color(93, 78, 53).getRGB();
            case GREEN:
                return new Color(42, 72, 41).getRGB();
            case PURPLE:
                return new Color(61, 41, 73).getRGB();
            default:
                return new Color(41, 55, 73).getRGB();
        }
    }

    public int getMenuTitleColor() {
        switch(this.color) {
            case YELLOW:
                return new Color(132, 107, 66).getRGB();
            case GREEN:
                return new Color(56, 105, 54).getRGB();
            case PURPLE:
                return new Color(85, 54, 105).getRGB();
            default:
                return new Color(54, 71, 105).getRGB();
        }
    }

    public ResourceLocation getBracket(int layer) {
        return new ResourceLocation("/chocomint/ui/bracket/" + this.color.getName() + "/layer" + layer + ".png");
    }
}
