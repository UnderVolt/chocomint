package io.undervolt.gui.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.api.event.events.TickEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CountryFlagManager implements Listener {
    protected static Map<String, DynamicTexture> flagCache = Maps.newHashMap();
    private List<String> cacheQueue = Lists.newArrayList();

    @EventHandler public void tick(TickEvent.ClientTickEvent event) {
        if(!cacheQueue.isEmpty()) {
            this.getCountryFlag(cacheQueue.get(0));
            this.cacheQueue.remove(0);
        }
    }

    public void addToQueue(String countryCode) {
        this.cacheQueue.add(countryCode);
    }

    public DynamicTexture getCachedCountryFlag(final String countryCode) {
        if(countryCode == null) return null;
        if(this.getFlagCache().containsKey(countryCode)) return this.getFlagCache().get(countryCode);
        return null;
    }

    public DynamicTexture getCountryFlag(final String countryCode) {
        if(countryCode == null) return null;
        if(this.getFlagCache().containsKey(countryCode)) return this.getFlagCache().get(countryCode);
        else {
            try {
                final BufferedImage flagTexture = ImageIO.read(new URL("https://raw.githubusercontent.com/gosquared/flags/master/flags/flags-iso/flat/64/" +
                        countryCode + ".png"));
                final DynamicTexture dynamicTexture = new DynamicTexture(flagTexture);
                this.getFlagCache().put(countryCode, dynamicTexture);
                return dynamicTexture;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, DynamicTexture> getFlagCache() {
        return flagCache;
    }
}
