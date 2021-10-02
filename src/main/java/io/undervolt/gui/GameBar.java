package io.undervolt.gui;

import com.google.common.collect.Lists;
import io.undervolt.api.event.events.NotificationEvent;
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
import io.undervolt.gui.notifications.*;
import io.undervolt.gui.user.UserCard;
import io.undervolt.gui.user.UserScreen;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GameBar extends Gui implements Listener {

    /** Declare Minecraft */
    private final Minecraft mc;

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** Declare panel status */
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
    public TextureGameBarButton notificationsButton;
    private UserGameBarButton userButton;
    private TextureGameBarButton chatButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton configButton;
    private TextureGameBarButton changeMinecraftAccountButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis */
    private GuiScreen parentScreen;

    /** Enable background drawing */
    private boolean backgroundDrawing;

    /** Constructor */
    public GameBar(final GuiScreen parentScreen, final Chocomint chocomint) {
        this.parentScreen = parentScreen;
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.sr = GameBridge.getScaledResolution();
        this.contributorsManager = chocomint.getContributorsManager();
        this.friendsManager = chocomint.getFriendsManager();
        this.fontRendererObj = this.mc.fontRendererObj;
        this.notificationOverlay = chocomint.getNotificationOverlay();
    }

    public void init(int width, int height) {

        // Initialize User Card
        this.userCard = new UserCard(this.mc, this.chocomint.getUser(), false, true, (user) -> {
            this.chocomint.displayMenuOrPanel(new UserScreen(this.mc.currentScreen, this.chocomint, this.chocomint.getUser()));
            this.userCard.setActive(false);
        });

        // Initialize Contributors Panel
        this.contributorsPanel = new ContributorsPanel(this.mc, this.contributorsManager, false);

        // Initialize Friends Panel
        this.friendsPanel = new FriendsPanel(this.chocomint, this.friendsManager, false);

        // Add buttons to the buttonList variable
        this.notificationsButton = new TextureGameBarButton(
                width - 20, 0, 20, 20, "notifications", (a) -> {
            if(!(this.mc.currentScreen instanceof Panel))
                this.chocomint.displayMenuOrPanel(new NotificationScreen(this.mc.currentScreen));
        });

        this.userButton = new UserGameBarButton(width - 48 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getAlias()),
                0, this.chocomint.getUser(), (a) -> {
            if(this.chocomint.getUser().getUsername().equals("Guest")) {
                if (!this.chocomint.getUser().getAlias().equals("Logging in..."))
                    this.chocomint.displayMenuOrPanel(new LoginGUI(this.mc.currentScreen, this.chocomint));
            } else {
                this.userCard.toggleActive();
                this.contributorsPanel.setActive(false);
                this.friendsPanel.setActive(false);
            }
        });

        this.changeMinecraftAccountButton = new TextureGameBarButton(
                width - 57 - this.userButton.width, 0, 20, 20, "change", (a) -> {
            this.chocomint.displayMenuOrPanel(new MinecraftLoginGUI(this.mc.currentScreen, this.chocomint));
        });

        this.friendsButton = new TextureGameBarButton(
                width - 79 - this.userButton.width, 0, 20, 20, "friends", (a) -> {
            this.contributorsPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.toggleActive();
        });

        this.chatButton = new TextureGameBarButton(width - 101 - this.userButton.width, 0, 20, 20, "chat", (a) -> {
            this.contributorsPanel.setActive(false);
            this.userCard.setActive(false);
            this.friendsPanel.setActive(false);
            this.chocomint.displayMenuOrPanel(new Chat("", this.mc.currentScreen, this.chocomint, this.mc.getCurrentServerData()));
        });

        this.configButton = new GameBarButton(width - 123 - this.userButton.width, 0, 20, 20, "C",
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

        this.userCard.drawCard(width - 132, 22, mouseX, mouseY);
        this.contributorsPanel.drawPanel(width, height);
        this.friendsPanel.drawPanel(width, height, mouseX, mouseY);

        boolean isAuthenticated = this.chocomint.getAlmendra() != null && this.chocomint.getAlmendra().isAuthenticated();

        this.friendsButton.setEnabled(isAuthenticated);
        this.chatButton.setEnabled(isAuthenticated);

        this.userButton.draw(mouseX, mouseY);
        this.userButton.setX(width - 45 - this.mc.fontRendererObj.getStringWidth(this.chocomint.getUser().getAlias()));
        this.notificationsButton.draw(mouseX, mouseY);
        this.changeMinecraftAccountButton.draw(mouseX, mouseY);
        this.changeMinecraftAccountButton.setX(width - 47 - this.userButton.width);
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

        if (mouseX >= 4 && mouseY >= 4 && mouseX <= 16 && mouseY <= 16) {
            this.userCard.setActive(false);
            this.friendsPanel.setActive(false);
            this.contributorsPanel.toggleActive();
        }

        // Friends panel click
        if(this.friendsPanel.isActive) {
            this.friendsPanel.click(mouseX, mouseY);
        }

        this.userButton.click(mouseX, mouseY, mouseButton);
        this.notificationsButton.click(mouseX, mouseY, mouseButton);
        this.changeMinecraftAccountButton.click(mouseX, mouseY, mouseButton);
        this.friendsButton.click(mouseX, mouseY, mouseButton);
        this.chatButton.click(mouseX, mouseY, mouseButton);
        if(this.mc.theWorld != null && this.mc.thePlayer != null)
            this.configButton.click(mouseX, mouseY, mouseButton);

        GL11.glColor3f(1, 1, 1);

    }

    @EventHandler public void notif(NotificationEvent.Add e) {
        if(!(this.mc.currentScreen instanceof NotificationScreen))
            if (this.chocomint.getNotificationManager().getNotifications().size() > 0 &&
                !this.chocomint.getNotificationManager().isRead()) {
                this.chocomint.getGameBar().notificationsButton.setTexture(
                        new ResourceLocation("/chocomint/icon/notifications_unread.png")
                );
            }
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }

    public void setParentScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
}
