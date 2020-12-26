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

public class UserScreen extends Menu {
    private final User user;
    private final Chocomint chocomint;
    private final UserManager userManager;
    private final DynamicTexture image;
    private final DynamicTexture countryFlag;
    private boolean drawAlias = true;

    private GuiScreen prev;

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
            this.buttonList.add(this.sendDMButton = new GuiButton(103, 20, 140, this.width - 40, 20, "Enviar un mensaje privado"));
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

        if(this.user.isDeveloper()) {
            this.chocomint.getRenderUtils().drawRoundedRect(85, 89, 3 + this.fontRendererObj.getStringWidth("DEV"), 11,
            3, new Color(47, 56, 168).getRGB());
            GL11.glColor3f(255, 255, 255);
            this.fontRendererObj.drawString("DEV",87, 91, Color.WHITE.getRGB());
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
}
