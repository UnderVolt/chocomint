package io.undervolt.gui.user;

import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class UserCard extends Gui {

    private final User user;
    private final Chocomint chocomint;
    private final Minecraft mc;
    private boolean isActive;
    private final DynamicTexture pfp;

    public UserCard(final Chocomint chocomint, final Minecraft mc, final User user, boolean isActive, final DynamicTexture pfp) throws IOException {
        this.chocomint = chocomint;
        this.isActive = isActive;
        this.mc = mc;
        this.user = user;
        this.pfp = pfp;
    }

    public void drawCard(int screenWidth, int screenHeight) {
        if(this.isActive()) {
            this.chocomint.getRenderUtils().
                    drawRoundedRect(screenWidth - 132, 22, 130,
                    38, 4, new Color(22, 22, 22).getRGB());

            GL11.glPushMatrix();
            GL11.glColor3f(255, 255, 255);

            if(this.pfp != null) {
                this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation("Profile", this.pfp));
                Gui.drawModalRectWithCustomSizedTexture(screenWidth - 128, 26, 0, 0, 30, 30, 30, 30);
            } else {
                this.chocomint.getRenderUtils().drawRoundedRect(screenWidth - 128, 26, 30, 30, 3, Color.RED.getRGB());
            }

            GL11.glPopMatrix();

            drawString(mc.fontRendererObj, this.user.getUsername(), screenWidth - 94,
                    30, Color.WHITE.getRGB());
            drawString(mc.fontRendererObj, this.user.getStatusString().toUpperCase(),
                    screenWidth - 94, 42, Color.WHITE.getRGB());
            this.chocomint.getRenderUtils().
                drawFilledCircle(screenWidth - 98, 55, 3, this.user.getStatusColor());
        }
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
