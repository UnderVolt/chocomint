package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.mod.RenderMod;
import org.lwjgl.opengl.GL11;

public class Mod extends Drawable {

   public RenderMod renderMod;

   /**
    * Drawable variant for RenderMods
    * @param renderMod Mod to use
    */
   public Mod(RenderMod renderMod)
   {
      this.renderMod = renderMod;
      this.width = 100;
      this.height = 12;
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      GL11.glPushMatrix();
      GL11.glTranslatef(x, y, 0);
      this.renderMod.render();
      GL11.glPopMatrix();
   }
}
