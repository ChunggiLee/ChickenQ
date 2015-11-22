package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignInActivity extends Activity implements View.OnClickListener {
    private final static String CHECK_URL = "http://chickenq.hexa.pro/user/check.php";

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
            final String portal_id = etID.getText().toString();
            final String portal_pw = etPW.getText().toString();

            if (portal_id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (portal_pw.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                AsyncJsonParser parser = new AsyncJsonParser(new LoginHandler(), CHECK_URL);
                parser.addGetParam("portal_id", portal_id);
                parser.addGetParam("key", getMD5(portal_pw));
                parser.execute();
            }
        }
    }

    private class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jObj = (JSONObject) msg.obj;
            String result = "";
            try {
                result = jObj.getString("result");
            } catch (JSONException e) {
            }

            if (msg.what == -1) {
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_LONG).show();
            } else if (result.compareTo("true") != 0) {
                Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 잘못되었습니다", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences.Editor editor = loginPreferences.edit();
                editor.putString("portal_id", etID.getText().toString());
                editor.putString("key", getMD5(etPW.getText().toString()));
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
}
