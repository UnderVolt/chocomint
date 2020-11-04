package io.undervolt.instance;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.NotificationManager;
import net.minecraft.client.Minecraft;

public class Chocomint {

    private final NotificationManager notificationManager;
    private GameBridge gameBridge;

    /** Initialize constructor */
    public Chocomint() {
        //TODO: Find out why it won't load inside init method
        this.notificationManager = new NotificationManager();
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
