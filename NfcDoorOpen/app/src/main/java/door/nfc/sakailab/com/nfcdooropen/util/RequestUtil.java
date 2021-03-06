package door.nfc.sakailab.com.nfcdooropen.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class RequestUtil {

    public static JSONObject requestGet(String reqAPI,
                                        JSONObject reqValues) throws JSONException {
        JSONObject resData = null;
        ArrayList<NameValuePair> params = getParamValues(reqValues);
        String query = URLEncodedUtils.format(params, "utf-8");
        HttpGet requestGet = new HttpGet(reqAPI + "?" + query);
        resData = request(requestGet);
        return resData;
    }

    public static JSONObject requestPost(String reqAPI, JSONObject reqValues) {
        JSONObject resData = null;
        HttpPost requestPost = new HttpPost(reqAPI);
        try {
            StringEntity se = new StringEntity(reqValues.toString());
            requestPost.setEntity(se);
            requestPost.setHeader("Accept", "application/json");
            requestPost.setHeader("Content-type", "application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resData = request(requestPost);
        return resData;
    }

    private static ArrayList<NameValuePair> getParamValues(
            JSONObject reqValues) throws JSONException {
        ArrayList<NameValuePair> params = new ArrayList<>();
        Iterator<String> iter = reqValues.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            params.add(new BasicNameValuePair(key, reqValues.getString(key)));
        }

        return params;
    }

    private static String getResponceJSONString(HttpResponse result) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            result.getEntity().writeTo(outputStream);
            String resData = outputStream.toString(); // JSONデータ
            return resData;
        } catch (IOException e) {
        }
        return null;
    }

    private static JSONObject request(HttpUriRequest request) {

        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse res = null;
            res = httpClient.execute(request);
            int resultCode = res.getStatusLine().getStatusCode();
            switch (resultCode) {
                case HttpStatus.SC_OK:
                    JSONObject data = new JSONObject(getResponceJSONString(res));
                    return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}