package com.unist.hexa.chickenq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.BoardData;

import java.net.URL;

/**
 * Created by user on 2015-08-11.
 */
public class BoardWriteActivity extends Activity {

    private static final String TAG = "BoardWriteActivity";
    private static final String WriteUrl = "http://chickenq.hexa.pro/board/write.php?";
    EditText TitleEdt, ContentEdt;
    String TitleStr, ContentStr, MenuStr, PeopleStr, PlaceStr, start_time;
    int _id, user_id=10032, MenuNum, PeopleNum, PlaceNum, duration=100;
    Button MenuBtn, PeopleBtn, PlaceBtn, UploadBtn;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        TitleEdt = (EditText) findViewById(R.id.TitleEditText);
        ContentEdt = (EditText) findViewById(R.id.ContentEdit);

        MenuBtn = (Button)findViewById(R.id.MenuButton);
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialogSelectOption();
            }
        });


        PeopleBtn = (Button)findViewById(R.id.PeopleButton);
        PeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeopleDialogSelectOption();
            }
        });

        PlaceBtn = (Button)findViewById(R.id.PlaceButton);
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

        BoardData boardData = new BoardData(_id, user_id, TitleStr, ContentStr, start_time, MenuNum, PeopleNum, PlaceNum, duration);
        intent.putExtra("boardData", boardData);

        try{
            URL url = new URL("http://chickenq.hexa.pro/board/write.php?id=" + user_id + "&title=" + TitleStr + "&cont=" + ContentStr + "&dur=" + duration + "&menu=" + MenuNum + "&limit=" + PeopleNum + "&locat=" + PlaceNum);

            Log.d("url : ", "http://chickenq.hexa.pro/board/write.php?id=" + user_id + "&title=" + TitleStr + "&cont=" + ContentStr + "&dur=" + duration + "&menu=" + MenuNum + "&limit=" + PeopleNum + "&locat=" + PlaceNum);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            url.openStream();

            finish();
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "오류!", Toast.LENGTH_LONG).show();
        }

    }
}
