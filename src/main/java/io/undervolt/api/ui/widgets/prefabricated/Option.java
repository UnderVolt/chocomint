package io.undervolt.gui.clickable.dropdown;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Option extends Gui {

    private final Minecraft mc;
    private final Chocomint chocomint;
    private String optionName;
    private Consumer optionConsumer;
    private final OptionSet parentOptionSet;
    private int x, y, width;

    public Option(OptionSet parentOptionSet, String optionText, Consumer optionConsumer) {
        this.mc = GameBridge.getMinecraft();
        this.optionName = optionText;
        this.optionConsumer = optionConsumer;
        this.parentOptionSet = parentOptionSet;
        this.chocomint = GameBridge.getChocomint();
    }

    public void draw(int x, int y, int width, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        if(mouseX > x + 3 && mouseY > y && mouseX < x + width - 3 && mouseY < y + 11) {
            GL11.glColor3f(1, 1, 1);
            this.chocomint.getRenderUtils().drawRoundedRect(x + 3, y, width - 6, 11, 2, new Color(47, 50, 52).getRGB());
        }
        GL11.glColor3f(1, 1, 1);
        this.mc.fontRendererObj.drawString(optionName, x + 6, y + 2, Color.WHITE.getRGB());
    }

    public void click(boolean enabled, int mouseX, int mouseY, int mouseButton) {
        if(enabled) {
            if (mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + 11) {
                optionConsumer.accept(0);
                this.parentOptionSet.selectOption(this.parentOptionSet.getOptions().indexOf(this));
            }
        }
    }

    public void setOptionName(String optionText) {
        this.optionName = optionText;
    }

    public void setOptionConsumer(Consumer optionConsumer) {
        this.optionConsumer = optionConsumer;
    }

    public String getName() {
        return optionName;
    }

    public Consumer getConsumer() {
        return optionConsumer;
    }

    public static class OptionSet {
        private List<Option> optionList;
        private int selectedOption = -1;

        public OptionSet(List<Option> options) {
            this.optionList = options;
        }

        public static OptionSet newOptionSet(Option[] options) {
            return new OptionSet(Arrays.asList(options));
        }

        public static OptionSet newOptionSet(List<Option> options) {
            return new OptionSet(options);
        }

        public static OptionSet newOptionSet() {
            return new OptionSet(Lists.newArrayList());
        }

        public List<Option> getOptions() {
            return optionList;
        }

        public void setOptions(List<Option> options) {
            this.optionList = options;
        }

        public void addOption(Option option) {
            this.optionList.add(option);
        }

        public void selectOption(int optionID) {
            this.selectedOption = optionID;
        }

        public Option getSelectedOption() {
            if(selectedOption == -1)
                return null;
            else return optionList.get(selectedOption);
        }
    }
}
