package io.undervolt.client.gui;

import io.undervolt.client.gui.notifications.NotificationPanel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

public class GameBar extends GuiScreen {

    /** Declare panel status */
    public final NotificationPanel notificationPanel;

    /** Declare screen resolution */
    // private ScaledResolution sr = new ScaledResolution(this.mc);

    /** Declare buttons */
    private GuiButton notificationsButton;
    private GuiButton userButton;
    private GuiButton musicButton;
    private GuiButton friendsButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis*/
    private final GuiScreen previousScreen;

    /** Constructor */
    public GameBar(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.mc.getClient().getNotificationManager());
    }

    @Override
    public void initGui() {

        super.initGui();

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new GuiButton(
                101,
                22, 2, 20, 20, "N"
        ));
        this.buttonList.add(this.userButton = new GuiButton(
                102,
                104, 2, 60, 20, "[] Usuario"
        ));
        this.buttonList.add(this.musicButton = new GuiButton(
                103,
                130, 2, 20, 20, "M"
        ));
        this.buttonList.add(this.notificationsButton = new GuiButton(
                104,
                152, 2, 20, 20, "F"
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // Draw default background
        this.drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, new Color(138, 102, 102).getRGB());

        // Draw main rectangle (width x 20 res, #222)
        drawRect(0, 0, this.width, 24, new Color(22, 22, 22).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 19, 19, Color.RED.getRGB());
        drawString(this.mc.fontRendererObj, "chocomint", 24, 8, Color.WHITE.getRGB());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 101:
                this.notificationPanel.toggleActive();
                break;
        }
    }
}
