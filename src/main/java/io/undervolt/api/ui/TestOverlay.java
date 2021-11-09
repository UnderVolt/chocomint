package io.undervolt.api.ui;

import io.undervolt.api.ui.font.FontBridge;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.prefabricated.InputToggle;
import io.undervolt.gui.SampleMenu;
import io.undervolt.gui.SampleScreen;
import io.undervolt.gui.friends.SocialOverlay;
import io.undervolt.gui.menu.MenuOverlay;
import io.undervolt.gui.user.ProfileOverlay;
import io.undervolt.utils.AnimationUI;
import io.undervolt.utils.Colour;
import net.minecraft.client.gui.GuiScreen;

public class TestOverlay extends MenuOverlay {

   public TestOverlay(GuiScreen prev) {
      super(prev, "Menu no-tan-ultrasecreto de pruebas probadoras", MenuColor.DARK);
   }

   private Drawable ScreenTrigger(AnimationUI screen)
   {
      return new Padding(
           EdgeInsets.vertical(5),
           new Gesture(
                new Box(
                     FontBridge.Raleway.getStringWidth(screen.getClass().getName()) + 10,
                     13,
                     new Colour(22, 22, 22),
                     new Text(
                          screen.getClass().getName()
                     )
                )
                     .setAlignment(Alignment.CENTER)
                     .setBorderRadius(EdgeInsets.all(3))
           ).onPress((child, mouseX, mouseY, button) ->
                this.chocomint.displayMenuOrPanel(screen))
      );
   }

   @Override public void load()
   {
      menuChildren = new Drawable[] {
           new Padding(
                EdgeInsets.all(5),
                new Column(
                     new Padding(
                          new EdgeInsets(5, 5, 5, 0),
                          new Text(
                               "Opciones pre-lanzado",
                               12
                          )
                     ),
                     new Padding(
                          EdgeInsets.vertical(5),
                          new Column(
                               new Gesture(
                                    new InputToggle(
                                         "Mostrar bordes",
                                         DebugOptions.showBounds
                                    )
                               ).onPress((child, mouseX, mouseY, button) -> {
                                  DebugOptions.showBounds = !DebugOptions.showBounds;
                                  ((InputToggle) child).setChecked(DebugOptions.showBounds);
                               }),
                               new Gesture(
                                    new InputToggle(
                                         "Mostrar animaciones",
                                         DebugOptions.showAnimations
                                    )
                               ).onPress((child, mouseX, mouseY, button) -> {
                                  DebugOptions.showAnimations = !DebugOptions.showAnimations;
                                  ((InputToggle) child).setChecked(DebugOptions.showAnimations);
                               }),
                               new Gesture(
                                    new InputToggle(
                                         "Verbose",
                                         DebugOptions.verbose
                                    )
                               ).onPress((child, mouseX, mouseY, button) -> {
                                  DebugOptions.verbose = !DebugOptions.verbose;
                                  ((InputToggle) child).setChecked(DebugOptions.verbose);
                               })
                          )
                     ),
                     new Padding(
                          EdgeInsets.all(5),
                          new Text(
                               "Lanzamiento de ejemplos",
                               12
                          )
                     ),
                     new Padding(
                          EdgeInsets.all(5),
                          new Column(
                               ScreenTrigger(new SampleScreen()),
                               ScreenTrigger(new SampleMenu(previousScreen)),
                               ScreenTrigger(new ProfileOverlay(previousScreen, "gerar")),
                               ScreenTrigger(new NotificationOverlay(previousScreen)),
                               ScreenTrigger(new SocialOverlay(previousScreen))
                          )
                     )
                )
           )
      };

      super.load();
   }
}
