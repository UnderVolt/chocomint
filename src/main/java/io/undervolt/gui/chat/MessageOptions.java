package io.undervolt.gui.chat;

import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MessageOptions extends GameBar {

    private GuiButton sendPMButton;
    private GuiButton reportMessageButton;
    private GuiButton seeProfileButton;

    private final Message message;
    private final ChatManager chatManager;
    private final GuiScreen previous;
    private final Chocomint chocomint;

    /**
     * Constructor
     *  @param previousScreen
     * @param chocomint
     * @param message
     */
    public MessageOptions(final GuiScreen previousScreen, final Chocomint chocomint, Message message) {
        super(previousScreen, chocomint);
        this.message = message;
        this.chatManager = chocomint.getChatManager();
        this.previous = previousScreen;
        this.chocomint = chocomint;
    }

    @Override
    public void initGui() {
        this.buttonList.add(this.sendPMButton = new GuiButton(100, this.width / 2 - 100, 25, "Enviar un mensaje privado"));

        // Will remove comment when implemented
        //this.buttonList.add(this.reportMessageButton = new GuiButton(101, this.width / 2 - 100, 25, "Reportar jugador"));
        //this.buttonList.add(this.seeProfileButton = new GuiButton(102, this.width / 2 - 100, 25, "Ver Perfil"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id) {
            case 100:
                this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(this.message.getUser()));
                if(this.previous instanceof Chat) {
                    this.mc.displayGuiScreen(previous);
                } else if(this.previous instanceof AvailableRoomsGUI) {
                    this.mc.displayGuiScreen(((AvailableRoomsGUI) this.previous).previous);
                } else {
                    this.mc.displayGuiScreen(new Chat("", null, this.chocomint, this.mc.getCurrentServerData()));
                }
                break;
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) this.mc.displayGuiScreen(this.previous);
    }
}