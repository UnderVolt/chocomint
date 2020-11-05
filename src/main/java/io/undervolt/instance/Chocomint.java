package io.undervolt.instance;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.user.User;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Chocomint {

    private final NotificationManager notificationManager;
    private GameBridge gameBridge;
    private final RenderUtils renderUtils;

    /** Initialize constructor */
    public Chocomint(final Minecraft mc) {
        //TODO: Find out why it won't load inside init method
        this.notificationManager = new NotificationManager();
        this.renderUtils = new RenderUtils(mc);
    }

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                //this.notificationManager = new NotificationManager();
                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                //TODO: Register events & hooks
                break;
            case POSTINIT:
                //TODO: Throw post setup
                break;            
        }
    }

    public GameBridge getGameBridge() {
        return gameBridge;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

}
