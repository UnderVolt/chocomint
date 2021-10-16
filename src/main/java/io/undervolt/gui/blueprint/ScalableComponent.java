package io.undervolt.gui.blueprint;

import com.google.common.collect.Lists;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.clickable.Clickable;
import io.undervolt.gui.clickable.dropdown.Option;
import io.undervolt.gui.clickable.floating.FloatingMenu;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableComponent extends Clickable {

    private final RenderUtils renderUtils;
    private ComponentBlueprint componentBlueprint;
    public float scale = 1.0F, rotation = 0;
    public final int initialWidth, initialHeight;

    private final List<ComponentOption> componentOptionList = Lists.newArrayList();

    private final Option.OptionSet optionSet = Option.OptionSet.newOptionSet();
    private final FloatingMenu floatingMenu;

    public ScalableComponent(int x, int y, int width, int height, ComponentBlueprint componentBlueprint) {
        super(x, y, width, height, a -> {});
        this.renderUtils = GameBridge.getChocomint().getRenderUtils();
        this.componentBlueprint = componentBlueprint;
        this.initialWidth = width;
        this.initialHeight = height;

        this.optionSet.addOption(new Option(this.optionSet, "Esquina superior izquierda", a -> {
            this.anchorX(AnchorType.POS0); this.anchorY(AnchorType.POS0);
        }));

        this.optionSet.addOption(new Option(this.optionSet, "Esquina superior derecha", a -> {
            this.anchorX(AnchorType.POS100); this.anchorY(AnchorType.POS0);
        }));

        this.optionSet.addOption(new Option(this.optionSet, "Esquina inferior izquierda", a -> {
            this.anchorX(AnchorType.POS0); this.anchorY(AnchorType.POS100);
        }));

        this.optionSet.addOption(new Option(this.optionSet, "Esquina inferior derecha", a -> {
            this.anchorX(AnchorType.POS100); this.anchorY(AnchorType.POS100);
        }));

        this.floatingMenu = new FloatingMenu(150, this.optionSet);

        this.componentOptionList.add(new ComponentOption("R", a -> {
            this.rotation += 90;
            this.componentBlueprint.calculateBoxWidthAndHeight(this);
        }));
    }

    public void drawComponent(int x, int y) {}

    @Override
    public void draw(int mouseX, int mouseY) {
        //double center = Math.atan2(height / 2, width / 2);
        //float cx = x * (float) Math.cos(rotation + center);
        //float cy = y * (float) Math.sin(rotation + center);

        GL11.glPushMatrix();
        GL11.glColor3f(1, 1, 1);
        GL11.glTranslatef(x, y, 1);
        GL11.glRotatef(rotation, 0, 0, 1);
        //GL11.glTranslatef(cx, cy, 1);
        GL11.glScalef(scale, scale, 0);
        this.drawComponent(0, 0);
        GL11.glPopMatrix();
    }

    protected void drawToolbox(int mouseX, int mouseY) {
        // Initial box
        this.renderUtils.drawLine(x - 3, y - 3, x + width + 3, y - 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x - 3, y - 3, x - 3, y + height + 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x + width + 3, y - 3, x + width + 3, y + height + 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x - 3, y + height + 3, x + width + 3, y + height + 3, 1, Color.ORANGE.getRGB());
        drawRect(x - 3, y - 3, x + width + 3, y + height + 3, new Color(255, 200, 0, 40).getRGB());
        // Dots
        this.renderUtils.drawFilledCircle(x - 3, y - 3, 3, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + width + 3, y - 3, 3, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x - 3, y + height + 3, 3, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + width + 3, y + height + 3, 3, Color.ORANGE.getRGB());
        this.renderUtils.drawLine(x + ((width) / 2), y - 15, x + ((width) / 2), y - 3, 1, Color.ORANGE.getRGB());
        this.renderUtils.drawFilledCircle(x + ((width) / 2), y - 15, 5, Color.ORANGE.getRGB());
        // Buttons

        AtomicInteger x = new AtomicInteger(-6);
        this.componentOptionList.forEach(componentOption -> {
            componentOption.draw(this.x + x.get(), y + height + 7, mouseX, mouseY);
            x.set(x.get() + 12);
        });

        this.floatingMenu.draw(mouseX, mouseY);
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        this.floatingMenu.click(mouseX, mouseY, mouseButton);
        if(mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height) {
            this.componentBlueprint.setSelectedComponent(this);
            if(mouseButton == 1) {
                this.floatingMenu.setFittingX(mouseX);
                this.floatingMenu.setFittingY(mouseY);
                this.floatingMenu.setActive(true);
            } else {
                this.floatingMenu.setActive(false);
                this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.DRAG);
                this.componentBlueprint.setCurrentComponent(this);
            }
        } else {
            this.floatingMenu.setActive(false);
        }

        if(this.componentBlueprint.getSelectedComponent() != null)
            if(this.componentBlueprint.getSelectedComponent().equals(this)) {

                this.componentOptionList.forEach(componentOption -> componentOption.click(mouseX, mouseY, mouseButton));

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
                if(mouseX > x + ((width) / 2) - 5 && mouseY > y - 20 && mouseX < x + ((width) / 2) + 5 && mouseY < y - 10) {
                    this.componentBlueprint.setCurrentComponent(this);
                    this.componentBlueprint.setCurrentComponentAction(ComponentBlueprint.BlueprintAction.ROTATE);
                }
            }
    }

    public void anchorX(AnchorType anchorType) {
        switch(anchorType) {
            case POS0:
                this.x = 5;
                break;
            case POS100:
                this.x = GameBridge.getScaledResolution().getScaledWidth() - 5 - this.width;
                break;
            case POS50:
                this.x = GameBridge.getScaledResolution().getScaledWidth() / 2 - this.width / 2;
                break;
        }
    }

    public void anchorY(AnchorType anchorType) {
        switch(anchorType) {
            case POS0:
                this.y = 5;
                break;
            case POS100:
                this.y = GameBridge.getScaledResolution().getScaledHeight() - 5 - this.height;
                break;
            case POS50:
                this.x = GameBridge.getScaledResolution().getScaledHeight() / 2 - this.height / 2;
                break;
        }
    }

    public enum AnchorType {
        POS0, POS50, POS100
    }
}
