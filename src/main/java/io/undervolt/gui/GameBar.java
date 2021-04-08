package io.undervolt.gui;

import io.undervolt.api.animation.AnimationRender;
import io.undervolt.bridge.GameBridge;
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

public class GameBar extends Gui {

    /**
     * Declare Minecraft
     */
    private final Minecraft mc;

    /**
     * Declare Chocomint
     */
    private final Chocomint chocomint;

    /**
     * Declare panel status
     */
    private final NotificationManager notificationManager;
    public NotificationPanel notificationPanel;
    private int notificationScroll = 0;
    private final NotificationOverlay notificationOverlay;

    /**
     * Declare User card
     */
    public UserCard userCard;

    /**
     * Declare contributors panel
     */
    private final ContributorsManager contributorsManager;
    public ContributorsPanel contributorsPanel;

    /**
     * Declare friends panel
     */
    private final FriendsManager friendsManager;
    public FriendsPanel friendsPanel;

    /**
     * UI elements
     */
    private final ScaledResolution sr;
    private final FontRenderer fontRendererObj;

    /**
     * Declare buttons
     */
    private final List<GuiButton> buttonList;
    private TextureGameBarButton notificationsButton;
    private TextureGameBarButton userButton;
    private TextureGameBarButton musicButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton contributorsButton;
    private GameBarButton configButton;
    private TextureGameBarButton changeMinecraftAccountButton;

    /**
     * Declare requirement for previous screen, to prevent accumulation of cached Guis
     */
    private final GuiScreen parentScreen;

    /**
     * Enable background drawing
     */
    private boolean backgroundDrawing;

    /**
     * Current menú
     */
    private AnimationRender view;

