package io.undervolt.gui.chat;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MessageOptions extends AnimationUI {

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
     * @param message
     */
    public MessageOptions(final GuiScreen previousScreen, Message message) {
        this.chocomint = GameBridge.getChocomint();
        this.chatManager = this.chocomint.getChatManager();
        this.previous = previousScreen;
        this.message = message;
        this.gameBar = new GameBar(this );
    }

    @Override
    public void initGui() {
        this.gameBar.init(width, height);
        this.buttonList.add(this.sendPMButton = new GuiButton(100, this.width / 2 - 100, 25, "Enviar un mensaje privado"));

        // Will remove comment when implemented
        //this.buttonList.add(this.reportMessageButton = new GuiButton(101, this.width / 2 - 100, 25, "Reportar jugador"));
        //this.buttonList.add(this.seeProfileButton = new GuiButton(102, this.width / 2 - 100, 25, "Ver Perfil"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.gameBar.draw(mouseX, mouseY, partialTicks, width, height);
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
                    this.mc.displayGuiScreen(new Chat("", null, this.mc.getCurrentServerData()));
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
