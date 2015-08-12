package com.unist.hexa.chickenq;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardListAdapter;

import org.json.JSONArray;

/**
 * Created by JM on 15. 8. 13..
 */
public class BoardListActivity extends AppCompatActivity {

    FloatingActionMenu floatingActionMenu;

    private static final String BOARD_URL = "http://silvara.kr:8080/~silvara/mysql_to_json.php";
    BoardListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        setTitle("게시판");

        setup_board();
        setup_fab();
    }

    private void setup_board() {
        ListView listView = (ListView) findViewById(R.id.lv_board);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mAdapter = new BoardListAdapter(this);
        listView.setAdapter(mAdapter);
        new AsyncJsonParser(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == -1) {
                    Toast.makeText(getApplicationContext(), "네트워크 연결 오류", Toast.LENGTH_LONG).show();
                } else {
                    mAdapter.parseJson((JSONArray) msg.obj);
                }
            }
        }, BOARD_URL, "select * from board").execute();
    }

    private void setup_fab() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu1);
        findViewById(R.id.fab_write).setOnClickListener(onClickListener);
        findViewById(R.id.fab_search).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";
            switch(v.getId()) {
                case R.id.fab_write:
                    text = "글쓰기";
                    break;
                case R.id.fab_search:
                    text = "검색하기";
                    break;
            }

            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            floatingActionMenu.close(true);
        }
    };
}