    /**
     * Constructor
     */
    public GameBar(final GuiScreen parentScreen, final Chocomint chocomint, final List<GuiButton> buttonList) {
        this.buttonList = buttonList;
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
            this.mc.displayGuiScreen(new UserScreen(this.parentScreen, this.chocomint, this.chocomint.getUser()));
        });

        // Initialize Contributors Panel
        this.contributorsPanel = new ContributorsPanel(this.mc, this.contributorsManager, false);

        // Initialize Friends Panel
        this.friendsPanel = new FriendsPanel(this.chocomint, this.friendsManager, false);

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new TextureGameBarButton(
                1337101,
                width - 20, 0, 20, 20, "notifications"
        ));

        this.buttonList.add(this.userButton = new TextureGameBarButton(
                1337102,
                width - 48, 0, 20, 20,
                this.mc.getTextureManager().getDynamicTextureLocation(this.chocomint.getUser().getUsername(),
                        this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(this.chocomint.IS_ONLINE ? this.chocomint.getUser().getImage() :
                                "a,iVBORw0KGgoAAAANSUhEUgAAApcAAAKXCAYAAADXbGGRAAAcNElEQVR4nO3dL2xc+bnH4bftDRiTgAlIV2MSMAFZyZaigAmpFNOYLHCgTQJS0IKQkpUKSgpCSgK6IIFNoU0nYEEGbMEYBHgqdcEYtJJdqQZjYqkXpM7uZjeJHb8+v/PnechFt/5J+e74c86cGf/s9u3b/w0AAEjw89IHAACgPcQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQlAABpxCUAAGnEJQAAacQl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")
                )
        ));
        this.buttonList.add(this.changeMinecraftAccountButton = new TextureGameBarButton(
                1337107,
                width - 70, 0, 20, 20, "change"
        ));
        this.buttonList.add(this.musicButton = new TextureGameBarButton(
                1337103,
                width - 92, 0, 20, 20, "music"
        ));
        this.buttonList.add(this.friendsButton = new TextureGameBarButton(
                1337104,
                width - 114, 0, 20, 20, "friends"
        ));
        if (this.mc.theWorld != null && this.mc.thePlayer != null)
            this.buttonList.add(this.configButton = new GameBarButton(
                    1337106,
                    width - 136, 0, 20, 20, "C"
            ));
    }


    private double dw;

    public void draw(int mouseX, int mouseY, float partialTicks, int width, int height) {


        this.userCard.drawCard(width - 132, 22);
        this.notificationPanel.drawPanel(width, height, this.notificationScroll);
        this.contributorsPanel.drawPanel(width, height);
        this.friendsPanel.drawPanel(width, height);

        if (!notificationPanel.isActive())
            this.notificationOverlay.drawOverlay(5, 27);

        // Draw main rectangle (width x g20 res, #222)
        drawRect(0, 0, width, 20, new Color(32, 34, 37).getRGB());

        // Draw logo placeholder until resources are loaded
        drawRect(4, 4, 10, 16, new Color(65, 44, 25).getRGB());
        drawRect(10, 4, 16, 16, new Color(63, 222, 160).getRGB());
        drawString(this.fontRendererObj, "chocomint", 20, 6, Color.WHITE.getRGB());

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int width, int height) {
        // Toggle off userCard's visibility if clicked outside of rendered area
        this.userCard.click(mouseY, mouseX);

        // Same as above, but with Notifications
        if (this.notificationPanel.isActive()) {
            if (mouseX < width - 120) {
                this.notificationPanel.toggle();
                this.view = null;
            }


            for (Notification notification : this.notificationManager.getNotifications()) {
                if (mouseX >= notification.getX() + notificationScroll && mouseY >= notification.getY() + notificationScroll && mouseX <= notification.getX() + notificationScroll + 110 && mouseY <= notification.getY() + notificationScroll + 35) {
                    notification.getConsumer().accept(this.parentScreen);
                }
            }
        }

        // Friends panel click
        if (this.friendsPanel.isActive()) {
            this.friendsPanel.click(mouseX, mouseY);
        }

        if (mouseX >= 4 && mouseY >= 4 && mouseX <= 16 && mouseY <= 16) {
            if (this.view != null) {
                this.view.toggle();
            }
            if (this.view instanceof ContributorsPanel) {
                this.clearView();
            }else{
                this.contributorsPanel.toggle();
                this.view = this.contributorsPanel;
            }
        }


    }

    public void handleMouseInput(int width, int height) throws IOException {

        int i = Mouse.getEventDWheel();

        if (notificationPanel.isActive()) {
            if (i > 0 && (this.notificationScroll <= 0)) this.notificationScroll += 4.5;
            else if (i < 0 && !((this.notificationManager.getNotifications().size() * 45) <= (height - 45) - this.notificationScroll))
                this.notificationScroll -= 4.5;
        }
    }

    public void actionPerformed(GuiButton button) throws IOException {
        if (this.view != null) {
            this.view.toggle();
        }
        switch (button.id) {
            case 1337101:
                if (this.view instanceof NotificationPanel) {
                    this.clearView();
                    break;
                }
                this.notificationPanel.toggle();
                this.view = this.notificationPanel;

                break;
            case 1337102:
                if (this.view instanceof UserCard) {
                    this.clearView();
                    break;
                }

                if (this.chocomint.getUser().getUsername().equals("Guest")) {
                    this.mc.displayGuiScreen(new LoginGUI(this.parentScreen, this.chocomint));
                }else{
                    this.userCard.toggle();
                    this.view = this.userCard;
                }
                break;
            case 1337104:
                if (this.view instanceof FriendsPanel) {
                    this.clearView();
                    break;
                }
                this.view = this.friendsPanel;
                this.friendsPanel.toggle();
                break;
            case 1337106:
                this.mc.displayGuiScreen(new GuiMods(this.parentScreen, this.chocomint));
                this.clearView();
                break;
            case 1337107:
                this.mc.displayGuiScreen(new MinecraftLoginGUI(this.parentScreen, this.chocomint));
                this.clearView();
                break;
            default:
                this.clearView();
                break;
        }
    }

    public void clearView() {
        this.view = null;
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }
}
