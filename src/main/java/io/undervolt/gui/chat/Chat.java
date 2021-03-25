package io.undervolt.gui.chat;

import io.undervolt.api.almendra.Almendra;
import io.undervolt.console.Console;
import io.undervolt.gui.GameBar;
import io.undervolt.gui.GameBarButton;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Chat extends AnimationUI {

    /** Declare Chocomint */
    private final Chocomint chocomint;

    /** GameBar */
    private final GameBar gameBar;

    /** Declare ChatManager */
    private final ChatManager chatManager;

    /** TextField */
    private GuiTextField textField;
    private final String initialText;
    private int sentHistoryCursor = -1;
    private String historyBuffer = "";

    /** Console */
    private final Console console;

    /** Previous GuiScreen */
    private final GuiScreen prev;

    /** Server */
    private final ServerData serverData;
    private GameBarButton serverReservedButton;

    /** Almendra */
    private final Almendra almendra;

    /** Chat height */
    private int chatHeight;

    /** Scroll implementation */
    private float scroll = 0;

    public Chat(final String initialText, final GuiScreen prev, final Chocomint chocomint, final ServerData serverData) {

        this.prev = prev;
        this.initialText = initialText;

        this.chocomint = chocomint;
        this.chatManager = this.chocomint.getChatManager();
        this.almendra = this.chocomint.getAlmendra();

        this.serverData = serverData;

        this.console = this.chocomint.getConsole();
        this.gameBar = new GameBar(this, this.chocomint, this.buttonList);
    }

    @Override
    public void initGui() {

        this.chatHeight = (int) (this.height * .33);

        this.sentHistoryCursor = this.chatManager.getSentMessages().size();

        if(this.chatManager.getSelectedTab() == null) {
            if (this.mc.theWorld == null || this.mc.thePlayer == null) {
                if (this.chatManager.getOpenTabs().size() > 1)
                    this.chatManager.setSelectedTab(this.chatManager.getOpenTabs().get(1));
                else {
                    this.chatManager.addTab(this.chatManager.getReservedLogTab());
                    this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
                }
            } else this.chatManager.setSelectedTab(this.chatManager.getReservedServerTab());
        }

        this.textField = new GuiTextField(0, this.fontRendererObj, 13,
                this.height - 10, this.width, this.height);

        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setFocused(true);
        this.textField.setMaxStringLength(100);
        this.textField.setCanLoseFocus(false);
        this.textField.setText(this.initialText);

        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger x = new AtomicInteger(0);
        this.chatManager.getOpenTabs().forEach(tab -> {
            this.buttonList.add(new GameBarButton(i.get(), x.get(), this.chatHeight - 18,
                    18 + this.fontRendererObj.getStringWidth(tab.getName()),
                    18, (tab.isRead() ? "" : "\247e• \247f") + tab.getName()));
            this.buttonList.get(i.get()).enabled = tab != this.chatManager.getSelectedTab();
            x.set(x.get() + 18 + this.fontRendererObj.getStringWidth(tab.getName()));
            i.set(i.get() + 1);
        });

            if(this.chatManager.getOpenTabs().contains(this.chatManager.getReservedServerTab()))
                this.serverReservedButton = (GameBarButton) this.buttonList.get(this.chatManager.getOpenTabs().indexOf(this.chatManager.getReservedServerTab()));
            else {
                this.chatManager.getOpenTabs().add(0, this.chatManager.getReservedServerTab());
                this.chatManager.setSelectedTab(this.chatManager.getReservedServerTab());
                this.serverReservedButton = (GameBarButton) this.buttonList.get(this.chatManager.getOpenTabs().indexOf(this.chatManager.getReservedServerTab()));
            }


        /* Declaring everything related to tabs */
        GameBarButton addTabButton;
        this.buttonList.add(addTabButton = new GameBarButton(1337097, this.width - 18,
                this.chatHeight - 18, 18, 18, "+"));
        if(this.chocomint.getUser().getUsername().equals("Guest")) addTabButton.enabled = false;

        GameBarButton closeTabButton;
        this.buttonList.add(closeTabButton = new GameBarButton(1400000, this.width - 18,
                this.chatHeight, 18, 18, "✕"));
        if(this.chatManager.getSelectedTab().equals(this.chatManager.getReservedServerTab()) ||
                (this.chocomint.getUser().getUsername().equals("Guest") && this.chatManager.getSelectedTab().equals(this.chatManager.getReservedLogTab())))
            closeTabButton.enabled = false;


        if(this.mc.theWorld != null && this.mc.thePlayer != null) {
            if(this.serverData != null) {
                this.serverReservedButton.enabled = true;
                this.serverReservedButton.buttonText = this.serverData.serverIP;
            } else {
                this.serverReservedButton.enabled = true;
                this.serverReservedButton.buttonText = "Un jugador";
            }
        } else {
            this.serverReservedButton.enabled = false;
            this.serverReservedButton.buttonText = "No está jugando";
        }

        this.gameBar.init(width, height);
        super.initGui();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        drawRect(0, this.chatHeight, this.width, this.height, new Color(36, 36, 36, 100).getRGB());

        GL11.glPushMatrix();
        GlStateManager.translate(0, scroll, 0);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(0, 0, this.width * 2, (int)(this.height * 2 - this.height * .66));
        GL11.glColor3f(255,255,255);

        if(this.chatManager.getSelectedTab() != null) {
            int i = this.height - 23;
            for (int id = this.chatManager.getSelectedTab().getMessages().size(); id-- > 0; ) {
                Message message = this.chatManager.getSelectedTab().getMessages().get(id);
                this.fontRendererObj.drawStringWithShadow("\247e" +
                        (message.getUser() != null ? message.getUser() + "\247f: " : "")
                        + message.getMessage(), 5, i, Color.WHITE.getRGB());
                i = i - 12;
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        drawRect(0, this.height - 12, this.width, this.height, new Color(0,0,0,130).getRGB());
        if((!this.almendra.isAuthenticated()) && this.chatManager.getSelectedTab() != this.chatManager.getReservedServerTab()
                && this.chatManager.getSelectedTab() != this.chatManager.getReservedLogTab())
            this.fontRendererObj.drawString("Inicia sesión para poder hablar",10, this.height - 10, Color.GRAY.getRGB());
        else
            this.textField.drawTextBox();
        this.fontRendererObj.drawString(">", 5, this.height - 10, Color.CYAN.getRGB());

        drawRect(0, this.chatHeight - 18, this.width, this.chatHeight,
                Color.BLACK.getRGB());

        this.gameBar.draw(mouseX, mouseY, partialTicks, width, height);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id < 1337097) {
            this.chatManager.setSelectedTab(this.chatManager.getOpenTabs().get(button.id));
            this.chatManager.getOpenTabs().get(button.id).setRead();
            button.displayString = this.chatManager.getOpenTabs().get(button.id).getName();
            this.buttonList.forEach(guiButton -> {
                if(guiButton == serverReservedButton) {
                    if(this.mc.theWorld != null && this.mc.thePlayer != null) {
                        if(this.serverData != null) {
                            this.serverReservedButton.enabled = true;
                            this.serverReservedButton.buttonText = this.serverData.serverIP;
                        } else {
                            this.serverReservedButton.enabled = true;
                            this.serverReservedButton.buttonText = "Un jugador";
                        }
                    } else {
                        this.serverReservedButton.enabled = false;
                        this.serverReservedButton.buttonText = "No está jugando";
                    }
                } else guiButton.enabled = guiButton.id != button.id;
            });
            this.update(false);
        } else if(button.id == 1337097)
            this.mc.displayGuiScreen(new AvailableRooms(this, this.chocomint, this.chatManager));
        else if(button.id == 1400000) {
            this.chatManager.removeCurrentTab();
            this.update(false);
        } else {
            this.gameBar.actionPerformed(button);
            super.actionPerformed(button);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode != 28 && keyCode != 156) {
            switch(keyCode) {
                case 1:
                    this.mc.displayGuiScreen(this.prev);
                    break;
                case Keyboard.KEY_F9:
                    this.mc.displayGuiScreen(new AvailableRooms(this, this.chocomint, this.chatManager));
                    break;
                case Keyboard.KEY_UP:
                    this.getSentHistory(-1);
                    break;
                case Keyboard.KEY_DOWN:
                    this.getSentHistory(1);
                    break;
            }
            if(!(this.chocomint.getUser().getUsername().equals("Guest") && this.chatManager.getSelectedTab() != this.chatManager.getReservedServerTab()
                && this.chatManager.getSelectedTab() != this.chatManager.getReservedLogTab()))
                this.textField.textboxKeyTyped(typedChar, keyCode);
        } else {
            if(this.chatManager.getSelectedTab() == this.chatManager.getReservedServerTab() && (this.mc.thePlayer == null || this.mc.theWorld == null)) return;
            if(!this.textField.getText().equals("")) {
                if(this.chatManager.getSelectedTab() == this.chatManager.getReservedServerTab())
                    this.mc.thePlayer.sendChatMessage(this.textField.getText().trim());
                else if(this.chatManager.getSelectedTab() == this.chatManager.getReservedLogTab()) {
                    this.chatManager.getSelectedTab().addMessage((this.chocomint.getUser().isDeveloper() ? "§9" : "") + this.chocomint.getUser().getUsername(), this.textField.getText());
                    this.console.processCommand(this.chatManager.getReservedLogTab(), this.textField.getText());
                } else {
                    if(this.textField.getText().trim().startsWith("/")) {
                        if(!this.chatManager.getOpenTabs().contains(this.chatManager.getReservedLogTab()))
                            this.chatManager.getOpenTabs().add(this.chatManager.getReservedLogTab());
                        this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
                        this.chatManager.getReservedLogTab().addMessage(null, "\247cPara prevenir tu seguridad, te hemos redireccionado a la pestaña de comandos");
                        this.chatManager.getSelectedTab().addMessage((this.chocomint.getUser().isDeveloper() ? "§9" : "") + this.chocomint.getUser().getUsername(), this.textField.getText().trim());
                        this.chocomint.getConsole().processCommand(this.chatManager.getReservedLogTab(), this.textField.getText().trim());
                        this.textField.setText("");
                        this.update(false);
                    } else {
                        this.almendra.sendMessage(this.chatManager.getSelectedTab(), this.textField.getText().trim(), this.chocomint.getUser());
                    }
                }
                this.chatManager.getSentMessages().add(new Message(this.chocomint.getUser().getUsername(), this.textField.getText().trim()));
                this.textField.setText("");
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        this.gameBar.mouseClicked(mouseX, mouseY, mouseButton, width, height);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.gameBar.handleMouseInput(width, height);

        int i = Mouse.getEventDWheel();

        if (i < 0 && !(this.scroll <= 0)) this.scroll -=7.8;
        else if (i > 0  && (this.scroll <= (this.chatManager.getSelectedTab().getMessages().size() * 12) -
                (this.height * .66) + 12)) this.scroll += 7.8;
    }

    public Console getConsole() {
        return console;
    }

    public void update(boolean useNew) {
        if(useNew) this.mc.displayGuiScreen(new Chat(this.textField.getText().trim(), this.prev, this.chocomint, this.serverData));
        else this.mc.displayGuiScreen(this);
    }

    public void getSentHistory(int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        int j = this.chatManager.getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);

        if (i != this.sentHistoryCursor)
        {
            if (i == j)
            {
                this.sentHistoryCursor = j;
                this.textField.setText(this.historyBuffer);
            }
            else
            {
                if (this.sentHistoryCursor == j)
                {
                    this.historyBuffer = this.textField.getText();
                }

                this.textField.setText(this.chatManager.getSentMessages().get(i).getMessage());
                this.sentHistoryCursor = i;
            }
        }
    }
}
