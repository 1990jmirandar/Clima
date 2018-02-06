package com.miranda.javier.miclima.ws;

import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lenovo on 2/5/2018.
 */

public class ClimaWS {
    public String getClima(Double lat, Double lon){
        try{
            String urlString="https://api.darksky.net/forecast/ea76e78f539ef7dae1879fd1a45d3628/"+lat+","+lon;
            URL url = new URL(urlString);
            HttpURLConnection http= (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");

            int responseCode=http.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        http.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }else{
                return "Respuesta del servidor con codigo: "+responseCode;
            }
        }catch (Exception e){
            return e.getMessage();
        }


    }

}
