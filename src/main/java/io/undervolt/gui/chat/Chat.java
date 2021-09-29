package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.console.Console;
import io.undervolt.gui.GameBar;
import io.undervolt.gui.GameBarButton;
import io.undervolt.gui.clickable.CircularGameBarButton;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.gui.clickable.ClickableTab;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    public final GuiScreen prev;

    /** Server */
    private final ServerData serverData;

    /** Almendra */
    private final Almendra almendra;

    /** Chat height */
    private int chatHeight;

    /** Scroll implementation */
    private int scroll = 0;
    private double tw = Integer.MAX_VALUE;
    protected long ftime;

    /** Tabs */
    public Map<String, Clickable> clickableTabList = Maps.newHashMap();
    public List<String> clickablesToRemove = Lists.newArrayList();
    private AtomicInteger x = new AtomicInteger(23);

    public Chat(final String initialText, final GuiScreen prev, final Chocomint chocomint, final ServerData serverData) {

        this.prev = prev;
        this.initialText = initialText;

        this.chocomint = chocomint;
        this.chatManager = this.chocomint.getChatManager();
        this.almendra = this.chocomint.getAlmendra();

        this.serverData = serverData;

        this.console = this.chocomint.getConsole();
        this.gameBar = chocomint.getGameBar();
    }

    @Override
    public void initGui() {

        this.ftime = Minecraft.getSystemTime();

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
        } else if(this.chatManager.getSelectedTab() == this.chatManager.getReservedServerTab()
                && this.mc.theWorld == null) {
            this.chatManager.setSelectedTab(this.almendra.getAvailableRooms().getOrDefault("#general", this.chatManager.getReservedLogTab()));
        }

        this.textField = new GuiTextField(0, this.fontRendererObj, 13,
                this.height - 10, this.width, this.height);

        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setFocused(true);
        this.textField.setMaxStringLength(100);
        this.textField.setCanLoseFocus(false);
        this.textField.setText(this.initialText);

        this.clickableTabList.put("ADDTAB_RESERVED", new CircularGameBarButton(2, this.chatHeight - 17, 8, "+",
                a -> this.mc.displayGuiScreen(new AvailableRooms(this, chatHeight))));

        this.gameBar.init(width, height);
        super.initGui();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int hue = 0;

        if(tw != 0) {
            tw = (this.getAnimationTime(this.ftime, 3000.0D) * height);
        }

        if(tw/100 > 1) {
            hue = 130 / ((int) tw / 100);
        } else {
            hue = 130;
        }

        if(this.mc.theWorld == null && this.mc.thePlayer == null) {
            prev.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());
        }

        this.tabUpdateLoop();

        GL11.glPushMatrix();

        GlStateManager.translate(0, tw, 0);

        drawRect(0, this.chatHeight, this.width, this.height, new Color(36, 36, 36, 100).getRGB());

        GL11.glPushMatrix();
        GL11.glColor3f(255,255,255);

        if(this.chatManager.getSelectedTab() != null) {
            int i = this.height - 23 + scroll;
            for (int id = this.chatManager.getSelectedTab().getMessages().size(); id-- > 0; ) {
                this.chatManager.getSelectedTab().getMessages().get(id).drawMessage(i, chatHeight);
                i = i - 12;
            }
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/icon/chat.png"));
        drawModalRectWithCustomSizedTexture(0, this.chatHeight - 20, 0, 0, 20, 20, 20, 20);

        GL11.glPopMatrix();

        drawRect(0, this.height - 12, this.width, this.height, new Color(0,0,0,130).getRGB());
        if((!this.almendra.isAuthenticated()) && this.chatManager.getSelectedTab() != this.chatManager.getReservedServerTab()
                && this.chatManager.getSelectedTab() != this.chatManager.getReservedLogTab())
            this.fontRendererObj.drawString("Inicia sesión para poder hablar",10, this.height - 10, Color.GRAY.getRGB());
        else
            this.textField.drawTextBox();
        this.fontRendererObj.drawString(">", 5, this.height - 10, Color.CYAN.getRGB());

        drawRect(0, this.chatHeight - 18, this.width, this.chatHeight,
                new Color(22, 24, 26).getRGB());

        clickableTabList.forEach((name, tab) -> tab.draw(mouseX, mouseY));

        GL11.glPopMatrix();

        this.gameBar.draw(mouseX, mouseY, partialTicks, width, height);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void tabUpdateLoop() {
        if(!clickablesToRemove.isEmpty()) {
            clickableTabList.clear();
            this.clickableTabList.put("ADDTAB_RESERVED", new CircularGameBarButton(2, this.chatHeight - 17, 8, "+",
                    a -> this.chocomint.displayMenuOrPanel(new AvailableRooms(this, chatHeight))));
            x.set(23);
        }

        if(this.clickableTabList.containsKey("SERVER_RESERVED"))
            ((ClickableTab) this.clickableTabList.get("SERVER_RESERVED")).setTabName("Chat del juego");

        this.chatManager.getOpenTabs().forEach(tab -> {
            if(!clickableTabList.containsKey(tab.getName())) {
                if (this.mc.theWorld != null) {
                    clickableTabList.put(tab.getName(), new ClickableTab(x.get(), this.chatHeight - 18, tab));
                    x.set(x.get() + 26 + this.fontRendererObj.getStringWidth(tab.getName()));
                } else {
                    if (!tab.getName().equals("SERVER_RESERVED")) {
                        clickableTabList.put(tab.getName(), new ClickableTab(x.get(), this.chatHeight - 18, tab));
                        x.set(x.get() + 26 + this.fontRendererObj.getStringWidth(tab.getName()));
                    }
                }
            }
        });

        clickablesToRemove.clear();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1337097)
            this.mc.displayGuiScreen(new AvailableRooms(this, this.chatHeight));
        else if(button.id == 1400000) {
            this.chatManager.removeCurrentTab();
            this.update(false);
        } else {
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
                    this.mc.displayGuiScreen(new AvailableRooms(this, this.chatHeight));
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
        this.chatManager.getSelectedTab().getMessages().forEach(message -> message.click(this, mouseX, mouseY));
        clickableTabList.forEach((name, tab) -> tab.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i < 0 && !(this.scroll <= 0)) this.scroll -=8;
        else if (i > 0  && (this.scroll <= (this.chatManager.getSelectedTab().getMessages().size() * 12) -
                (this.height * .66) + 12)) this.scroll += 8;
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
