package io.undervolt.gui.chat;

import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class SendPMGui extends GameBar {
    private final AvailableRoomsGUI previous;
    private final Chocomint chocomint;
    private final ChatManager chatManager;

    private GuiTextField textField;

    public SendPMGui(final AvailableRoomsGUI previous, final Chocomint chocomint) {
        super(previous, chocomint);
        this.chocomint = chocomint;
        this.previous = previous;
        this.chatManager = chocomint.getChatManager();
    }

    @Override
    public void initGui() {

        this.textField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100,
                22, 200, 23);

        this.textField.setFocused(true);

        this.buttonList.add(new GuiButton(13370197, this.width / 2 - 100, 50, "Enviar un mensaje"));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawRect(0,0,this.width,this.height, new Color(0,0,0,100).getRGB());
        this.textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1337097) {
            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
            this.mc.displayGuiScreen(previous.previous);
        } else {
            super.actionPerformed(button);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
            this.textField.textboxKeyTyped(typedChar, keyCode);
        } else {
            if(!this.textField.getText().isEmpty()) {
                this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
                this.mc.displayGuiScreen(previous.previous);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
