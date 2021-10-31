package io.undervolt.gui;

import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.prefabricated.DropdownMenu;
import io.undervolt.api.ui.widgets.prefabricated.InputToggle;
import io.undervolt.api.ui.widgets.prefabricated.Option;
import io.undervolt.api.ui.widgets.prefabricated.Toggle;
import io.undervolt.gui.menu.MenuOverlay;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;

public class SampleMenu extends MenuOverlay {

   /**
    * Sample menu to test stuff
    * @param prev
    */
   public SampleMenu(GuiScreen prev)
   {
      super(prev, "Settings", MenuColor.DARK);
   }

   @Override public void load()
   {
      Option.OptionSet optionSet = Option.OptionSet.newOptionSet();
      Option[] options = new Option[]{
           new Option(optionSet, "Opción 1", a->{}),
           new Option(optionSet, "Opción 2", a->{}),
           new Option(optionSet, "Opción 3", a->{}),
           new Option(optionSet, "Opción 4", a->{}),
           new Option(optionSet, "Opción 5", a->{}),
           new Option(optionSet, "Opción 6", a->{}),

      };
      optionSet.setOptions(Arrays.asList(options));

      menuChildren = new Drawable[] {
           new Column(
                new Gesture(
                     new InputToggle("Saracatunga", false)
                ).onPress(
                     (child, mouseX, mouseY, button) -> {
                        InputToggle toggle = (InputToggle) child;
                        toggle.setChecked(!toggle.isChecked());
                     }
                ),
                new DropdownMenu(optionSet),
                new Gesture(
                     new InputToggle("Sucutruqui", true)
                ).onPress(
                     (child, mouseX, mouseY, button) -> {
                        InputToggle toggle = (InputToggle) child;
                        toggle.setChecked(!toggle.isChecked());
                     }
                )
           )
      };

      super.load();
   }
}
