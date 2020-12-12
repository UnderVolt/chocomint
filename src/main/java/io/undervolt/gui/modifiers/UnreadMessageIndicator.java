package io.undervolt.gui.modifiers;

import io.undervolt.api.event.events.RenderGameOverlayEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.gui.chat.ChatManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UnreadMessageIndicator extends Gui implements Listener {

    private final ChatManager chatManager;
    private final Minecraft mc;
    private final ResourceLocation messageIndicatorTexture;
    private final ScaledResolution scaledResolution;

    public UnreadMessageIndicator(final ChatManager chatManager, final Minecraft mc, final String messageIndicatorTexture) {
        this.chatManager = chatManager;
        this.mc = mc;
        this.messageIndicatorTexture = new ResourceLocation("/chocomint/icon/" + messageIndicatorTexture + ".png");
        this.scaledResolution = new ScaledResolution(this.mc);
    }

    @EventHandler public void draw(RenderGameOverlayEvent event) {
        if(this.chatManager.hasUnreadMessages()) this.drawUnreadMessageIcon();
    }

    public void drawUnreadMessageIcon() {
        drawRect(this.scaledResolution.getScaledWidth() - 22, this.scaledResolution.getScaledHeight() - 22,
                this.scaledResolution.getScaledWidth() - 4, this.scaledResolution.getScaledHeight() - 4, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(this.messageIndicatorTexture);
        GL11.glColor3f(255,255,255);
        Gui.drawModalRectWithCustomSizedTexture(
                this.scaledResolution.getScaledWidth() - 22, this.scaledResolution.getScaledHeight() - 22, 0, 0, 18, 18, 18, 18
        );
    }

}
