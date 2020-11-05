package io.undervolt.gui;

import io.netty.util.concurrent.Promise;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationPanel;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserCard;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

public class GameBar extends GuiScreen {

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** Declare panel status */
    private final NotificationManager notificationManager;
    public NotificationPanel notificationPanel;

    /** Declare User card */
    public UserCard userCard;
    private final User user;

    /** Declare screen resolution */
    private ScaledResolution sr;

    /** Declare buttons */
    private GameBarButton notificationsButton;
    private GameBarButton userButton;
    private GameBarButton musicButton;
    private GameBarButton friendsButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis*/
    private final GuiScreen previousScreen;

    /** Constructor */
    public GameBar(final GuiScreen previousScreen, final Chocomint chocomint) {
        this.previousScreen = previousScreen;
        this.chocomint = chocomint;
        this.notificationManager = chocomint.getNotificationManager();
        this.sr = chocomint.getGameBridge().getScaledResolution();
        // This is only a mock user under Minecraft's credentials.
        // Will make use of the UnderVolt API in the future.
        this.user = chocomint.getUser();
    }

    @Override
    public void initGui() {

        // Initialize Notifications
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.chocomint.getNotificationManager());

        // Initialize User Card
        this.userCard = new UserCard(this.mc, this.user, false);

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new GameBarButton(
                101,
                this.width - 20, 0, 20, 20, "N"
        ));
        this.buttonList.add(this.userButton = new GameBarButton(
                102,
                this.width - 84, 0, 62, 20, "[ ] Usuario"
        ));
        this.buttonList.add(this.musicButton = new GameBarButton(
                103,
                this.width - 108, 0, 20, 20, "M"
        ));
        this.buttonList.add(this.notificationsButton = new GameBarButton(
                104,
                this.width - 130, 0, 20, 20, "F"
        ));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // Draw default background
        this.drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, new Color(138, 102, 102).getRGB());

        // Draw main rectangle (width x 20 res, #222)
        drawRect(0, 0, this.width, 20, new Color(22, 22, 22).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 16, 16, Color.RED.getRGB());
        drawString(this.mc.fontRendererObj, "chocomint", 20, 6, Color.WHITE.getRGB());

        this.userCard.drawCard(this.width, this.height);
        this.notificationPanel.drawPanel(this.width, this.height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 101:
                this.notificationPanel.toggleActive();
                this.userCard.setActive(false);
                break;
            case 102:
                this.userCard.toggleActive();
                this.notificationPanel.setActive(false);
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        this.notificationManager.updateNotifications(
                this.notificationManager.clearNotifications()
        );
    }
}
