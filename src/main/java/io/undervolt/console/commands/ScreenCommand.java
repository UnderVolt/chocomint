package io.undervolt.console.commands;

import io.undervolt.console.Command;
import io.undervolt.gui.chat.Tab;
import io.undervolt.gui.menu.Menu;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ScreenCommand extends Command {
    public ScreenCommand(Chocomint chocomint) {
        super(chocomint, "Screen", "displayscreen", "Displays a screen within the games resources.");
    }

    private Class<?>[] classesToLoad(String[] classNames) throws ClassNotFoundException {
        Class<?>[] object = new Class[classNames.length];
        for(int i = 0; i < classNames.length; i++) {
                object[i] = Class.forName(classNames[i]);
        }
        return object;
    }

    private Object[] transformClassesToObjects(Class<?>[] classes) throws IllegalAccessException, InstantiationException {
        Object[] objects = new Object[classes.length];
        for(int i = 0; i < classes.length; i++) {
            objects[i] = classes[i].newInstance();
        }
        return objects;
    }

    @Override
    public void execute(Tab tab, String[] args) {
        System.out.println("a");

        if(args.length < 1)  {
            this.returnMessage("\247cUsage: /displayscreen [package.screenName]");
        } else {
            try {
                Class<?>[] classes = this.classesToLoad(args);

                Class<?> mainClass = classes[0];
                Class<?>[] dependencies = Arrays.copyOfRange(classes, 1, classes.length);
                Object[] depObjs = transformClassesToObjects(dependencies);

                if (mainClass.getSuperclass().equals(GuiScreen.class) || mainClass.getSuperclass().equals(Menu.class)) {
                    System.out.println(mainClass.getName() + " está siendo lanzada...");
                    Constructor con = mainClass.getConstructor(dependencies);
                    this.getChocomint().getMinecraft().displayGuiScreen((Menu) con.newInstance(depObjs));
                } else
                    this.returnMessage("\247cThe specified class was not a screen.");

            } catch (Exception e) {
                this.returnMessage("\247cHubo un error.");
                e.printStackTrace();
            }
        }
    }
}
