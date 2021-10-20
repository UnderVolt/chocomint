package io.undervolt.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;


public class OverlayUtils {

    private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);


    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
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

    public static void drawFilledCircle(int xx, int yy, float radius, int col, ResourceLocation res) {
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
            Minecraft.getMinecraft().getTextureManager().bindTexture(res);
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

    public static void drawRect(float g, float h, float i, float j, Color c) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, c.getRGB());
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        Gui.drawRect((int) g, (int) h, (int) i, (int) j, col1);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f2 = (col1 >> 16 & 0xFF) / 255.0F;
        float f3 = (col1 >> 8 & 0xFF) / 255.0F;
        float f4 = (col1 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GlStateManager.color(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, Color c, Color c2) {
        drawRect(x, y, x2, y2, c2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        glColor(c);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(Color color) {
        GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
                color.getAlpha() / 255.0F);
    }

    public static void drawLine(float x, float y, float x1, float y1, float width, int colour) {
        float red = (float) (colour >> 16 & 0xFF) / 255F;
        float green = (float) (colour >> 8 & 0xFF) / 255F;
        float blue = (float) (colour & 0xFF) / 255F;
        float alpha = (float) (colour >> 24 & 0xFF) / 255F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
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

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawSmoothRect(int left, int top, int right, int bottom, int color) {
        drawSmoothRect(left, top, right, bottom, 3, color);

    }

    public static void drawSmoothRect(int left, int top, int right, int bottom, int circleSize, int color) {
        left += circleSize;
        right -= circleSize;
        Gui.drawRect(left, top, right, bottom, color);
        int i = circleSize - 1;
        Gui.drawRect(left - circleSize, top + i, left, bottom - i, color);
        Gui.drawRect(right, top + i, right + circleSize, bottom - i, color);

        OverlayUtils.drawFilledCircle(left, top + circleSize, circleSize, color);
        OverlayUtils.drawFilledCircle(left, bottom - circleSize, circleSize, color);

        OverlayUtils.drawFilledCircle(right, top + circleSize, circleSize, color);
        OverlayUtils.drawFilledCircle(right, bottom - circleSize, circleSize, color);

    }

    public static void drawArc(float cx, float cy, float r, float startAngle, float angle, int segments, int color) {
        float red = (float) (color >> 16 & 0xFF) / 255F;
        float green = (float) (color >> 8 & 0xFF) / 255F;
        float blue = (float) (color & 0xFF) / 255F;
        float alpha = (float) (color >> 24 & 0xFF) / 255F;

        float theta = angle / (float) (segments - 1);

        double tf = Math.tan(theta);
        float rf = MathHelper.cos(theta);


        float x = r * MathHelper.cos(startAngle);
        float y = r * MathHelper.sin(startAngle);

        GlStateManager.pushMatrix();
        GlStateManager.color(red, green, blue, alpha);
        GL11.glBegin(GL_LINE_STRIP);
        for (int ii = 0; ii < segments; ii++) {
            GL11.glVertex2f(x + cx, y + cy);

            float tx = -y;
            float ty = x;

            x += tx * tf;
            y += ty * tf;

            x *= rf;
            y *= rf;
        }
        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawSolidBox() {

        drawSolidBox(DEFAULT_AABB);
    }

    public static void drawSolidBox(AxisAlignedBB bb) {

        glBegin(GL_QUADS);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }

    public static void drawOutlinedBox() {

        drawOutlinedBox(DEFAULT_AABB);
    }

    public static void drawOutlinedBox(AxisAlignedBB bb) {

        glBegin(GL_LINES);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }

    public void onPanelButtonClick(Button button)
    {
    }

    public static void drawRoundedRect(int x0, int y0, int x1, int y1, float radius, int color, float zLevel)
    {
        int i = 18;
        float f = 90.0F / (float)i;
        float f1 = (float)(color >> 24 & 255) / 255.0F;
        float f2 = (float)(color >> 16 & 255) / 255.0F;
        float f3 = (float)(color >> 8 & 255) / 255.0F;
        float f4 = (float)(color & 255) / 255.0F;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex3f((float)x0 + radius, (float)y0, zLevel);
        GL11.glVertex3f((float)x0 + radius, (float)y1, zLevel);
        GL11.glVertex3f((float)x1 - radius, (float)y0, zLevel);
        GL11.glVertex3f((float)x1 - radius, (float)y1, zLevel);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex3f((float)x0, (float)y0 + radius, zLevel);
        GL11.glVertex3f((float)x0 + radius, (float)y0 + radius, zLevel);
        GL11.glVertex3f((float)x0, (float)y1 - radius, zLevel);
        GL11.glVertex3f((float)x0 + radius, (float)y1 - radius, zLevel);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex3f((float)x1, (float)y0 + radius, zLevel);
        GL11.glVertex3f((float)x1 - radius, (float)y0 + radius, zLevel);
        GL11.glVertex3f((float)x1, (float)y1 - radius, zLevel);
        GL11.glVertex3f((float)x1 - radius, (float)y1 - radius, zLevel);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        float f5 = (float)x1 - radius;
        float f6 = (float)y0 + radius;
        GL11.glVertex3f(f5, f6, zLevel);

        for (int j = 0; j <= i; ++j)
        {
            float f7 = (float)j * f;
            GL11.glVertex3f((float)((double)f5 + (double)radius * Math.cos(Math.toRadians((double)f7))), (float)((double)f6 - (double)radius * Math.sin(Math.toRadians((double)f7))), zLevel);
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x0 + radius;
        f6 = (float)y0 + radius;
        GL11.glVertex3f(f5, f6, zLevel);

        for (int k = 0; k <= i; ++k)
        {
            float f8 = (float)k * f;
            GL11.glVertex3f((float)((double)f5 - (double)radius * Math.cos(Math.toRadians((double)f8))), (float)((double)f6 - (double)radius * Math.sin(Math.toRadians((double)f8))), zLevel);
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x0 + radius;
        f6 = (float)y1 - radius;
        GL11.glVertex3f(f5, f6, zLevel);

        for (int l = 0; l <= i; ++l)
        {
            float f9 = (float)l * f;
            GL11.glVertex3f((float)((double)f5 - (double)radius * Math.cos(Math.toRadians((double)f9))), (float)((double)f6 + (double)radius * Math.sin(Math.toRadians((double)f9))), zLevel);
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x1 - radius;
        f6 = (float)y1 - radius;
        GL11.glVertex3f(f5, f6, zLevel);

        for (int i1 = 0; i1 <= i; ++i1)
        {
            float f10 = (float)i1 * f;
            GL11.glVertex3f((float)((double)f5 + (double)radius * Math.cos(Math.toRadians((double)f10))), (float)((double)f6 + (double)radius * Math.sin(Math.toRadians((double)f10))), zLevel);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public static void drawRoundedRect(int x0, int y0, int x1, int y1, float radius, int color)
    {
        int i = 18;
        float f = 90.0F / (float)i;
        float f1 = (float)(color >> 24 & 255) / 255.0F;
        float f2 = (float)(color >> 16 & 255) / 255.0F;
        float f3 = (float)(color >> 8 & 255) / 255.0F;
        float f4 = (float)(color & 255) / 255.0F;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f2, f3, f4, f1);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f((float)x0 + radius, (float)y0);
        GL11.glVertex2f((float)x0 + radius, (float)y1);
        GL11.glVertex2f((float)x1 - radius, (float)y0);
        GL11.glVertex2f((float)x1 - radius, (float)y1);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f((float)x0, (float)y0 + radius);
        GL11.glVertex2f((float)x0 + radius, (float)y0 + radius);
        GL11.glVertex2f((float)x0, (float)y1 - radius);
        GL11.glVertex2f((float)x0 + radius, (float)y1 - radius);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f((float)x1, (float)y0 + radius);
        GL11.glVertex2f((float)x1 - radius, (float)y0 + radius);
        GL11.glVertex2f((float)x1, (float)y1 - radius);
        GL11.glVertex2f((float)x1 - radius, (float)y1 - radius);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        float f5 = (float)x1 - radius;
        float f6 = (float)y0 + radius;
        GL11.glVertex2f(f5, f6);

        for (int j = 0; j <= i; ++j)
        {
            float f7 = (float)j * f;
            GL11.glVertex2f((float)((double)f5 + (double)radius * Math.cos(Math.toRadians((double)f7))), (float)((double)f6 - (double)radius * Math.sin(Math.toRadians((double)f7))));
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x0 + radius;
        f6 = (float)y0 + radius;
        GL11.glVertex2f(f5, f6);

        for (int k = 0; k <= i; ++k)
        {
            float f8 = (float)k * f;
            GL11.glVertex2f((float)((double)f5 - (double)radius * Math.cos(Math.toRadians((double)f8))), (float)((double)f6 - (double)radius * Math.sin(Math.toRadians((double)f8))));
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x0 + radius;
        f6 = (float)y1 - radius;
        GL11.glVertex2f(f5, f6);

        for (int l = 0; l <= i; ++l)
        {
            float f9 = (float)l * f;
            GL11.glVertex2f((float)((double)f5 - (double)radius * Math.cos(Math.toRadians((double)f9))), (float)((double)f6 + (double)radius * Math.sin(Math.toRadians((double)f9))));
        }

        GL11.glEnd();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        f5 = (float)x1 - radius;
        f6 = (float)y1 - radius;
        GL11.glVertex2f(f5, f6);

        for (int i1 = 0; i1 <= i; ++i1)
        {
            float f10 = (float)i1 * f;
            GL11.glVertex2f((float)((double)f5 + (double)radius * Math.cos(Math.toRadians((double)f10))), (float)((double)f6 + (double)radius * Math.sin(Math.toRadians((double)f10))));
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public static void drawOutlinedBox(int x, int y, int width, int height)
    {
        drawOutlinedBox(x, y, width, height, 0.75F);
    }

    public static void drawOutlinedBox(int x, int y, int width, int height, float opacity)
    {
        if (opacity >= 0.0F && opacity <= 1.0F)
        {
            x = x + width;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, opacity);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float)(x - width), (float)y);
            GL11.glVertex2f((float)(x - width), (float)(y + height));
            GL11.glVertex2f((float)x, (float)(y + height));
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(0.65F, 0.65F, 0.65F, 0.75F);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float)(x - width), (float)y);
            GL11.glVertex2f((float)(x - width - 1), (float)y);
            GL11.glVertex2f((float)(x - width - 1), (float)(y + height));
            GL11.glVertex2f((float)(x - width), (float)(y + height));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float)(x + 1), (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glVertex2f((float)x, (float)(y + height));
            GL11.glVertex2f((float)(x + 1), (float)(y + height));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float)(x - width), (float)(y + height));
            GL11.glVertex2f((float)(x - width), (float)(y + height + 1));
            GL11.glVertex2f((float)x, (float)(y + height + 1));
            GL11.glVertex2f((float)x, (float)(y + height));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float)(x - width), (float)(y - 1));
            GL11.glVertex2f((float)(x - width), (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glVertex2f((float)x, (float)(y - 1));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f((float)(x - width - 1), (float)y);
            GL11.glVertex2f((float)(x - width), (float)y);
            GL11.glVertex2f((float)(x - width), (float)(y - 1));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f((float)x, (float)(y - 1));
            GL11.glVertex2f((float)x, (float)y);
            GL11.glVertex2f((float)(x + 1), (float)y);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f((float)(x - width - 1), (float)(y + height));
            GL11.glVertex2f((float)(x - width), (float)(y + height + 1));
            GL11.glVertex2f((float)(x - width), (float)(y + height));
            GL11.glEnd();
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f((float)x, (float)(y + height));
            GL11.glVertex2f((float)x, (float)(y + height + 1));
            GL11.glVertex2f((float)(x + 1), (float)(y + height));
            GL11.glEnd();
        }
        else
        {
            throw new IllegalArgumentException("Opacity must be between 0.0 and 1.0 inclusive: " + opacity);
        }
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), (double)zLevel).tex((double)((float)textureX * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, (double)zLevel).tex((double)((float)(textureX + width) * f), (double)((float)textureY * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, (double)zLevel).tex((double)((float)textureX * f), (double)((float)textureY * f1)).endVertex();
        tessellator.draw();
    }

}