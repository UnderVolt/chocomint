package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AvailableRoomsGUI extends GameBar {

    private final Almendra almendra;
    private final ChatManager chatManager;
    private List<Tab> availableRooms = Lists.newArrayList();
    public final Chat previous;
    private final Chocomint chocomint;
    private GuiButton pmButton;

    public AvailableRoomsGUI(final Chat previous, final Chocomint chocomint, final ChatManager chatManager) {
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

        this.buttonList.add(this.pmButton = new GuiButton(1337, this.width / 2 - 100, y.get(), "Enviar un mensaje privado"));

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
                this.mc.displayGuiScreen(new SendPMGui(this, this.chocomint));
            } else {
                super.actionPerformed(button);
            }
        }
    }
}
