package io.undervolt.gui;

import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationPanel;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserCard;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

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
    private TextureGameBarButton notificationsButton;
    private GameBarButton userButton;
    private TextureGameBarButton musicButton;
    private TextureGameBarButton friendsButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis */
    private final GuiScreen previousScreen;

    /** Enable background drawing */
    private boolean backgroundDrawing;

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
        this.userCard = new UserCard(this.chocomint, this.mc, this.user, false);

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new TextureGameBarButton(
                1337101,
                this.width - 20, 0, 20, 20, "notifications"
        ));
        this.buttonList.add(this.userButton = new GameBarButton(
                1337102,
                this.width - 84, 0, 62, 20, "[ ] Usuario"
        ));
        this.buttonList.add(this.musicButton = new TextureGameBarButton(
                1337103,
                this.width - 108, 0, 20, 20, "music"
        ));
        this.buttonList.add(this.friendsButton = new TextureGameBarButton(
                1337104,
                this.width - 130, 0, 20, 20, "friends"
        ));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // Draw default background
        if(this.backgroundDrawing) this.drawDefaultBackground();

        // Draw main rectangle (width x 20 res, #222)
        drawRect(0, 0, this.width, 20, new Color(22, 22, 22).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 10, 16, new Color(65, 44, 25).getRGB());
        drawRect(10, 4, 16, 16, new Color(63, 222, 160).getRGB());
        drawString(this.fontRendererObj, "chocomint", 20, 6, Color.WHITE.getRGB());

        this.userCard.drawCard(this.width, this.height);
        this.notificationPanel.drawPanel(this.width, this.height);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        // Toggle off userCard's visibility if clicked outside of rendered area
        if(this.userCard.isActive()) {
            if (mouseX < this.width - 132 || mouseX > this.width - 2
                    || mouseY > 60)
                this.userCard.setActive(false);
        }

        // Same as above, but with Notifications
        if(this.notificationPanel.isActive()) {
            if (mouseX < this.width - 120)
                this.notificationPanel.setActive(false);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1337101:
                this.notificationPanel.toggleActive();
                this.userCard.setActive(false);
                break;
            case 1337102:
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

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }
}
