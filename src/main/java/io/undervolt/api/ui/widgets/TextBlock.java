package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;

import java.awt.*;
import java.util.Arrays;

public class TextBlock extends Drawable {

   protected String text;
   protected Text.TextStyle style;

   private int lineAmount;
   private String[] lines;

   public TextBlock(String text) {
      this.text = text;
      this.style = new Text.TextStyle();
   }

   @Override public void init() {
      this.width = this.parent.width - 25;
      this.height = this.parent.height;

      this.lineAmount = (int) Math.ceil(mc.fontRendererObj.getStringWidth(this.text) / this.width);
      this.lines = new String[lineAmount];

      String splitSeq = this.text.contains(" ") ? " " : "(?!^)";
      String[] splitText = this.text.split(splitSeq);
      String processedText = "";

      for (String word : splitText) {
         processedText += word + " ";
         int processedTextLength = (int) Math.ceil(mc.fontRendererObj.getStringWidth(processedText) / this.width - 1);

         if (lines[processedTextLength] == null) {
            lines[processedTextLength] = "";
         }

         lines[processedTextLength] += word + " ";
      }

      if(lines.length * 9 > height) {
         int lastLine = (int) Math.ceil(height / 9) - 2;
         lines[lastLine] = lines[lastLine].trim().substring(0, lines[lastLine].length() - 3) + "...";
         lines = Arrays.copyOf(lines, lastLine + 1);
      }
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
      this.width = this.parent.width;
      this.height = this.parent.height;

      for(int i = 0; i < this.lines.length; i++) {
         String line = this.lines[i];
         this.mc.fontRendererObj.drawString(line, x, y + 9 * i, style.textColor.getRGB());
      }
   }

   public int getLineAmount() {
      return lineAmount;
   }
}
