package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.font.FontBridge;
import io.undervolt.api.ui.font.UnicodeFont;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.gui.notifications.Notification;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NotificationCard extends Drawable {

   protected Notification notification;
   private UnicodeFont font;

   /**
    * Component that draws a card given a notification
    * @param width Card's width
    * @param notification Notification object to display
    */
   public NotificationCard(int width, Notification notification) {
      this.width = width;
      this.notification = notification;
   }

   /**
    * Component that draws a card given a notification
    * @param notification Notification object to display
    */
   public NotificationCard(Notification notification) {
      this(120, notification);
   }

   @Override
   public void init() {
      this.font = FontBridge.Raleway;
   }

   @Override
   public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
      
      mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, width, 40, 3, new Color(32, 34, 36).getRGB());
      mc.getChocomint().getRenderUtils().drawRoundedRect(x, y, x + 16, y + 40, 3, 0, 3, 0, new Color(28, 32, 34).getRGB());
      mc.getChocomint().getRenderUtils().drawFilledCircle(x + 8, y + 20, 2, notification.getPriorityColor());

      String title = this.notification.title;
      if(font.getStringWidth(title) > width - 35) {
         title = title.substring(0, Math.min(title.length(), 14)) + "...";
      }

      font.drawString(title, x + 23, y + 5, this.notification.getPriorityColor());

      if(font.getStringWidth(this.notification.description) > width - 44 ) {
         String splitSeq = this.notification.description.contains(" ") ? " " : "(?!^)";
         StringBuilder firstLine = new StringBuilder();
         StringBuilder secondLine = new StringBuilder();

         for (String msgSplit : this.notification.description.split(splitSeq)){
            if(font.getStringWidth(firstLine + msgSplit) < width - 40) {
               firstLine.append(msgSplit).append(splitSeq.replace("(?!^)", ""));
            } else {
               secondLine.append(msgSplit).append(splitSeq.replace("(?!^)", ""));
            }
         }

         if(font.getStringWidth(secondLine.toString()) > width - 45) {
            secondLine = new StringBuilder(secondLine.substring(0, Math.min(secondLine.length(), 14)) + "...");
         }

         font.drawString(firstLine.toString(), x + 23, y + 16, new Color(111, 112, 113).getRGB());
         font.drawString(secondLine.toString(), x + 23, y + 24, new Color(111, 112, 113).getRGB());
      } else {
         font.drawString(this.notification.description, x + 23, y + 16, new Color(111, 112, 113).getRGB());
      }

      if(mouseX > x && mouseY > y && mouseX < x + this.width && mouseY < y + 35) {
         mc.getChocomint().getRenderUtils().drawFilledCircle(x + (int) this.width - 10, y + 20, 5,
              (mouseX > x + this.width - 15 && mouseY > y + 15 && mouseX < x + this.width - 5 && mouseY < y + 25)
                   ? new Color(255, 81, 81).getRGB() : new Color(28, 32, 34).getRGB());
         GL11.glPushMatrix();
         GL11.glTranslatef(x + this.width - 13, y + 16.5f, 0);
         font.drawString("\247l✕", 0, -2,
              (mouseX > x + this.width - 15 && mouseY > y + 15 && mouseX < x + this.width - 5 && mouseY < y + 25)
                   ? Color.white.getRGB() : Color.gray.getRGB());
         GL11.glPopMatrix();
      }
   }
}
