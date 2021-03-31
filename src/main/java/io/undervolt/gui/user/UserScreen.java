package io.undervolt.gui.user;

import io.undervolt.gui.GameBarButton;
import io.undervolt.gui.MenuScrollClickableButton;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.menu.Menu;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class UserScreen extends Menu {
    private User user;
    private final Chocomint chocomint;
    private final UserManager userManager;
    private DynamicTexture image, banner, countryFlag;
    private String imageString, bannerString, countyFlagString;
    private boolean drawAlias = true;

    private Instant createdAt;
    private String createdMonth, createdYear;

    private boolean isFriend;

    private GuiScreen prev;

    private final String username;

    private boolean showDevInfoCard = false;

    private MenuScrollClickableButton logOutButton;
    private MenuScrollClickableButton profileSettingsButton;
    private MenuScrollClickableButton friendRequestButton;
    private MenuScrollClickableButton sendDMButton;
    private MenuScrollClickableButton deleteFriendButton;

    public UserScreen(GuiScreen prev, Chocomint chocomint, final User user) {
        super(prev, chocomint, "Perfil", 0);
        this.chocomint = chocomint;
        this.prev = prev;
        this.userManager = chocomint.getUserManager();
        this.user = user;
        this.username = this.user.getUsername();
        this.setParameters(user);
    }

    public UserScreen(GuiScreen prev, Chocomint chocomint, String username) {
        super(prev, chocomint, "Perfil", 0);
        this.chocomint = chocomint;
        this.prev = prev;
        this.userManager = chocomint.getUserManager();
        this.username = username;
    }

    public void setParameters(User user) {
        this.imageString = user.getImage();
        this.countyFlagString = user.getCountryCode();
        this.bannerString = user.getBanner();

        this.createdAt = Instant.parse(user.getCreateDate());

        this.createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
        this.createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));

        this.isFriend = this.chocomint.getFriendsManager().friendsPool.containsKey(user.getUsername());

        if(this.logOutButton != null) this.logOutButton.setConsumer((a)-> {
            this.chocomint.getConfig().setToken(null);
            this.chocomint.setUser(this.chocomint.getUserManager().setUser((String) null));
            this.chocomint.getAlmendra().disconnect();
            this.chocomint.getChatManager().removeTabs();
            this.mc.displayGuiScreen(this.prev);
        });

        if(this.profileSettingsButton != null) this.profileSettingsButton.setConsumer((a)->{
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                URI oURL = new URI("https://www.undervolt.io/user/" + this.user.getUsername());
                desktop.browse(oURL);
            } catch (URISyntaxException | IOException e) {
                this.chocomint.getNotificationManager().addNotification(
                        new Notification(Notification.Priority.WARNING, "Error abriendo el navegador", e.getMessage(), obj->{})
                );
                e.printStackTrace();
            }
        });

        if(this.sendDMButton != null) this.sendDMButton.setConsumer((a)->{
            this.chocomint.getChatManager().setSelectedTab(this.chocomint.getChatManager().getOrCreateTabByName(this.username));
            this.mc.displayGuiScreen(new Chat("", this, this.chocomint, this.mc.getCurrentServerData()));
        });

        if(this.deleteFriendButton != null) this.deleteFriendButton.setConsumer((a)->{
            this.user.removeFriend();
            this.mc.displayGuiScreen(this);
        });

        if(this.friendRequestButton != null) this.friendRequestButton.setConsumer((a)->{
            if(this.chocomint.getFriendsManager().friendRequestPool.containsKey(this.username)) {
                this.user.acceptFriendRequest();
                this.mc.displayGuiScreen(this);
            } else {
                this.user.sendFriendRequest();
                this.friendRequestButton.setTexture("friend-check");
            }
        });
    }

    @Override
    public void initGui() {

        this.logOutButton = new MenuScrollClickableButton("exit", (a)->{}, 20, 20, new Color(32,34,37).getRGB(), new Color(175, 27, 27).getRGB());
        this.profileSettingsButton = new MenuScrollClickableButton("external", (a)-> {}, 16, 16, new Color(39, 39, 45).getRGB(), new Color(39, 39, 45).getRGB());
        this.sendDMButton = new MenuScrollClickableButton("message", (a)-> {}, 20, 20, new Color(32,34,37).getRGB(), new Color(54,57,63).getRGB());
        this.deleteFriendButton = new MenuScrollClickableButton("remove-friend", (a)-> {}, 20, 20, new Color(32, 177, 32).getRGB(), new Color(175, 27, 27).getRGB());
        this.friendRequestButton = new MenuScrollClickableButton(
                this.chocomint.getFriendsManager().friendRequestPool.containsKey(this.username) ?
                        "friend-check" : "add-friend", (a)-> {},
                20, 20, new Color(32,34,37).getRGB(), new Color(54,57,63).getRGB());

        if(this.user == null) {
            Multithreading.runAsync(()->this.setParameters(this.user = this.userManager.getUser(this.username)));
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {
        this.setPageSize(this.height);

        if(user != null) {
            if(user.getAlias().toLowerCase().equals(user.getUsername())) {
                this.drawAlias = false;
            }

            if(this.image == null) {
                this.image = this.userManager.getUserProfilePictureManager().getImageAsDynamicTexture(imageString);
                this.countryFlag = this.userManager.getCountryFlagManager().getCountryFlag(countyFlagString);
                if(user.getBanner() != null)
                    this.banner = this.userManager.getUserProfilePictureManager().getImageAsDynamicTexture(bannerString);
                else this.banner = null;
            }

            if (!this.user.getUsername().equals("Guest")) {
                if (this.user.getUsername().equals(this.chocomint.getUser().getUsername())) {
                    if(logOutButton != null) this.logOutButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + 40);
                } else {
                    if (this.isFriend) {
                        if(sendDMButton != null) this.sendDMButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + 40);
                        if(deleteFriendButton != null) this.deleteFriendButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + 65);
                        if (!this.user.isOnline()) this.sendDMButton.setEnabled(false);
                    } else if (this.chocomint.getFriendsManager().friendRequestPool.containsKey(this.user.getUsername()))
                        if(friendRequestButton != null) this.friendRequestButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + 40);
                }
            }

            this.mc.getTextureManager().bindTexture(
                    this.mc.getTextureManager().getDynamicTextureLocation("pfp1", image));
            Gui.drawModalRectWithCustomSizedTexture(x + 20, scroll + 40, 0, 0, 60, 60, 60, 60);

            GL11.glPushMatrix();
            GlStateManager.translate(x + 85, scroll + 45, 0);
            GlStateManager.scale(1.5, 1.5, 0);
            this.fontRendererObj.drawString(this.user.getAlias(), 0, 0, Color.white.getRGB());
            GL11.glPopMatrix();

            if (drawAlias)
                this.fontRendererObj.drawString("(" + this.user.getUsername() + ")", x + 105, scroll + 60, Color.LIGHT_GRAY.getRGB());
            if (isFriend) {
                this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/icon/friends.png"));
                drawModalRectWithCustomSizedTexture(x + 84 + (int) (this.mc.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + 40, 0, 0, 20, 20, 20, 20);
                if(profileSettingsButton != null) this.profileSettingsButton.draw(mouseX, mouseY, this.getContentMargin() + 107 + (int) (this.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + 41);
            } else
                if(profileSettingsButton != null) this.profileSettingsButton.draw(mouseX, mouseY, this.getContentMargin() + 85 + (int) (this.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + 41);

            GL11.glPushMatrix();
            GL11.glColor3f(255, 255, 255);
            GlStateManager.translate(x + 85, scroll + 57, 0);
            this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation(user.getCountryCode(), this.countryFlag));
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 15, 15, 15, 15);
            GL11.glPopMatrix();

            String dateToDraw;
            if (this.createdMonth.contains("ene") && this.createdYear.equals("2020"))
                dateToDraw = "Desde el principio";
            else
                dateToDraw = "Se unió en " + this.createdMonth + " de " + this.createdYear;

            this.fontRendererObj.drawString(dateToDraw, x + 85, scroll + 72, Color.WHITE.getRGB());

            if (this.user.isDeveloper()) {
                this.chocomint.getRenderUtils().drawRoundedRect(x + 85, scroll + 89, 3 + this.fontRendererObj.getStringWidth("DEV"), 11,
                        3, new Color(47, 56, 168).getRGB());
                GL11.glColor3f(255, 255, 255);
                this.fontRendererObj.drawString("DEV", x + 87, scroll + 91, Color.WHITE.getRGB());

                if (this.showDevInfoCard) {
                    String devInfoCardText = "Este usuario es un desarrollador oficial de chocomint";
                    this.chocomint.getRenderUtils().drawRoundedRect(x + 92 + this.fontRendererObj.getStringWidth("DEV"), scroll + 86,
                            12 + this.fontRendererObj.getStringWidth(devInfoCardText), 17, 3, new Color(78, 78, 78, 120).getRGB());
                    GL11.glColor3f(255, 255, 255);
                    this.fontRendererObj.drawString(devInfoCardText, 98 + this.fontRendererObj.getStringWidth("DEV"), 91, Color.WHITE.getRGB());
                }
            }

            this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/ui/bracket-simple.png"));
            drawModalRectWithCustomSizedTexture(x, scroll + 120, 0, 0, this.getContentWidth(), 25, this.getContentWidth(), 25);

            drawRect(x, scroll + 143, this.getContentMargin() + this.getContentWidth(), this.height, new Color(54, 57, 63).getRGB());
            if (this.user.getUsername().equals(this.chocomint.getUser().getUsername()))
                drawCenteredString(this.fontRendererObj, "Has estado jugando por " + this.chocomint.getParsedOpenTime(), this.width / 2, 195, Color.WHITE.getRGB());
        } else {
            drawRect(x + 20, scroll + 40, x + 20 + 60, scroll + 40 + 60, new Color(100, 100, 100, 100).getRGB());
            GL11.glColor3f(255,255,255);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/ui/bracket-simple.png"));
            drawModalRectWithCustomSizedTexture(x, scroll + 120, 0, 0, this.getContentWidth(), 25, this.getContentWidth(), 25);

            drawRect(x, scroll + 143, this.getContentMargin() + this.getContentWidth(), this.height, new Color(54, 57, 63).getRGB());
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 100) this.mc.displayGuiScreen(prev);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        if(this.user.isDeveloper() && mouseX >= this.getContentMargin() + 85 && mouseY >= this.scroll + 89 &&
                mouseX <= this.getContentMargin() + 88 + this.fontRendererObj.getStringWidth("DEV") && mouseY <= this.scroll + 100) {
            this.showDevInfoCard = true;
        } else {
            this.showDevInfoCard = false;
        }

        this.sendDMButton.registerClick(mouseX, mouseY);
        this.profileSettingsButton.registerClick(mouseX, mouseY);
        this.friendRequestButton.registerClick(mouseX, mouseY);
        this.deleteFriendButton.registerClick(mouseX, mouseY);
        this.logOutButton.registerClick(mouseX, mouseY);
    }

    public boolean isOverButton(MenuScrollClickableButton button, int mouseX, int mouseY) {
        return button.isEnabled() && (mouseX > button.getX() && mouseY > button.getY() && mouseX < button.getX() + button.getW()
                && mouseY < button.getY() + button.getH());
    }
}
