package io.undervolt.utils.config;

import com.google.common.collect.Lists;
import io.undervolt.api.event.events.RenderGameOverlayEvent;
import io.undervolt.api.event.handler.EventHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.instance.Chocomint;
import io.undervolt.mod.Mod;
import io.undervolt.mod.RenderMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ConfigurableManager extends Gui implements Listener {
    public final transient List<Configurable> configurableList = Lists.newArrayList();
    public final transient List<RenderMod> renderModList = Lists.newArrayList();
    public final transient List<Mod> modList = Lists.newArrayList();
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().disableInnerClassSerialization().create();
    private final transient Chocomint chocomint;
    private final transient ProfileLoader.Profile currentProfile;

    public ConfigurableManager(final Chocomint chocomint) {
        this.chocomint = chocomint;
        this.currentProfile = chocomint.getLoader().selectedProfile;
    }

    public void saveConfig(ProfileLoader.Profile profile) {
        this.configurableList.forEach(configurable -> {
            File b = new File(profile.getFile() + File.separator + configurable.getName() + ".json");
            try (Writer writer = new FileWriter(b)) {
                gson.toJson(configurable, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void register(Configurable c) {

        System.out.println("Loading: " + c.getName());
        File b = new File(currentProfile.getFile() + File.separator + c.getName() + ".json");

        if (b.exists()) {
            System.out.println("Loaded config for " + c.getName());
            this.loadConfig(b, c);
        } else {
            System.out.println("Config for " + c.getName() + " didn't exist. Creating file...");
            c.saveConfig(currentProfile);
        }

        if (!configurableList.contains(c))
            configurableList.add(c);

        if(c instanceof Mod)
            if(!modList.contains(c)) {
                modList.add((Mod) c);
                if(c instanceof RenderMod)
                    if(!renderModList.contains(c)) {
                        renderModList.add((RenderMod) c);
                    }
            }

        this.chocomint.getEventManager().registerEvents(c);
    }

    public void reloadConfig(ProfileLoader.Profile profile) {
        this.configurableList.forEach(configurable -> {
            File b = new File(profile.getFile() + File.separator + configurable.getName() + ".json");
            this.loadConfig(b, configurable);
        });
    }

    public Configurable getUpdatedFields(Configurable configurable) {
        if(this.configurableList.contains(configurable)) {
            int a = this.configurableList.indexOf(configurable);
            try {
                return configurableList.get(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void loadConfig(final File b, final Configurable configurable) {
        if (b.exists()) {
            try {
                String json = new String(Files.readAllBytes(b.toPath()), StandardCharsets.UTF_8);
                Configurable old = gson.fromJson(json, configurable.getClass());

                for (Field field : old.getClass().getFields()) {
                    Field f2 = configurable.getClass().getField(field.getName());
                    if (!Modifier.isFinal(f2.getModifiers())) {
                        f2.setAccessible(true);
                        f2.set(configurable, field.get(old));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onRender(RenderGameOverlayEvent e) {
        renderModList.forEach(renderMod -> {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor3f(255,255,255);
            GL11.glPushMatrix();
            GlStateManager.translate(renderMod.x, renderMod.y, 0);
            GL11.glScalef(renderMod.scale, renderMod.scale, renderMod.scale);
            renderMod.render();
            GL11.glPopMatrix();
        });
    }

}
