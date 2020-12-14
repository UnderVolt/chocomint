package io.undervolt.gui.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.gui.GuiPasswordField;
import io.undervolt.gui.TextureGameBarButton;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class LoginGUI extends GuiScreen {

    private final RestUtils restUtils;
    private final Config config;

    private final GuiScreen parent;
    private GuiTextField user;
    private GuiTextField pass;

    public LoginGUI(final GuiScreen parent, final Chocomint chocomint) {
        this.parent = parent;
        this.restUtils = chocomint.getRestUtils();
        this.config = chocomint.getConfig();
    }

    @Override
    public void initGui() {
        this.user = new GuiTextField(99, mc.fontRendererObj, this.width / 2 - 75, this.height / 3, 150, 18);
        this.pass = new GuiPasswordField(99, this.width / 2 - 75, this.height / 3 + 23, 150, 18);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 3 + 70, 100, 20, "Link Account"));
        this.buttonList.add(new TextureGameBarButton(45, 5, 5, 20, 20, "back"));
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("user", this.user.getText());
                    obj.put("pass", this.pass.getText());
                    restUtils.sendJSONRequest("api/login", obj, (js) -> {

                        JSONObject obj2 = new JSONObject();
                        try {
                            if (js.get("code").getAsInt() == 200) {
                                obj2.put("accessToken", js.get("accessToken").getAsString());
                                obj2.put("beta", true);
                                restUtils.sendJSONRequest("noid/login", obj2, (json) -> {
                                    try {
                                        if (json.get("code").getAsInt() == 200) {
                                            JsonObject cfg = new JsonObject();
                                            String token = json.get("user").getAsJsonObject().get("mcToken").getAsString();
                                            cfg.addProperty("token", token);
                                            try (Writer writer = new FileWriter(new File(this.mc.mcDataDir, "uvpt.json"))) {
                                                Gson gson = new GsonBuilder().create();
                                                gson.toJson(cfg, writer);
                                                writer.flush();
                                            }
                                            this.config.setToken(token);
                                            this.mc.displayGuiScreen(this.parent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 45:
                this.mc.displayGuiScreen(parent);
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
    public void updateScreen() {
        this.user.updateCursorCounter();
        this.pass.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor3f(1, 1, 1);

        this.drawDefaultBackground();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();

        GL11.glPushMatrix();

        this.user.drawTextBox();
        this.pass.drawTextBox();

        this.drawString(mc.fontRendererObj, this.user.isFocused() || this.user.getText().length() > 0 ? "" : "Username", this.width / 2 - 70, this.height / 3 + 5, -1);
        this.drawString(mc.fontRendererObj, this.pass.isFocused() || this.pass.getText().length() > 0 ? "" : "Password", this.width / 2 - 70, this.height / 3 + 28, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();

    }

    public void drawRect(int x, int y, int width, int height, Color c) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        Gui.drawRect(0, 0, width, height, c.getRGB());
        GL11.glPopMatrix();
    }
}
