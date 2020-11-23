package io.undervolt.gui.contributors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class ContributorsManager {

    private final Minecraft mc;

    private final LinkedHashMap<String, DynamicTexture> contributors = new LinkedHashMap<>();
    private final HashMap<String, Integer> commits = new HashMap<>();
    private String err = "";

    public final LinkedHashMap<String, DynamicTexture> getContributors() {
        return contributors;
    }

    public final HashMap<String, Integer> getCommits() {
        return commits;
    }

    public final String getErr() {
        return err;
    }

    public ContributorsManager(final Minecraft mc) {
        this.mc = mc;
        System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        fetch(1);
    }

    private void fetch(int tries) {
        try {
            String content = IOUtils.toString(URI.create("https://api.github.com/repos/undervolt/chocomint/stats/contributors"), StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            JsonArray a = parser.parse(content).getAsJsonArray();
            StreamSupport.stream(a.spliterator(), false).map(JsonElement::getAsJsonObject).filter(o -> o.has("total") &
                    o.get("total").getAsInt() > 20).sorted(Comparator.comparingLong(o -> ((JsonObject) o).get("total").getAsInt()).reversed()).forEach(o -> {
                JsonObject con = o.get("author").getAsJsonObject();

                try {
                    BufferedImage bi = ImageIO.read(new URL(con.get("avatar_url").getAsString() + "&size=64"));
                    this.mc.addScheduledTask(() -> {
                        DynamicTexture tx = new DynamicTexture(bi);

                        try {
                            tx.loadTexture(this.mc.getResourceManager());
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
