package io.undervolt.gui.notifications;

import io.undervolt.gui.Panel;
import net.minecraft.client.gui.GuiScreen;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationScreen extends Panel {

    private final NotificationManager notificationManager;

    public NotificationScreen(GuiScreen previousScreen) {
        super(previousScreen, "Notificaciones", previousScreen.height);
        this.notificationManager = this.chocomint.getNotificationManager();
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawContent(int mouseX, int mouseY, float partialTicks, int panelWidth, int scroll) {
        AtomicInteger y = new AtomicInteger(scroll);
        this.notificationManager.getNotifications().forEach(notification -> {
            notification.draw(this.mc, 5, y.get(), panelWidth - 10);
            y.set(y.get() + 45);
        });

        this.pageSize = y.get();
    }


}
