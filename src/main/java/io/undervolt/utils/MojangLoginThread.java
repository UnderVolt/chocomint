package io.undervolt.utils;

import com.google.common.collect.Maps;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import java.net.Proxy;
import java.util.Map;

import io.undervolt.utils.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MojangLoginThread extends Thread {
	
    private final Minecraft mc;
    private final String password;
    private String status;
    private final String username;
    private final Config config;

    public MojangLoginThread(final Config config, final Minecraft mc, String username, String password) {
        super("Mojang Login Thread");
        this.mc = mc;
        this.config = config;
        this.username = username;
        this.password = password;
        this.status = "\247eWaiting...";
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(username);
        auth.setPassword(password);

        try {
            auth.logIn();
            this.config.saveMinecraftCredentials(auth.saveForStorage());

            return new Session(auth.getSelectedProfile().getName(), auth
                    .getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getStatus() {
        return this.status;
    }

    public void run() {
        if (this.password.equals("")) {
            this.mc.setSession(new Session(this.username, "", "", "mojang"));
            this.status = ("\247aIniciada la sesión. (" + this.username + " - no premium)");

            final Map<String, Object> offlineCred = Maps.newHashMap();

            offlineCred.put("displayName", this.username);
            offlineCred.put("accessToken", "");
            offlineCred.put("userid", "");
            offlineCred.put("uuid", "");
            offlineCred.put("username", this.username);

            this.config.saveMinecraftCredentials(offlineCred);

            return;
        }

        this.status = "\2471Iniciando sesión...";
        Session auth = createSession(this.username, this.password);

        if (auth == null)
            this.status = "\2474Inicio de sesión fallido.";
        else {
            this.status = ("\247aIniciada la sesión. (" + auth.getUsername() + ")");
            this.mc.setSession(auth);
        }
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
