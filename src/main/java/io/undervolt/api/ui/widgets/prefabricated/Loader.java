package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.utils.Colour;
import net.minecraft.util.ResourceLocation;

public class Loader extends Drawable {

   private final ResourceLocation loaderIcon;
   private Rotation rotation;

   /**
    * Loader icon for placeholders
    * @param size Box size. Will affect spinner.
    */
   public Loader(int size)
   {
      loaderIcon = new ResourceLocation("chocomint/icon/loader.png");
      width = size;
      height = size;
   }

   @Override public void load()
   {
      internal = new Drawable[] {
           new Box(
                Colour.Black.alpha(100),
                rotation = new Rotation(
                     new Image(loaderIcon)
                          .setWidth((int) (width))
                          .setHeight((int) (width))
                ).setAdjustWidth(false)
           ).setBorderRadius(
                EdgeInsets.all((int) width / 5)
           )
      };

      super.load();
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      rotation.angle += deltaTime * 80;
      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
   }
}
