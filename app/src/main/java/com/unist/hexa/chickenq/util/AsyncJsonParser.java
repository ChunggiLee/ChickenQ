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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * JSON Parser using HttpClient
 */
public class AsyncJsonParser extends AsyncTask<Void, Void, JSONArray> {
    private static final String TAG = "AsyncJsonParser";

    Handler resultHandler;
    String sendUrl;
    Exception error;
    public int what = 0;

    public AsyncJsonParser(Handler handler, String url, String sql) {
        init(handler, url, sql);
    }

    public AsyncJsonParser(Handler handler, String url, String sql, int w) {
        init(handler, url, sql);
        what = w;
    }

    private void init(Handler handler, String url, String sql) {
        resultHandler = handler;
        try {
            String sql_urlencode = URLEncoder.encode(sql, "utf-8");
            sendUrl = String.format("%s?sql=%s", url, sql_urlencode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        HttpJsonParser jParser = new HttpJsonParser();
        error = null;
        try {
            return jParser.getJSONFromUrl(sendUrl);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        if (resultHandler != null) {
            if (jsonArray == null) {
                Message msg = resultHandler.obtainMessage(-1);
                msg.obj = error.getMessage();
                resultHandler.sendMessage(msg);
            } else {
                Message msg = resultHandler.obtainMessage(what);
                msg.obj = jsonArray;
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

        public JSONArray getJSONFromUrl(String url) throws IOException, JSONException, RuntimeException {
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
                JSONArray jArr = new JSONArray(json);
                if (jArr.length() == 0)
                    throw new RuntimeException(ERROR_NO_RESULT);
                return jArr;

            } catch (IOException e) {
                throw new IOException(ERROR_BUFFER_CONVERT);
            }
        }

    }
}