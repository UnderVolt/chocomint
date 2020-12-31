package io.undervolt.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.undervolt.api.event.handler.Listener;
import net.minecraft.client.gui.Gui;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class Configurable extends Gui implements Listener {
    private final transient String name;
    private final transient Gson gson = new GsonBuilder().setPrettyPrinting().disableInnerClassSerialization().create();

    public Configurable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void saveConfig(ProfileLoader.Profile profile) {
        File b = new File(profile.getFile() + File.separator + this.getName() + ".json");
        try (Writer writer = new FileWriter(b)) {
            gson.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
