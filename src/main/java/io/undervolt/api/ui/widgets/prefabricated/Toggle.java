package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.bridge.GameBridge;

public class Checkbox extends Drawable {

   protected boolean checked;

   public Checkbox(boolean checked)
   {
      this.checked = checked;
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      boolean hovered = (mouseX >= x) && (mouseX <= x + this.width) && (mouseY >= y) && (
           mouseY <= y + this.height);
      GameBridge.getChocomint().getRenderUtils().drawRoundedRect(x, y, 50, 30, 8);
   }
}
