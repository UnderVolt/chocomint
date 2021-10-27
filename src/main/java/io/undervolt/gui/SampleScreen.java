package io.undervolt.gui;

import com.google.common.collect.ObjectArrays;
import io.undervolt.api.ui.BasicScreen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.blueprint.Hitbox;
import io.undervolt.mod.RenderMod;
import io.undervolt.utils.config.ConfigurableManager;
import net.minecraft.client.renderer.entity.Render;

import java.util.List;

public class SampleScreen extends BasicScreen {

   private ConfigurableManager configurableManager;
   private List<RenderMod> modList;

   protected Absolute hitboxPosition;
   protected Hitbox hitbox;

   public SampleScreen()
   {
      this.configurableManager = GameBridge.getChocomint().getConfigurableManager();
      this.modList = this.configurableManager.renderModList;
   }

   @Override public void load()
   {
      Drawable[] mods = new Drawable[modList.size() + 1];

      for(int i = 0; i < modList.size(); i++)
      {
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

      mods[modList.size()] = hitboxPosition = new Absolute(
           0,
           0,
           hitbox = new Hitbox()
      );

      children = mods;
   }
}
