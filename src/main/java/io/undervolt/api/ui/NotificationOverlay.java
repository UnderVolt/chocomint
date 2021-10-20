package io.undervolt.api.ui;

import io.undervolt.api.ui.widgets.*;
import io.undervolt.api.ui.widgets.Container;
import io.undervolt.api.ui.widgets.Drawable;
import io.undervolt.gui.PanelOverlay;
import io.undervolt.utils.VColor;
import net.minecraft.client.gui.GuiScreen;

public class NotificationOverlay extends PanelOverlay {

    public NotificationOverlay(GuiScreen previousScreen) {
        super(previousScreen);
    }

    private Drawable not = new Padding(
            new EdgeInsets(0, 0, 0, 5),
            new Container(
                    200, 40,
                    new Padding(
                            EdgeInsets.all(7),
                            new Column(
                                    new Text("Título"),
                                    new Text("Descripción línea 1").style(
                                            new Text.TextStyle().setFontSize(7)
                                    ),
                                    new Text("Descripción línea 2").style(
                                            new Text.TextStyle().setFontSize(7)
                                    )
                            )
                    )
            ).setBackgroundColor(new VColor(11)).setBorderRadius(EdgeInsets.all(3))
    );

    @Override public void load() {

        Drawable[] notifications = new Drawable[] {
                not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not, not
        };

        panelChildren = new Drawable[] {
                new Padding(
                        new EdgeInsets(0, 0, 0, 15),
                        new Row(
                                new Text("Notificaciones").style(
                                        new Text.TextStyle().setFontSize(11)
                                )

                        )
                ),
                new Container(
                        getPanelWidth(),
                        height,
                        new Scrollable(
                                ScrollDirection.COLUMN,
                                new Column(
                                        notifications
                                )
                        )
                ).setOverflowHidden(true)
        };

        super.load();
    }
}
