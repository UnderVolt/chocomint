package io.undervolt.api.ui.widgets.prefabricated;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Option {

    private String optionName;
    private Consumer optionConsumer;
    private final OptionSet parentOptionSet;

    public Option(OptionSet parentOptionSet, String optionText, Consumer optionConsumer) {
        this.optionName = optionText;
        this.optionConsumer = optionConsumer;
        this.parentOptionSet = parentOptionSet;
    }

    public void setOptionName(String optionText) {
        this.optionName = optionText;
    }

    public void setOptionConsumer(Consumer optionConsumer) {
        this.optionConsumer = optionConsumer;
    }

    public String getName() {
        if(optionName == null)
            return "Ninguna opción seleccionada";
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
