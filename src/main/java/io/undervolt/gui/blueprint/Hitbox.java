package io.undervolt.gui.blueprint;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.utils.Colour;

import java.awt.*;
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
                     Colour.Orange.setAlpha(100)
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
      if (children != null) {
         this.width = (int) Arrays.stream(children).sorted(Comparator.comparing(Drawable::getWidth))
              .collect(Collectors.toList()).get(0).getWidth();
         this.height = (int) Arrays.stream(children).sorted(Comparator.comparing(Drawable::getWidth))
              .collect(Collectors.toList()).get(0).getHeight();
      }

      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
   }

   public void setChildren(Mod[] children) {
      this.children = children;
      this.width = (int) Arrays.stream(children).sorted(Comparator.comparing(Drawable::getWidth))
           .collect(Collectors.toList()).get(0).getWidth();
      this.height = (int) Arrays.stream(children).sorted(Comparator.comparing(Drawable::getWidth))
           .collect(Collectors.toList()).get(0).getHeight();
      internal = CreateHitbox();
   }
}
