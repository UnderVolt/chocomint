package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.chat.Tab;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class ClickableTab extends Clickable {

    private final Chocomint chocomint;
    private final Minecraft mc;
    private final Tab tab;

    public ClickableTab(int x, int y, Tab tab) {
        super(x, y, 50, 20, null);
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.tab = tab;
        this.consumer = (a) -> {
            this.chocomint.getChatManager().setSelectedTab(tab);
        };
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        boolean isMouseOver = (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (
                mouseY <= this.y + this.height);

        boolean mouseOverCloseButton = (mouseX > x + width - 15 && mouseY > y + 5 && mouseX < x + width - 6 && mouseY < y + 15);

        this.width = 24 + this.mc.fontRendererObj.getStringWidth(tab.getName());
        this.chocomint.getRenderUtils().drawRoundedRect(x, y, x + width, y + height,
                8, 8, 0, 0,
                this.chocomint.getChatManager().getSelectedTab().equals(this.tab) ?  new Color(79, 82, 92).getRGB()
                        : (isMouseOver ?  new Color(54,57,63).getRGB() : new Color(32,34,37).getRGB()));
        GL11.glColor3f(255, 255, 255);
        this.mc.fontRendererObj.drawString(tab.getName(), x + 5, y + 7, tab.isRead() ? Color.WHITE.getRGB() :  new Color(255, 218, 108).getRGB());
        if(!tab.getName().equals("SERVER_RESERVED")) {
            if(isMouseOver) {
                this.chocomint.getRenderUtils().drawFilledCircle(x + width - 10, y + 11, 4,
                        mouseOverCloseButton ? new Color(255, 81, 81).getRGB() : 0);
                GL11.glPushMatrix();
                GlStateManager.translate(x + width - 12, y + 8.5, 0);
                GL11.glScalef(0.75f, 0.75f, 0);
                this.mc.fontRendererObj.drawString("✕", 0, 0, Color.WHITE.getRGB());
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if(mouseX > x + width - 15 && mouseY > y + 5 && mouseX < x + width - 6 && mouseY < y + 15) {
            this.chocomint.getChatManager().removeTab(tab);
            ((Chat) this.mc.currentScreen).clickablesToRemove.add(tab.getName());
        } else super.click(mouseX, mouseY);
    }
}