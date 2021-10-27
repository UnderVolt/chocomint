package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Image;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class InterfaceTexturedButton extends Drawable {

   private String iconName;
   private Gesture.Function function;

   /**
    * Creates a rectangular button with a texture
    * @param iconName Icon's texture canonical name
    * @param function Function to be performed on click
    */
   public InterfaceTexturedButton(String iconName, Gesture.Function function)
   {
      this.iconName = iconName;
      this.function = function;
   }

   @Override public void load()
   {
      internal = new Drawable[]{
           new Gesture(
                new Box(
                     20,
                     20,
                     new Padding(
                          EdgeInsets.all(2),
                          new Image(
                               new ResourceLocation("chocomint/icon/" + iconName + ".png")
                          ).setWidth(16).setHeight(16)
                     )
                ).setBorderRadius(EdgeInsets.all(2)).setBackgroundColor(new Color(32, 34, 37))
           ).onPress(function)
      };

      super.load();
   }
}
