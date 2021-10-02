package io.undervolt.gui.notifications;

import io.undervolt.gui.Panel;
import io.undervolt.gui.clickable.ClickableLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationScreen extends Panel {

    private final NotificationManager notificationManager;
    private ClickableLabel clearNotificationsButton;

    public NotificationScreen(GuiScreen previousScreen) {
        super(previousScreen, "Notificaciones", previousScreen.height);
        this.notificationManager = this.chocomint.getNotificationManager();
    }

    @Override
    public void initGui() {
        this.chocomint.getGameBar().notificationsButton.setTexture(
                new ResourceLocation("/chocomint/icon/notifications.png")
        );
        this.clearNotificationsButton = new ClickableLabel(
                this.width - this.mc.fontRendererObj.getStringWidth("LIMPIAR") - 5, 28, "LIMPIAR",
                a -> this.notificationManager.clearNotifications());
        super.initGui();
    }

    @Override
    public void drawContent(int mouseX, int mouseY, float partialTicks, int margin, int scroll) {
        this.clearNotificationsButton.draw(mouseX, mouseY);
        AtomicInteger y = new AtomicInteger(scroll);
        this.notificationManager.getNotifications().forEach(notification -> {
            notification.draw(this.mc, margin + 5, y.get() + 50, this.getPanelWidth() - 10);
            y.set(y.get() + 45);
        });

        this.pageSize = y.get();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.clearNotificationsButton.click(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
