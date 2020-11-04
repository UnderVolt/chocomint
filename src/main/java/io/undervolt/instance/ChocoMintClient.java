package io.undervolt.instance;

import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.notifications.NotificationManager;
import net.minecraft.client.Minecraft;

public class ChocoMintClient {

    private NotificationManager notificationManager;
    private GameBridge gameBridge;

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                this.notificationManager = new NotificationManager();
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
