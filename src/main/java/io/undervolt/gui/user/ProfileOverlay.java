package io.undervolt.gui.user;

import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.api.ui.widgets.Box;
import io.undervolt.api.ui.widgets.Image;
import io.undervolt.api.ui.widgets.prefabricated.Loader;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.menu.MenuOverlay;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.Colour;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screen crafted to display a specific user's information.
 * @author Gerardo Wacker
 */
public class ProfileOverlay extends MenuOverlay {

   private final Chocomint chocomint;
   private final UserManager userManager;

   /** Banner images, both texture and BufferedImage to properly set its width and height */
   private DynamicTexture banner;
   private BufferedImage bannerBufferedImage;

   /** Country flag */
   private DynamicTexture countryFlag;

   /** Profile picture */
   private DynamicTexture image;

   /** User objects (will be used to check if volatile User is enabled) */
   private User user;
   private String username;

   /** Creation date */
   private Instant createdAt;
   private String createdMonth;
   private String createdYear;

   /** Alias */
   private boolean drawAlias = false;

   /** Friend checking */
   private boolean isFriend;

   /**
    * Screen crafted to display a specific user's information.
    * Constructor applied to prefabricated User objects.
    * @param parentScreen Screen to draw in the background
    * @param user Prefabricated User object
    */
   public ProfileOverlay(GuiScreen parentScreen, User user) {
      super(parentScreen, "Perfil del usuario", MenuColor.PURPLE, "user");

      chocomint = GameBridge.getChocomint();
      userManager = this.chocomint.getUserManager();

      this.user = user;

      username = user.getUsername();
      image = chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(user.getImage());
      countryFlag = chocomint.getCountryFlagManager().getCountryFlag(user.getCountryCode());

      if(user.getBanner() != null) {
         banner = chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(user.getBanner());
         bannerBufferedImage = chocomint.getUserProfilePictureManager().getImageAsBufferedImage(user.getBanner());
      } else {
         banner = null;
         bannerBufferedImage = null;
      }

      createdAt = Instant.parse(user.getCreateDate());
      createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
      createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));

      isFriend = chocomint.getFriendsManager().friendsPool.containsKey(username);

      if(user.getAlias().equalsIgnoreCase(username)) {
         drawAlias = false;
      }
   }

   /**
    * Screen crafted to display a specific user's information.
    * Constructor applied to inexistent User objects, will fetch user's information.
    * @param parentScreen Screen to draw in the background
    * @param username User's username
    */
   public ProfileOverlay(GuiScreen parentScreen, String username) {
      super(parentScreen, "Perfil del usuario", MenuColor.PURPLE, "user");
      chocomint = GameBridge.getChocomint();
      userManager = GameBridge.getChocomint().getUserManager();

      this.username = username.toLowerCase();

      // Create a new thread to fetch user's data asynchronously
      Multithreading.runAsync(() ->
      {
         user = userManager.getUser(username);
         chocomint.getUserProfilePictureManager().addImageToCache(user.getImage());
         chocomint.getCountryFlagManager().addToQueue(user.getCountryCode());

         if(user.getBanner() != null) {
            chocomint.getUserProfilePictureManager().addImageToCache(user.getBanner());
         }

         banner = null;
         bannerBufferedImage = null;

         createdAt = Instant.parse(user.getCreateDate());
         createdMonth = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("MMM"));
         createdYear = ZonedDateTime.ofInstant(createdAt, ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("uuuu"));

         isFriend = chocomint.getFriendsManager().friendsPool.containsKey(username);

         if(user.getAlias().equalsIgnoreCase(username)) {
            drawAlias = false;
         }
      });
   }

   @NonNull private Drawable[] Buttons() {

      return new Drawable[] {};

   }

   private Drawable DeveloperBadge() {

      if(user != null) {
         if (user.isDeveloper())
            return new Box(
                 3 + this.fontRendererObj.getStringWidth("DEV"),
                 11,
                 new Padding(
                      EdgeInsets.all(2),
                      new Text("DEV")
                 )
            ).setBackgroundColor(new Color(47, 56, 168)).setBorderRadius(EdgeInsets.all(3));
      }

      return new Box(
           3 + this.fontRendererObj.getStringWidth("DEV"),
           11
      );

   }

   private Drawable Banner() {

      ResourceLocation defaultBannerLocation = new ResourceLocation("/chocomint/ui/banner/default.jpg");

      if (this.banner == null) {
         return new Image(defaultBannerLocation).setWidth(getContentWidth()).setHeight(getBannerPadding())
              .setImageHeight(this.getContentWidth() / (1600/1000));
      }
      return new Image(banner).setWidth(getContentWidth()).setHeight(getBannerPadding())
           .setImageWidth(getContentWidth()).setImageHeight(getContentWidth() / (bannerBufferedImage.getWidth() / bannerBufferedImage.getHeight()));

   }

   private String getUsernameToRender() {
      if (user != null) {
         return user.getAlias();
      }
      return username;
   }

   private Drawable[] LoaderOverlay()
   {
      return new Drawable[] {
           new Box(
                getContentWidth(),
                height,
                Colour.Black.alpha(100)
           ),
           new Box(
                width,
                height,
                new Positioned(
                     Alignment.CENTER,
                     new Loader(35)
                )
           )
      };
   }

   private Drawable[] CreateOverlay() {

      return new Drawable[] {
           Banner(),
           new Box(
                getContentWidth(),
                93,
                new Padding(
                     EdgeInsets.all(18),
                     new Row(
                          new Image(
                               image
                          ).setHeight(60).setWidth(60),
                          new Padding(
                               EdgeInsets.horizontal(10),
                               new Padding(
                                    EdgeInsets.vertical(5),
                                    new Column(
                                         new Row(
                                              new Text(getUsernameToRender()).style(
                                                   new Text.TextStyle().setFontSize(14)
                                              ),
                                              new Padding(
                                                   EdgeInsets.horizontal(5),
                                                   new Gesture(
                                                        new Image(
                                                             new ResourceLocation("chocomint/icon/external.png")
                                                        ).setWidth(12).setHeight(12)
                                                   ).onPress((child, mouseX, mouseY, button) -> {

                                                      Desktop desktop = java.awt.Desktop.getDesktop();
                                                      try {
                                                         URI oURL = new URI("https://www.undervolt.io/user/");
                                                         desktop.browse(oURL);
                                                      } catch (URISyntaxException | IOException e) {
                                                         this.chocomint.getNotificationManager().addNotification(
                                                              new Notification(Notification.Priority.WARNING, "Error abriendo el navegador", e.getMessage(), obj -> {
                                                              })
                                                         );
                                                         e.printStackTrace();
                                                      }

                                                   })
                                              )
                                         ),
                                         new Image(
                                              countryFlag
                                         ).setWidth(14).setHeight(14),
                                         new Text("Se unió en " + createdMonth + " de " + createdYear),
                                         new Padding(
                                              new EdgeInsets(8, 0, 0, 0),
                                              DeveloperBadge()
                                         )
                                    )
                               )
                          )
                     )
                )

           ).setBackgroundColor(getMenuTitleColor()),
           new Padding(
                EdgeInsets.vertical(6),
                new Box(
                     getContentWidth(),
                     44,
                     new Padding(
                          new EdgeInsets(8, 18, 8, 18),
                          new Column(
                               new Text("Biografía").style(
                                    new Text.TextStyle().setFontSize(10)
                               ),
                               new Padding(
                                    EdgeInsets.vertical(5),
                                    new Text("No tengo biografía.")
                               )
                          )
                     )
                )
           ),
      };

   }

   @Override public void load()
   {
      if(user == null)
      {
         menuChildren = LoaderOverlay();
      }
      else
      {
         menuChildren = CreateOverlay();
      }

      super.load();

   }

   @Override protected void update() {

      if (createdMonth != null) {
         if (banner == null)
            if (user.getBanner() != null) {
               banner = chocomint.getUserProfilePictureManager().getCachedDynamicTexture(user.getBanner());
               bannerBufferedImage = chocomint.getUserProfilePictureManager().getCachedBufferedImage(user.getBanner());
            }
         if (image == null)
            image = chocomint.getUserProfilePictureManager().getImageAsDynamicTexture(user.getImage());
         if (countryFlag == null) {
            countryFlag = chocomint.getCountryFlagManager().getCachedCountryFlag(user.getCountryCode());
            reloadMenu(CreateOverlay());
         }
         if (!username.equals("Guest")) {
            if (username.equals(chocomint.getUser().getUsername())) {
               // TODO: Log out Button
            } else {
               if (this.isFriend) {
                  // TODO: Delete friend button
                  // TODO: Send private message button
               } else {
                  // TODO: Send friend request button
               }
            }
         }
      }

   }
}
