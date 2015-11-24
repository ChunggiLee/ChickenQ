package com.unist.hexa.chickenq;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.unist.hexa.chickenq.util.BoardListAdapter;

/**
 * Created by user on 2015-08-06.
 */
public class BoardActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        et_search = (EditText) findViewById(R.id.SearchEditText);
        et_search.addTextChangedListener(textWatcher);

        findViewById(R.id.SearchButton).setOnClickListener(this);
        findViewById(R.id.DetailButton).setOnClickListener(this);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new BoardFragment()).commit();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            BoardListAdapter.search_text = s.toString();
            BoardFragment.mAdapter.dataChange();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SearchButton:
                break;

            case R.id.DetailButton:
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = new BoardSearchDialogFragment();
                newFragment.show(ft, "dialog");
                break;
        }
    }
}
