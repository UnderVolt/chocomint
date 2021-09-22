package io.undervolt.api.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undervolt.api.readers.BufferedTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageCache {

    private static ImageCache instance;

    private BufferedTexture placeholder;
    private HashMap<String, UCBuff> unconvertedCollector;
    private HashMap<String, CDyn> imageCollector;

    public ImageCache(){
        this.unconvertedCollector = Maps.newHashMap();
        this.imageCollector = Maps.newHashMap();
    }

    public CDyn getCachedImage(String b64){
        if(this.imageCollector.containsKey(b64)) return imageCollector.get(b64);
        return new CDyn(placeholder);
    }

    public CDyn getUnsafeCachedImage(String b64){
        if(this.imageCollector.containsKey(b64)) return imageCollector.get(b64);
        return null;
    }

    public CDyn getCachedImageFixed(String b64){
        if(this.imageCollector.containsKey(b64)) return this.imageCollector.get(b64);
        else {
            CDyn tmp = new CDyn(null, 64, 64);
            tmp.res = new ResourceLocation("pixel/gui/cache.png");
            return tmp;
        }
    }

    public boolean isCached(String b64){
        return this.imageCollector.containsKey(b64);
    }

    public void addImageToCache(String b64, BufferedTexture dy){
        if(!this.imageCollector.containsKey(b64) && !imageCollector.containsKey(b64)) imageCollector.put(b64, new CDyn(dy));
    }

    public void addImageToCacheConverter(String b64, BufferedImage dy){
        if(!unconvertedCollector.containsKey(b64) && !imageCollector.containsKey(b64)) unconvertedCollector.put(b64, new UCBuff(dy));
    }

    public void addImageToCacheConverter(String b64, BufferedImage dym, int width, int height){
        if(!unconvertedCollector.containsKey(b64) && !imageCollector.containsKey(b64)) {
            System.out.println("Adding to cache: " + b64 + " ["+ width + " - " + height+"]");
            unconvertedCollector.put(b64, new UCBuff(width, height, dym));
        }
    }

    public void tickConverter(){
        if(this.placeholder == null){
            try{
                this.placeholder = new BufferedTexture(ImageIO.read(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("pixel/gui/cache.png"))));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for (String key : Lists.newArrayList(this.unconvertedCollector.keySet())) {
            if(!this.imageCollector.containsKey(key)){
                this.imageCollector.put(key, this.unconvertedCollector.get(key).getConverted());
                this.unconvertedCollector.remove(key);
            }
        }
    }

    public static ImageCache getInstance(){
        return instance == null ? instance = new ImageCache() : instance;
    }

    public class UCBuff{
        private int width;
        private int height;
        private BufferedImage bufferedImage;

        public UCBuff(int width, int height, BufferedImage bufferedImage) {
            this.width = width;
            this.height = height;
            this.bufferedImage = bufferedImage;
        }

        public UCBuff(BufferedImage bufferedImage) {
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            this.bufferedImage = bufferedImage;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public CDyn getConverted(){
            return new CDyn(width, height, bufferedImage);
        }
    }

    public static class CDyn{
        private int width;
        private int height;
        private BufferedImage buf;
        private BufferedTexture texture;
        private ResourceLocation res;

        public CDyn(int width, int height, BufferedImage bufferedImage) {
            this.width = width;
            this.height = height;
            this.buf = bufferedImage;
            this.texture = new BufferedTexture(bufferedImage);
            this.res = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("", this.texture);
        }

        public CDyn(BufferedTexture tex) {
            this.width = tex.width;
            this.height = tex.height;
            this.texture = tex;
        }

        public CDyn(BufferedTexture tex, int width, int height) {
            this.width = width;
            this.height = height;
            this.texture = tex;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public BufferedTexture getTexture() {
            return texture;
        }

        public BufferedImage getImage() {
            return this.buf;
        }

        public ResourceLocation getLocation(){
            return this.res;
        }
    }

}
