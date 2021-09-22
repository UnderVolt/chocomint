package io.undervolt.api.readers;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedTexture extends AbstractTexture {

    /** width of this icon in pixels */
    public final int width;

    /** height of this icon in pixels */
    public final int height;
    private static final String __OBFID = "CL_00001048";
    private boolean shadersInitialized;
    public final BufferedImage image;

    public BufferedTexture(BufferedImage bufferedImage)
    {
        this.shadersInitialized = false;
        this.image = bufferedImage;
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();

        System.out.println("Instanced BufferedTexture: ["+this.width+", "+this.height+"]");

        int[] dynamicTextureData = new int[bufferedImage.getWidth() * bufferedImage.getHeight() * 3];

        TextureUtil.allocateTexture(this.getGlTextureId(), bufferedImage.getWidth(), bufferedImage.getHeight());

        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), dynamicTextureData, 0, bufferedImage.getWidth());

        TextureUtil.uploadTexture(this.getGlTextureId(), dynamicTextureData, this.width, this.height);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
    }
}
