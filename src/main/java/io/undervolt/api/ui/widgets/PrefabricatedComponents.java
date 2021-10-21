package io.undervolt.api.ui.widgets;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.utils.ChocoColour;

import java.awt.*;

public class PrefabricatedComponents {

    public static Drawable CloseButton() {
        return new Circle(
                new Color(255, 81, 81),
                5,
                new Padding(
                     EdgeInsets.all(2),
                     new Text("\247l✕").style(
                          new Text.TextStyle().setDropShadow(false).setFontSize(8)
                     )
                )
        );
    }

    public static Drawable Notification(Notification notification) {
        return new Container(
                new Gesture(
                        new Padding(
                                EdgeInsets.all(5),
                                new Column(
                                        new Text(notification.title).style(
                                                new Text.TextStyle().setTextColor(new Color(notification.getPriorityColor()))
                                        ),
                                        new Text(notification.description).style(
                                                new Text.TextStyle().setTextColor(new ChocoColour(111))
                                        )
                                )
                        )
                ).onPress(((child, mouseX, mouseY, button) -> notification.getConsumer().accept(GameBridge.getMinecraft().currentScreen)))
        ).setWidth(GameBridge.getMinecraft().displayWidth / 3).setHeight(40).setBackgroundColor(new Color(32, 34, 36)).setBorderRadius(EdgeInsets.all(3));
    }
}
