package io.undervolt.gui;

import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class ExtendMenuExample extends Menu {
    public ExtendMenuExample(Chocomint chocomint) {
        super(chocomint);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {
        drawCenteredString(this.mc.fontRendererObj, "Texto de ejemplo", this.width / 2, this.height - 10, Color.WHITE.getRGB());
    }
}
