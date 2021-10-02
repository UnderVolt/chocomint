package io.undervolt.gui.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.api.event.events.TickEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class UserProfilePictureManager implements Listener {

    private final List<String> dynamicTextureQueue = Lists.newArrayList();

    private final Map<String, DynamicTexture> dynamicTextureMap = Maps.newHashMap();
    private final Map<String, BufferedImage> bufferedImageMap = Maps.newHashMap();
    private final Base64.Decoder decoder = Base64.getDecoder();

    @EventHandler public void tick(TickEvent.ClientTickEvent event) {
       if(!dynamicTextureQueue.isEmpty()) {
           this.getImageAsDynamicTexture(dynamicTextureQueue.get(0));
           this.dynamicTextureQueue.remove(0);
       }
    }

    public void addImageToCache(String imgStr) {
        this.dynamicTextureQueue.add(imgStr);
    }

    public DynamicTexture getCachedDynamicTexture(String imgStr) {
        return this.dynamicTextureMap.get(imgStr);
    }

    public DynamicTexture getImageAsDynamicTexture(String imgStr) {
        if(imgStr != null) {
            if (!dynamicTextureMap.containsKey(imgStr)) {
                BufferedImage image;
                byte[] imageByte;

                String[] imgStrArr = imgStr.split(",");
                String imageString = imgStrArr.length > 1 ? imgStrArr[1] : imgStrArr[0];
                try {
                    imageByte = decoder.decode(imageString);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                    image = ImageIO.read(bis);
                    bis.close();
                    this.bufferedImageMap.put(imgStr, image);
                    this.dynamicTextureMap.put(imgStr, new DynamicTexture(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dynamicTextureMap.get(imgStr);
    }

    public BufferedImage getImageAsBufferedImage(String imgStr) {
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
                    this.bufferedImageMap.put(imgStr, image);
                    this.dynamicTextureMap.put(imgStr, new DynamicTexture(image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return this.bufferedImageMap.get(imgStr);
    }
}
