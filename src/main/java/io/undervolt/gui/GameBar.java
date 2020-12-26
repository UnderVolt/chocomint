package io.undervolt.gui;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.config.GuiMods;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.contributors.ContributorsPanel;
import io.undervolt.gui.login.LoginGUI;
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
import java.util.Optional;

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

    /** UI elements */
    private final ScaledResolution sr;
    private long ftime;
    private final FontRenderer fontRendererObj;

    /** Declare buttons */
    private List<GuiButton> buttonList;
    private TextureGameBarButton notificationsButton;
    private GameBarButton userButton;
    private TextureGameBarButton musicButton;
    private TextureGameBarButton friendsButton;
    private GameBarButton contributorsButton;
    private GameBarButton configButton;

    /** Declare requirement for previous screen, to prevent accumulation of cached Guis */
    private final GuiScreen parentScreen;

    /** Enable background drawing */
    private boolean backgroundDrawing;

    /** Constructor */
    public GameBar(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.chocomint = GameBridge.getChocomint();
        this.mc = chocomint.getMinecraft();
        this.notificationManager = chocomint.getNotificationManager();
        this.sr = GameBridge.getScaledResolution();
        this.contributorsManager = chocomint.getContributorsManager();
        this.fontRendererObj = this.mc.fontRendererObj;
    }

    public void init(int width, int height) {
        this.buttonList = Lists.newArrayList();
        // Set username trim
        String username = this.chocomint.getUser().getUsername();
        if(this.fontRendererObj.getStringWidth("[ ] " + username) > 62) {
            username = username.substring(0, Math.min(username.length(), 6)) + "...";
        }

        // Initialize Notifications
        this.notificationPanel = new NotificationPanel(this.mc, false,
                this.chocomint.getNotificationManager());

        // Initialize User Card
        this.userCard = new UserCard(this.chocomint, this.mc, this.chocomint.getUser(), false, true, (user) -> {
            this.mc.displayGuiScreen(new UserScreen(this.parentScreen, this.chocomint.getUser()));
        });

        // Initialize Contributors Panel
        this.contributorsPanel = new ContributorsPanel(this.mc, this.contributorsManager, false);

        // Add buttons to the buttonList variable
        this.buttonList.add(this.notificationsButton = new TextureGameBarButton(
                1337101,
                width - 20, 0, 20, 20, "notifications"
        ));
        this.buttonList.add(this.userButton = new GameBarButton(
                1337102,
                width - 84, 0, 62, 20, "[ ] " + username
        ));
        this.buttonList.add(this.musicButton = new TextureGameBarButton(
                1337103,
                width - 108, 0, 20, 20, "music"
        ));
        this.buttonList.add(this.friendsButton = new TextureGameBarButton(
                1337104,
                width - 130, 0, 20, 20, "friends"
        ));
        this.buttonList.add(this.contributorsButton = new GameBarButton(
                1337105,
                width - 154, 0, 20, 20, "C"
        ));
        if(this.mc.theWorld != null && this.mc.thePlayer != null)
            this.buttonList.add(this.configButton = new GameBarButton(
                    1337106,
                    width - 178, 0, 20, 20, "C"
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

        this.buttonList.forEach(b -> b.drawButton(GameBridge.getMinecraft(), mouseX, mouseY));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int width, int height) {
        // Toggle off userCard's visibility if clicked outside of rendered area
        if(this.userCard.isActive()) {
            if(mouseY > 20) {
                if (mouseX >= userCard.x && mouseY >= userCard.y && mouseX <= userCard.x + 130 && mouseY <= userCard.y + 52) {
                    userCard.getConsumer().accept(userCard.getUser());
                } else {
                    this.userCard.toggleActive();
                }
            }
        }

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

        Optional<GuiButton> btn = this.buttonList.stream().filter(b -> b.mousePressed(GameBridge.getMinecraft(), mouseX, mouseY)).findFirst();
        if(btn.isPresent()) this.handleBtn(btn.get());
    }

    public void handleMouseInput(int width, int height) throws IOException {

        int i = Mouse.getEventDWheel();

        if(notificationPanel.isActive()) {
            if (i > 0 && (this.notificationScroll <= 0)) this.notificationScroll += 4.5;
            else if (i < 0 && !((this.notificationManager.getNotifications().size() * 45) <= (height - 45) - this.notificationScroll))
                this.notificationScroll -= 4.5;
        }
    }

    public void handleBtn(GuiButton button) {
        switch (button.id) {
            case 1337101:
                this.notificationPanel.toggleActive();
                this.contributorsPanel.setActive(false);
                this.userCard.setActive(false);
                break;
            case 1337102:
                System.out.println(this.userCard.isActive());
                this.userCard.toggleActive();
                this.notificationPanel.setActive(false);
                this.contributorsPanel.setActive(false);
                if(this.chocomint.getUser().getUsername().equals("Guest")) this.mc.displayGuiScreen(new LoginGUI( this.parentScreen ));
                break;
            case 1337105:
                this.notificationPanel.setActive(false);
                this.userCard.setActive(false);
                this.contributorsPanel.toggleActive();
                break;
            case 1337106:
                this.mc.displayGuiScreen(new GuiMods(this.parentScreen, this.chocomint));
                break;
        }
    }

    public void setBackgroundDrawing(boolean backgroundDrawing) {
        this.backgroundDrawing = backgroundDrawing;
    }
}
