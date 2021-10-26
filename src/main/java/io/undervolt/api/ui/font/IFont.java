package io.undervolt.api.ui.font;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class IFont {
   private int IMAGE_WIDTH = 1024;
   private int IMAGE_HEIGHT = 1024;
   private int texID;
   private final IntObject[] chars = new IntObject['ࠀ'];
   private final Font font;
   private int fontHeight = -1;
   private int charOffset;

   IFont(Font font, boolean antiAlias, int charOffset) {
      this.font = font;
      this.charOffset = charOffset;
      setupTexture(antiAlias);
   }

   private void setupTexture(boolean antiAlias) {
      if (this.font.getSize() <= 15) {
         this.IMAGE_WIDTH = 256;
         this.IMAGE_HEIGHT = 256;
      }
      if (this.font.getSize() <= 43) {
         this.IMAGE_WIDTH = 512;
         this.IMAGE_HEIGHT = 512;
      } else if (this.font.getSize() <= 91) {
         this.IMAGE_WIDTH = 1024;
         this.IMAGE_HEIGHT = 1024;
      } else {
         this.IMAGE_WIDTH = 2048;
         this.IMAGE_HEIGHT = 2048;
      }
      BufferedImage img = new BufferedImage(this.IMAGE_WIDTH, this.IMAGE_HEIGHT, 2);

      Graphics2D g = (Graphics2D) img.getGraphics();
      g.setFont(this.font);

      g.setColor(new Color(255, 255, 255, 0));
      g.fillRect(0, 0, this.IMAGE_WIDTH, this.IMAGE_HEIGHT);
      g.setColor(Color.white);

      int rowHeight = 0;
      int positionX = 0;
      int positionY = 0;
      for (int i = 0; i < 2048; i++) {
         char ch = (char) i;
         BufferedImage fontImage = getFontImage(ch, antiAlias);

         IntObject newIntObject = new IntObject();

         newIntObject.width = fontImage.getWidth();
         newIntObject.height = fontImage.getHeight();
         if (positionX + newIntObject.width >= this.IMAGE_WIDTH) {
            positionX = 0;
            positionY += rowHeight;
            rowHeight = 0;
         }
         newIntObject.storedX = positionX;
         newIntObject.storedY = positionY;
         if (newIntObject.height > this.fontHeight) {
            this.fontHeight = newIntObject.height;
         }
         if (newIntObject.height > rowHeight) {
            rowHeight = newIntObject.height;
         }
         this.chars[i] = newIntObject;
         g.drawImage(fontImage, positionX, positionY, null);

         positionX += newIntObject.width;
      }
      try {
         this.texID = TextureUtil.uploadTextureImageAllocate(
              TextureUtil.glGenTextures(), img, true, true);
      } catch (NullPointerException e) {
         e.printStackTrace();
      }
   }

   private BufferedImage getFontImage(char ch, boolean antiAlias) {
      BufferedImage temporaryFontImage = new BufferedImage(1, 1, 2);

      Graphics2D g = (Graphics2D) temporaryFontImage.getGraphics();
      if (antiAlias) {
         g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      } else {
         g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      }
      g.setFont(this.font);
      FontMetrics fontMetrics = g.getFontMetrics();
      int charwidth = fontMetrics.charWidth(ch) + 8;
      if (charwidth <= 0) {
         charwidth = 7;
      }
      int charheight = fontMetrics.getHeight() + 3;
      if (charheight <= 0) {
         charheight = this.font.getSize();
      }
      BufferedImage fontImage = new BufferedImage(charwidth, charheight, 2);

      Graphics2D gt = (Graphics2D) fontImage.getGraphics();
      if (antiAlias) {
         gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      } else {
         gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      }
      gt.setFont(this.font);
      gt.setColor(Color.WHITE);
      int charx = 3;
      int chary = 1;
      gt.drawString(String.valueOf(ch), 3, 1 + fontMetrics
           .getAscent());

      return fontImage;
   }

   private void drawChar(char c, float x, float y)
        throws ArrayIndexOutOfBoundsException {
      try {
         drawQuad(x, y, this.chars[c].width, this.chars[c].height, this.chars[c].storedX, this.chars[c].storedY, this.chars[c].width, this.chars[c].height);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
      float renderSRCX = srcX / this.IMAGE_WIDTH;
      float renderSRCY = srcY / this.IMAGE_HEIGHT;
      float renderSRCWidth = srcWidth / this.IMAGE_WIDTH;
      float renderSRCHeight = srcHeight / this.IMAGE_HEIGHT;
      GL11.glBegin(4);
      GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
      GL11.glVertex2d(x + width, y);
      GL11.glTexCoord2f(renderSRCX, renderSRCY);
      GL11.glVertex2d(x, y);
      GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
      GL11.glVertex2d(x, y + height);
      GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
      GL11.glVertex2d(x, y + height);
      GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
      GL11.glVertex2d(x + width, y + height);
      GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
      GL11.glVertex2d(x + width, y);
      GL11.glEnd();
   }

   public void drawString(String text, double x, double y, Color color, boolean shadow) {
      x *= 2.0D;
      y = y * 2.0D - 2.0D;
      GL11.glPushMatrix();

      GL11.glScaled(0.25D, 0.25D, 0.25D);

      TextureUtil.bindTexture(this.texID);
      glColor(shadow ? new Color(0.05F, 0.05F, 0.05F, color.getAlpha() / 255.0F) : color);

      int size = text.length();
      for (int indexInString = 0; indexInString < size; indexInString++) {
         char character = text.charAt(indexInString);
         if (character < this.chars.length) {
            drawChar(character, (float) x, (float) y);
            x += this.chars[character].width - this.charOffset;
         }
      }
      GL11.glPopMatrix();
   }

   private void glColor(Color color) {
      float red = color.getRed() / 255.0F;
      float green = color.getGreen() / 255.0F;
      float blue = color.getBlue() / 255.0F;
      float alpha = color.getAlpha() / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   int getStringHeight(String text) {
      int lines = 1;
      for (char c : text.toCharArray()) {
         if (c == '\n') {
            lines++;
         }
      }
      return (this.fontHeight - this.charOffset) / 2 * lines;
   }

   public int getHeight() {
      return (this.fontHeight - this.charOffset) / 2;
   }

   public int getStringWidth(String text) {
      int width = 0;
      for (char c : text.toCharArray()) {
         if (c < this.chars.length) {
            width += this.chars[c].width - this.charOffset;
         }
      }
      return width / 2;
   }

   public Font getFont() {
      return this.font;
   }

   private class IntObject {
      public int width;
      public int height;
      int storedX;
      int storedY;

      private IntObject() {
      }
   }
}
