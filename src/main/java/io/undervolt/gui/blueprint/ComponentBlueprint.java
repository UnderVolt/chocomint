package io.undervolt.gui.blueprint;

import com.google.common.collect.Lists;
import io.undervolt.utils.AnimationUI;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ComponentBlueprint extends AnimationUI {

    private ScalableComponent selectedComponent, currentComponent = null;
    private BlueprintAction currentComponentAction;
    protected int lastX, lastY;
    private final List<ScalableComponent> resizeableComponents = Lists.newArrayList();

    @Override
    public void initGui() {
        this.resizeableComponents.add(new ExampleIngameTextComponent(100, 100, "Sprinting [Toggled]", this));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawRect(0, 0, width, height, new Color(0, 0, 0, 100).getRGB());

        resizeableComponents.forEach(resizeableComponent -> resizeableComponent.draw(mouseX, mouseY));

        if(currentComponent != null) {

            double rotationRad = Math.abs(Math.toRadians(this.currentComponent.rotation));
            double expectedWidth = Math.abs(this.currentComponent.initialWidth * Math.cos(rotationRad)) + Math.abs(this.currentComponent.initialHeight * Math.sin(rotationRad));

            switch(this.currentComponentAction) {
                case DRAG:
                    this.currentComponent.x = this.currentComponent.x + mouseX - this.lastX;
                    this.currentComponent.y = this.currentComponent.y + mouseY - this.lastY;
                    break;
                case RESIZE_00:
                    float calc1 = (float)(this.currentComponent.width - mouseX + this.lastX) / (float) expectedWidth;
                    if(calc1 > 0.2) {
                        this.currentComponent.x = mouseX;
                        this.currentComponent.scale = calc1;
                        this.calculateBoxWidthAndHeight();
                    }
                    break;
                case RESIZE_22:
                    float calc2 = (float) (mouseX - 3 - this.currentComponent.x) / (float) expectedWidth;
                    if(calc2 > 0.2) {
                        this.currentComponent.scale = calc2;
                        this.calculateBoxWidthAndHeight();
                    }
                    break;
                case ROTATE:
                    this.currentComponent.y = mouseY + 15;
                    this.currentComponent.rotation += (mouseY - this.lastY) * 2;
                    this.calculateBoxWidthAndHeight();
                    break;
            }
        }

        if(selectedComponent != null) selectedComponent.drawToolbox();

        this.lastX = mouseX;
        this.lastY = mouseY;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void calculateBoxWidthAndHeight() {
        double rotationRad = Math.abs(Math.toRadians(this.currentComponent.rotation));
        double scaledWidth = this.currentComponent.initialWidth * this.currentComponent.scale;
        double scaledHeight = this.currentComponent.initialHeight * this.currentComponent.scale;
        this.currentComponent.width = (int)(Math.abs(scaledWidth * Math.cos(rotationRad)) + Math.abs(scaledHeight * Math.sin(rotationRad)));
        this.currentComponent.height = (int)(Math.abs(scaledWidth * Math.sin(rotationRad)) + Math.abs(scaledHeight * Math.cos(rotationRad)));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        resizeableComponents.forEach(resizeableComponent -> resizeableComponent.click(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.currentComponent = null;
        this.currentComponentAction = null;
    }

    public void setSelectedComponent(ScalableComponent selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    public ScalableComponent getSelectedComponent() {
        return selectedComponent;
    }

    public void setCurrentComponentAction(BlueprintAction currentComponentAction) {
        this.currentComponentAction = currentComponentAction;
    }

    public void setCurrentComponent(ScalableComponent currentComponent) {
        this.currentComponent = currentComponent;
    }

    enum BlueprintAction {
        DRAG, RESIZE_00, RESIZE_22, ROTATE
    }

    class ExampleIngameTextComponent extends ScalableComponent {

        private String textToDisplay;

        public ExampleIngameTextComponent(int x, int y, String textToDisplay, ComponentBlueprint componentBlueprint) {
            super(x, y, mc.fontRendererObj.getStringWidth(textToDisplay), 9, componentBlueprint);
            this.textToDisplay = textToDisplay;
        }

        @Override
        public void drawComponent(int x, int y) {
            drawString(mc.fontRendererObj, this.textToDisplay, x, y, Color.WHITE.getRGB());
        }
    }
}
