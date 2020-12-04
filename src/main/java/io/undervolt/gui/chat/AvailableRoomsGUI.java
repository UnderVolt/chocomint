package io.undervolt.gui.chat;

import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AvailableRoomsGUI extends GameBar {

    private final Almendra almendra;
    private final ChatManager chatManager;

    public AvailableRoomsGUI(final GuiScreen previous, final Chocomint chocomint, final ChatManager chatManager) {
        super(previous, chocomint);
        this.almendra = chocomint.getAlmendra();
        this.chatManager = chatManager;
    }

    @Override
    public void initGui() {

        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger y = new AtomicInteger(25);
        this.almendra.getAvailableRooms().forEach((roomName, tab) -> {
            this.buttonList.add(new GuiButton(i.get(), this.width / 2 - 100, y.get(), roomName));
            i.set(i.get() + 1);
            y.set(y.get() + 22);
        });

        super.initGui();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }
}
