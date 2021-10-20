package io.undervolt.gui;

import io.undervolt.api.ui.UIView;
import io.undervolt.api.ui.widgets.*;
import io.undervolt.utils.VColor;
import net.minecraft.client.gui.GuiScreen;

public class DeclarativePanel extends UIView {

    private final String name;
    private final Orientation orientation;
    private final GuiScreen previousScreen;

    public DeclarativePanel(String name, Orientation orientation, GuiScreen previousScreen) {
        this.name = name;
        this.orientation = orientation;
        this.previousScreen = previousScreen;
    }

    protected Drawable register() { return null; }

    @Override public void load() {
        this.addWidgets(
                new Scrollable(
                        ScrollDirection.COLUMN,
                        new Padding(
                                EdgeInsets.horizontal(getWidth() - getPanelWidth()),
                                new Container(
                                        getPanelWidth(),
                                        height,
                                        new Padding(
                                                EdgeInsets.vertical(25),
                                                register()
                                        )
                                ).setBackgroundColor(new VColor(22))
                        )
                )
        );
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(this.mc.theWorld == null && this.mc.thePlayer == null)
            previousScreen.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected int getPanelWidth() {
        return Math.min(this.width / 3, 250);
    }

    protected enum Orientation {
        LEFT, RIGHT
    }
}
