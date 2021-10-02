package io.undervolt.gui.clickable;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class UserGameBarButton extends GameBarButton {

    private User user;
    private ResourceLocation pictureLocation;
    private final Chocomint chocomint = GameBridge.getChocomint();

    public UserGameBarButton(int x, int y, User user, Consumer consumer) {
        super(x, y, 40, 20, "", consumer);
        this.user = user;
        this.pictureLocation = this.user.getUsername().equalsIgnoreCase("Guest") ? new ResourceLocation("/chocomint/icon/user.png") : this.mc.getTextureManager().getDynamicTextureLocation(user.getUsername(), this.chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(user.getImage()));
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.width = 20 + this.mc.fontRendererObj.getStringWidth(this.user.getAlias());
        this.mc.getTextureManager().bindTexture(this.pictureLocation);
        GL11.glColor3f(255,255,255);
        Gui.drawModalRectWithCustomSizedTexture(this.x + 2,this.y + 2,0,0,16,
                16, 16, 16);

        GL11.glPushMatrix();
        GlStateManager.translate(this.x + 23, this.y + 7.5, 0);
        GL11.glScalef(0.75F, 0.75F, 0);
        this.mc.fontRendererObj.drawString(user.getAlias(), 0, 0, Color.WHITE.getRGB());
        GL11.glPopMatrix();

    }

    public void setUser(User user) {
        this.user = user;
        this.setPictureLocation(this.mc.getTextureManager().getDynamicTextureLocation(user.getUsername(),
                this.chocomint.getUserProfilePictureManager().getCachedDynamicTexture(user.getImage())));
    }

    public void setPictureLocation(ResourceLocation resourceLocation) {
        this.pictureLocation = resourceLocation;
    }
}
