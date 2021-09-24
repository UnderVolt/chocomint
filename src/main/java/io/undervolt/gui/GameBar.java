package io.undervolt.gui;

import com.google.common.collect.Lists;
import io.undervolt.api.event.events.UserConnectEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.gui.clickable.GameBarButton;
import io.undervolt.gui.clickable.TextureGameBarButton;
import io.undervolt.gui.clickable.UserGameBarButton;
import io.undervolt.gui.config.GuiMods;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.contributors.ContributorsPanel;
import io.undervolt.gui.friends.FriendsManager;
import io.undervolt.gui.friends.FriendsPanel;
import io.undervolt.gui.login.LoginGUI;
import io.undervolt.gui.login.MinecraftLoginGUI;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationOverlay;
import io.undervolt.gui.notifications.NotificationPanel;
import io.undervolt.gui.user.UserCard;
import io.undervolt.gui.user.UserScreen;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameBar extends Gui implements Listener {

    /** Declare Minecraft */
    private final Minecraft mc;

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** Declare panel status */
    private final NotificationManager notificationManager;
    public NotificationPanel notificationPanel;
    private int notificationScroll = 0;
    private final NotificationOverlay notificationOverlay;

    /** Declare User card */
    public UserCard userCard;

    /** Declare contributors panel */
    private final ContributorsManager contributorsManager;
    public ContributorsPanel contributorsPanel;

    /** Declare friends panel */
    private final FriendsManager friendsManager;
    public FriendsPanel friendsPanel;

    /** UI elements */
    private final ScaledResolution sr;
    private long ftime;
    private final FontRenderer fontRendererObj;

    /** Declare buttons */
    private TextureGameBarButton notificationsButton;
    private UserGameBarButton userButton;
    private TextureGameBarButton chatButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton configButton;
    private TextureGameBarButton changeMinecraftAccountButton;

    private final List<Clickable> clickableList = Lists.newArrayList();

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis */
    private GuiScreen parentScreen;

    /** Enable background drawing */
    private boolean backgroundDrawing;

    /** Constructor */
    public GameBar(final GuiScreen parentScreen, final Chocomint chocomint) {
        this.parentScreen = parentScreen;
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.notificationManager = chocomint.getNotificationManager();
        this.sr = GameBridge.getScaledResolution();
        this.contributorsManager = chocomint.getContributorsManager();
        this.friendsManager = chocomint.getFriendsManager();
        this.fontRendererObj = this.mc.fontRendererObj;
        this.notificationOverlay = chocomint.getNotificationOverlay();
    }

    public void init(int width, int height) {

        // Initialize Notifications
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.chocomint.getNotificationManager());

        // Initialize User Card
        this.userCard = new UserCard(this.mc, this.chocomint.getUser(), false, true, (user) -> {
            this.mc.displayGuiScreen(new UserScreen(this.mc.currentScreen, this.chocomint, this.chocomint.getUser()));
            this.userCard.setActive(false);
        });

        // Initialize Contributors Panel
        this.contributorsPanel = new ContributorsPanel(this.mc, this.contributorsManager, false);

        // Initialize Friends Panel
        this.friendsPanel = new FriendsPanel(this.chocomint, this.friendsManager, false);

        // Add buttons to the buttonList variable
        this.notificationsButton = new TextureGameBarButton(
                width - 20, 0, 20, 20, "notifications", (a) -> {
            this.notificationPanel.toggleActive();
            this.contributorsPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.setActive(false);
        });

        this.changeMinecraftAccountButton = new TextureGameBarButton(
                width - 70 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getUsername()), 0, 20, 20, "change", (a) -> {
            this.mc.displayGuiScreen(new MinecraftLoginGUI(this.mc.currentScreen, this.chocomint));
        });

        this.userButton = new UserGameBarButton(width - 48 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getUsername()),
                0, this.chocomint.getUser(), (a) -> {
            if(this.chocomint.getUser().getUsername().equals("Guest")) this.mc.displayGuiScreen(new LoginGUI(this.mc.currentScreen, this.chocomint));
            else {
                this.userCard.toggleActive();
                this.notificationPanel.setActive(false);
                this.contributorsPanel.setActive(false);
                this.friendsPanel.setActive(false);
            }
        });

        this.friendsButton = new TextureGameBarButton(
                width - 92 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getUsername()), 0, 20, 20, "friends", (a) -> {
            this.notificationPanel.setActive(false);
            this.contributorsPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.toggleActive();
        });

        this.chatButton = new TextureGameBarButton(width - 114 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getUsername()), 0, 20, 20, "chat", (a) -> {
            this.notificationPanel.setActive(false);
            this.contributorsPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.setActive(false);
            this.mc.displayGuiScreen(new Chat("", this.mc.currentScreen, this.chocomint, this.mc.getCurrentServerData()));
        });

        this.configButton = new GameBarButton(width - 136 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getUsername()), 0, 20, 20, "C",
                (a) -> {
                    this.mc.displayGuiScreen(new GuiMods(this.parentScreen, this.chocomint));
                });
    }

    @EventHandler public void login(UserConnectEvent event) {
        this.userButton.setUser(event.getUser());
        this.userCard.setUser(event.getUser());
    }

    private double dw;

    public void draw(int mouseX, int mouseY, float partialTicks, int width, int height) {

        // Draw main rectangle (width x g20 res, #222)
        drawRect(0, 0, width, 20, new Color(32,34,37).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 10, 16, new Color(65, 44, 25).getRGB());
        drawRect(10, 4, 16, 16, new Color(63, 222, 160).getRGB());
        drawString(this.fontRendererObj, "chocomint", 20, 6, Color.WHITE.getRGB());

        this.userCard.drawCard(width - 132, 22);
        this.notificationPanel.drawPanel(width, height, this.notificationScroll);
        this.contributorsPanel.drawPanel(width, height);
        this.friendsPanel.drawPanel(width, height);

        if(!notificationPanel.isActive())
            this.notificationOverlay.drawOverlay(5, 27);

        boolean isAuthenticated = this.chocomint.getAlmendra() != null && this.chocomint.getAlmendra().isAuthenticated();

        this.friendsButton.setEnabled(isAuthenticated);
        this.chatButton.setEnabled(isAuthenticated);

        this.userButton.draw(mouseX, mouseY);
        this.notificationsButton.draw(mouseX, mouseY);
        this.changeMinecraftAccountButton.draw(mouseX, mouseY);
        if(!this.chocomint.getUser().getUsername().equalsIgnoreCase("Guest")) {
            this.friendsButton.draw(mouseX, mouseY);
            this.chatButton.draw(mouseX, mouseY);
        }

        if(this.mc.theWorld != null && this.mc.thePlayer != null)
            this.configButton.draw(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int width, int height) {
        // Toggle off userCard's visibility if clicked outside of rendered area
        this.userCard.click(mouseY, mouseX);

        // Same as above, but with Notifications
        if(this.notificationPanel.isActive()) {
            if (mouseX < width - 120)
                this.notificationPanel.setActive(false);

            for (Notification notification : this.notificationManager.getNotifications()) {
                if (mouseX >= notification.getX() + notificationScroll && mouseY >= notification.getY() +notificationScroll && mouseX <= notification.getX() + notificationScroll + 110 && mouseY <= notification.getY() + notificationScroll + 35) {
                    notification.getConsumer().accept(this.parentScreen);
                }
            }
        }

        if (mouseX >= 4 && mouseY >= 4 && mouseX <= 16 && mouseY <= 16) {
            this.notificationPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.setActive(false);
            this.contributorsPanel.toggleActive();
        }

        // Friends panel click
        if(this.friendsPanel.isActive) {
            this.friendsPanel.click(mouseX, mouseY);
        }

        this.userButton.click(mouseX, mouseY);
        this.notificationsButton.click(mouseX, mouseY);
        this.changeMinecraftAccountButton.click(mouseX, mouseY);
        this.friendsButton.click(mouseX, mouseY);
        this.chatButton.click(mouseX, mouseY);
        if(this.mc.theWorld != null && this.mc.thePlayer != null)
            this.configButton.click(mouseX, mouseY);

    }

    public void handleMouseInput(int width, int height) throws IOException {

        int i = Mouse.getEventDWheel();

        if(notificationPanel.isActive()) {
            if (i > 0 && (this.notificationScroll <= 0)) this.notificationScroll += 4.5;
            else if (i < 0 && !((this.notificationManager.getNotifications().size() * 45) <= (height - 45) - this.notificationScroll))
                this.notificationScroll -= 4.5;
        }
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }

    public void setParentScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
}
