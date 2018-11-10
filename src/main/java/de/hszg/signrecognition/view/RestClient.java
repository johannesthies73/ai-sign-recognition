package de.hszg.signrecognition.view;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RestClient {

    String serverAdress;
    int port;
    String contextPath;

    public RestClient(String serverAdress, int port, String contextPath) {
        this.serverAdress = serverAdress;
        this.port = port;
        this.contextPath = contextPath;
    }

    public void sendData(String payload) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(this.serverAdress + ":" + this.port + this.contextPath);

        httpPost.addHeader("Content-Type", "application/json");
        HttpEntity entity = null;
        try {
            entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(entity);

        String jsonStringResponse = null;

        try {
            HttpResponse response = client.execute(httpPost);
            jsonStringResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(jsonStringResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerAdress() {
        return serverAdress;
    }

    public void setServerAdress(String serverAdress) {
        this.serverAdress = serverAdress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
