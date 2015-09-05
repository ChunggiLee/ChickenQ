package com.unist.hexa.chickenq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardData;
import com.unist.hexa.chickenq.util.BoardListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2015-08-06.
 */
public class BoardSearchActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "BoardSearchActivity";
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
        win = getWindow();
        win.setContentView(R.layout.activity_board_search);

        findViewById(R.id.DetailButton).setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.SearchEditText);

        findViewById(R.id.SearchButton).setOnClickListener(this);
        findViewById(R.id.DetailButton).setOnClickListener(this);
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

                LayoutInflater inflater = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                linear = (LinearLayout)inflater.inflate(R.layout.layout_popup, null);

                LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                win.addContentView(linear, paramlinear);

                setArrayList();

                Button ok = (Button) findViewById(R.id.OkButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "  : " + SelectNum[0] + "  : " + SelectNum[1] + "  : " + SelectNum[2]);
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });

                Button cancel = (Button) findViewById(R.id.CancelButton);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });

                break;
        }
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
        //asyncJsonParser.addGetParam("text", SearchStr);
        asyncJsonParser.addGetParam("text", SearchStr);
        asyncJsonParser.addGetParam("menu", Menu);
        asyncJsonParser.addGetParam("locat", Place);
        asyncJsonParser.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BoardData boardData = mAdapter.getBoardData(position);
        Intent intent = new Intent(this, BoardViewActivity.class);
        intent.putExtra("boardData", boardData);
        startActivity(intent);
    }

    public void setArrayList() {

        final String MenuItems[] = { "전체", "치킨", "피자", "짜장면", "탕수육", "패스트푸드"};
        //final String PeopleItems[] = { "전체", "1명", "2명", "3명", "4명", "5명", "6명 이상"};
        final String PlaceItems[] = { "전체", "공학관", "경영관", "학생회관", "기숙사", "기숙사 휴게실"};

        // 리스트 항목 추가
        MenuList = new ArrayList<String>();
        MenuList.add(MenuItems[0]);
        MenuList.add(MenuItems[1]);
        MenuList.add(MenuItems[2]);
        MenuList.add(MenuItems[3]);
        MenuList.add(MenuItems[4]);
        MenuList.add(MenuItems[5]);



        ArrayAdapter<String> MenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, MenuList);


        //스피너 속성
        Spinner MenuSp = (Spinner) this.findViewById(R.id.MenuSpinner);
        MenuSp.setPrompt("메뉴를 선택하세요."); // 스피너 제목
        MenuSp.setAdapter(MenuAdapter);
        MenuSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(BoardSearchActivity.this, MenuItems[arg2], Toast.LENGTH_SHORT).show();//해당목차눌렸을때
                SelectNum[0] = arg2;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        /*
        // 리스트 항목 추가
        PeopleList = new ArrayList<String>();
        PeopleList.add(PeopleItems[0]);
        PeopleList.add(PeopleItems[1]);
        PeopleList.add(PeopleItems[2]);
        PeopleList.add(PeopleItems[3]);
        PeopleList.add(PeopleItems[4]);
        PeopleList.add(PeopleItems[5]);
        PeopleList.add(PeopleItems[6]);



        ArrayAdapter<String> PeopleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, PeopleList);


        //스피너 속성
        Spinner PeopleSp = (Spinner) this.findViewById(R.id.PeopleSpinner);
        PeopleSp.setPrompt("인원을 선택하세요."); // 스피너 제목
        PeopleSp.setAdapter(PeopleAdapter);
        PeopleSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(BoardSearchActivity.this, PeopleItems[arg2], Toast.LENGTH_SHORT).show();//해당목차눌렸을때
                SelectNum[1] = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });*/

        // 리스트 항목 추가
        PlaceList = new ArrayList<String>();
        PlaceList.add(PlaceItems[0]);
        PlaceList.add(PlaceItems[1]);
        PlaceList.add(PlaceItems[2]);
        PlaceList.add(PlaceItems[3]);
        PlaceList.add(PlaceItems[4]);
        PlaceList.add(PlaceItems[5]);

        ArrayAdapter<String> PlaceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, PlaceList);


        //스피너 속성
        Spinner PlaceSp = (Spinner) this.findViewById(R.id.PlaceSpinner);
        PlaceSp.setPrompt("장소를 선택하세요"); // 스피너 제목
        PlaceSp.setAdapter(PlaceAdapter);
        PlaceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(BoardSearchActivity.this, PlaceItems[arg2], Toast.LENGTH_SHORT).show();//해당목차눌렸을때
                SelectNum[2] = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

}
