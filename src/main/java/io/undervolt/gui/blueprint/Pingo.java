package io.undervolt.gui.blueprint;

import io.undervolt.api.ui.UIView;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.api.ui.widgets.Container;
import io.undervolt.api.ui.widgets.Image;
import io.undervolt.bridge.GameBridge;
import io.undervolt.utils.VColor;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class Pingo extends UIView {

    private Drawable button(String text, Consumer action) {
        return new Padding(
                EdgeInsets.vertical(5),
                new Gesture(
                        new Container(
                                new Padding(
                                        EdgeInsets.all(7),
                                        new Text(text)
                                )
                        )
                                .setWidth(GameBridge.getMinecraft().fontRendererObj.getStringWidth(text) + 14)
                                .setHeight(23)
                                .setBorderRadius(EdgeInsets.all(5))
                                .setBackgroundColor(new VColor(33))
                ).onPress((child, mouseX, mouseY, button) -> {
                    action.accept(0);
                })
        );
    }

    @Override
    public void load() {
        this.addWidgets(
                new Container(
                        new Scrollable(
                                ScrollDirection.COLUMN,
                                new Padding(
                                        EdgeInsets.all(20),
                                        new Column(
                                                new Padding(
                                                        EdgeInsets.vertical(20),
                                                        new Text("UI de ejemplo").style(
                                                                new Text.TextStyle().setFontSize(16)
                                                        )
                                                ),
                                                new Scrollable(
                                                        ScrollDirection.COLUMN,
                                                        new Column(
                                                                button("Hola", a -> System.out.println("Ojoooooo")),
                                                                button("Chau", a -> System.out.println("Fiumbaaa")),
                                                                button("Culo", a -> System.out.println("Corrrrte")),
                                                                new Image(
                                                                        new ResourceLocation("/chocomint/icon/chat_unread.png")
                                                                ).setWidth(10).setHeight(10)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .setWidth(width / 3)
                .setHeight(height)
                .setBackgroundColor(new VColor(22))
        );
    }
}
