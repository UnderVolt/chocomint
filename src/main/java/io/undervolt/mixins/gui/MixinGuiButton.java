package io.undervolt.mixins.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

import java.awt.*;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {

    @Shadow public boolean visible, enabled;
    @Shadow protected boolean hovered;
    @Shadow public String displayString;
    @Shadow protected int width, height;
    @Shadow public int xPosition, yPosition;
    @Shadow protected abstract int getHoverState(boolean mouseOver);
    @Shadow protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    /**
     * @author Gerardo Wacker
     * @reason Custom buttons
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {

            final RenderUtils renderUtils = GameBridge.getChocomint().getRenderUtils();
            final FontRenderer fontRenderer = mc.fontRendererObj;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            Color color = new Color(32,34,37);

            if (!this.enabled)
            {
                color = new Color(41,43,47);
            }
            else if (this.hovered)
            {
                color = new Color(79, 82, 92);
            }

            renderUtils.drawRoundedRect(this.xPosition, this.yPosition, this.width, this.height, 4, color.getRGB());

            this.mouseDragged(mc, mouseX, mouseY);

            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, Color.white.getRGB());
        }
    }

}
