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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserScreen extends Menu {
    private User user;
    private final Chocomint chocomint;
    private final UserManager userManager;
    private DynamicTexture image;
    private DynamicTexture banner;
    private DynamicTexture countryFlag;
    private BufferedImage bannerBufferedImage;
    private boolean drawAlias = true;

    private Instant createdAt;
    private String createdMonth;
    private String createdYear;
    private String username;

    private boolean isFriend;

    private GuiScreen prev;

    private boolean showDevInfoCard = false;

    private MenuScrollClickableButton logOutButton;
    private MenuScrollClickableButton profileSettingsButton;
    private MenuScrollClickableButton friendRequestButton;
    private MenuScrollClickableButton sendDMButton;
    private MenuScrollClickableButton deleteFriendButton;

    private ResourceLocation bracketSimple;

    public UserScreen(GuiScreen prev, Chocomint chocomint, final String username) {
        super(prev, chocomint, "Perfil de usuario", MenuColor.PURPLE,"user", 0);
        this.chocomint = chocomint;
        this.prev = prev;
        this.userManager = chocomint.getUserManager();
        this.username = username.toLowerCase();

        Multithreading.runAsync(()->{
            this.user = this.userManager.getUser(username);
            this.chocomint.getUserProfilePictureManager().addImageToCache(this.user.getImage());
            this.chocomint.getCountryFlagManager().addToQueue(user.getCountryCode());
            if(this.user.getBanner() != null) {
                this.banner = this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getBanner());
                this.bannerBufferedImage = this.chocomint.getUserProfilePictureManager().getImageAsBufferedImage(this.user.getBanner());
            } else {
                this.banner = null;
                this.bannerBufferedImage = null;
            }

            this.createdAt = Instant.parse(this.user.getCreateDate());

            this.createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
            this.createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));

            this.isFriend = this.chocomint.getFriendsManager().friendsPool.containsKey(this.username);

            if(user.getAlias().equalsIgnoreCase(this.username)) {
                this.drawAlias = false;
            }
        });
    }

    public UserScreen(GuiScreen prev, Chocomint chocomint, final User user) {
        super(prev, chocomint, "Perfil de usuario", MenuColor.PURPLE,"user", 0);
        this.chocomint = chocomint;
        this.prev = prev;
        this.user = user;
        this.userManager = chocomint.getUserManager();
        this.username = this.user.getUsername();
        this.image = this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getImage());
        this.countryFlag = this.chocomint.getCountryFlagManager().getCountryFlag(user.getCountryCode());
        if(this.user.getBanner() != null) {
            this.banner = this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getBanner());
            this.bannerBufferedImage = this.chocomint.getUserProfilePictureManager().getImageAsBufferedImage(this.user.getBanner());
        } else {
            this.banner = null;
            this.bannerBufferedImage = null;
        }

        this.createdAt = Instant.parse(this.user.getCreateDate());

        this.createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
        this.createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));

        this.isFriend = this.chocomint.getFriendsManager().friendsPool.containsKey(this.username);

        if(user.getAlias().equalsIgnoreCase(this.username)) {
            this.drawAlias = false;
        }
    }

    @Override
    public void initGui() {

        this.bracketSimple = this.getSimpleBracket();

        this.logOutButton = new MenuScrollClickableButton("exit", (a)-> {
            this.chocomint.getConfig().setToken(null);
            this.chocomint.setUser(this.chocomint.getUserManager().setUser((String) null));
            this.chocomint.getAlmendra().disconnect();
            this.chocomint.getChatManager().removeTabs();
            this.mc.displayGuiScreen(this.prev);
        }, 20, 20, new Color(32,34,37).getRGB(), new Color(175, 27, 27).getRGB());

        this.profileSettingsButton = new MenuScrollClickableButton("external", (a)-> {
            Desktop desktop = java.awt.Desktop.getDesktop();
            try {
                URI oURL = new URI("https://www.undervolt.io/user/" + this.username);
                desktop.browse(oURL);
            } catch (URISyntaxException | IOException e) {
                this.chocomint.getNotificationManager().addNotification(
                        new Notification(Notification.Priority.WARNING, "Error abriendo el navegador", e.getMessage(), obj->{})
                );
                e.printStackTrace();
            }
        }, 16, 16, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 0).getRGB());

        this.sendDMButton = new MenuScrollClickableButton("message", (a)-> {
            this.chocomint.getChatManager().setSelectedTab(this.chocomint.getChatManager().getOrCreateTabByName(this.username));
            this.mc.displayGuiScreen(new Chat("", this, this.chocomint, this.mc.getCurrentServerData()));
        }, 20, 20, new Color(32,34,37).getRGB(), new Color(54,57,63).getRGB());

        this.deleteFriendButton = new MenuScrollClickableButton("remove-friend", (a)-> {
            this.user.removeFriend();
            this.mc.displayGuiScreen(this);
        }, 20, 20, new Color(32, 177, 32).getRGB(), new Color(175, 27, 27).getRGB());

        this.friendRequestButton = new MenuScrollClickableButton(
                this.chocomint.getFriendsManager().friendRequestPool.containsKey(this.username) ?
                        "friend-check" : "add-friend", (a)-> {
                    if(this.chocomint.getFriendsManager().friendRequestPool.containsKey(this.username)) {
                        this.user.acceptFriendRequest();
                        this.mc.displayGuiScreen(this);
                    } else {
                        this.user.sendFriendRequest();
                        this.friendRequestButton.setTexture("friend-check");
                    }
        }, 20, 20, new Color(32,34,37).getRGB(), new Color(54,57,63).getRGB());

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private int getBannerPadding() {
        return (int)(this.height * .16 > 160 ? 160 : this.height * .16) + 10;
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {
        this.setPageSize(this.height);

        if(this.banner != null) {
            this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation("banner", this.banner));
            Gui.drawModalRectWithCustomSizedTexture(x, 50 + scroll, 0, 0, this.getContentWidth(),
                    getBannerPadding() + 22, this.getContentWidth(),
                    this.getContentWidth() / (this.bannerBufferedImage.getWidth() / this.bannerBufferedImage.getHeight()));
        }

        GL11.glColor3f(1, 1, 1);
        drawRect(x, scroll + getBannerPadding() + 53, this.getContentMargin() + this.getContentWidth(), scroll + getBannerPadding() + 146, this.getMenuTitleColor());

        if(this.createdMonth != null) {
            if(this.image == null)
                this.image = this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getImage());
            if(this.countryFlag == null)
                this.countryFlag = this.chocomint.getCountryFlagManager().getCachedCountryFlag(this.user.getCountryCode());
            if (!this.username.equals("Guest")) {
                if (this.username.equals(this.chocomint.getUser().getUsername())) {
                    this.logOutButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + getBannerPadding() + 70);
                } else {
                    if (this.isFriend) {
                        this.sendDMButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + getBannerPadding() + 70);
                        this.deleteFriendButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + getBannerPadding() + 95);
                        if (!this.user.isOnline()) this.sendDMButton.setEnabled(false);
                    } else
                        this.friendRequestButton.draw(mouseX, mouseY, this.getContentMargin() + this.getContentWidth() - 30, scroll + getBannerPadding() + 95);
                }
            }

            if(this.image != null) {
                this.mc.getTextureManager().bindTexture(
                        this.mc.getTextureManager().getDynamicTextureLocation("pfp1", image));
                Gui.drawModalRectWithCustomSizedTexture(x + 20, scroll + getBannerPadding() + 70, 0, 0, 60, 60, 60, 60);
            }

            GL11.glPushMatrix();
            GlStateManager.translate(x + 90, scroll + getBannerPadding() + 75, 0);
            GlStateManager.scale(1.5, 1.5, 0);
            this.fontRendererObj.drawString(this.user.getAlias(), 0, 0, Color.white.getRGB());
            GL11.glPopMatrix();

            if (drawAlias)
                this.fontRendererObj.drawString("(" + this.username + ")", x + 105, scroll + getBannerPadding() + 90, Color.LIGHT_GRAY.getRGB());
            if (isFriend) {
                this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/icon/friends.png"));
                drawModalRectWithCustomSizedTexture(x + 89 + (int) (this.mc.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + getBannerPadding() + 70, 0, 0, 20, 20, 20, 20);
                this.profileSettingsButton.draw(mouseX, mouseY, this.getContentMargin() + 112 + (int) (this.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + getBannerPadding() + 71);
            } else
                this.profileSettingsButton.draw(mouseX, mouseY, this.getContentMargin() + 90 + (int) (this.fontRendererObj.getStringWidth(this.user.getAlias()) * 1.5), scroll + getBannerPadding() + 71);

            if(this.countryFlag != null) {
                GL11.glPushMatrix();
                GL11.glColor3f(255, 255, 255);
                GlStateManager.translate(x + 90, scroll + getBannerPadding() + 87, 0);
                this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation(user.getCountryCode(), this.countryFlag));
                drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 15, 15, 15, 15);
                GL11.glPopMatrix();
            }

            String dateToDraw;
            if (this.createdMonth.contains("ene") && this.createdYear.equals("2020"))
                dateToDraw = "Desde el principio";
            else
                dateToDraw = "Se unió en " + this.createdMonth + " de " + this.createdYear;

            this.fontRendererObj.drawString(dateToDraw, x + 90, scroll + getBannerPadding() + 102, Color.WHITE.getRGB());

            if (this.user.isDeveloper()) {
                this.chocomint.getRenderUtils().drawRoundedRect(x + 90, scroll + getBannerPadding() + 119, 3 + this.fontRendererObj.getStringWidth("DEV"), 11,
                        3, new Color(47, 56, 168).getRGB());
                GL11.glColor3f(255, 255, 255);
                this.fontRendererObj.drawString("DEV", x + 92, scroll + getBannerPadding() + 121, Color.WHITE.getRGB());

                if (this.showDevInfoCard) {
                    String devInfoCardText = "Este usuario es un desarrollador oficial de chocomint";
                    this.chocomint.getRenderUtils().drawRoundedRect(x + 97 + this.fontRendererObj.getStringWidth("DEV"), scroll + getBannerPadding() + 116,
                            12 + this.fontRendererObj.getStringWidth(devInfoCardText), 17, 3, new Color(78, 78, 78, 120).getRGB());
                    GL11.glColor3f(255, 255, 255);
                    this.fontRendererObj.drawString(devInfoCardText, x + 103 + this.fontRendererObj.getStringWidth("DEV"), scroll + getBannerPadding() + 121, Color.WHITE.getRGB());
                }
            }
        }

        GL11.glPushMatrix();
        GlStateManager.translate(x + 20, this.scroll + getBannerPadding() + 165, 0);
        GL11.glScalef(1.1f, 1.1f, 0f);
        this.fontRendererObj.drawString("Biografía", 0, 0, Color.WHITE.getRGB());
        GL11.glPopMatrix();

        this.fontRendererObj.drawString("\247oNo tengo biografía", x + 20, this.scroll + getBannerPadding() + 180, Color.WHITE.getRGB());
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 100) this.mc.displayGuiScreen(prev);
    }

    public ResourceLocation getSimpleBracket() {
        return new ResourceLocation("/chocomint/ui/bracket/" + this.color.getName() + "/simple.png");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        if(this.user.isDeveloper() && mouseX >= this.getContentMargin() + 115 && mouseY >= this.scroll + getBannerPadding() + 119 &&
                mouseX <= this.getContentMargin() + 118 + this.fontRendererObj.getStringWidth("DEV") && mouseY <= this.scroll + getBannerPadding() + 130) {
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
