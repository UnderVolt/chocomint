package io.undervolt.gui;

import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.contributors.ContributorsPanel;
import io.undervolt.gui.login.LoginGUI;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationPanel;
import io.undervolt.gui.user.UserCard;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class GameBar extends AnimationUI {

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** Declare panel status */
    private final NotificationManager notificationManager;
    public NotificationPanel notificationPanel;
    private int notificationScroll = 0;


    /** Declare User card */
    public UserCard userCard;

    /** Declare contributors panel */
    private final ContributorsManager contributorsManager;
    public ContributorsPanel contributorsPanel;

    /** UI elements */
    private final ScaledResolution sr;
    private long ftime;

    /** Declare buttons */
    private TextureGameBarButton notificationsButton;
    private GameBarButton userButton;
    private TextureGameBarButton musicButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton contributorsButton;

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
        this.contributorsManager = chocomint.getContributorsManager();
    }

    @Override
    public void initGui() {

        // Set username trim
        String username = this.chocomint.getUser().getUsername();
        if(this.fontRendererObj.getStringWidth("[ ] " + username) > 62) {
            username = username.substring(0, Math.min(username.length(), 6)) + "...";
        }

        // Initialize Notifications
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.chocomint.getNotificationManager());

        // Initialize User Card
        this.userCard = new UserCard(this.chocomint, this.mc, this.chocomint.getUser(), false, this.chocomint.getUser().getImage());

        // Initialize Contributors Panel
        this.contributorsPanel = new ContributorsPanel(this.mc, this.contributorsManager, false);

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new TextureGameBarButton(
                1337101,
                this.width - 20, 0, 20, 20, "notifications"
        ));
        this.buttonList.add(this.userButton = new GameBarButton(
                1337102,
                this.width - 84, 0, 62, 20, "[ ] " + username
        ));
        this.buttonList.add(this.musicButton = new TextureGameBarButton(
                1337103,
                this.width - 108, 0, 20, 20, "music"
        ));
        this.buttonList.add(this.friendsButton = new TextureGameBarButton(
                1337104,
                this.width - 130, 0, 20, 20, "friends"
        ));
        this.buttonList.add(this.contributorsButton = new GameBarButton(
                1337105,
                this.width - 154, 0, 20, 20, "C"
        ));

        this.ftime = Minecraft.getSystemTime();

        super.initGui();
    }

    private double dw;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        // Draw default background
        if(this.backgroundDrawing) this.drawDefaultBackground();

        // Draw main rectangle (width x g20 res, #222)
        drawRect(0, 0, this.width, 20, new Color(32,34,37).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 10, 16, new Color(65, 44, 25).getRGB());
        drawRect(10, 4, 16, 16, new Color(63, 222, 160).getRGB());
        drawString(this.fontRendererObj, "chocomint", 20, 6, Color.WHITE.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.userCard.drawCard(this.width, this.height);
        this.notificationPanel.drawPanel(this.width, this.height, this.notificationScroll);
        this.contributorsPanel.drawPanel(this.width, this.height);
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

            for (Notification notification : this.notificationManager.getNotifications()) {
                if (mouseX >= notification.getX() + notificationScroll && mouseY >= notification.getY() +notificationScroll && mouseX <= notification.getX() + notificationScroll + 110 && mouseY <= notification.getY() + notificationScroll + 35) {
                    notification.getConsumer().accept((this.mc.currentScreen instanceof Chat ? this.previousScreen : this));
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {

        int i = Mouse.getEventDWheel();

        if(notificationPanel.isActive()) {
            if (i > 0 && (this.notificationScroll <= 0)) this.notificationScroll += 4.5;
            else if (i < 0 && !((this.notificationManager.getNotifications().size() * 45) <= (this.height - 45) - this.notificationScroll))
                this.notificationScroll -= 4.5;
        }

        super.handleMouseInput();
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1337101:
                this.notificationPanel.toggleActive();
                this.contributorsPanel.setActive(false);
                this.userCard.setActive(false);
                break;
            case 1337102:
                this.userCard.toggleActive();
                this.notificationPanel.setActive(false);
                this.contributorsPanel.setActive(false);
                if(this.chocomint.getUser().getUsername().equals("Guest")) this.mc.displayGuiScreen(new LoginGUI(this, this.chocomint));
                break;
            case 1337105:
                this.notificationPanel.setActive(false);
                this.userCard.setActive(false);
                this.contributorsPanel.toggleActive();
                break;
        }
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }
}
