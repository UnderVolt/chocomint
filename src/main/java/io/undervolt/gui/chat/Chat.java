package io.undervolt.gui.chat;

import io.undervolt.gui.GameBar;
import io.undervolt.gui.GameBarButton;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Chat extends GameBar {

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
    private String initialText;

    /** Previous GuiScreen */
    private final GuiScreen prev;

    /** Server */
    private ServerData serverData;
    private GameBarButton serverReservedButton;

    public Chat(final String initialText, final GuiScreen prev, final Chocomint chocomint, final ServerData serverData) {
        super(prev, chocomint);

        this.prev = prev;
        this.initialText = initialText;

        this.chocomint = chocomint;
        this.chatManager = this.chocomint.getChatManager();

        this.serverData = serverData;

        this.mockUser1 = chocomint.getUser();
    }

    @Override
    public void initGui() {

        if(this.serverData == null) {
            if (this.chatManager.getOpenTabs().get(1) != null)
                this.selectedTab = this.chatManager.getOpenTabs().get(1);
        } else this.selectedTab = this.chatManager.getReservedServerTab();

        this.textField = new GuiTextField(0, this.fontRendererObj, 10,
                this.height - 10, this.width, this.height);

        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setFocused(true);
        this.textField.setMaxStringLength(255);
        this.textField.setCanLoseFocus(false);
        this.textField.setText(this.initialText);

        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger x = new AtomicInteger(0);
        this.chatManager.getOpenTabs().forEach(tab -> {
            this.buttonList.add(new GameBarButton(i.get(), x.get(), (int)(this.height * 0.66) - 18,
                    10 + this.fontRendererObj.getStringWidth(tab.getName()),
                    18, tab.getName()));
            x.set(x.get() + 10 + this.fontRendererObj.getStringWidth(tab.getName()));
            i.set(i.get() + 1);
        });

        this.serverReservedButton = (GameBarButton) this.buttonList.get(this.chatManager.getOpenTabs().indexOf(this.chatManager.getReservedServerTab()));

        if(this.serverData == null) {
            this.serverReservedButton.enabled = false;
            this.serverReservedButton.buttonText = "No conectado";
        } else {
            this.serverReservedButton.enabled = true;
            this.serverReservedButton.buttonText = this.serverData.serverIP;
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        drawRect(0, (int)(this.height * 0.66), this.width, this.height, new Color(36, 36, 36, 100).getRGB());

        this.textField.drawTextBox();
        drawString(this.fontRendererObj, ">", 3, this.height - 10, Color.CYAN.getRGB());

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, 0, this.width * 2, (int) (this.height * 0.66));
        GL11.glColor3f(255,255,255);

        if(selectedTab != null) {
            int i = this.height - 21;
            for (int id = selectedTab.getMessages().size(); id-- > 0; ) {
                Message message = selectedTab.getMessages().get(id);
                this.fontRendererObj.drawString("\247e" +
                        (message.getUser() != null ? message.getUser().getUsername() + "\247f: " : "")
                        + message.getMessage(), 5, i, Color.WHITE.getRGB());
                i = i - 12;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        drawRect(0, (int)(this.height * 0.66) - 18, this.width, (int)(this.height * 0.66),
                Color.BLACK.getRGB());


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id < 1337098)
            this.selectedTab = this.chatManager.getOpenTabs().get(button.id);
        else super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            if(keyCode == 1) this.mc.displayGuiScreen(this.prev);
            this.textField.textboxKeyTyped(typedChar, keyCode);
        } else {
            if(!this.textField.getText().equals("")) {
                if(this.selectedTab == this.chatManager.getReservedServerTab())
                    this.mc.thePlayer.sendChatMessage(this.textField.getText().trim());
                else
                    this.selectedTab.addMessage(this.chocomint.getUser(), this.textField.getText().trim());
                this.textField.setText("");
            }
        }
    }
}
