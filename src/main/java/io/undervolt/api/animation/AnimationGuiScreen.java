package io.undervolt.api.animation;

import io.undervolt.api.animation.curves.Curves;
import io.undervolt.api.ui.widgets.controllers.AnimationController;
import io.undervolt.utils.MathUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class AnimationGuiScreen extends GuiScreen {

    public ArrayList<AnimationVector> points = new ArrayList<AnimationVector>();
    public ArrayList<AnimationVector> renderPoints = new ArrayList<AnimationVector>();

    private static double t = 0.01;


    @Override
    public void initGui() {
        super.initGui();
    }


    AnimationController controller = new AnimationController(Animations.easeInOut());

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.drawDefaultBackground();
        GlStateManager.enableAlpha();

        for (AnimationVector vector :
                points) {
            boolean isHover = mouseX >= vector.getX() - 5 && mouseY >= vector.getY() - 5 && mouseX < vector.getX() + 5 && mouseY < vector.getY() + 5;
            drawMainPoints(vector.getX(), vector.getY(), isHover);
        }

        for (AnimationVector vector :
                renderPoints) {
            drawRect(vector.getX(), vector.getY());
        }

        if(points.size() == 4){
            double s1 = controller.forward();
            AnimationVector solution = new AnimationVector(MathUtil.Lerp(0, width, (float) s1), 50);
            drawMainPoints(solution.getX(), solution.getY(), true);
        }




        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawRect(double x, double y){
        drawRect((float) x - 1, (float) y - 1, (float) x + 1, (float) y + 1, Color.white.getRGB());
    }

    public void drawMainPoints(double x, double y, boolean isHover){
        if(isHover){
            drawRect((float) (x - 10), (float) (y - 10), (float) x + 10, (float) y + 10, Color.white.getRGB());
        }
        drawRect((float) (x - 5), (float) (y - 5), (float) x + 5, (float) y + 5, isHover ? Color.green.getRGB() : Color.blue.getRGB());
    }


    public void math(){
        if(points.size() != 4) return;
        for (double k = t; k <= 1 + t; k += t) {
            double r = 1 - k;
            AnimationVector vectorA = points.get(0);
            AnimationVector vectorB = points.get(1);
            AnimationVector vectorC = points.get(2);
            AnimationVector vectorD = points.get(3);

            double x =  Math.pow(r, 3) * vectorA.getX() + 3 * k * Math.pow(r, 2) * vectorB.getX()
                    + 3 * Math.pow(k, 2) * (1 - k) * vectorC.getX() + Math.pow(k, 3) * vectorD.getX();
            double y = Math.pow(r, 3) * vectorA.getY() + 3 * k * Math.pow(r, 2) * vectorB.getY()
                    + 3 * Math.pow(k, 2) * (1 - k) * vectorC.getY() + Math.pow(k, 3) * vectorD.getY();

            renderPoints.add(new AnimationVector(x, y));
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if(mouseButton == 0){
            if(points.size() < 4){
                this.points.add(new AnimationVector(mouseX, mouseY));
            }
            if(points.size() == 4){
                this.controller.setAnimations(this.points.get(1), this.points.get(2));
                math();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        if(points.size() == 4){
            renderPoints.clear();
            math();
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0){
            if(points.size() == 4){
                for (AnimationVector vector :
                        points) {
                    boolean isHover = mouseX >= vector.getX() - 5 && mouseY >= vector.getY() - 5 && mouseX < vector.getX() + 5 && mouseY < vector.getY() + 5;
                    if(isHover){
                        if(vector.getX() != mouseX || vector.getY() != mouseY){
                            vector.setX(mouseX).setY(mouseY);
                        }
                    }
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
}
