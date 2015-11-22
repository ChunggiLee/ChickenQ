package com.unist.hexa.chickenq;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardData;
import com.unist.hexa.chickenq.util.BoardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2015-08-06.
 */
public class BoardActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {
    private static final String TAG = "BoardActivity";
    private static final String BOARD_URL = "http://chickenq.hexa.pro/board/search.php";

    BoardListAdapter mAdapter;
    ArrayList<String> MenuList, PlaceList;
    String SearchStr;
    LinearLayout linear;
    int SelectNum[] = {0, 0, 0};
    String Menu, Place;
    EditText et_search;
    Window win;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        et_search = (EditText) findViewById(R.id.SearchEditText);

        findViewById(R.id.SearchButton).setOnClickListener(this);
        findViewById(R.id.DetailButton).setOnClickListener(this);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new BoardFragment()).commit();
    }

    private void setup_board() {
        ListView listView = (ListView) findViewById(R.id.lv_board);

        // For scrolling ListView
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // ListView setting
        mAdapter = new BoardListAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        SearchStr = et_search.getText().toString();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == -1) {
                    Toast.makeText(getApplicationContext(), "네트워크 연결 오류", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jObj = (JSONObject) msg.obj;
                    try {
                        mAdapter.parseJson(jObj.getJSONArray("board"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        Menu = Integer.toString(SelectNum[0]);
        Place = Integer.toString(SelectNum[2]);

        Log.d("result", "Menu : " + Menu + " Place : " + Place + " title : " + SearchStr);

        AsyncJsonParser asyncJsonParser = new AsyncJsonParser(handler, BOARD_URL);
        asyncJsonParser.addGetParam("text", SearchStr);
        asyncJsonParser.addGetParam("menu", Menu);
        asyncJsonParser.addGetParam("locat", Place);
        asyncJsonParser.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SearchButton:
                SearchStr = et_search.getText().toString(); // TODO: Search progress
                Log.d(TAG, SearchStr);
                setup_board();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BoardData boardData = mAdapter.getBoardData(position);
        Intent intent = new Intent(this, BoardViewActivity.class);
        intent.putExtra("boardData", boardData);
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mAdapter.dataChange();
    }
}
