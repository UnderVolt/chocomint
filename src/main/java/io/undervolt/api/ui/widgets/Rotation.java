package io.undervolt.api.ui.widgets;

import io.undervolt.api.ui.Screen;
import org.lwjgl.opengl.GL11;

public class Rotation extends Drawable {
   public float angle;
   protected Drawable child;
   protected boolean adjustWidth = true;

   public Rotation(float angle, Drawable child) {
      this.angle = angle;
      this.child = child;
      this.child.parent = this.parent;
      double rotationRad = Math.abs(Math.toRadians(this.angle));
      this.width = (float) (Math.abs(child.width * Math.cos(rotationRad)) + Math.abs(child.height * Math.sin(rotationRad)));
      this.height = (float) (Math.abs(child.width * Math.sin(rotationRad)) + Math.abs(child.height * Math.cos(rotationRad)));
   }

   @Override
   public void load() {
      this.child.load();
   }

   public Rotation(Drawable child) {
      this(0, child);
   }

   @Override
   public void draw(Screen ui, int x, int y, int mouseX, int mouseY, float deltaTime) {
      float wDif;
      float hDif;

      if(adjustWidth) {
         double rotationRad = Math.abs(Math.toRadians(this.angle));
         this.width = (float) (Math.abs(child.width * Math.cos(rotationRad)) + Math.abs(child.height * Math.sin(rotationRad)));
         this.height = (float) (Math.abs(child.width * Math.sin(rotationRad)) + Math.abs(child.height * Math.cos(rotationRad)));
         wDif = (width - child.width) / 2;
         hDif = (height - child.height) / 2;
      } else {
         wDif = 0;
         hDif = 0;
      }

      GL11.glPushMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor3f(1, 1, 1);
      GL11.glTranslatef(x + wDif, y + hDif, 0);
      GL11.glTranslatef((child.getWidth() / 2), (child.getHeight() / 2), 0);
      GL11.glRotatef(angle, 0, 0, 1);
      GL11.glTranslatef(-(child.getWidth() / 2), -(child.getHeight() / 2), 0);
      child.draw(ui, 0, 0, mouseX, mouseY, deltaTime);
      GL11.glPopMatrix();
      super.draw(ui, x, y, mouseX, mouseY, deltaTime);
   }

   public void setAngle(float angle) {
      this.angle = angle;
   }

   @Override
   public void onPress(int x, int y, int button) {
      child.onPress(x, y, button);
   }

   @Override
   public void onDrag(int x, int y, int button, long timeSinceLastClick) {
      child.onDrag(x, y, button, timeSinceLastClick);
   }

   @Override
   public void onRelease(int x, int y, int button) {
      child.onRelease(x, y, button);
   }

   public Rotation setAdjustWidth(boolean adjustWidth) {
      this.adjustWidth = adjustWidth;
      return this;
   }
}
