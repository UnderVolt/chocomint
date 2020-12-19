package io.undervolt.api.discord;

import io.undervolt.instance.Chocomint;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class PresenceHandler {

    private final Chocomint chocomint;
    private final transient DiscordRichPresence presence;

    public PresenceHandler(Chocomint chocomint) {
        this.chocomint = chocomint;
        this.presence = new DiscordRichPresence();

        new Thread(()-> {
            DiscordEventHandlers handler = new DiscordEventHandlers.Builder()
                    .setReadyEventHandler(user -> System.out.println(user.username + "#" + user.discriminator)).build();

            DiscordRPC.discordInitialize("438049445909626900", handler, true);

            this.startPresence();
        }).start();
    }

    public void startPresence() {
        presence.state = "En el menú principal";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = "pixel-fade";
        presence.largeImageText = chocomint.getClientName() + "(" + chocomint.getCommitName() + ")";
        presence.partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
        presence.partySize = 1;
        presence.partyMax = 5;
        presence.spectateSecret = "MTIzNDV8MTIzNDV8MTMyNDU0";
        presence.joinSecret = "MTI4NzM0OjFpMmhuZToxMjMxMjM=";

        DiscordRPC.discordUpdatePresence(presence);
    }
}
