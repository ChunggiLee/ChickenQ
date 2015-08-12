package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by user on 2015-08-06.
 */
public class SearchActivity extends Activity implements View.OnClickListener {

    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        et_search = (EditText) findViewById(R.id.SearchEditText);

        findViewById(R.id.SearchButton).setOnClickListener(this);
        findViewById(R.id.NewButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SearchButton:
                String search_str = et_search.getText().toString(); // TODO: Search progress
                startActivity(new Intent(SearchActivity.this, PartyListActivity.class));
                break;
            case R.id.NewButton:
                startActivity(new Intent(SearchActivity.this, WriteActivity.class));
                break;
        }
    }
}
