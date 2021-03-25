package io.undervolt.instance;

import com.google.common.collect.Lists;
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
import io.undervolt.gui.Background;
import io.undervolt.gui.RenderUtils;
import io.undervolt.gui.chat.ChatManager;
import io.undervolt.gui.contributors.ContributorsManager;
import io.undervolt.gui.friends.FriendsManager;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.notifications.NotificationManager;
import io.undervolt.gui.notifications.NotificationOverlay;
import io.undervolt.gui.user.User;
import io.undervolt.gui.user.UserManager;
import io.undervolt.gui.user.UserProfilePictureManager;
import io.undervolt.mod.ModLoader;
import io.undervolt.utils.Multithreading;
import io.undervolt.utils.RestUtils;
import io.undervolt.utils.config.Config;
import io.undervolt.utils.config.ConfigurableManager;
import io.undervolt.utils.config.ProfileLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Chocomint implements Listener {

    private final Minecraft mc;
    private final long millisAtStart;
    private GameBridge gameBridge;
    private final String clientName;
    private final String commitName;

    private User user;
    private final String chocomintUser;
    private UserManager userManager;
    private final UserProfilePictureManager userProfilePictureManager;

    private final Sambayon sambayon;

    private ChatManager chatManager;
    private Almendra almendra;
    private Console console;

    private FriendsManager friendsManager;

    private ContributorsManager contributorsManager;

    private final EventManager eventManager;

    private ScreenshotUploader screenshotUploader;

    private NotificationOverlay notificationOverlay;
    private NotificationManager notificationManager;

    private final ProfileLoader loader;
    private final ConfigurableManager configurableManager;
    private Config config;
    private ModLoader modLoader;
    private File rootPath;

    private final RenderUtils renderUtils;
    private RestUtils restUtils;

    public boolean IS_ONLINE = false;

    // Configurables
    private Background background;

    /** Initialize constructor */
    public Chocomint(final Minecraft mc) {
        this.millisAtStart = System.currentTimeMillis();
        this.commitName = "testCommit";
        this.clientName = "chocomint";

        this.rootPath = new File(Minecraft.getMinecraft().mcDataDir + File.separator + getClientName());
        rootPath.mkdir();

        this.loader = new ProfileLoader(rootPath);
        this.configurableManager = new ConfigurableManager(this);

        this.eventManager = new EventManager();
        this.sambayon = new Sambayon(this);
        this.renderUtils = new RenderUtils(mc);
        this.mc = mc;
        this.chocomintUser = "\247bchocomint";
        this.userProfilePictureManager = new UserProfilePictureManager();
    }

    public void init(LaunchType type){
        switch(type){
            case PREINIT:
                this.gameBridge = new GameBridge();
                this.notificationManager = new NotificationManager(this);

                // ModLoader
                this.modLoader = new ModLoader(this);

                // Profiles
                this.loader.availableProfiles.forEach(profile -> System.out.println("Registered profile: " + profile.getName()));
                System.out.println("Current profile: " + this.loader.selectedProfile.getName());

                // Register configurables
                this.getEventManager().registerEvents(this.configurableManager);
                this.background = new Background(this);
                this.configurableManager.register(this.background);

                this.modLoader.load(new File(this.rootPath + File.separator + "mods"));

                this.configurableManager.configurableList.forEach(configurable -> System.out.println("Registered configurable: " + configurable.getName()));

                this.eventManager.registerEvents(this);
                this.eventManager.callEvent(new InitEvent.PreInitEvent());

                //TODO: Load heavy stuff
                //TODO: Load external mods
                break;
            case INIT:

                this.initOfflineUser();
                this.screenshotUploader = new ScreenshotUploader(this);
                this.notificationOverlay = new NotificationOverlay(this);

                this.getEventManager().registerEvents(this.notificationOverlay);
                this.config = new Config(this);

                this.getConfig().loadMinecraftSession();

                this.friendsManager = new FriendsManager();

                this.eventManager.callEvent(new InitEvent.ClientInitEvent());

                //TODO: Register events & hooks
                break;
            case POSTINIT:

                Multithreading.schedule(this::checkAndLoadOnlinePlay, 0, 30, TimeUnit.SECONDS);

                this.contributorsManager = new ContributorsManager(this.mc);

                this.chatManager = new ChatManager(this);
                this.console = new Console(this);

                // Register Commands
                this.console.registerCommand(new VersionCommand(this));
                this.console.registerCommand(new HelpCommand(this));

                this.eventManager.callEvent(new InitEvent.PostInitEvent());

                //TODO: Throw post setup
                break;            
        }
    }

    public void checkAndLoadOnlinePlay() {
        if(!this.IS_ONLINE) {
            if (this.sambayon.isAccesible()) {
                this.restUtils = new RestUtils(this);
                this.config = new Config(this);
                this.userManager = new UserManager(this);
                this.getConfig().loadToken();
                this.friendsManager = new FriendsManager();
                this.user = this.userManager.setUser(this.config.getToken());
                try {
                    this.almendra = new Almendra(this);
                    this.getEventManager().registerEvents(this.almendra);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.notificationManager.addNotification(new Notification(Notification.Priority.NOTICE, "Sesión iniciada", "Se ha iniciado la sesión correctamente", (a)->{}));
                this.IS_ONLINE = true;

            } else {
                this.notificationManager.addNotification(new Notification(Notification.Priority.CRITICAL, "Sesión fallida", "Reintentando en 30 segundos.", (a)->{}));
                this.IS_ONLINE = false;
            }
        }
    }

    public void initOfflineUser() {
        this.user = new User("Guest", User.Status.OFFLINE, null, false, null, null, null, null);
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

    public NotificationOverlay getNotificationOverlay() {
        return notificationOverlay;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ScreenshotUploader getScreenshotUploader() {
        return screenshotUploader;
    }

    public ProfileLoader getLoader() {
        return loader;
    }

    public ConfigurableManager getConfigurableManager() {
        return configurableManager;
    }

    public Background getBackground() {
        return background;
    }

    public FriendsManager getFriendsManager() {
        return friendsManager;
    }

    public String getParsedOpenTime() {

        long milliseconds = System.currentTimeMillis() - this.millisAtStart;

        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;

        return minutes + " minutos y " + seconds + " segundos";
    }

    public UserProfilePictureManager getUserProfilePictureManager() {
        return userProfilePictureManager;
    }
}
