package com.unist.hexa.chickenq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardData;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2015-08-11.
 */
public class BoardWriteActivity extends Activity {

    private static final String TAG = "BoardWriteActivity";
    private static final String WriteUrl = "http://chickenq.hexa.pro/board/write.php?";
    EditText TitleEdt, ContentEdt;
    String TitleStr, ContentStr, MenuStr, PeopleStr, PlaceStr, start_time;
    int _id, user_id, MenuNum, PeopleNum, PlaceNum, duration, id1;
    ImageButton MenuBtn, PeopleBtn, PlaceBtn;
    Button UploadBtn;
    BoardData boardData;
    Window win;
    LinearLayout linear;
    ArrayList<String> TimeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = getWindow();
        win.setContentView(R.layout.activity_board_write);

        TitleEdt = (EditText) findViewById(R.id.TitleEditText);
        ContentEdt = (EditText) findViewById(R.id.ContentEdit);

        MenuBtn = (ImageButton)findViewById(R.id.MenuButton);
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialogSelectOption();
            }
        });


        PeopleBtn = (ImageButton)findViewById(R.id.PeopleButton);
        PeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeopleDialogSelectOption();
            }
        });

        PlaceBtn = (ImageButton)findViewById(R.id.PlaceButton);
        PlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceDialogSelectOption();
            }
        });

        UploadBtn = (Button) findViewById(R.id.UploadButton);
        UploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
            }
        });

        ImageButton TimeBtn = (ImageButton) findViewById(R.id.TimeBtn);
        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                linear = (LinearLayout)inflater.inflate(R.layout.layout_popup_time, null);

                LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                win.addContentView(linear, paramlinear);

                setArrayList();

                Button ok = (Button) findViewById(R.id.OkButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                        TextView durationTxt = (TextView) findViewById(R.id.durationTxt);
                        durationTxt.setText("대기시간: " + duration + "분");
                    }
                });

            }
        });
        duration=5;
    }

    private void MenuDialogSelectOption() {
        final String items[] = { "치킨", "피자", "짜장면", "탕수육", "패스트푸드"};
        MenuStr = items[0];
        AlertDialog.Builder ab = new AlertDialog.Builder(BoardWriteActivity.this);
        ab.setTitle("메뉴 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        MenuNum = whichButton;
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), items[MenuNum] + "선택되었습니다.",Toast.LENGTH_SHORT).show();
                        final TextView MenuTv = (TextView) findViewById(R.id.MenuTextView);
                        MenuStr = items[MenuNum];
                        MenuTv.setText(MenuStr);

                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        //Toast.makeText(getApplicationContext(), items[whichButton] + "선택되었습니다.",Toast.LENGTH_SHORT).show();
                        //final TextView MenuTv = (TextView) findViewById(R.id.MenuTextView);
                        //MenuTv.setText(MenuStr);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                });
        ab.show();
    }

    private void PeopleDialogSelectOption() {
        final String items[] = { "1명", "2명", "3명", "4명", "5명", "6명 이상"};
        AlertDialog.Builder ab = new AlertDialog.Builder(BoardWriteActivity.this);
        ab.setTitle("인원 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        PeopleNum = whichButton;
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        Toast.makeText(getApplicationContext(), items[PeopleNum] + "선택되었습니다.",Toast.LENGTH_SHORT).show();
                        final TextView PeopleTv = (TextView) findViewById(R.id.PeopleTextView);
                        PeopleStr = items[PeopleNum];
                        PeopleTv.setText(PeopleStr);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                });
        ab.show();
    }

    private void PlaceDialogSelectOption() {
        final String items[] = { "공학관", "경영관", "학생회관", "기숙사", "기숙사 휴게실"};
        AlertDialog.Builder ab = new AlertDialog.Builder(BoardWriteActivity.this);
        ab.setTitle("장소 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        PlaceNum = whichButton;
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        Toast.makeText(getApplicationContext(), items[PlaceNum] + "선택되었습니다.", Toast.LENGTH_SHORT).show();
                        final TextView PlaceTv = (TextView) findViewById(R.id.PlaceTextView);
                        PlaceStr = items[PlaceNum];
                        PlaceTv.setText(PlaceStr);

                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                });
        ab.show();
    }

    public void Upload() {
        TitleStr = TitleEdt.getText().toString();
        ContentStr = ContentEdt.getText().toString();
        Intent intent = new Intent(BoardWriteActivity.this, BoardViewActivity.class);


        // 시스템으로부터 현재시간(ms) 가져오기
        long now = System.currentTimeMillis();
        // Data 객체에 시간을 저장한다.
        Date date = new Date(now);
        // 각자 사용할 포맷을 정하고 문자열로 만든다.
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        start_time =sdfNow.format(date);
        boardData = new BoardData(_id, user_id, TitleStr, ContentStr, start_time, MenuNum, PeopleNum, PlaceNum, duration, "test", false);
        intent.putExtra("boardData", boardData);

        //Log.d("BoardWriteActivity", id1+"");
        try{
            id1 = getSharedPreferences("setting_login", 0).getInt("uid", 0);

            String writeUrl = "http://chickenq.hexa.pro/board/write.php";
            AsyncJsonParser parser = new AsyncJsonParser(mOnPostParseListener, writeUrl);
            parser.addGetParam("id", id1);
            parser.addGetParam("title", TitleStr);
            parser.addGetParam("cont", ContentStr);
            parser.addGetParam("dur", duration);
            parser.addGetParam("menu", MenuNum);
            parser.addGetParam("limit", PeopleNum);
            parser.addGetParam("locat", PlaceNum);
            parser.execute();

            finish();
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "오류!", Toast.LENGTH_LONG).show();
        }

    }

    public void setArrayList() {

        final String TimeItems[] = { "5", "10", "15", "20", "25", "30", "40" , "50", "60", "70", "80", "90", "100", "110", "120"};

        // 리스트 항목 추가
        TimeList = new ArrayList<String>();
        TimeList.add(TimeItems[0]);
        TimeList.add(TimeItems[1]);
        TimeList.add(TimeItems[2]);
        TimeList.add(TimeItems[3]);
        TimeList.add(TimeItems[4]);
        TimeList.add(TimeItems[5]);
        TimeList.add(TimeItems[6]);
        TimeList.add(TimeItems[7]);
        TimeList.add(TimeItems[8]);
        TimeList.add(TimeItems[9]);
        TimeList.add(TimeItems[10]);
        TimeList.add(TimeItems[11]);
        TimeList.add(TimeItems[12]);
        TimeList.add(TimeItems[13]);
        TimeList.add(TimeItems[14]);






        ArrayAdapter<String> TimeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, TimeList);


        //스피너 속성
        Spinner TimeSp = (Spinner) this.findViewById(R.id.TimeSp);
        TimeSp.setPrompt("대기시간을 선택하세요."); // 스피너 제목
        TimeSp.setAdapter(TimeAdapter);
        TimeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                duration = Integer.parseInt(TimeItems[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    AsyncJsonParser.OnPostParseListener mOnPostParseListener = new AsyncJsonParser.OnPostParseListener() {
        @Override
        public void onPostParse(JSONObject jObj, int what) throws JSONException {

        }
    };

}
