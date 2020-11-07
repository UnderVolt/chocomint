package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MockChat extends GuiScreen {

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** Declare ChatManager */
    private final ChatManager chatManager;

    /** Declaring everything related to tabs */
    private Tab selectedTab;
    private final User mockUser1;
    private final User mockUser2 = new User("Usuario2", User.Status.ONLINE);

    public MockChat(final Chocomint chocomint) {
        this.chocomint = chocomint;
        this.chatManager = this.chocomint.getChatManager();
        this.mockUser1 = chocomint.getUser();

        this.chatManager.getOpenTabs().get(0).addMessage(mockUser1, "Mensaje 1");
        this.chatManager.getOpenTabs().get(0).addMessage(mockUser2, "Mensaje 2");
        this.chatManager.getOpenTabs().get(0).addMessage(mockUser1, "Mensaje 3");

        this.chatManager.getOpenTabs().get(1).addMessage(mockUser2, "Mensaje 4");
        this.chatManager.getOpenTabs().get(1).addMessage(mockUser1, "Mensaje 5");
        this.chatManager.getOpenTabs().get(1).addMessage(mockUser2, "Mensaje 6");
    }

    @Override
    public void initGui() {

        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger x = new AtomicInteger(0);
        this.chatManager.getOpenTabs().forEach(tab -> {
            this.buttonList.add(new GuiButton(i.get(), x.get(), this.height - 100,
                    10 + this.fontRendererObj.getStringWidth(tab.getName()),
                    18, tab.getName()));
            x.set(x.get() + 10 + this.fontRendererObj.getStringWidth(tab.getName()));
            i.set(i.get() + 1);
        });

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, new Color(138, 102, 102).getRGB());

        drawRect(0, this.height - 100, this.width, this.height,
                Color.BLACK.getRGB());

        drawRect(0, this.width - 100, this.width, this.height - 82,
                new Color(36, 36, 36, 100).getRGB());


        if(selectedTab != null) {
            AtomicInteger i = new AtomicInteger(this.height - 12);
            selectedTab.getMessages().forEach(message -> {
                this.fontRendererObj.drawString("\247e" + message.getUser().getUsername() +
                        "\247f: " + message.getMessage(), 5, i.get(), Color.WHITE.getRGB());
                i.set(i.get() - 12);
            });
        }

        GL11.glScissor(0, this.height - 100, this.width, this.height);
        GL11.glColor3f(255,255,255);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.selectedTab = this.chatManager.getOpenTabs().get(button.id);
    }
}
