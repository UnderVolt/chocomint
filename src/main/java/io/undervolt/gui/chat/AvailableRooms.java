package io.undervolt.gui.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.api.almendra.Almendra;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.GameBar;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.gui.clickable.ClickableLabel;
import io.undervolt.gui.user.UserSearch;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AvailableRooms extends AnimationUI {

    private final Almendra almendra;
    private final ChatManager chatManager;
    private List<Tab> availableRooms = Lists.newArrayList();
    public final GuiScreen previous;
    private final Chocomint chocomint;
    private GuiButton pmButton;

    private final int chatHeight;

    private final Map<String, Clickable> clickableMap = Maps.newHashMap();
    private final AtomicInteger y = new AtomicInteger(30);

    public AvailableRooms(final GuiScreen previous, final int chatHeight) {
        this.chocomint = GameBridge.getChocomint();
        this.almendra = chocomint.getAlmendra();
        this.chatManager = chocomint.getChatManager();
        this.chatHeight = chatHeight;
        this.previous = previous;
    }

    @Override
    public void initGui() {
        this.almendra.getAvailableRooms().forEach((roomName, tab) -> {
            this.clickableMap.put(roomName, new ClickableLabel(15, this.chatHeight - 18 + y.get(), roomName, a->{
                this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(roomName));
                this.mc.displayGuiScreen(previous);
            }));
            y.set(y.get() + 15);
        });

        this.clickableMap.put("#comandos", new ClickableLabel(15, this.chatHeight - 18 + y.get() + 20,  "#comandos", a->{
            this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
            if(!this.chatManager.getOpenTabs().contains(this.chatManager.getReservedLogTab()))
                this.chatManager.getOpenTabs().add(this.chatManager.getReservedLogTab());
            this.mc.displayGuiScreen(previous);
        }));

        this.clickableMap.put("USERSEARCH_RESERVED", new ClickableLabel(5, this.chatHeight - 18 + y.get() + 40,
                "> Buscar usuario", Color.WHITE.getRGB(),
                a-> this.chocomint.displayMenuOrPanel(new UserSearch(this.previous, chocomint))));

        super.initGui();
        this.chocomint.getGameBar().init(width, height);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.previous.drawScreen(mouseX, mouseY, partialTicks);

        drawRect(0,this.chatHeight - 18, 120, this.height, new Color(32,34,37).getRGB());

        this.fontRendererObj.drawString("> Salas globales", 5, this.chatHeight - 18 + 15, Color.WHITE.getRGB());
        this.fontRendererObj.drawString("> Salas locales", 5, this.chatHeight - 18 + y.get() + 5, Color.WHITE.getRGB());
        this.clickableMap.forEach((k, v) -> v.draw(mouseX, mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.chocomint.getGameBar().draw(mouseX, mouseY, partialTicks, width, height);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.clickableMap.forEach((k, v) -> v.click(mouseX, mouseY, mouseButton));
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
        super.mouseClicked(mouseX, mouseY, mouseButton);
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
