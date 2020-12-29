package io.undervolt.gui.user;

import io.undervolt.gui.GameBarButton;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.menu.Menu;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserScreen extends Menu {
    private final User user;
    private final Chocomint chocomint;
    private final UserManager userManager;
    private final DynamicTexture image, banner, countryFlag;
    private boolean drawAlias = true;

    private final Instant createdAt;
    private final String createdMonth, createdYear;

    private GuiScreen prev;

    private boolean showDevInfoCard = false;

    private GuiButton logOutButton;
    private GuiButton profileSettingsButton;
    private GuiButton friendRequestButton;
    private GuiButton sendDMButton;

    public UserScreen(GuiScreen prev, Chocomint chocomint, final User user) {
        super(prev, chocomint, "Perfil", 0);
        this.chocomint = chocomint;
        this.prev = prev;
        this.user = user;
        this.userManager = chocomint.getUserManager();
        this.image = this.userManager.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getImage());
        this.countryFlag = this.userManager.getCountryFlagManager().getCountryFlag(user.getCountryCode());
        if(this.user.getBanner() != null)
            this.banner = this.userManager.getUserProfilePictureManager().getImageAsDynamicTexture(this.user.getBanner());
        else this.banner = null;

        this.createdAt = Instant.parse(this.user.getCreateDate());

        this.createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
        this.createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));
    }

    @Override
    public void initGui() {

        if(user.getAlias().toLowerCase().equals(user.getUsername())) {
            this.drawAlias = false;
        }

        if(this.user.getUsername().equals(this.chocomint.getUser().getUsername())) {
            this.buttonList.add(this.logOutButton = new GuiButton(101, 20, 140, this.width - 40, 20, "Cerrar sesión"));
            this.buttonList.add(this.profileSettingsButton = new GuiButton(102, 20, 165, this.width - 40, 20, "Opciones de perfil"));
        } else {
            if(this.chocomint.getFriendsManager().friendsPool.containsKey(this.user.getUsername()))
                this.buttonList.add(this.sendDMButton = new GuiButton(103, 20, 140, this.width - 40, 20, "Enviar un mensaje privado"));
            else
                this.buttonList.add(this.friendRequestButton = new GuiButton(104, 20, 165, this.width - 40, 20, "Enviar solicitud de amistad"));
        }

        if(this.user.getUsername().equals("Guest")) {
            this.friendRequestButton.enabled = false;
            this.sendDMButton.enabled = false;
            this.logOutButton.enabled = false;
            this.profileSettingsButton.enabled = false;
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(this.height);
        this.mc.getTextureManager().bindTexture(
                this.mc.getTextureManager().getDynamicTextureLocation("pfp1", image));
        Gui.drawModalRectWithCustomSizedTexture(20, 40, 0, 0, 60, 60, 60, 60);

        GL11.glPushMatrix();
        GlStateManager.translate(85, 45, 0);
        GlStateManager.scale(1.5, 1.5, 0);
        this.fontRendererObj.drawString(this.user.getAlias(), 0, 0, Color.white.getRGB());
        GL11.glPopMatrix();

        if(drawAlias) this.fontRendererObj.drawString("(" + this.user.getUsername() + ")", 105, 60, Color.LIGHT_GRAY.getRGB());

        GL11.glPushMatrix();
        GL11.glColor3f(255, 255, 255);
        GlStateManager.translate(85, 57, 0);
        this.mc.getTextureManager().bindTexture(this.mc.getTextureManager().getDynamicTextureLocation(user.getCountryCode(), this.countryFlag));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 15, 15, 15, 15);
        GL11.glPopMatrix();

        String dateToDraw;
        if(this.createdMonth.equals("ene.") && this.createdYear.equals("2020"))
            dateToDraw = "Desde el principio";
        else
            dateToDraw = "Se unió en " + this.createdMonth + " de " + this.createdYear;

        this.fontRendererObj.drawString(dateToDraw, 85, 72, Color.WHITE.getRGB());

        if(this.user.isDeveloper()) {
            this.chocomint.getRenderUtils().drawRoundedRect(85, 89, 3 + this.fontRendererObj.getStringWidth("DEV"), 11,
            3, new Color(47, 56, 168).getRGB());
            GL11.glColor3f(255, 255, 255);
            this.fontRendererObj.drawString("DEV",87, 91, Color.WHITE.getRGB());

            if(this.showDevInfoCard) {
                String devInfoCardText = "Este usuario es un desarrollador oficial de chocomint";
                this.chocomint.getRenderUtils().drawRoundedRect(92 + this.fontRendererObj.getStringWidth("DEV"), 86,
                        12 + this.fontRendererObj.getStringWidth(devInfoCardText), 17, 3, new Color(78, 78, 78, 120).getRGB());
                GL11.glColor3f(255, 255, 255);
                this.fontRendererObj.drawString(devInfoCardText, 98 + this.fontRendererObj.getStringWidth("DEV"), 91, Color.WHITE.getRGB());
            }
        }

        drawRect(0, 120, this.width, this.height, new Color(54,57,63).getRGB());
        if(this.user.getUsername().equals(this.chocomint.getUser().getUsername()))
            drawCenteredString(this.fontRendererObj, "Has estado jugando por " + this.chocomint.getParsedOpenTime(), this.width / 2, 195, Color.WHITE.getRGB());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id) {
            case 101:
                this.chocomint.getConfig().setToken(null);
                this.chocomint.setUser(this.chocomint.getUserManager().setUser((String) null));
                this.chocomint.getAlmendra().disconnect();
                this.chocomint.getChatManager().removeTabs();
                this.mc.displayGuiScreen(this.prev);
                break;
            case 102:
                Desktop desktop = java.awt.Desktop.getDesktop();
                try {
                    URI oURL = new URI("https://undervolt.io");
                    desktop.browse(oURL);
                } catch (URISyntaxException e) {
                    this.chocomint.getNotificationManager().addNotification(
                            new Notification(Notification.Priority.WARNING, "Error abriendo el navegador", e.getMessage(), obj->{})
                    );
                    e.printStackTrace();
                }
                break;
            case 103:
                this.chocomint.getChatManager().setSelectedTab(this.chocomint.getChatManager().getOrCreateTabByName(this.user.getUsername()));
                this.mc.displayGuiScreen(new Chat("", this, this.chocomint, this.mc.getCurrentServerData()));
                break;
            case 104:
                this.chocomint.getNotificationManager().addNotification(
                        new Notification(Notification.Priority.ALERT, "Soon", "La función no ha sido implementada.", obj->{})
                );
                break;
        }

        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if(this.user.isDeveloper() && mouseX >= 85 && mouseY >= 89 && mouseX <= 88 + this.fontRendererObj.getStringWidth("DEV") && mouseY <= 100) {
            this.showDevInfoCard = true;
        } else {
            this.showDevInfoCard = false;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
