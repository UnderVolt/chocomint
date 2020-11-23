package io.undervolt.instance;

import io.undervolt.api.event.EventManager;
import io.undervolt.bridge.GameBridge;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.user.User;
import io.undervolt.utils.RestUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Chocomint {

    private NotificationManager notificationManager;
    private ChatManager chatManager;
    private final User user;
    private GameBridge gameBridge;
    private final RenderUtils renderUtils;
    private final RestUtils restUtils;
    private EventManager eventManager;
    private ContributorsManager contributorsManager;

    /** Initialize constructor */
    public Chocomint(final Minecraft mc) {
        this.user = new User(mc.getSession().getUsername(), User.Status.ONLINE);
        this.renderUtils = new RenderUtils(mc);
        this.restUtils = new RestUtils();
    }

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                this.notificationManager = new NotificationManager();
                this.eventManager = new EventManager();
                this.contributorsManager = new ContributorsManager();
                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                this.chatManager = new ChatManager();
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

    public User getUser() {
        return user;
    }

    public RenderUtils getRenderUtils() {
        return renderUtils;
    }

    public RestUtils getRestUtils() {
        return restUtils;
    }
  
    public EventManager getEventManager() {
        return eventManager;
    }
  
    public ChatManager getChatManager() {
        return chatManager;
    }

    public ContributorsManager getContributorsManager() {
        return contributorsManager;
    }
}
