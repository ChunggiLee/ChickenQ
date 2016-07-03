package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.EmailClient;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SignUpActivty";
    private static final String CHECK_URL = "http://chickenq.hexa.pro/user/checkid.php";
    private static final String SENDER_EMAIL = "asdzxc6442@gmail.com"; // TODO: create chickenq email
    private static final String SENDER_PW = "gjafwudkhrrkaxkt";
    private static final String AUTH_EMAIL_TITLE = "ChickenQ 회원가입 인증코드입니다.";
    private static final String AUTH_EMAIL_BODY_START = "인증코드는 ";
    private static final String AUTH_EMAIL_BODY_END = " 입니다";
    private int NUMBER;
    private String code;
    int check = 0;
    boolean checkid, checknum, checkpwd = false;
    EditText etID, etPassword, etEmail, etNumber, etPasswordRe;
    TextView txt;
    CheckBox chkAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etID = (EditText) findViewById(R.id.edit_ID);
        etPassword = (EditText) findViewById(R.id.edit_Password);
        etPasswordRe = (EditText) findViewById(R.id.edit_PasswordRe);
        etEmail = (EditText) findViewById(R.id.edit_Email);
        etNumber = (EditText) findViewById(R.id.edit_Number);
        chkAgree = (CheckBox) findViewById(R.id.chb_Agree);
        txt = (TextView) findViewById(R.id.txt);

        findViewById(R.id.btn_checkid).setOnClickListener(this);
        findViewById(R.id.btn_checknum).setOnClickListener(this);
        findViewById(R.id.btn_identification).setOnClickListener(this);
        findViewById(R.id.btn_registor).setOnClickListener(this);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().toString().equals(etPasswordRe.getText().toString())) {
                    txt.setText("같음");
                    checkpwd = true;
                } else {
                    txt.setText("다름");
                    checkpwd = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
        etPasswordRe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPassword.getText().toString().equals(etPasswordRe.getText().toString())){
                    txt.setText("같음");  // TODO : CHANGE TEXT COLOR
                    checkpwd = true;
                }else{
                    txt.setText("다름");
                    checkpwd = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    AsyncJsonParser.OnPostParseListener mOnPostParseListener = new AsyncJsonParser.OnPostParseListener() {
        @Override
        public void onPostParse(JSONObject jObj, int what) throws JSONException {
            if (jObj == null) {
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_LONG).show();
            } else if (jObj.getInt("check") == 0) {
                Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다", Toast.LENGTH_LONG).show();
                //checkid = true;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkid:
                AsyncJsonParser parser = new AsyncJsonParser(mOnPostParseListener, CHECK_URL);
                parser.addGetParam("id", etID.getText().toString());
                parser.execute();
                // TODO : JSON Parser
                break;
            case R.id.btn_identification:
                // TODO: Send Information
                final String email = etEmail.getText().toString();
                if (!email.endsWith("@unist.ac.kr")){
                    Toast.makeText(getApplicationContext(), "UNIST 메일을 이용해 주십시오", Toast.LENGTH_SHORT).show();
                    break;
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        EmailClient emailClient = new EmailClient(SENDER_EMAIL, SENDER_PW);
                        code = "";
                        for (int i = 0; i < 4; i++) code += ((int) (Math.random() * 10));
                        NUMBER = Integer.parseInt(code);
                        emailClient.sendMail(AUTH_EMAIL_TITLE, AUTH_EMAIL_BODY_START + code + AUTH_EMAIL_BODY_END, SENDER_EMAIL, email);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.btn_checknum:
                String num = etNumber.getText().toString();
                if (!num.equals(code)) {
                    Toast.makeText(this, "인증번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "인증번호가 확인되었습니다", Toast.LENGTH_SHORT).show();
                    checknum = true;
                }
                break;
            case R.id.btn_registor:
                // TODO: Toast position
                if (!checkid){
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 부탁드립니다", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!checkpwd) {
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 부탁드립니다", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!checknum){
                    Toast.makeText(getApplicationContext(), "인증번호 확인을 부탁드립니다", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (chkAgree.isChecked()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.signup_finish), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.signup_need_agree), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}