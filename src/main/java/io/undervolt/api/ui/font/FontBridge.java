package io.undervolt.api.ui.font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;


public class FontBridge {

   public static final UnicodeFont Raleway = new UnicodeFont(getRaleway(36), true, 8);
   public static final UnicodeFont Roboto = new UnicodeFont(getRoboto(36), true, 8);
   public static final UnicodeFont Poppins = new UnicodeFont(getFontPoppins(36), true, 8);

   public static final UnicodeFont RalewayBold = new UnicodeFont(getRaleway(36).deriveFont(Font.BOLD), true, 8);
   public static final UnicodeFont RobotoBold = new UnicodeFont(getRoboto(36).deriveFont(Font.BOLD), true, 8);
   public static final UnicodeFont PoppinsBold = new UnicodeFont(getFontPoppins(36).deriveFont(Font.BOLD), true, 8);



   public static void loadFonts() {
      Raleway.getFont();
      Roboto.getFont();
      Poppins.getFont();
   }

   private static Font getRaleway(int size)
   {
      Font font;
      try
      {
         InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("chocomint/font/raleway.ttf")).getInputStream();
         font = Font.createFont(0, is);
         font = font.deriveFont(0, size);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         System.out.println("Error loading font");
         font = new Font("default", 0, size);
      }
      return font;
   }

   private static Font getRoboto(int size)
   {
      Font font;
      try
      {
         InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("chocomint/font/roboto.ttf")).getInputStream();
         font = Font.createFont(0, is);
         font = font.deriveFont(0, size);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         System.out.println("Error loading font");
         font = new Font("default", 0, size);
      }
      return font;
   }
   private static Font getFontPoppins(int size)
   {
      Font font;
      try
      {
         InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("chocomint/font/Poppins.ttf")).getInputStream();
         font = Font.createFont(0, is);
         font = font.deriveFont(0, size);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         System.out.println("Error loading font");
         font = new Font("default", 0, size);
      }
      return font;
   }
}