package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import io.undervolt.mod.RenderMod;
import org.lwjgl.opengl.GL11;

public class Mod extends Drawable {

   public RenderMod renderMod;
   protected boolean visible = true;

   /**
    * Drawable variant for RenderMods
    * @param renderMod Mod to use
    */
   public Mod(RenderMod renderMod)
   {
      this.renderMod = renderMod;
      this.width = renderMod.width;
      this.height = renderMod.height;
   }

   @Override public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      if(visible)
      {
         this.render(ui, x, y, mouseX, mouseY, deltaTime);
      }
      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
   }

   public void render(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime)
   {
      GL11.glPushMatrix();
      GL11.glTranslatef(x, y, 0);
      GL11.glScalef(this.renderMod.scale, this.renderMod.scale, 1);
      this.renderMod.render();
      GL11.glPopMatrix();
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }
}
