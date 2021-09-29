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
import net.minecraft.client.Minecraft;
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
    public final Chat previous;
    private final Chocomint chocomint;

    private boolean backwards = false;
    private double tw = Integer.MAX_VALUE;
    protected long ftime;

    private AnimationUI newScreen;

    private final int chatHeight;

    private final Map<String, Clickable> clickableMap = Maps.newHashMap();
    private final AtomicInteger y = new AtomicInteger(30);

    public AvailableRooms(final Chat previous, final int chatHeight) {
        this.ftime = Minecraft.getSystemTime();
        this.chocomint = GameBridge.getChocomint();
        this.almendra = chocomint.getAlmendra();
        this.chatManager = chocomint.getChatManager();
        this.chatHeight = chatHeight;
        this.previous = previous;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.almendra.getAvailableRooms().forEach((roomName, tab) -> {
            this.clickableMap.put(roomName, new ClickableLabel(15, this.chatHeight - 18 + y.get(), roomName, a->{
                this.chatManager.setSelectedTab(this.chatManager.getOrCreateTabByName(roomName));
                this.fadeOut();
            }));
            y.set(y.get() + 15);
        });

        this.clickableMap.put("#comandos", new ClickableLabel(15, this.chatHeight - 18 + y.get() + 20,  "#comandos", a->{
            this.chatManager.setSelectedTab(this.chatManager.getReservedLogTab());
            if(!this.chatManager.getOpenTabs().contains(this.chatManager.getReservedLogTab()))
                this.chatManager.getOpenTabs().add(this.chatManager.getReservedLogTab());
            this.fadeOut();
        }));

        this.clickableMap.put("USERSEARCH_RESERVED", new ClickableLabel(5, this.chatHeight - 18 + y.get() + 40,
                "> Buscar usuario", Color.WHITE.getRGB(),
                a-> this.chocomint.displayMenuOrPanel(new UserSearch(this.previous, chocomint))));

        previous.width = width;
        previous.height = height;
        this.ftime = Minecraft.getSystemTime();
        this.chocomint.getGameBar().init(width, height);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int hue = 0;

        if(tw != 0) {
            tw = this.getAnimationTime(this.ftime, 2500.0D) * 120;
        } else {
            if(backwards)
                if(newScreen != null)
                    this.mc.displayGuiScreen(this.newScreen);
                else this.mc.displayGuiScreen(this.previous);
        }

        if(!backwards) {
            hue = 130 - (int)(this.getAnimationTime(this.ftime, 3500.0D) * 130);
        } else {
            hue = (int)(this.getAnimationTime(this.ftime, 3500.0D) * 130);
        }

        this.previous.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPushMatrix();
        GlStateManager.translate(backwards ? -120 + tw : -tw, 0, 0);

        drawRect(120, this.chatHeight - 18, this.width + 120, this.height, new Color(0, 0, 0, hue).getRGB());
        drawRect(0,this.chatHeight - 18, 120, this.height, new Color(32,34,37).getRGB());

        this.fontRendererObj.drawString("> Salas globales", 5, this.chatHeight - 18 + 15, Color.WHITE.getRGB());
        this.fontRendererObj.drawString("> Salas locales", 5, this.chatHeight - 18 + y.get() + 5, Color.WHITE.getRGB());
        this.clickableMap.forEach((k, v) -> v.draw(mouseX, mouseY));

        GL11.glPushMatrix();
        GlStateManager.translate(130, this.chatHeight - 18, 0);
        GL11.glRotatef(90, 0, 0, 1);
        drawGradientRect(0, 0, this.height - this.chatHeight + 18, 10, 0, new Color(0, 0, 0, 100).getRGB());
        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPopMatrix();

        this.chocomint.getGameBar().draw(mouseX, mouseY, partialTicks, width, height);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    public void fadeOut() {
        if(!this.backwards) {
            this.backwards = true;
            this.ftime = Minecraft.getSystemTime();
            this.tw = 0.1;
        }
    }

    public void displayNewUI(AnimationUI ui) {
        this.newScreen = ui;
        this.fadeOut();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseX > 120 || mouseY < this.chatHeight - 18)
            this.fadeOut();
        this.clickableMap.forEach((k, v) -> v.click(mouseX, mouseY, mouseButton));
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch(keyCode) {
            case Keyboard.KEY_F9:
                this.mc.displayGuiScreen(new UserSearch(this, this.chocomint));
                break;
            case 1:
                this.fadeOut();
        }
        super.keyTyped(typedChar, keyCode);
    }
}
