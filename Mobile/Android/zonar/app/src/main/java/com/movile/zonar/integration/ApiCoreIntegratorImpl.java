package com.movile.zonar.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by marortiz2 on 6/15/16.
 */
public class ApiCoreIntegratorImpl implements ApiCoreIntegrator {
    private static final String URL_GET_PARAMETER_SEPARATOR = "/";
    private static final String GET = "GET";
    private static final String USER_AGENT = "User-Agent";

    public String get(String urlRequest, Map<String, String> parameters) {


        StringBuffer chaine = new StringBuffer("");
        try {
            URL url = new URL(parameters.size() > 0 ? buildUrlParameters(urlRequest, parameters) : urlRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(USER_AGENT, "");
            connection.setRequestMethod(GET);
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (Exception e) {
            // writing exception to log
            e.printStackTrace();
        }

        return chaine.toString();
    }


    private String buildUrlParameters(String url, Map<String, String> parameters) {

        StringBuilder parametersSb = new StringBuilder(url);
        parametersSb.append(URL_GET_PARAMETER_SEPARATOR);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            parametersSb.append(entry.getKey());
            parametersSb.append(URL_GET_PARAMETER_SEPARATOR);
            parametersSb.append(entry.getValue());
        }

        return parametersSb.toString();
    }
}
