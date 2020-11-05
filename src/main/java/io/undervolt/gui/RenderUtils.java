package io.undervolt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class RenderUtils extends Gui {

    private final Minecraft mc;

    public RenderUtils(final Minecraft mc) {
        this.mc = mc;
    }

    public void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glBegin(6);

        for (int i = 0; i < 50; i++) {
            float x = radius * MathHelper.sin((float) (i * 0.12566370614359174D));
            float y = radius * MathHelper.cos((float) (i * 0.12566370614359174D));
            GlStateManager.color(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public void drawFilledCircle(int xx, int yy, float radius, int col, ResourceLocation res) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        //GL11.glBegin(6);

        for (int i = 0; i < 50; i++) {
            float x = radius * MathHelper.sin((float) (i * 0.12566370614359174D));
            float y = radius * MathHelper.cos((float) (i * 0.12566370614359174D));
            GlStateManager.color(f2, f3, f4, f);
            this.mc.getTextureManager().bindTexture(res);
            GL11.glVertex2f(xx + x, yy + y);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 150, 150, 150, 150);
        }

        GL11.glDisable(3042);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glDisable(2848);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }




    public static void drawCircle(int xx, int yy, int radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        for (int i = 0; i < 70; i++) {
            float x = radius * MathHelper.cos((float) (i * 0.08975979010256552D));
            float y = radius * MathHelper.sin((float) (i * 0.08975979010256552D));
            GlStateManager.color(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public void drawLine(float x, float y, float x1, float y1, float width, int colour) {
        float red = (float) (colour >> 16 & 0xFF) / 255F;
        float green = (float) (colour >> 8 & 0xFF) / 255F;
        float blue = (float) (colour & 0xFF) / 255F;
        float alpha = (float) (colour >> 24 & 0xFF) / 255F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glPushMatrix();
        GlStateManager.color(red, green, blue, alpha);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public void drawRoundedRect(float x, float y, float w, float h, float radius, int color){
        GL11.glPushMatrix();
        GL11.glTranslatef(x,y,0);
        this.drawRoundedRect(0,0,w,h,radius,radius,radius,radius,color);
        GL11.glPopMatrix();
    }

    public void drawRoundedRect(float x0, float y0, float x1, float y1, float radiusTopLeft, float radiusTopRight, float radiusBottomLeft, float radiusBottomRight, int color) {
        int numberOfArcs = 18;

        float angleIncrement = 90.0F / numberOfArcs;

        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        GL11.glDisable(2884);
        GL11.glDisable(3553);

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(r, g, b, a);

        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radiusTopLeft, y0);
        GL11.glVertex2f(x0 + radiusBottomLeft, y1);
        GL11.glVertex2f(x1 - radiusTopRight, y0);
        GL11.glVertex2f(x1 - radiusBottomRight, y1);
        GL11.glEnd();

        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radiusTopLeft);
        GL11.glVertex2f(x0 + radiusTopLeft, y0 + radiusTopLeft);
        GL11.glVertex2f(x0, y1 - radiusBottomLeft);
        GL11.glVertex2f(x0 + radiusBottomLeft, y1 - radiusBottomLeft);
        GL11.glEnd();

        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radiusTopRight);
        GL11.glVertex2f(x1 - radiusTopRight, y0 + radiusTopRight);
        GL11.glVertex2f(x1, y1 - radiusBottomRight);
        GL11.glVertex2f(x1 - radiusBottomRight, y1 - radiusBottomRight);
        GL11.glEnd();

        GL11.glBegin(6);
        float centerX = x1 - radiusTopRight;
        float centerY = y0 + radiusTopRight;
        GL11.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numberOfArcs; i++)
        {
            float angle = i * angleIncrement;
            GL11.glVertex2f((float)(centerX + radiusTopRight * Math.cos(Math.toRadians(angle))), (float)(centerY - radiusTopRight * Math.sin(Math.toRadians(angle))));
        }
        GL11.glEnd();

        GL11.glBegin(6);
        centerX = x0 + radiusTopLeft;
        centerY = y0 + radiusTopLeft;
        GL11.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numberOfArcs; i++)
        {
            float angle = i * angleIncrement;
            GL11.glVertex2f((float)(centerX - radiusTopLeft * Math.cos(Math.toRadians(angle))), (float)(centerY - radiusTopLeft * Math.sin(Math.toRadians(angle))));
        }
        GL11.glEnd();

        GL11.glBegin(6);
        centerX = x0 + radiusBottomLeft;
        centerY = y1 - radiusBottomLeft;
        GL11.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numberOfArcs; i++)
        {
            float angle = i * angleIncrement;
            GL11.glVertex2f((float)(centerX - radiusBottomLeft * Math.cos(Math.toRadians(angle))), (float)(centerY + radiusBottomLeft * Math.sin(Math.toRadians(angle))));
        }
        GL11.glEnd();

        GL11.glBegin(6);
        centerX = x1 - radiusBottomRight;
        centerY = y1 - radiusBottomRight;
        GL11.glVertex2f(centerX, centerY);
        for (int i = 0; i <= numberOfArcs; i++)
        {
            float angle = i * angleIncrement;
            GL11.glVertex2f((float)(centerX + radiusBottomRight * Math.cos(Math.toRadians(angle))), (float)(centerY + radiusBottomRight * Math.sin(Math.toRadians(angle))));
        }
        GL11.glEnd();

        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }
}
