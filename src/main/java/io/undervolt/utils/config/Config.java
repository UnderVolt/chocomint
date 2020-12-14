package io.undervolt.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.RestUtils;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config {

    private final Minecraft mc;
    private final Gson gson;
    private final RestUtils restUtils;

    private String mcToken;

    public Config(final Chocomint chocomint) {
        this.mc = chocomint.getMinecraft();
        this.gson = new GsonBuilder().create();
        this.restUtils = chocomint.getRestUtils();

        this.loadToken();
    }

    private void loadToken(){
        System.out.println("Cargando token");
        final File tkC = new File(this.mc.mcDataDir, "uvpt.json");

        try {
            if(tkC.exists()){
                try {
                    String js = IOUtils.toString(new FileInputStream(tkC), StandardCharsets.UTF_8);
                    JsonObject json = this.gson.fromJson(js, JsonObject.class);

                    this.restUtils.sendJsonRequest("noid/validateToken", js, (obj)->{
                        JsonObject res = this.gson.fromJson(obj, JsonObject.class);
                        if(res.get("code").getAsInt() == 200) this.mcToken = json.get("token").getAsString();
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
    }
}
