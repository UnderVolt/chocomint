package io.undervolt.gui.user;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class CountryFlagManager {
    protected static Map<String, DynamicTexture> flagCache = Maps.newHashMap();

    public DynamicTexture getCountryFlag(final String countryCode) {
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
