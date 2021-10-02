package io.undervolt.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.gui.user.User;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Config {

    private final Minecraft mc;
    private final Gson gson;
    private final RestUtils restUtils;
    private final File tkC;
    private final File mojCrd;

    private String mcToken;

    public Config(final Chocomint chocomint) {
        this.mc = chocomint.getMinecraft();
        this.gson = new GsonBuilder().create();
        this.restUtils = chocomint.getRestUtils();

        this.tkC = new File(this.mc.mcDataDir, "uvpt.json");
        this.mojCrd = new File(this.mc.mcDataDir, "mojcrd.json");
    }

    public void loadMinecraftSession() {
        System.out.println("Cargando sesión de Minecraft");

        try {
            if(mojCrd.exists()) {

                System.out.println("El archivo de configuración de credenciales existe. Verificando tipo de cuenta...");

                String credentials = IOUtils.toString(new FileInputStream(mojCrd), StandardCharsets.UTF_8);
                Type typeOfHashMap = new TypeToken<Map<String, Object>>() { }.getType();

                final Map<String, Object> credentialsAsMap = gson.fromJson(credentials, typeOfHashMap);

                if(credentialsAsMap.get("accessToken").equals("")) {
                    System.out.println("Detectada cuenta offline. Cargando usuario...");
                    this.mc.setSession(new Session((String) credentialsAsMap.get("username"), "", "", "mojang"));
                    System.out.println("El sistema ha iniciado sesión fuera de línea");
                } else {
                    System.out.println("Detectada cuenta de Mojang. Cargando servicio de autenticación...");

                    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);

                    auth.loadFromStorage(credentialsAsMap);

                    if(auth.canLogIn()) {
                        auth.logIn();
                        this.saveMinecraftCredentials(auth.saveForStorage());

                        this.mc.setSession(new Session(auth.getSelectedProfile().getName(), auth
                                .getSelectedProfile().getId().toString(),
                                auth.getAuthenticatedToken(), "mojang"));

                        System.out.println("El sistema ha iniciado sesión con Mojang");
                    } else
                        System.out.println("El sistema no puede iniciar la sesión.");
                }
            } else {
                System.out.println("No existían credenciales guardadas.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadToken() {
        System.out.println("Cargando token");

        try {
            if(tkC.exists()){
                this.mc.getChocomint().getUser().setAlias("Logging in...");
                try {
                    String js = IOUtils.toString(new FileInputStream(tkC), StandardCharsets.UTF_8);
                    JSONObject json = new JSONObject(js);

                    this.restUtils.sendJsonRequest("api/user", json, (obj)->{
                        try {
                            JsonObject res = this.gson.fromJson(obj, JsonObject.class);
                            if(res.get("code").getAsInt() == 200) this.mcToken = json.getString("token");
                            else this.mc.getChocomint().getUser().setAlias("Guest");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getToken() {
        return mcToken;
    }

    public boolean hasToken(){
        return this.mcToken != null && this.mcToken.length() > 0;
    }

    public void setToken(String token) {
        this.mcToken = token;
        if(token == null) {
            if(tkC.delete()) {
                System.out.println("Cerrada la sesión, eliminado archivo");
            } else {
                this.mc.getChocomint().getNotificationManager().addNotification(
                        new Notification(Notification.Priority.WARNING, "No se pudo cerrar sesión", "Fue imposible eliminar los datos del usuario.", obj->{})
                );
            }
        }
    }

    public void saveMinecraftCredentials(final Map<String, Object> session) {
        final Gson gson = new GsonBuilder().create();

        try {
            final Writer writer = new FileWriter(this.mojCrd);
            gson.toJson(session, writer);
            writer.flush();

            System.out.println("Creado archivo de configuración de credenciales");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
