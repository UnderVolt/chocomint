package io.undervolt.mod;

import com.google.common.collect.Lists;
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

    public ModLoader(final Chocomint chocomint) {
        this.configurableManager = chocomint.getConfigurableManager();
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
                this.configurableManager.register((Configurable) c.newInstance());
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
    }

    public Mod getMod(Mod mod) {
        return (Mod) this.configurableManager.getUpdatedFields(mod);
    }
}
