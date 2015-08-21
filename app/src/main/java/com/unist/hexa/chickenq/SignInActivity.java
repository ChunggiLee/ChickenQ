package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;


public class SignInActivity extends Activity {

    EditText etID, etPW;
    CheckBox Auto_Login;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etID = (EditText) findViewById(R.id.IDEditText);
        etPW = (EditText) findViewById(R.id.PasswordEditText);
        Auto_Login = (CheckBox) findViewById(R.id.autoLoginCbx);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        if(setting.getBoolean("Auto_Login_enabled", false)){
            etID.setText(setting.getString("ID", ""));
            etPW.setText(setting.getString("PW", ""));
            Auto_Login.setChecked(true);
        }

        Auto_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (Auto_Login.isChecked()) {
                    String ID = etID.getText().toString();
                    String PW = etPW.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }
                else {
                    editor.clear();
                    editor.commit();
                }
            }
        });

        Button joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistorActivity.class);
                startActivity(intent);
            }
        });

        ImageButton checkBtn = (ImageButton) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portal_id = etID.getText().toString();
                String portal_pw = etPW.getText().toString();

                // TODO: Login statement
                boolean success = !(portal_id.isEmpty() || portal_pw.isEmpty());
                if (success) {
                    Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
