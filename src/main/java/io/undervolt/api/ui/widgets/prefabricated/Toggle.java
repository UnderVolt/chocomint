package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.Colour;
import io.undervolt.utils.MathUtil;

import java.awt.*;

public class Toggle extends Drawable {

   protected boolean checked;

   /**
    * Simple toggle button
    * @param checked Is it checked?
    */
   public Toggle(boolean checked)
   {
      this.width = 25;
      this.height = 13;
      this.checked = checked;
   }

   private float currentDotPos = 0;

   private float currentAlpha = 100;

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      boolean hovered = (mouseX >= x) && (mouseX <= x + this.width) && (mouseY >= y) && (
           mouseY <= y + this.height);

      float posX = checked ? (int) width - (height / 2) : (height / 2);
      this.currentDotPos = MathUtil.Lerp(this.currentDotPos, posX, 0.15f);

      int alpha = hovered ? 130 : 90;
      this.currentAlpha = MathUtil.Lerp(this.currentAlpha, alpha, 0.15f);

      GameBridge.getChocomint().getRenderUtils().drawRoundedRect(x + 2, y + 1, width - 2, height - 2, height / 2 - 1,
           Colour.AliceBlue.alpha(alpha));
      GameBridge.getChocomint().getRenderUtils().drawFilledCircle(x + currentDotPos, y + (height / 2), (height / 2),
           checked ? Colour.AliceBlue.getRGB() : new Color(64, 68, 75).getRGB());
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }
}
