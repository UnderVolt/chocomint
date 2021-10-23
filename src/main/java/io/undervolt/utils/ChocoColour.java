
package io.undervolt.utils;

import java.awt.*;
import java.awt.color.ColorSpace;

public class ChocoColour extends Color {
    public ChocoColour(int r, int g, int b) {
        super(r, g, b);
    }

    public ChocoColour(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public ChocoColour(int color) {
        super(color, color, color);
    }

    public ChocoColour(int color, int alpha) {
        super(color, color, color, alpha);
    }

    public ChocoColour(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public ChocoColour(float r, float g, float b) {
        super(r, g, b);
    }

    public ChocoColour(ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
    }

    public static Color Lerp(Color a, Color b, float t) {
        t = MathUtil.Clamp01(t);
        return getColor(a, b, t);
    }

    public ChocoColour setAlpha(int alpha){
        return new ChocoColour(this.getRed(), this.getGreen(), this.getBlue(), alpha);
    }

    public static ChocoColour getHSBColor(float h, float s, float v){
        Color tmp = Color.getHSBColor(h, s, v);
        return new ChocoColour(tmp.getRed(), tmp.getGreen(), tmp.getBlue());
    }

    public static ChocoColour getHSBAColor(float h, float s, float v, float o){
        Color tmp = Color.getHSBColor(h, s, v);
        return new ChocoColour(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), Math.round(o));
    }

    // Interpolates between colors /a/ and /b/ by /t/ without clamping the interpolant
    public static Color LerpUnclamped(Color a, Color b, float t) {
        return getColor(a, b, t);
    }

    private static Color getColor(Color a, Color b, float t) {
        return new Color(
                (int) Math.min(255, a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) Math.min(255, a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) Math.min(255, a.getBlue() + (b.getBlue() - a.getBlue()) * t),
                (int) Math.min(255, a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t)
        );
    }
}