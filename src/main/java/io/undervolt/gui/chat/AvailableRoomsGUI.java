package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AvailableRoomsGUI extends GameBar {

    private final Almendra almendra;
    private final ChatManager chatManager;
    private List<Tab> availableRooms = Lists.newArrayList();
    private final Chat chatScreen;

    public AvailableRoomsGUI(final Chat previous, final Chocomint chocomint, final ChatManager chatManager) {
        super(previous, chocomint);
        this.almendra = chocomint.getAlmendra();
        this.chatManager = chatManager;
        this.chatScreen = previous;
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

        super.initGui();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if(button.id < 1337097) {
            this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(availableRooms.get(button.id).getName()));
            this.mc.displayGuiScreen(chatScreen);
        } else super.actionPerformed(button);
    }
}
