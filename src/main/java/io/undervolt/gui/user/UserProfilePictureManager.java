package io.undervolt.gui.user;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class UserProfilePictureManager {
    private final Map<String, DynamicTexture> dynamicTextureMap = Maps.newHashMap();
    private final Base64.Decoder decoder = Base64.getDecoder();

    public DynamicTexture getImageAsDynamicTexture(String imgStr) {
        if(imgStr != null) {
            if (!dynamicTextureMap.containsKey(imgStr)) {
                BufferedImage image;
                byte[] imageByte;

                String imageString = imgStr.split(",")[1];
                try {
                    imageByte = decoder.decode(imageString);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                    image = ImageIO.read(bis);
                    bis.close();
                    this.dynamicTextureMap.put(imgStr, new DynamicTexture(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dynamicTextureMap.get(imgStr);
    }
}
