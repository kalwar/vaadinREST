package org.moriano.vaadinREST.model;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by moriano on 30/08/14.
 */
public class FullHttpResponse {

    private final HttpResponse response;
    private final HttpRequestBase request;

    public FullHttpResponse(HttpResponse response, HttpRequestBase request) {
        this.response = response;
        this.request = request;
    }

    public Map<String, String> getResponseHeaders() {
        Header[] headers = this.response.getAllHeaders();
        Map<String, String> result = new TreeMap<>();
        for(Header header : headers) {
            result.put(header.getName(), header.getValue());

        }
        return result;
    }

    public Map<String, String> getRequestHeaders() {
        Header[] headers = this.request.getAllHeaders();
        Map<String, String> result = new TreeMap<>();
        for(Header header : headers) {
            result.put(header.getName(), header.getValue());

        }
        return result;
    }

    public String getResponse() {
        StringBuilder responseRaw = new StringBuilder();
        String result = "";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                responseRaw.append(line);
            }

            result = responseRaw.toString();

        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    public int getResponseCode() {
        return this.response.getStatusLine().getStatusCode();
    }
}
