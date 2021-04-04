package io.undervolt.gui.chat;

import io.undervolt.api.animation.AnimationScreen;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class MessageOptions extends AnimationScreen {

    private GuiButton sendPMButton;
    private GuiButton reportMessageButton;
    private GuiButton seeProfileButton;

    private final Message message;
    private final ChatManager chatManager;
    private final GuiScreen previous;
    private final Chocomint chocomint;
    private final GameBar gameBar;

    /**
     * Constructor
     *  @param previousScreen
     * @param chocomint
     * @param message
     */
    public MessageOptions(final GuiScreen previousScreen, final Chocomint chocomint, Message message) {
        this.message = message;
        this.chatManager = chocomint.getChatManager();
        this.previous = previousScreen;
        this.chocomint = chocomint;
        this.gameBar = new GameBar(this, chocomint, this.buttonList);
    }

    @Override
    public void initGui() {
        this.gameBar.init(width, height);
        //this.buttonList.add(this.sendPMButton = new GuiButton(100, this.width / 2 - 100, 25, "Enviar un mensaje privado"));

        // Will remove comment when implemented
        //this.buttonList.add(this.reportMessageButton = new GuiButton(101, this.width / 2 - 100, 25, "Reportar jugador"));
        //this.buttonList.add(this.seeProfileButton = new GuiButton(102, this.width / 2 - 100, 25, "Ver Perfil"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.gameBar.draw(mouseX, mouseY, partialTicks, width, height);
        drawCenteredString(this.fontRendererObj, "WIP", this.width / 2, (this.height + this.fontRendererObj.FONT_HEIGHT) / 2, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id) {
            case 100:
                this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.message.getUser()));
                if(this.previous instanceof Chat) {
                    this.mc.displayGuiScreen(previous);
                } else if(this.previous instanceof AvailableRooms) {
                    this.mc.displayGuiScreen(((AvailableRooms) this.previous).previous);
                } else {
                    this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
                }
                break;
        }
        this.gameBar.actionPerformed(button);
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
    }
}
