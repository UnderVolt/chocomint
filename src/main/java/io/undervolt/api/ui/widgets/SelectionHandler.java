package io.undervolt.api.ui.widgets;

import com.google.common.collect.Lists;
import io.undervolt.api.ui.Screen;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.Colour;
import net.minecraft.client.gui.Gui;

import java.util.List;

public class SelectionHandler extends Gui {

   protected final Screen parentScreen;
   protected boolean selecting;
   protected int x, y, width, height;
   private final List<Drawable> selectedDrawables;
   protected boolean enabled = true;

   /**
    * Handles selecting components within a screen;
    * @param parentScreen Screen to fetch components from
    */
   public SelectionHandler(Screen parentScreen)
   {
      this.parentScreen = parentScreen;
      this.selectedDrawables = Lists.newArrayList();
   }

   private void drawBox(int x, int y, int width, int height)
   {
      GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x + width, y, 1, Colour.White.getRGB());
      GameBridge.getChocomint().getRenderUtils().drawLine(x, y, x, y + height, 1, Colour.White.getRGB());
      GameBridge.getChocomint().getRenderUtils().drawLine(x + width, y, x + width, y + height, 1, Colour.White.getRGB());
      GameBridge.getChocomint().getRenderUtils().drawLine(x, y + height, x + width, y + height, 1, Colour.White.getRGB());
      GameBridge.getChocomint().getRenderUtils().drawRect(x, y, width, height, Colour.White.alpha(50));
   }

   public void draw(int mouseX, int mouseY, float deltaTime)
   {
      if(enabled && selecting)
      {
         this.width = mouseX - this.x;
         this.height = mouseY - this.y;
         this.drawBox(x, y, width, height);
      }
   }

   public void click(int mouseX, int mouseY, int mouseButton)
   {
      if(enabled && mouseButton == 0)
      {
         this.selectedDrawables.clear();
         this.x = mouseX;
         this.y = mouseY;
         this.selecting = true;
      }
   }

   public void release()
   {
      if(enabled)
      {
         this.parentScreen.widgets.forEach(parent ->
         {
            Drawable w = ((Box) parent).child;

            if(w.getX() > this.x && w.getX() < this.x + this.width)
            {
               if(w.getY() > this.y && w.getY() < this.y + this.height)
               {
                  this.selectedDrawables.add(w);
               }
            }
         });

         this.selecting = false;
      }
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   /**
    * @return Selected drawables
    */
   public List<Drawable> getSelectedDrawables()
   {
      return selectedDrawables;
   }
}
