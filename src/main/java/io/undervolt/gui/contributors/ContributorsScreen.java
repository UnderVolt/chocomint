package io.undervolt.gui.contributors;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import io.undervolt.instance.Chocomint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class ContributorsScreen extends GuiScreen {

    /** Initialize Chocomint */
    private final Chocomint chocomint;

    /** Previous screen */
    private final GuiScreen prev;

    /** Define contributors */
    private static LinkedHashMap<String, DynamicTexture> contributors = new LinkedHashMap<>();
    private static HashMap<String, Integer> commits = new HashMap<>();
    private static String err = "";

    static {
        System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        fetch(1);
    }

    public ContributorsScreen(final Chocomint chocomint, final GuiScreen prev) {
        this.chocomint = chocomint;
        this.prev = prev;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, new Color(138, 102, 102).getRGB());

        GlStateManager.scale(2f, 2f, 2f);
        drawCenteredString(this.fontRendererObj, err.isEmpty() ? (contributors.isEmpty() ? "Loading contributors..." : "Contributors") : "Error: " + err, width / 4, 10 / 2, -1);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);

        int x = width / 2 - 100;
        AtomicInteger y = new AtomicInteger(70);
        contributors.forEach((contributorStatistic, textureId) -> {
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.bindTexture(textureId.getGlTextureId());

            drawScaledCustomSizeModalRect(x, y.get(), 0, 0, 20, 20, 20, 20, 20, 20);
            drawString(this.fontRendererObj, contributorStatistic, x + 25, y.get(), 0xE0E0E0);
            drawString(this.fontRendererObj, commits.get(contributorStatistic) + " commits", x + 25, y.get() + 10, 0x757575);

            y.addAndGet(25);
        });
    }

    private static void fetch(int tries) {
        try {
            String content = IOUtils.toString(URI.create("https://api.github.com/repos/undervolt/chocomint/stats/contributors"), StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            JsonArray a = parser.parse(content).getAsJsonArray();
            StreamSupport.stream(a.spliterator(), false).map(JsonElement::getAsJsonObject).filter(o -> o.has("total") &
                    o.get("total").getAsInt() > 20).sorted(Comparator.comparingLong(o -> ((JsonObject) o).get("total").getAsInt()).reversed()).forEach(o -> {
                JsonObject con = o.get("author").getAsJsonObject();

                try {
                    BufferedImage bi = ImageIO.read(new URL(con.get("avatar_url").getAsString() + "&size=64"));
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        DynamicTexture tx = new DynamicTexture(bi);

                        try {
                            tx.loadTexture(Minecraft.getMinecraft().getResourceManager());
                            contributors.put(con.get("login").getAsString(), tx);
                            commits.put(con.get("login").getAsString(), o.get("total").getAsInt());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).get();
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (tries < 5) fetch(tries + 1);
            else err = e.getMessage();
        }
    }

}
