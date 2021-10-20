package io.undervolt.utils;

import io.undervolt.api.ui.Screen;
import org.lwjgl.opengl.GL11;

public class GFXUtil {

    public static void applyScissor(int x, int y, int x2, int y2, Screen vw) {
        GL11.glScissor((x * vw.getScaleFactor()), (int) ((vw.getHeight() - y2) * vw.getScaleFactor()), ((x2 - x) * vw.getScaleFactor()), ((y2 - y) * vw.getScaleFactor()));
    }

    public static void enableScissor()
    {
        GL11.glEnable(3089);
    }

    public static void disableScissor()
    {
        GL11.glDisable(3089);
    }
}
