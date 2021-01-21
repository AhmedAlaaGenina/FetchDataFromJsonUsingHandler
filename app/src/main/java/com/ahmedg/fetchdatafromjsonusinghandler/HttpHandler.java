package com.ahmedg.fetchdatafromjsonusinghandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {


    String makeServiceCallJson(String reqUrl) throws IOException {
        String response  = null;
        java.net.URL url = null;
        HttpsURLConnection httpConn;
        InputStream inputStream;

        try {
            url = new URL(reqUrl);
            httpConn = (HttpsURLConnection) url.openConnection();
            httpConn.connect();
            inputStream = httpConn.getInputStream();
            response  = convertStreamToString(inputStream);
            httpConn.disconnect();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return response ;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
