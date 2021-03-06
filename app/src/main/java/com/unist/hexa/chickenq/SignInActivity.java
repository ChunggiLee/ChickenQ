package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignInActivity extends Activity implements View.OnClickListener {
    private final static String CHECK_URL = "http://chickenq.hexa.pro/user/check.php";
    private String ID_URL = "http://chickenq.hexa.pro/user/getfpid.php?pid=";

    EditText etID, etPW;
    CheckBox ckbAutoLogin;
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etID = (EditText) findViewById(R.id.IDEditText);
        etPW = (EditText) findViewById(R.id.PasswordEditText);
        ckbAutoLogin = (CheckBox) findViewById(R.id.autoLoginCbx);
        loginPreferences = getSharedPreferences("setting_login", 0);

        findViewById(R.id.joinBtn).setOnClickListener(this);
        findViewById(R.id.checkBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.joinBtn) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.checkBtn) {
            final String id = etID.getText().toString();
            final String pw = etPW.getText().toString();

            if (id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (pw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                AsyncJsonParser parser = new AsyncJsonParser(mOnPostParseListener, CHECK_URL);
                parser.addGetParam("id", id);
                parser.addGetParam("key", pw);
                parser.execute();
            }
        }
    }

    AsyncJsonParser.OnPostParseListener mOnPostParseListener = new AsyncJsonParser.OnPostParseListener() {
        @Override
        public void onPostParse(JSONObject jObj, int what) throws JSONException {
            if (jObj == null) {
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_LONG).show();
            } else if (jObj.getString("result").compareTo("true") != 0) {
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 잘못되었습니다", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putString("id", etID.getText().toString());
                editor.putInt("uid", jObj.getInt("uid"));
                editor.putString("key", etPW.getText().toString());
                ID_URL += etID.getText().toString();
                urlReadFunc();
                if (ckbAutoLogin.isChecked())
                    editor.putBoolean("auto_login", true);
                editor.apply();

                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                finish();
            }
        }
    };

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void urlReadFunc() {
        try{
            URL url = new URL(ID_URL);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            readStream(conn.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readStream(InputStream in) {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putInt("id", json.getInt("id"));
                editor.apply();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) reader.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
