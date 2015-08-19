package com.unist.hexa.chickenq;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.unist.hexa.chickenq.util.BoardData;

/**
 * Created by user on 2015-08-11.
 */
public class BoardViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);

        Bundle bundle = getIntent().getExtras();
        BoardData boardData = bundle.getParcelable("boardData");
        final String title = boardData.title;
        final String contents = boardData.contents;

        TextView tv_title = (TextView) findViewById(R.id.TitleTextView);
        tv_title.setText(title);
        TextView tv_contents = (TextView) findViewById(R.id.ContentTextView);
        tv_contents.setText(contents);

    }
}
