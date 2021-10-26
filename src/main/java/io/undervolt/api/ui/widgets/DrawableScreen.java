package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class DrawableScreen extends Drawable {

    protected Screen screen;
    protected int xPos, yPos;

    public DrawableScreen(float width, float height, Screen screen) {
        this.width = width;
        this.height = height;
        this.screen = screen;
        this.screen.width = (int) width;
        this.screen.height = (int) height;
    }

    @Override public void init() {
        this.screen.initGui();
    }

    @Override
    public void draw(io.undervolt.api.ui.Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
        screen.width = (int) width;
        screen.height = (int) height;
        this.xPos = x;
        this.yPos = y;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor3f(1, 1, 1);
        GL11.glTranslatef(x, y, 0);
        screen.drawScreen(mouseX - x, mouseY - y, deltaTime);
        GL11.glPopMatrix();
    }

    @Override
    public void onPress(int x, int y, int button) {
        System.out.println(x - xPos);
        System.out.println(y - yPos);
        try {
            this.screen.mouseClicked(x - xPos, y - yPos, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
