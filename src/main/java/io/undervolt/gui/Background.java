package io.undervolt.gui;

import io.undervolt.api.event.events.InitEvent;
import io.undervolt.api.event.handler.EventHandler;
import io.undervolt.api.event.handler.Listener;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.config.Configurable;
import io.undervolt.utils.config.ProfileLoader;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background extends Configurable implements Listener {

    public String backgroundPath = "";
    public DrawType drawType = DrawType.STRETCH;

    private final transient Chocomint chocomint;
    private transient ResourceLocation resourceLocation;

    private transient int imageWidth, imageHeight;

    public Background(final Chocomint chocomint) {
        super("Background");
        this.chocomint = chocomint;
    }

    @EventHandler public void init(InitEvent.PostInitEvent event) {
        File backgroundFile = new File(this.backgroundPath.replace("/", File.separator));
        this.setBackground(backgroundFile);
    }

    public ResourceLocation getBackground() throws IOException {
        if(this.backgroundPath.equals("")) {
            return null;
        } else {
            return this.resourceLocation;
        }
    }

    public void setBackground(File backgroundFile) {
        try {
            if(backgroundFile.canRead()) {
                final BufferedImage bufferedImage = ImageIO.read(backgroundFile);
                this.imageWidth = bufferedImage.getWidth();
                this.imageHeight = bufferedImage.getHeight();
                this.resourceLocation = this.chocomint.getMinecraft().getTextureManager().getDynamicTextureLocation(
                        RandomStringUtils.random(5, true, true), new DynamicTexture(bufferedImage)
                );
            } else {
                System.out.println("Background image could not be read (" + backgroundFile.getAbsolutePath() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum DrawType {
        STRETCH, ORIGINAL_SIZE
    }

    @Override
    public void saveConfig(ProfileLoader.Profile profile) {
        this.setBackground(new File(this.backgroundPath));
        super.saveConfig(profile);
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
