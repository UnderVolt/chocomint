package io.undervolt.gui.notifications;

import com.google.common.collect.Lists;
import io.undervolt.api.event.events.NotificationEvent;
import io.undervolt.api.event.events.RenderGameOverlayEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.gui.chat.Message;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationOverlay extends Gui implements Listener {
    private final Chocomint chocomint;
    private final Minecraft mc;
    private final ScaledResolution scaledResolution;
    private final List<Notification> notificationsToDraw = Lists.newArrayList();

    public NotificationOverlay(Chocomint chocomint) {
        this.chocomint = chocomint;
        this.mc = chocomint.getMinecraft();
        this.scaledResolution = this.chocomint.getGameBridge().getScaledResolution();
    }

    public void drawOverlay(int x, int y) {
        AtomicInteger yy = new AtomicInteger(y);
        this.notificationsToDraw.forEach(notification -> {
            notification.draw(mc, x, yy.get());
            yy.set(yy.get() + 43);
        });
    }

    @EventHandler public void add(NotificationEvent.Add e) {
        System.out.println(e.notification.title);
        this.notificationsToDraw.add(e.notification);
        Multithreading.delay(() -> this.notificationsToDraw.remove(e.notification), 8, TimeUnit.SECONDS);
    }

    @EventHandler public void remove(NotificationEvent.Remove e) {
        this.notificationsToDraw.remove(e.notification);
    }

    @EventHandler public void clear(NotificationEvent.Clear e) {
        this.notificationsToDraw.clear();
    }

    @EventHandler public void draw(RenderGameOverlayEvent event) {
        if(this.mc.currentScreen == null) this.drawOverlay(5, 5);
    }
}
