package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.GameBar;
import io.undervolt.gui.user.UserSearch;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AvailableRooms extends GameBar {

    private final Almendra almendra;
    private final ChatManager chatManager;
    private List<Tab> availableRooms = Lists.newArrayList();
    public final GuiScreen previous;
    private final Chocomint chocomint;
    private GuiButton pmButton;

    public AvailableRooms(final GuiScreen previous, final Chocomint chocomint, final ChatManager chatManager) {
        super(previous, chocomint);
        this.chocomint = chocomint;
        this.almendra = chocomint.getAlmendra();
        this.chatManager = chatManager;
        this.previous = previous;
    }

    @Override
    public void initGui() {

        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger y = new AtomicInteger(25);

        this.almendra.getAvailableRooms().forEach((roomName, tab) -> {
            this.availableRooms.add(tab);
            this.buttonList.add(new GuiButton(i.get(), this.width / 2 - 100, y.get(), roomName));
            i.set(i.get() + 1);
            y.set(y.get() + 22);
        });

        this.buttonList.add(this.pmButton = new GuiButton(1337, this.width / 2 - 100, y.get(), "#comandos"));

        super.initGui();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawRect(0,0,this.width,this.height, new Color(0,0,0,100).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id < 30) {
            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(availableRooms.get(button.id).getName()));
            this.mc.displayGuiScreen(previous);
        } else {
            if(button.id == 1337) {
                this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
                this.chatManager.getOpenTabs().add(this.chatManager.getReservedLogTab());
                this.mc.displayGuiScreen(previous);
            } else {
                super.actionPerformed(button);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch(keyCode) {
            case Keyboard.KEY_F9:
                this.mc.displayGuiScreen(new UserSearch(this, this.chocomint));
                break;
            case Keyboard.KEY_F10:
                this.chatManager.addTab(this.chatManager.getReservedLogTab());
                this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
                if(previous instanceof Chat)
                    this.mc.displayGuiScreen(previous);
                else
                    this.mc.displayGuiScreen(new Chat("", this.previous, this.chocomint, this.mc.getCurrentServerData()));
                this.mc.displayGuiScreen(previous);
                break;
            case 1:
                this.mc.displayGuiScreen(previous);
        }
        super.keyTyped(typedChar, keyCode);
    }
}
