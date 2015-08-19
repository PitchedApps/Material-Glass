package com.pitchedapps.material.glass.free.utilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class JSONParser {
//TODO add support for httpclient and httpcore (tried adding jars but it didn't work)
    public static JSONObject getJSONfromURL(String url) {
        try {
            HttpClient cl = new DefaultHttpClient();
            HttpResponse response = cl.execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == 200) {
                final String data = EntityUtils.toString(response.getEntity());
                return new JSONObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
