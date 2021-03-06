package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2015-08-06.
 */
public class SplashActivity extends Activity {
    private static final String CHECK_URL = "http://chickenq.hexa.pro/user/check.php";
    private static final int LOADING_TIME = 3000;

    private SharedPreferences loginPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginPreferences = getSharedPreferences("setting_login", MODE_PRIVATE);

        new Handler().postDelayed(afterLoading, LOADING_TIME);
    }

    Runnable afterLoading = new Runnable() {
        @Override
        public void run() {
            if (loginPreferences.getBoolean("auto_login", false)) {
                final String id = loginPreferences.getString("id", "");
                final String key = loginPreferences.getString("key", "");
                Log.d("SplashActivity", id + key);

                AsyncJsonParser parser = new AsyncJsonParser(mOnPostParseListener, CHECK_URL);
                parser.addGetParam("id", id);
                parser.addGetParam("key", key);
                parser.execute();
            } else {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        }
    };

    AsyncJsonParser.OnPostParseListener mOnPostParseListener = new AsyncJsonParser.OnPostParseListener() {
        @Override
        public void onPostParse(JSONObject jObj, int what) throws JSONException {
            if (jObj == null || !jObj.getString("result").equals("true")) {
                Toast.makeText(getApplicationContext(), "자동 로그인에 실패하였습니다. 다시 로그인해주세요", Toast.LENGTH_LONG).show();

                // Clear SharedPreference
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.remove("auto_login");
                editor.remove("portal_id");
                editor.remove("key");
                editor.apply();

                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
            }
            finish();
        }
    };
}
