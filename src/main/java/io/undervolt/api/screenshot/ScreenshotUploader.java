package io.undervolt.api.screenshot;

import com.google.gson.JsonObject;
import io.undervolt.gui.notifications.Notification;
import io.undervolt.instance.Chocomint;
import io.undervolt.utils.MultipartUtility;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

            //this.chocomint.getNotificationManager().addNotification((new Notification(Notification.Priority.NOTICE, "Uploaded screenshot", "asd", action)));

            return true;
        } catch (Exception e) {
            //this.chocomint.getNotificationManager().addNotification(new Notification(Notification.Priority.CRITICAL, "Failed to upload screenshot", e.getMessage(), action));
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.close(conn);
            IOUtils.closeQuietly(wr);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(baos);
        }
    }

    public void up(BufferedImage bufferedImage) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();

        String encoded = Base64.encodeBase64String(imageInByte);
        String boundary = "===" + System.currentTimeMillis() + "===";
        String contentType = "multipart/form-data; boundary=" + boundary;
        String data = "screenshot = " + encoded;

        HttpClient httpclient = HttpClients.createDefault();
        StringEntity requestEntity = new StringEntity(data, ContentType.create("multipart/form-data"));

        HttpPost postMethod = new HttpPost("http://f.mscpn.com/upload");
        postMethod.setEntity(requestEntity);

        HttpResponse response = httpclient.execute(postMethod);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println(responseString);
        }
    }

    public void uploadScreenshot(final File screenshot) throws IOException {
        MultipartUtility multipart = new MultipartUtility("http://f.mscpn.com/upload", "UTF-8");
        multipart.addFilePart("screenshot", screenshot);

        List<String> response = multipart.finish();
        response.forEach(line -> this.chocomint.getChatManager().getReservedLogTab().addMessage(this.chocomint.getChocomintUser(), line));
    }

}
