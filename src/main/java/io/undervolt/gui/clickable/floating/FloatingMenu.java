package io.undervolt.gui.clickable.floating;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.gui.clickable.dropdown.Option;
import io.undervolt.instance.Chocomint;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class FloatingMenu extends Clickable {

    private final Option.OptionSet optionSet;
    private boolean isActive;

    private final Chocomint chocomint;

    public FloatingMenu(int width, Option.OptionSet optionSet) {
        super(-50, -50, width, 0, a -> {});
        this.optionSet = optionSet;
        this.chocomint = GameBridge.getChocomint();
    }

    public void draw(int mouseX, int mouseY) {
        if(isActive) {
            AtomicInteger y = new AtomicInteger(4);
            this.chocomint.getRenderUtils().drawRoundedRect(x, this.y, width, height, 3, new Color(32, 34, 36).getRGB());
            this.optionSet.getOptions().forEach(option -> {
                option.draw(x, y.get() + this.y, width, mouseX, mouseY);
                y.set(y.get() + 12);
            });
            this.height = y.get() + 4;
        }
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(isActive)
            this.optionSet.getOptions().forEach(option -> option.click(isActive, mouseX, mouseY, mouseButton));
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setFittingX(int x) {
        if((x + width > GameBridge.getScaledResolution().getScaledWidth() - 5)) {
            this.x = GameBridge.getScaledResolution().getScaledWidth() - 5 - width;
        } else {
            this.x = x;
        }
    }

    public void setFittingY(int y) {
        if((y + height > GameBridge.getScaledResolution().getScaledHeight() - 5)) {
            this.y = GameBridge.getScaledResolution().getScaledHeight() - 5 - height;
        } else {
            this.y = y;
        }
    }
}
