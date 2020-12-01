package io.undervolt.api.screenshot;

import com.google.gson.JsonObject;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.MultipartUtility;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/** WON'T WORK */
//TODO: Fix

public class ScreenshotUploader {
    private final Chocomint chocomint;
    private JsonObject lastUploadData;

    public ScreenshotUploader(final Chocomint chocomint) {
        this.chocomint = chocomint;
    }

    public boolean upload(BufferedImage image) {

        System.out.println("Trying to upload screenshot");

        HttpURLConnection conn = null;
        ByteArrayOutputStream baos = null;
        OutputStreamWriter wr = null;
        BufferedReader in = null;

        try {
            URL url = new URL("http://f.mscpn.com/upload");

            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();

            String encoded = Base64.encodeBase64String(imageInByte);
            String data = "screenshot = " + encoded;

            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data");

            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            this.chocomint.getNotificationManager().addNotification((new Notification(Notification.Priority.NOTICE, "Uploaded screenshot", "asd")));

            return true;
        } catch (Exception e) {
            this.chocomint.getNotificationManager().addNotification(new Notification(Notification.Priority.CRITICAL, "Failed to upload screenshot", e.getMessage()));
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.close(conn);
            IOUtils.closeQuietly(wr);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(baos);
        }
    }

    public void uploadScreenshot(final BufferedImage screenshot) throws IOException {
        MultipartUtility multipart = new MultipartUtility("http://f.mscpn.com/upload", "UTF-8");
        multipart.addFilePart("screenshot", "screenshot.png", screenshot);

        List<String> response = multipart.finish();
        System.out.println("Respuesta:");
        for (String line : response) {
            System.out.println("Upload Files Response:::" + line);
        }
    }

}
