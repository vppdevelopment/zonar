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

   public String get(String urlRequest, Map<String, String> parameters, Map<String, Object> headers){


        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
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
        }

        return chaine.toString();
    }
}
