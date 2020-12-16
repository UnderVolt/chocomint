package io.undervolt.gui.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.gui.GuiPasswordField;
import io.undervolt.gui.TextureGameBarButton;
import io.undervolt.gui.menu.Menu;
import io.undervolt.instance.Chocomint;
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

public class LoginGUI extends Menu {

    private final RestUtils restUtils;
    private final Config config;
    private final Chocomint chocomint;

    private final GuiScreen parent;
    private GuiTextField user;
    private GuiTextField pass;

    public LoginGUI(final GuiScreen parent, final Chocomint chocomint) {
        super(parent, chocomint, "Iniciar sesión", parent.height);
        this.parent = parent;
        this.restUtils = chocomint.getRestUtils();
        this.config = chocomint.getConfig();
        this.chocomint = chocomint;
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
            try {
                JSONObject obj = new JSONObject();
                obj.put("user", this.user.getText());
                obj.put("pass", this.pass.getText());
                restUtils.sendJSONRequest("api/login", obj, (js) -> {
                    if (js.get("code").getAsInt() == 200) {
                        JsonObject cfg = new JsonObject();
                        cfg.addProperty("token", js.get("accessToken").getAsString());
                        try (Writer writer = new FileWriter(new File(this.mc.mcDataDir, "uvpt.json"))) {
                            Gson gson = new GsonBuilder().create();
                            gson.toJson(cfg, writer);
                            writer.flush();
                            System.out.println("Created token file");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.config.setToken(js.get("accessToken").getAsString());
                        this.chocomint.setUser(this.chocomint.getUserManager().setUser(js.get("accessToken").getAsString()));
                        this.mc.displayGuiScreen(this.parent);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawMenuItems(int mouseX, int mouseY, float partialTicks) {

        GL11.glPushMatrix();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("/chocomint/ui/undervolt.png"));
        drawModalRectWithCustomSizedTexture(this.width / 2 - 25, this.height / 3 - 38, 0, 0, 50, 57, 50, 57);

        this.user.drawTextBox();
        this.pass.drawTextBox();

        this.drawString(mc.fontRendererObj, this.user.isFocused() || this.user.getText().length() > 0 ? "" : "Usuario", this.width / 2 - 70, this.height / 3 + 41, -1);
        this.drawString(mc.fontRendererObj, this.pass.isFocused() || this.pass.getText().length() > 0 ? "" : "Contraseña", this.width / 2 - 70, this.height / 3 + 64, -1);

        GL11.glPopMatrix();

    }
}
