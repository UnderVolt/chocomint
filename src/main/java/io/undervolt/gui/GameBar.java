package io.undervolt.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.config.GuiMods;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.contributors.ContributorsPanel;
import io.undervolt.gui.friends.FriendsManager;
import io.undervolt.gui.friends.FriendsPanel;
import io.undervolt.gui.login.LoginGUI;
import io.undervolt.gui.login.MinecraftAccountGUI;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
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

    /** Declare Minecraft */
    private final Minecraft mc;

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

    /** Declare friends panel */
    private final FriendsManager friendsManager;
    public FriendsPanel friendsPanel;

    /** UI elements */
    private final ScaledResolution sr;
    private long ftime;
    private final FontRenderer fontRendererObj;

    /** Declare buttons */
    private final List<GuiButton> buttonList;
    private TextureGameBarButton notificationsButton;
    private GameBarButton userButton;
    private TextureGameBarButton musicButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton contributorsButton;
    private GameBarButton configButton;
    private TextureGameBarButton changeMinecraftAccountButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis */
    private final GuiScreen parentScreen;

    /** Enable background drawing */
    private boolean backgroundDrawing;

    /** Constructor */
    public GameBar(final GuiScreen parentScreen, final Chocomint chocomint, final List<GuiButton> buttonList) {
        this.buttonList = buttonList;
        this.parentScreen = parentScreen;
        this.chocomint = chocomint;
        this.mc = chocomint.getMinecraft();
        this.notificationManager = chocomint.getNotificationManager();
        this.sr = GameBridge.getScaledResolution();
        this.contributorsManager = chocomint.getContributorsManager();
        this.friendsManager = chocomint.getFriendsManager();
        this.fontRendererObj = this.mc.fontRendererObj;
    }

    public void init(int width, int height) {

        // Initialize Notifications
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.chocomint.getNotificationManager());

        // Initialize User Card
        this.userCard = new UserCard(this.chocomint, this.mc, this.chocomint.getUser(), false, true, (user) -> {
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
                        this.chocomint.getUserManager().getUserProfilePictureManager().getImageAsDynamicTexture(this.chocomint.getUser().getImage())
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
        if(this.mc.theWorld != null && this.mc.thePlayer != null)
            this.buttonList.add(this.configButton = new GameBarButton(
                    1337106,
                    width - 136, 0, 20, 20, "C"
            ));
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
    }

    public void handleMouseInput(int width, int height) throws IOException {

        int i = Mouse.getEventDWheel();

        if(notificationPanel.isActive()) {
            if (i > 0 && (this.notificationScroll <= 0)) this.notificationScroll += 4.5;
            else if (i < 0 && !((this.notificationManager.getNotifications().size() * 45) <= (height - 45) - this.notificationScroll))
                this.notificationScroll -= 4.5;
        }
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1337101:
                this.notificationPanel.toggleActive();
                this.contributorsPanel.setActive(false);
                this.userCard.setActive(false);
                this.friendsPanel.setActive(false);
                break;
            case 1337102:
                System.out.println(this.userCard.isActive());
                this.userCard.toggleActive();
                this.notificationPanel.setActive(false);
                this.contributorsPanel.setActive(false);
                this.friendsPanel.setActive(false);
                if(this.chocomint.getUser().getUsername().equals("Guest")) this.mc.displayGuiScreen(new LoginGUI(this.parentScreen, this.chocomint));
                break;
            case 1337104:
                this.notificationPanel.setActive(false);
                this.contributorsPanel.setActive(false);
                this.userCard.setActive(false);
                this.friendsPanel.toggleActive();
                break;
            case 1337106:
                this.mc.displayGuiScreen(new GuiMods(this.parentScreen, this.chocomint));
                break;
            case 1337107:
                this.mc.displayGuiScreen(new MinecraftAccountGUI(this.parentScreen, this.chocomint));
                break;
        }
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }
}
