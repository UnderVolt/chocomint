package io.undervolt.gui.user;

import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class UserCard extends Gui {

    private final User user;
    private final Chocomint chocomint;
    private final Minecraft mc;
    private boolean isActive;
    private final DynamicTexture dynamicTexture;
    private final boolean isSelf;
    private final Consumer<User> consumer;
    public int x, y;

    public UserCard(final Chocomint chocomint, final Minecraft mc, final User user, boolean isActive, boolean isSelf, Consumer<User> consumer) {
        this.chocomint = chocomint;
        this.isActive = isActive;
        this.mc = mc;
        this.user = user;
        this.isSelf = isSelf;
        this.consumer = consumer;
        this.dynamicTexture = this.chocomint.getUserManager().getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getImage());
    }

    public void drawCard(int x, int y) {

        if(this.isActive()) {

            String alias = this.user.getAlias();
            if(this.mc.fontRendererObj.getStringWidth(alias) > 80) {
                alias = alias.substring(0, Math.min(alias.length(), 10)) + "...";
            }

            this.x = x;
            this.y = y;

            this.chocomint.getRenderUtils().
                    drawRoundedRect(x, y, 130,
                    isSelf ? 46 : 38, 2, new Color(22, 22, 22).getRGB());

            GL11.glPushMatrix();
            GL11.glColor3f(255, 255, 255);

            if(this.user.getImage() != null) {
                this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation("Profile", this.dynamicTexture));
                Gui.drawModalRectWithCustomSizedTexture(x + 4, y + 4, 0, 0, 30, 30, 30, 30);
            } else {
                this.chocomint.getRenderUtils().drawRoundedRect(x + 4, y + 4, 30, 30, 3, Color.RED.getRGB());
            }

            GL11.glPopMatrix();

            drawString(mc.fontRendererObj, alias, x + 38, y + 8, Color.WHITE.getRGB());
            drawString(mc.fontRendererObj, this.user.getStatusString().toUpperCase(), x + 38, y + 19, Color.WHITE.getRGB());


            this.chocomint.getRenderUtils().
                    drawFilledCircle(x + 34, y + 33, 3, this.user.getStatusColor());


            if(this.isSelf) {
                GL11.glPushMatrix();
                GlStateManager.translate((x + 126 - (this.mc.fontRendererObj.getStringWidth("Opciones") * 0.825)), y + 36, 0);
                GlStateManager.scale(0.825, 0.825, 0);
                drawString(this.mc.fontRendererObj, "Opciones", 0, 0, Color.LIGHT_GRAY.getRGB());
                GL11.glPopMatrix();
            }
        }
    }

    public void click(int mouseY, int mouseX) {
        if(this.isActive()) {
            if(mouseY > 20) {
                if (mouseX >= x && mouseY >= y && mouseX <= x + 130 && mouseY <= y + (this.isSelf ? 46 : 38)) {
                    this.getConsumer().accept(getUser());
                }
            }
        }
    }

    public User getUser() {
        return user;
    }

    public Consumer<User> getConsumer() {
        return consumer;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }

}
