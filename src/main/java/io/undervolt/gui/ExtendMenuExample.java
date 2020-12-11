package io.undervolt.gui;

import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class ExtendMenuExample extends Menu {
    public ExtendMenuExample(GuiScreen prev, Chocomint chocomint) {
        super(prev, chocomint, 900);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        drawCenteredString(this.mc.fontRendererObj, "Texto de ejemplo", this.width / 2, this.height - 10, Color.WHITE.getRGB());
    }
}
