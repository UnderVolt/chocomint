package io.undervolt.instance;

import io.undervolt.api.almendra.Almendra;
import io.undervolt.api.event.EventManager;
import io.undervolt.api.event.events.InitEvent;
import io.undervolt.api.sambayon.Sambayon;
import io.undervolt.api.screenshot.ScreenshotUploader;
import io.undervolt.bridge.GameBridge;
import io.undervolt.console.Console;
import io.undervolt.console.commands.HelpCommand;
import io.undervolt.console.commands.VersionCommand;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.modifiers.UnreadMessageIndicator;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.utils.RestUtils;
import net.minecraft.client.Minecraft;

public class Chocomint {

    private NotificationManager notificationManager;
    private ChatManager chatManager;
    private final String user;
    private final String chocomintUser;
    private GameBridge gameBridge;
    private final RenderUtils renderUtils;
    private final RestUtils restUtils;
    private EventManager eventManager;
    private ContributorsManager contributorsManager;
    private final Minecraft mc;
    private Console console;
    private final Sambayon sambayon;
    private ScreenshotUploader screenshotUploader;
    private Almendra almendra;

    /** Initialize constructor */
    public Chocomint(final Minecraft mc) {
        this.user = mc.getSession().getUsername();
        this.renderUtils = new RenderUtils(mc);
        this.restUtils = new RestUtils();
        this.mc = mc;
        this.chocomintUser = "\247bchocomint";
        this.sambayon = new Sambayon(this);
    }

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                this.notificationManager = new NotificationManager();
                this.eventManager = new EventManager();
                this.contributorsManager = new ContributorsManager(this.mc);

                this.chatManager = new ChatManager(this);
                this.console = new Console(this);

                try {
                    this.almendra = new Almendra(this);
                    this.getEventManager().registerEvents(this.almendra);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.eventManager.callEvent(new InitEvent.PreInitEvent());

                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                this.screenshotUploader = new ScreenshotUploader(this);

                // Register Commands
                this.console.registerCommand(new VersionCommand(this));
                this.console.registerCommand(new HelpCommand(this));

                this.getEventManager().registerEvents(new UnreadMessageIndicator(this.getChatManager(), this.getMinecraft(), "friends"));

                this.eventManager.callEvent(new InitEvent.ClientInitEvent());

                //TODO: Register events & hooks
                break;
            case POSTINIT:

                this.eventManager.callEvent(new InitEvent.PostInitEvent());

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

    public String getUser() {
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

    public String getChocomintUser() {
        return chocomintUser;
    }

    public Almendra getAlmendra() {
        return almendra;
    }

    public Minecraft getMinecraft() {
        return mc;
    }

    public Console getConsole() {
        return console;
    }

    public Sambayon getSambayon() {
        return sambayon;
    }

    public ScreenshotUploader getScreenshotUploader() {
        return screenshotUploader;
    }
}
