package io.undervolt.api.ui;

import io.undervolt.utils.Colour;

public class BasicScreen extends Screen {

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      drawRect(0, 0, width, height, Colour.Black.getRGB());
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}

