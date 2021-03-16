package io.undervolt.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import io.undervolt.gui.notifications.Notification;
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

        this.loadToken();
        this.loadMinecraftSession();
    }

    private void loadMinecraftSession() {
        System.out.println("Cargando sesión de Minecraft");

        try {
            if(mojCrd.exists()) {

                String credentials = IOUtils.toString(new FileInputStream(mojCrd), StandardCharsets.UTF_8);

                YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);


                Type typeOfHashMap = new TypeToken<Map<String, Object>>() { }.getType();
                auth.loadFromStorage(gson.fromJson(credentials, typeOfHashMap));

                if(auth.canLogIn()) {
                    auth.logIn();

                    this.mc.setSession(new Session(auth.getSelectedProfile().getName(), auth
                            .getSelectedProfile().getId().toString(),
                            auth.getAuthenticatedToken(), "mojang"));

                    System.out.println("El sistema ha iniciado sesión con Mojang");
                } else
                    System.out.println("El sistema no puede iniciar la sesión.");

            } else {
                System.out.println("No existían credenciales guardadas.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadToken(){
        System.out.println("Cargando token");

        try {
            if(tkC.exists()){
                try {
                    String js = IOUtils.toString(new FileInputStream(tkC), StandardCharsets.UTF_8);
                    JSONObject json = new JSONObject(js);

                    this.restUtils.sendJsonRequest("api/user", json, (obj)->{
                        try {
                            System.out.println(json.getString("token"));
                            System.out.println(obj);
                            JsonObject res = this.gson.fromJson(obj, JsonObject.class);
                            if(res.get("code").getAsInt() == 200) this.mcToken = json.getString("token");
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
