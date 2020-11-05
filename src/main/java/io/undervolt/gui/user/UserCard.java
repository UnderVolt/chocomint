package io.undervolt.gui.user;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class UserCard extends Gui {

    private final User user;

    private final Minecraft mc;
    private boolean isActive;

    public UserCard(final Minecraft mc, final User user, boolean isActive) {
        this.isActive = isActive;
        this.mc = mc;
        this.user = user;
    }

    public void drawCard(int screenWidth, int screenHeight) {
        if(this.isActive()) {
            drawRect(screenWidth - 132, 22, screenWidth - 2,
                    60, new Color(22, 22, 22).getRGB());
            // Placeholder for avatar
            drawRect(screenWidth - 128, 26, screenWidth - 98,
                    56, Color.RED.getRGB());
            drawString(mc.fontRendererObj, this.user.getUsername(), screenWidth - 94,
                    30, Color.WHITE.getRGB());
            drawString(mc.fontRendererObj, this.user.getStatusString().toUpperCase(),
                    screenWidth - 94, 42, Color.WHITE.getRGB());
            this.mc.getChocomint().getRenderUtils()
                .drawFilledCircle(screenWidth - 98, 55, 3, this.user.getStatusColor());
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
