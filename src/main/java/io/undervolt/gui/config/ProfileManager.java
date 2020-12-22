package io.undervolt.gui.config;

import io.undervolt.gui.GameBar;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileManager extends GameBar {

    private final Chocomint chocomint;

    /**
     * Constructor
     *
     * @param previousScreen
     * @param chocomint
     */
    public ProfileManager(GuiScreen previousScreen, Chocomint chocomint) {
        super(previousScreen, chocomint);
        this.chocomint = chocomint;
    }

    @Override
    public void initGui() {

        AtomicInteger i = new AtomicInteger(22);
        AtomicInteger id = new AtomicInteger(1);
        this.chocomint.getLoader().availableProfiles.forEach(profile -> {
            this.buttonList.add(new GuiButton(id.get(),  this.width - 100, i.get(), 100, 20, profile.getName()));
            i.set(i.get() + 22);
            id.set(id.get() + 1);
        });

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id >= 1337100) {
            super.actionPerformed(button);
        } else {
            this.chocomint.getLoader().setSelectedProfile(this.chocomint.getLoader().availableProfiles.get(button.id - 1));
            this.chocomint.getConfigurableManager().reloadConfig(this.chocomint.getLoader().availableProfiles.get(button.id - 1));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
