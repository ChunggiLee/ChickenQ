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
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * JSON Parser using HttpClient
 */
public class AsyncJsonParser extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "AsyncJsonParser";

    public interface OnPostParseListener {
        void onPostParse(JSONObject jObj, int what) throws JSONException;
    }

    public interface OnParseErrorListener {
        void onParseError(JSONException e);
    }

    public OnPostParseListener onPostParseListener = new OnPostParseListener() {
        @Override
        public void onPostParse(JSONObject jObj, int what) {
        }
    };

    public OnParseErrorListener onParseErrorListener = new OnParseErrorListener() {
        @Override
        public void onParseError(JSONException e) {
            e.printStackTrace();
        }
    };

    private HashMap<String, String> getList, postList;

    // TODO temporary for array parameter
    private ArrayList<String> getList2;

    //    private Handler resultHandler;
    private String url;
    private Exception error;
    private int what = 0;

    public AsyncJsonParser(OnPostParseListener callback, String url) {
        onPostParseListener = callback;
        this.url = url;
        this.getList = new HashMap<>();
        this.postList = new HashMap<>();
        this.getList2 = new ArrayList<>();
    }

    public void setHandleMessage(OnPostParseListener callback) {
        onPostParseListener = callback;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void addGetParam(String key, String value) {
        getList.put(key, value);
    }

    public void addGetParam(String key, int value) {
        addGetParam(key, "" + value);
    }

    public void addGetParam(String param) {
        getList2.add(param);
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

        if (!getList2.isEmpty()) {
            if (getList.isEmpty())
                url += "?";
            for (String param : getList2) {
                url += String.format("%s&", param);
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
        try {
            onPostParseListener.onPostParse(jsonObject, what);
        } catch (JSONException e) {
            onParseErrorListener.onParseError(e);
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

    public static class ResultHandler<T> extends Handler {
        private final WeakReference<T> mActivity;

        public ResultHandler(T activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            T activity = mActivity.get();
            if(activity != null) {
                try {
                    Method m = mActivity.get().getClass().getMethod("onPostParse", Message.class);
                    m.invoke(activity, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}