package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2015-08-06.
 */
public class SplashActivity extends Activity {
    private final static int LOADING_TIME = 3000;

    private SharedPreferences loginPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginPreferences = getSharedPreferences("setting_login", MODE_PRIVATE);

        LoginHandler loginHandler = new LoginHandler();
        loginHandler.postDelayed(loginHandler.afterLoading, LOADING_TIME);
    }

    private class LoginHandler extends Handler {
        private static final String CHECK_URL = "http://chickenq.hexa.pro/user/check.php";

        Runnable afterLoading = new Runnable() {
            @Override
            public void run() {
                if (loginPreferences.getBoolean("auto_login", false)) {
                    final String portal_id = loginPreferences.getString("portal_id", "");
                    final String key = loginPreferences.getString("key", "");

                    AsyncJsonParser parser = new AsyncJsonParser(LoginHandler.this, CHECK_URL);
                    parser.addGetParam("portal_id", portal_id);
                    parser.addGetParam("key", key);
                    parser.execute();
                } else {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }
            }
        };

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            try {
                result = ((JSONObject) msg.obj).getString("result");
            } catch (JSONException e) {
            }

            if (msg.what == -1 || result.compareTo("true") != 0) {
                Toast.makeText(getApplicationContext(), "자동 로그인에 실패하였습니다. 다시 로그인해주세요", Toast.LENGTH_LONG).show();

                // Clear SharedPreference
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.remove("auto_login");
                editor.remove("portal_id");
                editor.remove("key");
                editor.apply();

                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), BoardListActivity.class));
            }
            finish();
        }
    }
}
