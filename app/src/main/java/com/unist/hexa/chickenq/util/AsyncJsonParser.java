package com.unist.hexa.chickenq.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * JSON Parser using HttpClient
 */
public class AsyncJsonParser extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "AsyncJsonParser";

    private HashMap<String, String> getList, postList;

    private Handler resultHandler;
    private String url;
    private Exception error;
    private int what = 0;

    public AsyncJsonParser(Handler handler, String url) {
        this.resultHandler = handler;
        this.url = url;
        this.getList = new HashMap<>();
        this.postList = new HashMap<>();
    }

    public AsyncJsonParser(Handler handler, String url, String sql) {
        init(handler, url, sql);
    }

    private void init(Handler handler, String url, String sql) {
        resultHandler = handler;
        try {
            String sql_urlencode = URLEncoder.encode(sql, "utf-8");
            url = String.format("%s?sql=%s", url, sql_urlencode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void addGetParam(String key, String value) {
        getList.put(key, value);
    }

    public void addPostParam(String key, String value) {
        postList.put(key, value);
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        // Apply GET Parameters
        if (!getList.isEmpty()) {
            url += "?";
            Set key = getList.keySet();
            for (Object aKey : key) {
                String keyName = (String) aKey;
                String valueName = getList.get(keyName);
                try {
                    url += String.format("%s=%s&", keyName, URLEncoder.encode(valueName, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        // Apply POST Parameters
        // TODO

        // Start request
        HttpJsonParser jParser = new HttpJsonParser();
        error = null;
        try {
            return jParser.getJSONFromUrl(url);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (resultHandler != null) {
            if (jsonObject == null) {
                Message msg = resultHandler.obtainMessage(-1);
                msg.obj = error.getMessage();
                resultHandler.sendMessage(msg);
            } else {
                Message msg = resultHandler.obtainMessage(what);
                msg.obj = jsonObject;
                resultHandler.sendMessage(msg);
            }
        }
    }

    private class HttpJsonParser {
        private static final String TAG = "HttpJsonParser";
        private static final int HTTP_TIMEOUT = 3000;

        public static final String ERROR_HTTP_REQUEST = "ERROR_HTTP_REQUEST";
        public static final String ERROR_BUFFER_CONVERT = "ERROR_BUFFER_CONVERT";
        public static final String ERROR_NO_RESULT = "ERROR_NO_RESULT";

        public JSONObject getJSONFromUrl(String url) throws IOException, JSONException, RuntimeException {
            Log.d(TAG, "url: " + url);

            // Making HTTP request
            InputStream is;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                // 3 timeout configures
                HttpParams httpParams = httpClient.getParams();
                httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT);
                httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT);
                // httpParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, new Long(HTTP_TIMEOUT));

                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (IOException e) {
                throw new IOException(ERROR_HTTP_REQUEST);
            }

            // Convert Buffer to String
            String json;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d(TAG, "result: " + line);
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();

                // try parse the string to a JSON object
                JSONObject jObj = new JSONObject(json);
                if (jObj.length() == 0)
                    throw new RuntimeException(ERROR_NO_RESULT);
                return jObj;

            } catch (IOException e) {
                throw new IOException(ERROR_BUFFER_CONVERT);
            }
        }

    }
}