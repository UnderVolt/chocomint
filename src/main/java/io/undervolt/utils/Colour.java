
package io.undervolt.utils;

import java.awt.*;
import java.awt.color.ColorSpace;

public class Colour extends Color {
    public Colour(int r, int g, int b) {
        super(r, g, b);
    }

    public Colour(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public Colour(int color) {
        super(color, color, color);
    }

    public Colour(int color, int alpha) {
        super(color, color, color, alpha);
    }

    public Colour(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public Colour(float r, float g, float b) {
        super(r, g, b);
    }

    public Colour(ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
    }

    public static Color Lerp(Color a, Color b, float t) {
        t = MathUtil.Clamp01(t);
        return getColor(a, b, t);
    }

    public Colour alpha(int alpha){
        return new Colour(this.getRed(), this.getGreen(), this.getBlue(), alpha);
    }

    public static Colour getHSBColor(float h, float s, float v){
        Color tmp = Color.getHSBColor(h, s, v);
        return new Colour(tmp.getRed(), tmp.getGreen(), tmp.getBlue());
    }

    public static Colour getHSBAColor(float h, float s, float v, float o){
        Color tmp = Color.getHSBColor(h, s, v);
        return new Colour(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), Math.round(o));
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

    public static Colour Transparent = new Colour(255, 255, 255, 0);
    public static Colour AliceBlue = new Colour(240, 248, 255, 255);
    public static Colour AntiqueWhite = new Colour(250, 235, 215, 255);
    public static Colour Aqua = new Colour(0, 255, 255, 255);
    public static Colour Aquamarine = new Colour(127, 255, 212, 255);
    public static Colour Azure = new Colour(240, 255, 255, 255);
    public static Colour Beige = new Colour(245, 245, 220, 255);
    public static Colour Bisque = new Colour(255, 228, 196, 255);
    public static Colour Black = new Colour(0, 0, 0, 255);
    public static Colour BlanchedAlmond = new Colour(255, 235, 205, 255);
    public static Colour Blue = new Colour(0, 0, 255, 255);
    public static Colour BlueViolet = new Colour(138, 43, 226, 255);
    public static Colour Brown = new Colour(165, 42, 42, 255);
    public static Colour BurlyWood = new Colour(222, 184, 135, 255);
    public static Colour CadetBlue = new Colour(95, 158, 160, 255);
    public static Colour Chartreuse = new Colour(127, 255, 0, 255);
    public static Colour Chocolate = new Colour(210, 105, 30, 255);
    public static Colour Coral = new Colour(255, 127, 80, 255);
    public static Colour CornflowerBlue = new Colour(100, 149, 237, 255);
    public static Colour Cornsilk = new Colour(255, 248, 220, 255);
    public static Colour Crimson = new Colour(220, 20, 60, 255);
    public static Colour Cyan = new Colour(0, 255, 255, 255);
    public static Colour DarkBlue = new Colour(0, 0, 139, 255);
    public static Colour DarkCyan = new Colour(0, 139, 139, 255);
    public static Colour DarkGoldenrod = new Colour(184, 134, 11, 255);
    public static Colour DarkGray = new Colour(169, 169, 169, 255);
    public static Colour DarkGreen = new Colour(0, 100, 0, 255);
    public static Colour DarkKhaki = new Colour(189, 183, 107, 255);
    public static Colour DarkMagenta = new Colour(139, 0, 139, 255);
    public static Colour DarkOliveGreen = new Colour(85, 107, 47, 255);
    public static Colour DarkOrange = new Colour(255, 140, 0, 255);
    public static Colour DarkOrchid = new Colour(153, 50, 204, 255);
    public static Colour DarkRed = new Colour(139, 0, 0, 255);
    public static Colour DarkSalmon = new Colour(233, 150, 122, 255);
    public static Colour DarkSeaGreen = new Colour(143, 188, 139, 255);
    public static Colour DarkSlateBlue = new Colour(72, 61, 139, 255);
    public static Colour DarkSlateGray = new Colour(47, 79, 79, 255);
    public static Colour DarkTurquoise = new Colour(0, 206, 209, 255);
    public static Colour DarkViolet = new Colour(148, 0, 211, 255);
    public static Colour DeepPink = new Colour(255, 20, 147, 255);
    public static Colour DeepSkyBlue = new Colour(0, 191, 255, 255);
    public static Colour DimGray = new Colour(105, 105, 105, 255);
    public static Colour DodgerBlue = new Colour(30, 144, 255, 255);
    public static Colour Firebrick = new Colour(178, 34, 34, 255);
    public static Colour FloralWhite = new Colour(255, 250, 240, 255);
    public static Colour ForestGreen = new Colour(34, 139, 34, 255);
    public static Colour Fuchsia = new Colour(255, 0, 255, 255);
    public static Colour Gainsboro = new Colour(220, 220, 220, 255);
    public static Colour GhostWhite = new Colour(248, 248, 255, 255);
    public static Colour Gold = new Colour(255, 215, 0, 255);
    public static Colour Goldenrod = new Colour(218, 165, 32, 255);
    public static Colour Gray = new Colour(128, 128, 128, 255);
    public static Colour Green = new Colour(0, 128, 0, 255);
    public static Colour GreenYellow = new Colour(173, 255, 47, 255);
    public static Colour Honeydew = new Colour(240, 255, 240, 255);
    public static Colour HotPink = new Colour(255, 105, 180, 255);
    public static Colour IndianRed = new Colour(205, 92, 92, 255);
    public static Colour Indigo = new Colour(75, 0, 130, 255);
    public static Colour Ivory = new Colour(255, 255, 240, 255);
    public static Colour Khaki = new Colour(240, 230, 140, 255);
    public static Colour Lavender = new Colour(230, 230, 250, 255);
    public static Colour LavenderBlush = new Colour(255, 240, 245, 255);
    public static Colour LawnGreen = new Colour(124, 252, 0, 255);
    public static Colour LemonChiffon = new Colour(255, 250, 205, 255);
    public static Colour LightBlue = new Colour(173, 216, 230, 255);
    public static Colour LightCoral = new Colour(240, 128, 128, 255);
    public static Colour LightCyan = new Colour(224, 255, 255, 255);
    public static Colour LightGoldenrodYellow = new Colour(250, 250, 210, 255);
    public static Colour LightGreen = new Colour(144, 238, 144, 255);
    public static Colour LightGray = new Colour(211, 211, 211, 255);
    public static Colour LightPink = new Colour(255, 182, 193, 255);
    public static Colour LightSalmon = new Colour(255, 160, 122, 255);
    public static Colour LightSeaGreen = new Colour(32, 178, 170, 255);
    public static Colour LightSkyBlue = new Colour(135, 206, 250, 255);
    public static Colour LightSlateGray = new Colour(119, 136, 153, 255);
    public static Colour LightSteelBlue = new Colour(176, 196, 222, 255);
    public static Colour LightYellow = new Colour(255, 255, 224, 255);
    public static Colour Lime = new Colour(0, 255, 0, 255);
    public static Colour LimeGreen = new Colour(50, 205, 50, 255);
    public static Colour Linen = new Colour(250, 240, 230, 255);
    public static Colour Magenta = new Colour(255, 0, 255, 255);
    public static Colour Maroon = new Colour(128, 0, 0, 255);
    public static Colour MediumAquamarine = new Colour(102, 205, 170, 255);
    public static Colour MediumBlue = new Colour(0, 0, 205, 255);
    public static Colour MediumOrchid = new Colour(186, 85, 211, 255);
    public static Colour MediumPurple = new Colour(147, 112, 219, 255);
    public static Colour MediumSeaGreen = new Colour(60, 179, 113, 255);
    public static Colour MediumSlateBlue = new Colour(123, 104, 238, 255);
    public static Colour MediumSpringGreen = new Colour(0, 250, 154, 255);
    public static Colour MediumTurquoise = new Colour(72, 209, 204, 255);
    public static Colour MediumVioletRed = new Colour(199, 21, 133, 255);
    public static Colour MidnightBlue = new Colour(25, 25, 112, 255);
    public static Colour MintCream = new Colour(245, 255, 250, 255);
    public static Colour MistyRose = new Colour(255, 228, 225, 255);
    public static Colour Moccasin = new Colour(255, 228, 181, 255);
    public static Colour NavajoWhite = new Colour(255, 222, 173, 255);
    public static Colour Navy = new Colour(0, 0, 128, 255);
    public static Colour OldLace = new Colour(253, 245, 230, 255);
    public static Colour Olive = new Colour(128, 128, 0, 255);
    public static Colour OliveDrab = new Colour(107, 142, 35, 255);
    public static Colour Orange = new Colour(255, 165, 0, 255);
    public static Colour OrangeRed = new Colour(255, 69, 0, 255);
    public static Colour Orchid = new Colour(218, 112, 214, 255);
    public static Colour PaleGoldenrod = new Colour(238, 232, 170, 255);
    public static Colour PaleGreen = new Colour(152, 251, 152, 255);
    public static Colour PaleTurquoise = new Colour(175, 238, 238, 255);
    public static Colour PaleVioletRed = new Colour(219, 112, 147, 255);
    public static Colour PapayaWhip = new Colour(255, 239, 213, 255);
    public static Colour PeachPuff = new Colour(255, 218, 185, 255);
    public static Colour Peru = new Colour(205, 133, 63, 255);
    public static Colour Pink = new Colour(255, 192, 203, 255);
    public static Colour Plum = new Colour(221, 160, 221, 255);
    public static Colour PowderBlue = new Colour(176, 224, 230, 255);
    public static Colour Purple = new Colour(128, 0, 128, 255);
    public static Colour Red = new Colour(255, 0, 0, 255);
    public static Colour RosyBrown = new Colour(188, 143, 143, 255);
    public static Colour RoyalBlue = new Colour(65, 105, 225, 255);
    public static Colour SaddleBrown = new Colour(139, 69, 19, 255);
    public static Colour Salmon = new Colour(250, 128, 114, 255);
    public static Colour SandyBrown = new Colour(244, 164, 96, 255);
    public static Colour SeaGreen = new Colour(46, 139, 87, 255);
    public static Colour SeaShell = new Colour(255, 245, 238, 255);
    public static Colour Sienna = new Colour(160, 82, 45, 255);
    public static Colour Silver = new Colour(192, 192, 192, 255);
    public static Colour SkyBlue = new Colour(135, 206, 235, 255);
    public static Colour SlateBlue = new Colour(106, 90, 205, 255);
    public static Colour SlateGray = new Colour(112, 128, 144, 255);
    public static Colour Snow = new Colour(255, 250, 250, 255);
    public static Colour SpringGreen = new Colour(0, 255, 127, 255);
    public static Colour SteelBlue = new Colour(70, 130, 180, 255);
    public static Colour Tan = new Colour(210, 180, 140, 255);
    public static Colour Teal = new Colour(0, 128, 128, 255);
    public static Colour Thistle = new Colour(216, 191, 216, 255);
    public static Colour Tomato = new Colour(255, 99, 71, 255);
    public static Colour Turquoise = new Colour(64, 224, 208, 255);
    public static Colour Violet = new Colour(238, 130, 238, 255);
    public static Colour Wheat = new Colour(245, 222, 179, 255);
    public static Colour White = new Colour(255, 255, 255, 255);
    public static Colour WhiteSmoke = new Colour(245, 245, 245, 255);
    public static Colour Yellow = new Colour(255, 255, 0, 255);
    public static Colour YellowGreen = new Colour(154, 205, 50, 255);
}