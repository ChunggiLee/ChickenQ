package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.unist.hexa.chickenq.mail.emailClient;


public class RegistorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        // TODO: Match layout

        final EditText etName = (EditText) findViewById(R.id.edit_name);
        final EditText etID = (EditText) findViewById(R.id.edit_ID);
        final EditText etPassword = (EditText) findViewById(R.id.edit_Password);
        final EditText etEmail = (EditText) findViewById(R.id.edit_Email);
        final EditText etNumber = (EditText) findViewById(R.id.edit_Number);

        // TODO: Send Infomation

        Button identificationBtn = (Button) findViewById(R.id.btn_identification);
        identificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmail.getText().toString();

                try
                {
                    String subject = "ChickenQ 회원가입 인증코드입니다.";
                    String body = "인증코드";
                    emailClient email = new emailClient("asdzxc6442@gmail.com",
                            "gjafwudkhrrkaxkt");
                    email.sendMail(subject, body, "asdzxc6442@gmail.com",
                            Email);
                } catch (Exception e)
                {
                    Log.d("Chung", "Error");
                    Log.d("Chung", e.toString());
                    Log.d("Chung", e.getMessage());
                }
            }
        });


        // TODO: Toast position
        Button registorBtn = (Button) findViewById(R.id.btn_registor);
        registorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox Option = (CheckBox) findViewById(R.id.chb_Agree);
                if (Option.isChecked()) {
                    Toast.makeText(getApplicationContext(), "회원가입을 축하드립니다.", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(getApplicationContext(), "개인정보 제공에 동의를 해주세요.", Toast.LENGTH_SHORT);
                }
            }
        });

    }
}
