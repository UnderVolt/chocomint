package io.undervolt.gui.notifications;

import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Container;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.DeclarativePanel;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class NotificationDeclarativeScreen extends DeclarativePanel {

    private final NotificationManager notificationManager = GameBridge.getChocomint().getNotificationManager();

    public NotificationDeclarativeScreen(GuiScreen prev) {
        super("Notificaciones", Orientation.RIGHT, prev);
    }

    Drawable[] notifications = new Drawable[this.notificationManager.getNotifications().size()];

    @Override synchronized protected Drawable register() {
        runNotificationLoop();

        return new Column(
                new Row(
                        new Text("Notificaciones"),
                        new Gesture(
                                new Text("Limpiar")
                        ).onHover((child, mouseX, mouseY) ->((Text) child).getStyle().setTextColor(new Color(255, 218, 108)))
                        .onPress((child, x, y, button) -> this.notificationManager.clearNotifications())
                ).mainAxisAlign(AxisAlignment.CENTER),
                new Container(
                        getPanelWidth(),
                        Integer.MAX_VALUE,
                        new Column(notifications)
                )
        );
    }

    @Override synchronized protected void update() {
        if(notifications.length != notificationManager.getNotifications().size()) {
            notificationManager.clearNotifications();

            notifications = new Drawable[this.notificationManager.getNotifications().size()];

            runNotificationLoop();
        }
    }

    void runNotificationLoop() {
        for(int i = 0; i < notificationManager.getNotifications().size(); i++) {
            notifications[i] = PrefabricatedComponents.Notification(notificationManager.getNotifications().get(i));
        }
    }

}
