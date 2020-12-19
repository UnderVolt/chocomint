package io.undervolt.instance;

import io.undervolt.api.almendra.Almendra;
import io.undervolt.api.event.EventManager;
import io.undervolt.api.event.events.InitEvent;
import io.undervolt.api.event.events.UserLoginEvent;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.api.sambayon.Sambayon;
import io.undervolt.api.screenshot.ScreenshotUploader;
import io.undervolt.bridge.GameBridge;
import io.undervolt.console.Console;
import io.undervolt.console.commands.HelpCommand;
import io.undervolt.console.commands.VersionCommand;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationOverlay;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserManager;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import net.minecraft.client.Minecraft;

public class Chocomint implements Listener {

    private NotificationManager notificationManager;
    private ChatManager chatManager;
    private User user;
    private final String chocomintUser;
    private GameBridge gameBridge;
    private final RenderUtils renderUtils;
    private final RestUtils restUtils;
    private final EventManager eventManager;
    private ContributorsManager contributorsManager;
    private final Minecraft mc;
    private Console console;
    private final Sambayon sambayon;
    private ScreenshotUploader screenshotUploader;
    private Almendra almendra;
    private final Config config;
    private final UserManager userManager;
    private final String clientName;
    private final String commitName;

    /** Initialize constructor */
    public Chocomint(final Minecraft mc) {
        this.eventManager = new EventManager();
        this.sambayon = new Sambayon(this);
        this.renderUtils = new RenderUtils(mc);
        this.restUtils = new RestUtils(this);
        this.mc = mc;
        this.chocomintUser = "\247bchocomint";
        this.userManager = new UserManager(this);
        this.config = new Config(this);
        this.user = this.userManager.setUser(this.config.getToken());
        this.commitName = "testCommit";
        this.clientName = "chocomint";
    }

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                this.notificationManager = new NotificationManager(this);
                this.contributorsManager = new ContributorsManager(this.mc);

                this.chatManager = new ChatManager(this);
                this.console = new Console(this);

                try {
                    this.almendra = new Almendra(this);
                    this.getEventManager().registerEvents(this.almendra);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.eventManager.registerEvents(this);
                this.eventManager.callEvent(new InitEvent.PreInitEvent());

                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:
                this.screenshotUploader = new ScreenshotUploader(this);

                // Register Commands
                this.console.registerCommand(new VersionCommand(this));
                this.console.registerCommand(new HelpCommand(this));

                this.getEventManager().registerEvents(new NotificationOverlay(this));

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

    public Config getConfig() {
        return config;
    }

    public void setUser(User user) {
        this.user = user;
        this.eventManager.callEvent(new UserLoginEvent(user));
    }

    public String getClientName() {
        return clientName;
    }

    public String getCommitName() {
        return commitName;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ScreenshotUploader getScreenshotUploader() {
        return screenshotUploader;
    }
}
