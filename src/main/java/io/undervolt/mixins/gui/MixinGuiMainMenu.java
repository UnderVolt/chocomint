package io.undervolt.mixins.gui;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.GameBar;
import io.undervolt.gui.GameBarButton;
import io.undervolt.gui.chat.AvailableRooms;
import io.undervolt.gui.chat.Chat;
import io.undervolt.gui.user.UserSearch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    /** Chat button */
    private GameBarButton chatButton;
    private GameBar bar;

    boolean alreadyExecuted = false;

    @Inject(method = "initGui", at = @At("RETURN") )
    private void initUI(CallbackInfo info) {
        this.buttonList.add(chatButton = new GameBarButton(103, this.width - 52, this.height - 15, 50, 15, "Chat"));
    }

    @Inject(method = "drawScreen", at = @At("HEAD") )
    private void init(CallbackInfo info) {
        if(this.alreadyExecuted) return;

        this.bar = new GameBar(null );
        this.bar.init(this.width, this.height);
        this.alreadyExecuted = true;
    }

    @Inject(method = "renderSkybox", at = @At("HEAD"), cancellable = true)
    private void drawSkybox(int a, int b, float c, CallbackInfo info) throws IOException {
        if(GameBridge.getChocomint().getBackground().getBackground() != null) {
            this.drawDefaultBackground();
            info.cancel();
        }
    }

    @Override
    public void onResize(Minecraft mc, int width, int height) {
        if(this.bar != null)
            this.bar.init(width, height);
        super.onResize(mc, width, height);
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void draw(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        this.bar.draw(mouseX, mouseY, partialTicks, this.width, this.height);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.bar.mouseClicked(mouseX, mouseY, mouseButton, this.width, this.height);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.bar.handleMouseInput(width, height);
        super.handleMouseInput();
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actPerformed(GuiButton button, CallbackInfo info) throws Exception {
        if(button.id == 103) this.mc.displayGuiScreen(new Chat("", this, null));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch(keyCode) {
            case Keyboard.KEY_F8:
                this.mc.displayGuiScreen(new Chat("", this, null));
                break;
            case Keyboard.KEY_F9:
                this.mc.displayGuiScreen(new AvailableRooms(this));
                break;
            case Keyboard.KEY_F10:
                this.mc.displayGuiScreen(new UserSearch(this ));
                break;
        }
    }
}