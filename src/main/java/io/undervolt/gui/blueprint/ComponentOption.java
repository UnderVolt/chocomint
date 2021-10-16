package io.undervolt.gui.blueprint;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.function.Consumer;

public class ComponentOption extends Clickable {

    private final Chocomint chocomint;
    private final Minecraft mc;

    private String letterPlaceholder;

    public ComponentOption(String letterPlaceholder, Consumer consumer) {
        super(-50, -50, 10, 10, consumer);
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.letterPlaceholder = letterPlaceholder;
    }

    public void draw(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        boolean mouseOver = (mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height);
        this.chocomint.getRenderUtils().drawFilledCircle(x + 5, y + 5, 5, mouseOver ? Color.YELLOW.getRGB() : Color.ORANGE.getRGB());
        this.mc.fontRendererObj.drawString(letterPlaceholder, x + 3, y + 2, Color.BLACK.getRGB());
    }
}
