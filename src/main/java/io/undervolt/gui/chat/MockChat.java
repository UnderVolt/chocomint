package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
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

    /** TextField */
    private GuiTextField textField;

    /** Previous GuiScreen */
    private final GuiScreen prev;

    public MockChat(final GuiScreen prev, final Chocomint chocomint) {
        this.prev = prev;

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

        if(this.chatManager.getOpenTabs().get(0) != null)
            this.selectedTab = this.chatManager.getOpenTabs().get(0);

        this.textField = new GuiTextField(0, this.fontRendererObj, 10,
                this.height - 10, this.width, this.height);

        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setFocused(true);
        this.textField.setMaxStringLength(255);
        this.textField.setCanLoseFocus(false);


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

        this.textField.drawTextBox();
        drawString(this.fontRendererObj, ">", 3, this.height - 10, Color.CYAN.getRGB());


        if(selectedTab != null) {
            int i = this.height - 21;
            for (int id = selectedTab.getMessages().size(); id-- > 0; ) {
                Message message = selectedTab.getMessages().get(id);
                this.fontRendererObj.drawString("\247e" + message.getUser().getUsername() +
                        "\247f: " + message.getMessage(), 5, i, Color.WHITE.getRGB());
                i = i - 12;
            }
        }

        GL11.glScissor(0, this.height - 100, this.width, this.height);
        GL11.glColor3f(255,255,255);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.selectedTab = this.chatManager.getOpenTabs().get(button.id);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.prev);
            this.textField.textboxKeyTyped(typedChar, keyCode);
        } else {
            if(!this.textField.getText().equals("")) {
                this.selectedTab.addMessage(this.chocomint.getUser(), this.textField.getText().trim());
                this.textField.setText("");
            }
        }
    }
}
