package io.undervolt.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.function.Consumer;

public class RestUtils {

    private static RestUtils instance;
    private String baseUrl = "http://api.undervolt.io/";

    public void sendJsonRequest(String endpoint, JSONObject obj, Consumer<String> callback) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            HttpPost postMethod = new HttpPost(baseUrl + endpoint);
            postMethod.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(postMethod);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                callback.accept(responseString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJSONRequest(String endpoint, JSONObject obj, Consumer<JsonObject> callback) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            HttpPost postMethod = new HttpPost(baseUrl + endpoint);
            postMethod.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(postMethod);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                System.out.println(responseString);
                callback.accept(new Gson().fromJson(responseString, JsonObject.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJsonRequest(String endpoint, String obj, Consumer<String> callback){
        try {
            HttpClient httpclient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(obj, ContentType.APPLICATION_JSON);

            HttpPost postMethod = new HttpPost(baseUrl + endpoint);
            postMethod.setEntity(requestEntity);

            HttpResponse response = httpclient.execute(postMethod);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                callback.accept(responseString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RestUtils getInstance(){
        return instance == null ? instance = new RestUtils() : instance;
    }
}
