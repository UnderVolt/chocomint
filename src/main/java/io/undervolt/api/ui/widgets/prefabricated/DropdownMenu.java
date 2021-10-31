package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.utils.Colour;

import java.awt.*;

public class DropdownMenu extends Drawable
{

   private final Option.OptionSet optionSet;
   private Options options;

   /**
    * Dropdown menu to select items
    * @param optionSet Option set
    */
   public DropdownMenu(Option.OptionSet optionSet)
   {
      this.optionSet = optionSet;
   }

   @Override public void load()
   {
      internal = new Drawable[] {
           new Gesture(
                new Box(
                     200,
                     16,
                     new Color(41, 43, 47),
                     new Padding(
                          new EdgeInsets(3, 5, 5, 3),
                          new Text(
                               optionSet.getSelectedOption() == null
                                    ? "Ninguna opción seleccionada" : optionSet.getSelectedOption().getName(),
                               10
                          )
                     )
                ).setBorderRadius(EdgeInsets.all(5))
           ).onPress((child, mouseX, mouseY, button) -> options.toggleOpen()),
           options = new Options(optionSet)
      };

      super.load();
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
      height = options.isMenuOpen ? options.getHeight() : 16;
   }

   static class Options extends Drawable
   {
      private final Option.OptionSet optionSet;
      private boolean isMenuOpen = false;

      /**
       * Options to select
       * @param optionSet Option Set
       */
      Options(Option.OptionSet optionSet) {
         this.optionSet = optionSet;
      }

      @Override public void load()
      {
         internal = new Drawable[] {
              new Box(1, 1)
         };

         super.load();
      }

      private Drawable Option(Option option)
      {
         return new Box(
              width - 4,
              13,
              new Padding(
                   EdgeInsets.all(2),
                   new Box(
                        width - 4,
                        10,
                        new Text(
                             option.getName(),
                             10
                        )
                   )
              )
         );
      }

      private Drawable[] CreateDropdown()
      {
         width = 200;
         height = optionSet.getOptions().size() * 13;

         Drawable[] options = new Drawable[optionSet.getOptions().size()];
         for(int i = 0; i < optionSet.getOptions().size(); i++)
         {
            options[i] = Option(optionSet.getOptions().get(i));
         }

         return new Drawable[] {
              new Box(
                   new Box(
                        width,
                        height,
                        new Color(41, 43, 47),
                        new Padding(
                             EdgeInsets.all(2),
                             new Column(
                                  options
                             )
                        )
                   )
              )
         };
      }

      public void setMenuOpen(boolean menuOpen)
      {
         isMenuOpen = menuOpen;
      }

      public void toggleOpen()
      {
         isMenuOpen = !isMenuOpen;
         if(isMenuOpen)
            internal = CreateDropdown();
         else
            internal = new Drawable[] {
                 new Box(0, 0)
            };
      }
   }
}
