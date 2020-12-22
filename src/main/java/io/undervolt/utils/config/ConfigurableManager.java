package io.undervolt.utils.config;

import com.google.common.collect.Lists;
import io.undervolt.api.event.handler.EventHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undervolt.api.event.EventManager;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().disableInnerClassSerialization().create();
    private final transient Chocomint chocomint;
    private final transient Loader.Profile currentProfile;

    public ConfigurableManager(final Chocomint chocomint) {
        this.chocomint = chocomint;
        this.currentProfile = chocomint.getLoader().selectedProfile;
    }

    public void saveConfig(Loader.Profile profile) {
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

        this.chocomint.getEventManager().registerEvents(c);
    }

    public void reloadConfig(Loader.Profile profile) {
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
}
