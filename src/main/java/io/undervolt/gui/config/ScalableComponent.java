package io.undervolt.gui.config;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.clickable.Clickable;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ResizeableComponent extends Clickable {

    private final RenderUtils renderUtils;
    private ComponentBlueprint componentBlueprint;
    public float scale = 1.0F, rotation = 0;
    public final int initialWidth, initialHeight;

    public ResizeableComponent(int x, int y, int width, int height, ComponentBlueprint componentBlueprint) {
        super(x, y, width, height, a -> {});
        this.renderUtils = GameBridge.getChocomint().getRenderUtils();
        this.componentBlueprint = componentBlueprint;
        this.initialWidth = width;
        this.initialHeight = height;
    }

    public void drawComponent(int x, int y) {}

    @Override
    public void draw(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glColor3f(1, 1, 1);
        GL11.glTranslatef(x, y, 1);
        GL11.glRotatef(rotation, 0, 0, 1);
        GL11.glScalef(scale, scale, 0);
        this.drawComponent(0, 0);
        GL11.glPopMatrix();
    }

    protected void drawToolbox() {
        // Initial box
        this.renderUtils.drawLine(x - 3, y - 3, x + width + 3, y - 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x - 3, y - 3, x - 3, y + height + 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x + width + 3, y - 3, x + width + 3, y + height + 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x - 3, y + height + 3, x + width + 3, y + height + 3, 1, Color.ORANGE.getRGB());
        drawRect(x - 3, y - 3, x + width + 3, y + height + 3, new Color(255, 200, 0, 40).getRGB());
        // Dots
        this.renderUtils.drawFilledCircle(x - 3, y - 3, 2, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + width + 3, y - 3, 2, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x - 3, y + height + 3, 2, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + width + 3, y + height + 3, 2, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + ((width) / 2), y - 10, 4, Color.ORANGE.getRGB());
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height) {
            this.componentBlueprint.setSelectedComponent(this);
            this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.DRAG);
            this.componentBlueprint.setCurrentComponent(this);
        }

        if(this.componentBlueprint.getSelectedComponent() != null)
            if(this.componentBlueprint.getSelectedComponent().equals(this)) {
                if(mouseX > x - 6 && mouseY > y - 6 && mouseX < x - 1 && mouseY < y - 1) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.RESIZE_00);
                }
                if(mouseX > x + width - 1 && mouseY > y + height - 1 && mouseX < x + width + 4 && mouseY < y + height + 4) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.RESIZE_22);
                }
                if(mouseX > x + width - 1 && mouseY > y - 6 && mouseX < x + width + 4 && mouseY < y - 1) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.RESIZE_22);
                }
                if(mouseX > x - 6 && mouseY > y + height - 1 && mouseX < x - 1 && mouseY < y + height + 4) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.RESIZE_00);
                }
                if(mouseX > x + ((width) / 2) - 4 && mouseY > y - 14 && mouseX < x + ((width) / 2) + 4 && mouseY < y - 6) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.ROTATE);
                }
            }
    }
}
