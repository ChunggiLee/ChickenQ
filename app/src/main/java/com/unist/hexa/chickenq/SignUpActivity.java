package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.EmailClient;


public class SignUpActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SignUpActivty";
    private static final String SENDER_EMAIL = "asdzxc6442@gmail.com"; // TODO: create chickenq email
    private static final String SENDER_PW = "gjafwudkhrrkaxkt";
    private static final String AUTH_EMAIL_TITLE = "ChickenQ 회원가입 인증코드입니다.";
    private static final String AUTH_EMAIL_BODY = "인증코드";

    EditText etName, etID, etPassword, etEmail, etNumber;
    CheckBox chkAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etName = (EditText) findViewById(R.id.edit_name);
        etID = (EditText) findViewById(R.id.edit_ID);
        etPassword = (EditText) findViewById(R.id.edit_Password);
        etEmail = (EditText) findViewById(R.id.edit_Email);
        etNumber = (EditText) findViewById(R.id.edit_Number);
        chkAgree = (CheckBox) findViewById(R.id.chb_Agree);

        findViewById(R.id.btn_identification).setOnClickListener(this);
        findViewById(R.id.btn_registor).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_identification:
                // TODO: Send Information
                String email = etEmail.getText().toString();
                EmailClient emailClient = new EmailClient(SENDER_EMAIL, SENDER_PW);
                emailClient.sendMail(AUTH_EMAIL_TITLE, AUTH_EMAIL_BODY, SENDER_EMAIL, email);
                break;

            case R.id.btn_registor:
                // TODO: Toast position
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
