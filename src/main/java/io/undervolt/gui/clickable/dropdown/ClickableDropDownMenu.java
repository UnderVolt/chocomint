package io.undervolt.gui.clickable.dropdown;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClickableDropDownMenu extends Clickable {

    private final Chocomint chocomint;
    private final Minecraft mc;

    private final Option.OptionSet optionSet;
    private boolean isMenuOpen = false;

    public ClickableDropDownMenu(int x, int y, int width, Option.OptionSet optionSet) {
        super(x, y, width, 16, a->{});
        this.chocomint = GameBridge.getChocomint();
        this.mc = GameBridge.getMinecraft();
        this.optionSet = optionSet;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        GL11.glDisable(GL11.GL_BLEND);

        boolean mouseOver = (mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height && !isMenuOpen);

        AtomicInteger y2 = new AtomicInteger( 4);
        this.chocomint.getRenderUtils().drawRoundedRect(x, y, width, height, 3, mouseOver ? new Color(47, 50, 52).getRGB() : new Color(32, 34, 36).getRGB());
        if(this.isMenuOpen) {
            optionSet.getOptions().forEach(option -> {
                option.draw(x, y + y2.get(), width, mouseX, mouseY);
                y2.set(y2.get() + 12);
            });
            y2.set(y2.get() + 3);
            this.height = y2.get();
        } else {
            this.height = 16;
        }

        GL11.glColor3f(1, 1, 1);
        if(!this.isMenuOpen) {
            this.mc.fontRendererObj.drawString((this.optionSet.getSelectedOption() != null) ? this.optionSet.getSelectedOption().getName() : "Elegir una opción", x + 6, y + 4, Color.WHITE.getRGB());
            this.mc.fontRendererObj.drawString("▼", x + width - 7, y + 4, Color.WHITE.getRGB());
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height) {
            if (isMenuOpen) {
                optionSet.getOptions().forEach(option -> option.click(isMenuOpen, mouseX, mouseY, mouseButton));
            }
            this.toggleMenuOpen();
        } else {
            this.isMenuOpen = false;
        }
    }

    public void toggleMenuOpen() {
        this.isMenuOpen = !this.isMenuOpen;
    }

}

