package io.undervolt.gui.chat;

import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SendPMGui extends GameBar {
    private final GuiScreen previous;
    private final Chocomint chocomint;
    private final ChatManager chatManager;
    private final Almendra almendra;
    private List<String> filteredUserList;

    private GuiTextField textField;

    public SendPMGui(final GuiScreen previous, final Chocomint chocomint) {
        super(previous, chocomint);
        this.chocomint = chocomint;
        this.previous = previous;
        this.chatManager = chocomint.getChatManager();
        this.almendra = chocomint.getAlmendra();
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

        if(this.filteredUserList != null) {
            AtomicInteger y = new AtomicInteger(5);
            this.filteredUserList.forEach(user -> {
                drawCenteredString(this.fontRendererObj, user, this.width / 2, y.get(), Color.WHITE.getRGB());
                y.set(y.get() + 12);
            });
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1337097) {
            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
            if(this.previous instanceof Chat) {
                this.mc.displayGuiScreen(previous);
            } else if(this.previous instanceof AvailableRoomsGUI) {
                this.mc.displayGuiScreen(((AvailableRoomsGUI) this.previous).previous);
            } else {
                this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
            }
            this.mc.displayGuiScreen(previous);
        } else {
            super.actionPerformed(button);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
            this.textField.textboxKeyTyped(typedChar, keyCode);

            if(this.textField.getText().length() >= 3) {
                this.filteredUserList = this.almendra.getConnectedUsers().stream().filter(user -> user.toLowerCase()
                        .startsWith(this.textField.getText().toLowerCase())).collect(Collectors.toList());
            }

        } else {
            if(!this.textField.getText().isEmpty()) {
                if(this.almendra.getConnectedUsers().contains(this.textField.getText().trim()) && !this.textField.getText().trim().equalsIgnoreCase(this.chocomint.getUser())) {
                    this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.textField.getText().trim()));
                    if(this.previous instanceof Chat) {
                        this.mc.displayGuiScreen(previous);
                    } else if(this.previous instanceof AvailableRoomsGUI) {
                        this.mc.displayGuiScreen(((AvailableRoomsGUI) this.previous).previous);
                    } else {
                        this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
                    }
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
