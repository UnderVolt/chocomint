package io.undervolt.gui.user;

import io.undervolt.gui.menu.Menu;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.*;

public class UserScreen extends Menu {
    private final User user;
    private final Chocomint chocomint;
    private final UserManager userManager;
    private final DynamicTexture image;

    public UserScreen(GuiScreen prev, Chocomint chocomint, final User user) {
        super(prev, chocomint, user.getUsername(), 0);
        this.chocomint = chocomint;
        this.user = user;
        this.userManager = chocomint.getUserManager();
        this.image = this.userManager.getImageAsDynamicTexture(this.user.getImage());
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        this.setPageSize(this.height);
        this.mc.getTextureManager().bindTexture(
                this.mc.getTextureManager().getDynamicTextureLocation("pfp1", image));
        Gui.drawModalRectWithCustomSizedTexture(20, 40, 0, 0, 60, 60, 60, 60);
        drawString(this.mc.fontRendererObj, this.user.getUsername(), 85, 60, Color.white.getRGB());
    }
}
