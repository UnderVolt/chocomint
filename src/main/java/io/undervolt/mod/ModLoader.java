package io.undervolt.mod;

import com.google.common.collect.Lists;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.config.Configurable;
import io.undervolt.utils.config.ConfigurableManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModLoader {
    private final ConfigurableManager configurableManager;
    private final Chocomint chocomint;
    private final List<Configurable> loadedMods = Lists.newArrayList();

    public ModLoader(final Chocomint chocomint) {
        this.configurableManager = chocomint.getConfigurableManager();
        this.chocomint = chocomint;
    }

    public File[] availableMods(final File modPath) {
        return modPath.listFiles((dir, filename) -> filename.endsWith(".choc"));
    }

    public void loadModFromJar(final File file) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {

        System.out.println(file.getPath() + " is being checked if it's a mod.");
        final JarFile jarFile = new JarFile(file);

        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + file.getPath() +"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }

            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className);

            System.out.println("Loaded class " + className + " from " + file.getPath());

            if(c.getSuperclass().equals(Mod.class) || c.getSuperclass().equals(RenderMod.class)) {
                System.out.println(className + " was assignable from mod");
                Configurable mod = (Configurable) c.newInstance();
                this.configurableManager.register(mod);
                this.getLoadedMods().add(mod);
            } else {
                System.out.println(className + " wasn't assignable from mod");
            }
        }
    }

    public void load(final File modPath) {
        modPath.mkdir();

        for(File jarFile : this.availableMods(modPath)) {
            try {
                this.loadModFromJar(jarFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.chocomint.getNotificationManager().addNotification(new Notification(Notification.Priority.NOTICE,
                "ModLoader", this.getLoadedMods().size() + " mods han sido cargados.", (a) -> {}));
    }

    public Mod getMod(Mod mod) {
        return (Mod) this.configurableManager.getUpdatedFields(mod);
    }

    public List<Configurable> getLoadedMods() {
        return loadedMods;
    }
}
