package io.undervolt.gui.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.gui.GuiPasswordField;
import io.undervolt.gui.Panel;
import io.undervolt.gui.clickable.Button;
import io.undervolt.gui.clickable.ClickableLabel;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginGUI extends Panel {

    private final RestUtils restUtils;
    private final Config config;
    private final Chocomint chocomint;

    private final Button linkAccount = new Button(this.width - this.getPanelWidth() + 9, this.scroll + 185, this.getPanelWidth() - 20, 20, "Vincular cuenta", (a) -> this.login());;
    private ClickableLabel createAccount;

    private GuiTextField user;
    private GuiTextField pass;

    public LoginGUI(final GuiScreen parent, final Chocomint chocomint) {
        super(parent, "Cuenta chocomint", parent.height);
        this.previousScreen = parent;
        this.restUtils = chocomint.getRestUtils();
        this.config = chocomint.getConfig();
        this.chocomint = chocomint;
    }

    @Override
    public void initGui() {
        this.user = new GuiTextField(99, mc.fontRendererObj, this.width - this.getPanelWidth() + 10, this.scroll + 120, this.getPanelWidth() - 20, 18);
        this.pass = new GuiPasswordField(99, this.width - this.getPanelWidth() + 10, this.scroll + 150, this.getPanelWidth() - 20, 18);
        this.createAccount = new ClickableLabel(this.width - this.getPanelWidth() + (this.getPanelWidth() / 2) - (this.mc.fontRendererObj.getStringWidth("¿No tenés cuenta?") / 2), this.scroll + 215, "¿No tenés cuenta?", a -> this.openCreateLink());
        super.initGui();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.user.mouseClicked(mouseX, mouseY, mouseButton);
        this.pass.mouseClicked(mouseX, mouseY, mouseButton);
        this.linkAccount.click(mouseX, mouseY, mouseButton);
        this.createAccount.click(mouseX, mouseY, mouseButton);
        this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, scroll);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void openCreateLink() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI oURL = new URI("https://www.undervolt.io/createaccount");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            this.chocomint.getNotificationManager().addNotification(
                    new Notification(Notification.Priority.WARNING, "Error abriendo el navegador", e.getMessage(), obj->{})
            );
            e.printStackTrace();
        }
    }

    public void login() {
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
                    this.fadeOut();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void drawContent(int mouseX, int mouseY, float partialTicks, int margin, int scroll) {

        GL11.glPushMatrix();

        this.user.drawTextBox();
        this.pass.drawTextBox();

        drawRect(margin + (this.getPanelWidth() / 2) - 25, 50, margin + (this.getPanelWidth() / 2) + 25, 100, Color.red.getRGB());

        this.linkAccount.draw(mouseX, mouseY);
        this.createAccount.draw(mouseX, mouseY);

        this.drawString(mc.fontRendererObj, this.user.isFocused() || this.user.getText().length() > 0 ? "" : "Usuario", this.width - this.getPanelWidth() + 15, 125, -1);
        this.drawString(mc.fontRendererObj, this.pass.isFocused() || this.pass.getText().length() > 0 ? "" : "Contraseña", this.width - this.getPanelWidth() + 15,  155, -1);

        GL11.glPopMatrix();

    }
}
