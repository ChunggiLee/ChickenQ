package com.unist.hexa.chickenq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardData;
import com.unist.hexa.chickenq.util.BoardListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JM on 15. 8. 13..
 */
public class BoardFragment extends android.support.v4.app.Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String BOARD_URL = "http://chickenq.hexa.pro/board/list.php";

    FloatingActionMenu floatingActionMenu;
    public static BoardListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        getActivity().setTitle("게시판");

        setup_board(rootView);
        setup_fab(rootView);

        return rootView;
    }

    private void setup_board(View rootView) {
        ListView listView = (ListView) rootView.findViewById(R.id.lv_board);

        // Search init
        BoardListAdapter.search_menu = 0;
        BoardListAdapter.search_place = 0;
        BoardListAdapter.search_text = "";

        // For scrolling ListView
        final ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrollview);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // ListView setting
        mAdapter = new BoardListAdapter(getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        // Get Board List
        new AsyncJsonParser(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == -1) {
                    Toast.makeText(getActivity(), "네트워크 연결 오류", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jObj = (JSONObject) msg.obj;
                    try {
                        mAdapter.parseJson(jObj.getJSONArray("board"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, BOARD_URL).execute();
    }

    private void setup_fab(View rootView) {
        floatingActionMenu = (FloatingActionMenu) rootView.findViewById(R.id.menu1);
        rootView.findViewById(R.id.fab_write).setOnClickListener(this);
//        rootView.findViewById(R.id.fab_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab_write:
                startActivity(new Intent(getActivity(), BoardWriteActivity.class));
                break;
//            case R.id.fab_search:
//                startActivity(new Intent(getActivity(), BoardActivity.class));
//                break;
        }

        floatingActionMenu.close(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BoardData boardData = mAdapter.getBoardData(position);
        Intent intent = new Intent(getActivity(), BoardViewActivity.class);
        intent.putExtra("boardData", boardData);
        startActivity(intent);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BoardData boardData = mAdapter.getBoardData(position);
        boardData.additional_data_visible = !boardData.additional_data_visible;
        mAdapter.dataChange();
        return true;
    }
}
