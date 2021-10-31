package io.undervolt.gui.blueprint;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.Colour;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Hitbox extends Drawable {

   private Mod[] children;

   private Drawable[] CreateHitbox() {
      return new Drawable[]{
           new Absolute(
                -3,
                -3,
                new Box(
                     width + 6,
                     height + 6,
                     Colour.Orange.alpha(100)
                ).setBorder(
                     new BorderBox(
                          1,
                          Colour.Orange
                     )
                )
           ).setRelative(true),
           new Absolute(
                -3,
                -3,
                new Circle(
                     Colour.Orange,
                     3
                ).setCenteredRadius(true)
           ).setRelative(true),
           new Absolute(
                (int) width + 3,
                -3,
                new Circle(
                     Colour.Orange,
                     3
                ).setCenteredRadius(true)
           ).setRelative(true),
           new Absolute(
                (int) width + 3,
                (int) height + 3,
                new Circle(
                     Colour.Orange,
                     3
                ).setCenteredRadius(true)
           ).setRelative(true),
           new Absolute(
                -3,
                (int) height + 3,
                new Circle(
                     Colour.Orange,
                     3
                ).setCenteredRadius(true)
           ).setRelative(true),
      };
   }

   @Override public void load()
   {
      internal = CreateHitbox();

      super.load();
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
      if(children != null)
      {
         updateWidthAndHeight(children);
         Arrays.asList(children).forEach(c ->
         {
            c.render(ui, c.renderMod.x, c.renderMod.y, mouseX, mouseY, deltaTime);
         });
      }
   }

   private void updateWidthAndHeight(Mod[] children) {
      Mod lowestX = Arrays.stream(children).sorted(Comparator.comparing(Drawable::getX))
           .collect(Collectors.toList()).get(0);

      Mod highestWidth = Arrays.stream(children).sorted(Comparator.comparing(d -> ((Mod) d).getX() + ((Mod) d).getWidth())
           .reversed()).collect(Collectors.toList()).get(0);

      Mod lowestY = Arrays.stream(children).sorted(Comparator.comparing(Drawable::getY))
           .collect(Collectors.toList()).get(0);

      Mod highestHeight = Arrays.stream(children).sorted(Comparator.comparing(d -> ((Mod) d).getY() + ((Mod) d).getHeight())
           .reversed()).collect(Collectors.toList()).get(0);

      this.width = highestWidth.getX() + highestWidth.getWidth() - lowestX.getX();
      this.height = highestHeight.getY() + highestHeight.getHeight() - lowestY.getY();
   }

   public void setChildren(Mod[] children) {
      this.children = children;

      if(children != null)
      {
         updateWidthAndHeight(children);
      }
      else
      {
         this.width = 0;
         this.height = 0;
      }

      internal = CreateHitbox();
   }

   public void updateChildrenPosition(int x, int y)
   {
      Arrays.asList(children).forEach(mod -> {
         mod.setVisible(false);
         mod.renderMod.x += x;
         mod.renderMod.y += y;
         mod.renderMod.saveConfig(GameBridge.getChocomint().getLoader().selectedProfile);
      });
   }

   public Mod[] getChildren() {
      return children;
   }
}
