package io.undervolt.gui;

import io.undervolt.api.ui.BasicScreen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.blueprint.Hitbox;
import io.undervolt.mod.RenderMod;
import io.undervolt.utils.config.ConfigurableManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SampleScreen extends BasicScreen {

   private ConfigurableManager configurableManager;
   private List<RenderMod> modList;

   protected Absolute hitboxPosition;
   protected Hitbox hitbox;
   protected boolean moving;

   /** This is to avoid ConcurrentModificationExceptions */
   private boolean scheduledReloading = false;

   protected int lastX, lastY;

   public SampleScreen()
   {
      this.configurableManager = GameBridge.getChocomint().getConfigurableManager();
      this.modList = this.configurableManager.renderModList;
   }

   private Drawable[] mods;

   private void updatePositions()
   {
      for (Drawable drawable : this.mods) {
         if (drawable != hitboxPosition) {
            Mod mod = (Mod) ((Gesture) ((Absolute) drawable).getChild()).getChild();
            ((Absolute) drawable).setPosition(mod.renderMod.x, mod.renderMod.y);
            mod.setVisible(true);
         }
      }
   }

   @Override public void load()
   {
      mods = new Drawable[modList.size() + 1];

      for (int i = 0; i < modList.size(); i++) {
         RenderMod renderMod = modList.get(i);
         mods[i] = new Absolute(
              renderMod.x,
              renderMod.y,
              new Gesture(
                   new Mod(renderMod)
              ).onPress((child, mouseX, mouseY, button) -> {
                 hitbox.setChildren(new Mod[]{(Mod) child});
                 hitboxPosition.setPosition(renderMod.x, renderMod.y);
              })
         );
      }

      if(hitboxPosition == null)
         hitboxPosition = new Absolute(
              0,
              0,
              new Gesture(
                   hitbox = new Hitbox()
              )
                   .onPress((child, mouseX, mouseY, button) ->
                   {
                      this.selectionHandler.setEnabled(false);
                      this.moving = true;
                   })
                   .onRelease((child, mouseX, mouseY, button) -> {
                      this.selectionHandler.setEnabled(true);
                      this.moving = false;
                      this.updatePositions();
                   })
         );

      mods[modList.size()] = hitboxPosition;

      children = mods;
   }

   @Override protected void mouseReleased(int mouseX, int mouseY, int state)
   {
      super.mouseReleased(mouseX, mouseY, state);

      if (this.selectionHandler.getSelectedDrawables().size() > 0)
      {
         List<Drawable> selected = this.selectionHandler.getSelectedDrawables().stream()
              .filter(drawable -> drawable instanceof Absolute && ((Absolute) drawable).getChild() instanceof Gesture
                   && ((Gesture) ((Absolute) drawable).getChild()).getChild() instanceof Mod)
              .collect(Collectors.toList());

         Mod[] mods = new Mod[selected.size()];
         for (int i = 0; i < selected.size(); i++) {
            mods[i] = (Mod) ((Gesture) ((Absolute) selected.get(i)).getChild()).getChild();
         }

         if (mods.length > 0)
         {
            this.hitbox.setChildren(mods);

            int lowestX = (int) this.selectionHandler.getSelectedDrawables().stream()
                 .sorted(Comparator.comparing(Drawable::getX)).collect(Collectors.toList()).get(0).getX();
            int lowestY = (int) this.selectionHandler.getSelectedDrawables().stream()
                 .sorted(Comparator.comparing(Drawable::getY)).collect(Collectors.toList()).get(0).getY();

            this.hitboxPosition.setPosition(lowestX, lowestY);
         }
         else
         {
            this.hitbox.setChildren(null);
            this.hitboxPosition.setPosition(-10, -10);
         }
      }
   }

   @Override
   public void updateScreen() {
      if(scheduledReloading)
      {
         this.reloadScreen();
         this.scheduledReloading = false;
      }
   }

   @Override public void draw(int mouseX, int mouseY, float deltaTime)
   {
      if (hitbox != null)
      {
         if (hitbox.getChildren() != null)
         {
            if (moving)
            {
               hitbox.updateChildrenPosition(mouseX - this.lastX, mouseY - this.lastY);
               this.hitboxPosition.setPosition((int) this.hitboxPosition.getX() + mouseX - this.lastX, (int) this.hitboxPosition.getY() + mouseY - this.lastY);
            }
         }
      }

      this.lastX = mouseX;
      this.lastY = mouseY;
   }
}
