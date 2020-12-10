package io.undervolt.api.sambayon;

import com.google.common.collect.Maps;
import io.undervolt.instance.Chocomint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Map;

public class Sambayon {
    private final Chocomint chocomint;
    private final String SAMBAYON_ENDPOINT = "http://sambayon.undervolt.io/";
    private final Map<String, String> serverPool = Maps.newHashMap();

    public Sambayon(final Chocomint chocomint) {
        this.chocomint = chocomint;
    }

    public String getServer(final String serverName) {
        if(this.serverPool.containsKey(serverName)) {
            System.out.println("Got server from server pool");
            return this.serverPool.get(serverName);
        } else {
            System.out.println("Getting server from Sambayón...");
            try {
                HttpClient httpclient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity("{\"client_id\": \"chocomint\", \"req\":\"" + serverName + "\"}", ContentType.APPLICATION_JSON);

                HttpPost postMethod = new HttpPost(SAMBAYON_ENDPOINT);
                postMethod.setEntity(requestEntity);

                HttpResponse response = httpclient.execute(postMethod);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    JSONObject json = new JSONObject(EntityUtils.toString(entity));
                    if(json.getBoolean("success")) {
                        this.serverPool.put(serverName, json.getString("response"));
                        return json.getString("response");
                    } else {
                        throw new SambayonException(json.getString("response"));
                    }
                }


            } catch (Exception e) {
                this.chocomint.getChatManager().getReservedLogTab().addMessage(this.chocomint.getChocomintUser(), "Failed to get server: " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }
    }
}
