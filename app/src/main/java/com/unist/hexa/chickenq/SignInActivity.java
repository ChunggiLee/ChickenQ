package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final EditText etID = (EditText) findViewById(R.id.IDEditText);
        final EditText etPassword = (EditText) findViewById(R.id.PasswordEditText);

        Button checkBtn = (Button) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portal_id = etID.getText().toString();
                String portal_pw = etPassword.getText().toString();

                // TODO: Login statement
                boolean success = !(portal_id.isEmpty() || portal_pw.isEmpty());
                if (success) {
//                    Intent intent = new Intent(getApplicationContext(), BoardSearchActivity.class);
                    Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
