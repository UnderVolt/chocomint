package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.font.FontBridge;
import io.undervolt.api.ui.font.UnicodeFont;
import io.undervolt.bridge.GameBridge;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Text extends Drawable {

   protected String text;
   protected TextStyle style;

   private FontRenderer fontRendererObj;
   private UnicodeFont font;

   public Text(String text) {
      this.text = text;
      this.style = new TextStyle();
   }

   public Text(String text, Color colour) {
      this.text = text;
      this.style = new TextStyle().setTextColor(colour);
   }

   public Text(String text, int fontSize, Color colour) {
      this.text = text;
      this.style = new TextStyle().setTextColor(colour).setFontSize(fontSize);
   }

   public Text(String text, int fontSize) {
      this.text = text;
      this.style = new TextStyle().setFontSize(fontSize);
   }

   public Text(String text, TextStyle textStyle) {
      this.text = text;
      this.style = textStyle;
   }

   @Override
   public void init() {
      this.fontRendererObj = GameBridge.getFontRenderer();
      switch (this.style.fontType) {
         case ROBOTO:
            font = FontBridge.Roboto;
            break;
         case RALEWAY:
            font = FontBridge.Raleway;
            break;
         default:
            font = FontBridge.Poppins;
      }
   }

   @Override
   public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
      this.width = font.getStringWidth(this.text) * this.style.scale;
      this.height = font.FONT_HEIGHT * this.style.scale;
      switch (this.style.align) {
         case LEFT:
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0);
            GL11.glScalef(this.style.scale, this.style.scale, 0);
            if(this.style.fontType == FontType.POPPINS)
               font.drawString(this.text, 1, -3, this.style.textColor.getRGB());
            else if(this.style.fontType == FontType.RALEWAY)
               font.drawString(this.text, 0, -2, this.style.textColor.getRGB());
            else
               font.drawString(this.text, 0, 0, this.style.textColor.getRGB());
            //this.fontRendererObj.drawString(this.text, 0, 0, this.style.textColor.getRGB(), this.style.dropShadow);
            GL11.glPopMatrix();
            break;
         case RIGHT:
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0);
            GL11.glScalef(this.style.scale, this.style.scale, 0);
            font.setBidiFlag(true);
            if(this.style.fontType == FontType.POPPINS)
               font.drawString(this.text, 1, -3, this.style.textColor.getRGB());
            else if(this.style.fontType == FontType.RALEWAY)
               font.drawString(this.text, 0, 0, this.style.textColor.getRGB());
            else
               font.drawString(this.text, 0, 0, this.style.textColor.getRGB());
            //this.fontRendererObj.setBidiFlag(true);
            //this.fontRendererObj.drawString(this.text, 0, 0, this.style.textColor.getRGB(), this.style.dropShadow);
            GL11.glPopMatrix();
            break;
         case CENTER:
            GL11.glPushMatrix();
            GL11.glTranslatef(x - (this.width * 0.5f), y, 0);
            GL11.glScalef(this.style.scale, this.style.scale, 0);
            if(this.style.fontType == FontType.POPPINS)
               font.drawString(this.text, 1, -3, this.style.textColor.getRGB());
            else if(this.style.fontType == FontType.RALEWAY)
               font.drawString(this.text, 0, 0, this.style.textColor.getRGB());
            else
               font.drawString(this.text, 0, 0, this.style.textColor.getRGB());
            //this.fontRendererObj.drawString(this.text, 0, 0, this.style.textColor.getRGB(), this.style.dropShadow);
            GL11.glPopMatrix();
            break;
      }

   }

   public Text style(TextStyle style) {
      this.style = style;
      return this;
   }

   public void setText(String text) {
      this.text = text;
   }

   public TextStyle getStyle() {
      return style;
   }

   public static class TextStyle {
      protected boolean dropShadow = true;
      protected Color textColor = Color.white;
      protected TextAlign align = TextAlign.LEFT;
      private float scale = 1.0F;
      protected FontType fontType = FontType.RALEWAY;

      public TextStyle setDropShadow(boolean dropShadow) {
         this.dropShadow = dropShadow;
         return this;
      }

      public TextStyle setFontType(FontType fontType) {
         this.fontType = fontType;
         return this;
      }

      public TextStyle setTextColor(Color textColor) {
         this.textColor = textColor;
         return this;
      }

      public TextStyle setAlign(TextAlign align) {
         this.align = align;
         return this;
      }

      public TextStyle setFontSize(float fontSize) {
         this.scale = fontSize / 9;
         return this;
      }
   }

   public enum FontType {
      MINECRAFT, ROBOTO, POPPINS, RALEWAY
   }

   public enum TextAlign {
      LEFT, CENTER, RIGHT
   }

}
