package io.undervolt.gui.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.gui.GuiPasswordField;
import io.undervolt.gui.TextureGameBarButton;
import io.undervolt.gui.menu.Menu;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.MojangLoginThread;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class MinecraftLoginGUI extends Menu {

    private final Config config;
    private MojangLoginThread thread;

    private final GuiScreen parent;
    private GuiTextField user;
    private GuiTextField pass;

    public MinecraftLoginGUI(final GuiScreen parent, final Chocomint chocomint) {
        super(parent, chocomint, "Iniciar sesión en Mojang", parent.height);
        this.parent = parent;
        this.config = chocomint.getConfig();
    }

    @Override
    public void initGui() {
        this.user = new GuiTextField(99, mc.fontRendererObj, this.width / 2 - 75, this.height / 3 + 37, 150, 18);
        this.pass = new GuiPasswordField(99, this.width / 2 - 75, this.height / 3 + 60, 150, 18);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 3 + 90, 100, 20, "Vincular cuenta"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            this.login();
        }
        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.user.mouseClicked(mouseX, mouseY, mouseButton);
        this.pass.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.user.textboxKeyTyped(typedChar, keyCode);
        this.pass.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_TAB) {
            if (this.user.isFocused()) {
                this.user.setFocused(false);
                this.pass.setFocused(true);
            } else {
                this.user.setFocused(true);
                this.pass.setFocused(false);
            }
        }

        if(keyCode == Keyboard.KEY_RETURN) {
            if(!user.getText().isEmpty() && !this.pass.getText().isEmpty()) {
                this.login();
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks, int x, int scroll) {

        GL11.glPushMatrix();

        GL11.glColor3f(255, 255, 255);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/ui/mojang.png"));
        drawModalRectWithCustomSizedTexture(this.width / 2 - 25, this.height / 3 - 38, 0, 0, 50, 45, 50, 45);
        drawCenteredString(this.fontRendererObj, this.thread == null ? "(Ingresar solo usuario si no es premium)" : this.thread.getStatus(), this.width / 2, this.height / 3 + 14, Color.white.getRGB());

        this.user.drawTextBox();
        this.pass.drawTextBox();

        this.drawString(mc.fontRendererObj, this.user.isFocused() || this.user.getText().length() > 0 ? "" : "Usuario", this.width / 2 - 70, this.height / 3 + 42, -1);
        this.drawString(mc.fontRendererObj, this.pass.isFocused() || this.pass.getText().length() > 0 ? "" : "Contraseña", this.width / 2 - 70, this.height / 3 + 65, -1);

        GL11.glPopMatrix();

    }

    public void login() {
        this.thread = new MojangLoginThread(this.config, this.mc, this.user.getText(), this.pass.getText());
        this.thread.start();
    }
}
