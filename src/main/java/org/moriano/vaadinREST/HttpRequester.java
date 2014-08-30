package org.moriano.vaadinREST;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Simple class used to facilitate requests and separate the logic from the UI.
 */
public class HttpRequester {

    private final HttpClient httpClient = HttpClients.createDefault();

    public String call(String url, String method, Map<String, String> parameters) {

        StringBuilder responseRaw = new StringBuilder();
        String result = "";
        if(method.equals(HttpMethod.GET.getMethod())) {
            HttpGet get;
            HttpResponse response;

            try {
                get = new HttpGet(url);
                response = this.httpClient.execute(get);

                BufferedReader rd = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                while ((line = rd.readLine()) != null) {
                    responseRaw.append(line);
                }

                result = responseRaw.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = e.getMessage();
            }
        } else {
            result = "Ouch!, metod " + method + " not supported!";
        }

        return result;

    }

}
