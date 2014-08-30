package org.moriano.vaadinREST.util;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.moriano.vaadinREST.model.FullHttpResponse;
import org.moriano.vaadinREST.util.HttpMethod;

import java.io.IOException;
import java.util.Map;

/**
 * Simple class used to facilitate requests and separate the logic from the UI.
 */
public class HttpRequester {

    private final HttpClient httpClient = HttpClients.createDefault();

    public FullHttpResponse call(String url, String method, Map<String, String> parameters) {

        StringBuilder responseRaw = new StringBuilder();
        FullHttpResponse result = null;
        if(method.equals(HttpMethod.GET.getMethod())) {
            HttpGet get;
            HttpResponse response;

            try {
                get = new HttpGet(url);
                response = this.httpClient.execute(get);
                result = new FullHttpResponse(response, get);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return result;

    }

}
