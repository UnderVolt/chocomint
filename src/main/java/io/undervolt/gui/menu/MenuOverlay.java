package io.undervolt.gui.menu;

import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.api.ui.widgets.Box;
import io.undervolt.api.ui.widgets.Image;
import io.undervolt.utils.AnimationUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class MenuOverlay extends Screen {

   protected final GuiScreen previousScreen;

   private double tw = Integer.MAX_VALUE;
   private double layer1Y = Integer.MAX_VALUE;
   protected long ftime;
   private boolean backwards = false;

   private final String menuName, menuIcon;

   protected MenuColor color;

   private final ResourceLocation bracketLayer1;
   private final ResourceLocation bracketLayer2;
   private final ResourceLocation bracketLayer3;

   private AnimationUI newScreen;

   public MenuOverlay(final GuiScreen prev, final String menuName) {
      this(prev, menuName, MenuColor.DEFAULT, null);
   }

   public MenuOverlay(final GuiScreen prev, final String menuName, final MenuColor color) {
      this(prev, menuName, color, null);
   }

   public MenuOverlay(final GuiScreen prev, final String menuName, final MenuColor color, final String menuIcon) {
      this.menuName = menuName;
      this.menuIcon = menuIcon;
      this.previousScreen = prev;
      this.color = color;
      this.ftime = Minecraft.getSystemTime();

      this.bracketLayer1 = this.getBracket(1);
      this.bracketLayer2 = this.getBracket(2);
      this.bracketLayer3 = this.getBracket(3);
   }

   public MenuOverlay(final GuiScreen prev, final String menuName, final String menuIcon) {
      this(prev, menuName, MenuColor.DEFAULT, menuIcon);
   }

   //

   protected Drawable[] menuChildren;

   private Image MenuIcon() {
      if(menuIcon != null)
         return new Image(
              new ResourceLocation("chocomint/icon/" + menuIcon + ".png")
         ).setWidth(18).setHeight(18);
      else return new Image(
           new ResourceLocation("chocomint/icon/asd.png")
      ).setWidth(0).setHeight(18);
   }

   private Drawable[] CreateOverlay() {
      return new Drawable[]{
           new Padding(
                EdgeInsets.horizontal(getContentMargin()),
                new Box(
                     getContentWidth(),
                     height,
                     new Scrollable(
                          ScrollDirection.COLUMN,
                          new Column(
                               new Box(
                                    getContentWidth(),
                                    50,
                                    getMenuTitleColor(),
                                    new Padding(
                                         EdgeInsets.all(10),
                                         new Row(
                                              MenuIcon(),
                                              new Padding(
                                                   EdgeInsets.vertical(2),
                                                   new Text(menuName)
                                              )
                                         ).crossAxisAlign(AxisAlignment.END)
                                    )
                               ),
                               new Column(menuChildren)
                          )
                     )
                ).setBackgroundColor(getMenuContentColor())
           )
      };
   }

   @Override
   public void load() {

      children = CreateOverlay();

      this.chocomint.getGameBar().init(width, height);

   }

   @Override
   public void draw(int mouseX, int mouseY, float deltaTime) {
      if (tw != 0) {
         tw = this.getAnimationTime(this.ftime, 3500.0D) * (height + 170);
         if (backwards) tw = this.getAnimationTime(this.ftime, 1600.0D) * height;
      }

      if (layer1Y == this.height + 50)
         if (backwards)
            if (newScreen != null)
               this.mc.displayGuiScreen(this.newScreen);
            else this.mc.displayGuiScreen(this.previousScreen);

      this.chocomint.getGameBar().draw(mouseX, mouseY, deltaTime, width, height);
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseY > 20 && (mouseX < getContentMargin() || mouseX > getContentMargin() + getContentWidth()))
         this.fadeOut();
      this.chocomint.getGameBar().mouseClicked(mouseX, mouseY, mouseButton, width, height);
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor3f(1, 1, 1);
      int hue;

      if (!backwards) {
         hue = 130 - (int) (this.getAnimationTime(this.ftime, 4000.0D) * 130);
      } else {
         hue = (int) (this.getAnimationTime(this.ftime, 4000.0D) * 130);
      }

      if (this.mc.theWorld == null && this.mc.thePlayer == null)
         previousScreen.drawScreen(mouseX, mouseY, partialTicks);

      drawRect(0, 0, width, height, new Color(0, 0, 0, hue).getRGB());

      // Bracket animation layer 1
      GL11.glPushMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor3f(1, 1, 1);
      layer1Y = (this.getAnimationTime(this.ftime, 3000.0D) * (this.height + 40));
      if (backwards) {
         layer1Y = (this.height + 50) - (this.getAnimationTime(this.ftime, 5500.0D) * (this.height + 50));
      }
      GlStateManager.translate(0, layer1Y, 0);
      this.mc.getTextureManager().bindTexture(this.bracketLayer1);
      drawModalRectWithCustomSizedTexture(this.getContentMargin(), -50, 0, 0, this.getContentWidth(),
           300, this.getContentWidth(), 300);
      GL11.glPopMatrix();

      // Bracket animation layer 2
      GL11.glPushMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor3f(1, 1, 1);
      int layer2Y = (int) (this.getAnimationTime(this.ftime, 3500.0D) * (this.height + 40));
      if (backwards) {
         layer2Y = (this.height + 40) - (int) (this.getAnimationTime(this.ftime, 4000.0D) * (this.height) + 10);
      }
      GlStateManager.translate(0, layer2Y, 0);
      this.mc.getTextureManager().bindTexture(this.bracketLayer2);
      drawModalRectWithCustomSizedTexture(this.getContentMargin(), -30, 0, 0, this.getContentWidth(),
           300, this.getContentWidth(), 300);
      GL11.glPopMatrix();

      // Bracket animation layer 3
      GL11.glPushMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor3f(1, 1, 1);
      int layer3Y = (int) (this.getAnimationTime(this.ftime, 3700.0D) * (this.height + 50));
      if (backwards) {
         layer3Y = (this.height + 50) - (int) (this.getAnimationTime(this.ftime, 3500.0D) * (this.height + 50));
      }
      GlStateManager.translate(0, layer3Y, 0);
      this.mc.getTextureManager().bindTexture(this.bracketLayer3);
      drawModalRectWithCustomSizedTexture(this.getContentMargin(), 0, 0, 0, this.getContentWidth(),
           300, this.getContentWidth(), 300);
      GL11.glPopMatrix();

      GL11.glPushMatrix();
      GlStateManager.translate(0, backwards ? height - tw : tw, 0);
      super.drawScreen(mouseX, mouseY, partialTicks);
      GL11.glPopMatrix();
   }

   public void fadeOut() {
      if (!this.backwards) {
         this.backwards = true;
         this.ftime = Minecraft.getSystemTime();
         this.tw = 0.1;
      }
   }

   @Override
   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.chocomint.getGameBar().key(typedChar, keyCode);
      if (keyCode == 1) this.fadeOut();
   }

   protected int getContentWidth() {
      if (this.width < 600) {
         return this.width;
      } else {
         return 600;
      }
   }

   protected int getContentMargin() {
      if (this.getContentWidth() == this.width) {
         return 0;
      } else {
         return (this.width / 2) - 300;
      }
   }

   protected void reloadMenu(Drawable[] menuChildren) {
      this.menuChildren = menuChildren;
      this.reloadScreen(CreateOverlay());
   }

   public int getBannerPadding() {
      return (int) (this.height * .22 > 160 ? 160 : this.height * .22) + 10;
   }

   public enum MenuColor {
      DEFAULT, GREEN, PURPLE, YELLOW, DARK;

      public String getName() {
         switch (this) {
            case GREEN:
               return "green";
            case YELLOW:
               return "yellow";
            case PURPLE:
               return "purple";
            case DARK:
               return "dark";
            default:
               return "default";
         }
      }
   }

   public Color getMenuContentColor() {
      switch (this.color) {
         case YELLOW:
            return new Color(93, 78, 53);
         case GREEN:
            return new Color(42, 72, 41);
         case PURPLE:
            return new Color(61, 41, 73);
         case DARK:
            return new Color(47, 49, 54);
         default:
            return new Color(41, 55, 73);
      }
   }

   public Color getMenuTitleColor() {
      switch (this.color) {
         case YELLOW:
            return new Color(132, 107, 66);
         case GREEN:
            return new Color(56, 105, 54);
         case PURPLE:
            return new Color(85, 54, 105);
         case DARK:
            return new Color(64, 68, 75);
         default:
            return new Color(54, 71, 105);
      }
   }

   public ResourceLocation getBracket(int layer) {
      return new ResourceLocation("/chocomint/ui/bracket/" + this.color.getName() + "/layer" + layer + ".png");
   }

}
