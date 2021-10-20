package io.undervolt.gui;

import io.undervolt.api.ui.AnimatedOverlay;
import io.undervolt.api.ui.Overlay;
import io.undervolt.api.ui.Screen;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.utils.VColor;
import net.minecraft.client.gui.GuiScreen;

public class PanelOverlay extends AnimatedOverlay {

    public PanelOverlay(GuiScreen previousScreen) {
        super(previousScreen, OverlayPosition.RIGHT);
    }

    protected Drawable[] panelChildren;

    @Override public void load() {

        overlayWidth = getPanelWidth();
        overlayHeight = height;
        children = new Drawable[] {
                new Padding(
                        new EdgeInsets(0, width - overlayWidth, 0, 0),
                        new Container(
                                overlayWidth,
                                overlayHeight,
                                new Padding(
                                        new EdgeInsets(25, 5, 5, 0),
                                        new Column(panelChildren)
                                )
                        ).setBackgroundColor(new VColor(22))
                )
        };

        super.load();
    }

    protected int getPanelWidth() {
        return Math.min(this.width / 3, 250);
    }
}
