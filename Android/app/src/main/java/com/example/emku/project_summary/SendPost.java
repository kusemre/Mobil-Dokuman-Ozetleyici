package com.example.emku.project_summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EmKu on 18.12.2017.
 */

public class SendPost {
    public static String sendPost(String s) throws Exception {
        final String USER_AGENT = "Mozilla/5.0";
        String url = "http://192.168.1.7:3000";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-type", "application/json");


        // Send post request

        con.setDoOutput(true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"utf-8"));
        bw.write(s);
        bw.flush();
        bw.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(),"utf-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
