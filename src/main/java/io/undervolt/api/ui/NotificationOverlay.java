package io.undervolt.api.ui;

import io.undervolt.api.animation.Animations;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Box;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.api.ui.widgets.controllers.AnimationController;
import io.undervolt.api.ui.widgets.prefabricated.NotificationCard;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.PanelOverlay;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class NotificationOverlay extends PanelOverlay {

   private final NotificationManager notificationManager;

   public NotificationOverlay(GuiScreen previousScreen) {
      super(previousScreen);
      this.notificationManager = GameBridge.getChocomint().getNotificationManager();
   }

   protected List<Notification> notifications;

   private Drawable[] CreatePanel(List<Notification> notifications) {

      Drawable[] drawableNotifications = new Drawable[notifications.size()];

      for(int i = 0; i < notifications.size(); i++)
      {
         Notification notification = notifications.get(i);
         drawableNotifications[i] = new Padding(
              EdgeInsets.vertical(7),
              new NotificationCard(
                   getPanelWidth() - 10,
                   notification
              )
         );
      }

      return new Drawable[]{
           new Padding(
                new EdgeInsets(0, 0, 0, 15),
                new Row(
                     new Text("Notificaciones").style(
                          new Text.TextStyle().setFontSize(11)
                     )
                )
           ),
           new Box(
                getPanelWidth(),
                height,
                new Scrollable(
                     ScrollDirection.COLUMN,
                     new Column(
                          drawableNotifications
                     )
                )
           ).setOverflowHidden(true),
      };
   }

   @Override
   public void load()
   {
      notifications = notificationManager.getNotifications();
      panelChildren = CreatePanel(notifications);

      super.load();
   }

}
