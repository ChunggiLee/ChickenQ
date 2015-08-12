package com.unist.hexa.chickenq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2015-08-11.
 */
public class WriteActivity extends Activity {

    String MenuStr;
    String PeopleStr;
    String PlaceStr;
    String Title;
    int number;
    int number1;
    int number2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make);

        final EditText TitleEdt = (EditText) findViewById(R.id.TitleEditText);
        Button MenuBtn = (Button)findViewById(R.id.MenuButton);
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialogSelectOption();
            }
        });


        Button PeopleBtn = (Button)findViewById(R.id.PeopleButton);
        PeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeopleDialogSelectOption();

            }
        });

        Button PlaceBtn = (Button)findViewById(R.id.PlaceButton);
        PlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceDialogSelectOption();

            }
        });

        Button UploadBtn = (Button) findViewById(R.id.UploadButton);
        UploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title = TitleEdt.getText().toString();
                Intent intent = new Intent(WriteActivity.this, PartyActivity.class);
                intent.putExtra("Title", Title);
                intent.putExtra("MenuStr", MenuStr);
                intent.putExtra("PeopleStr", PeopleStr);
                intent.putExtra("PlaceStr", PlaceStr);
                startActivity(intent);
            }
        });
    }

    private void MenuDialogSelectOption() {
        final String items[] = { "치킨", "피자", "짜장면", "탕수육", "패스트푸드"};
        MenuStr = items[0];
        AlertDialog.Builder ab = new AlertDialog.Builder(WriteActivity.this);
        ab.setTitle("메뉴 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 각 리스트를 선택했을때
                number = whichButton;
                }
            }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), items[number] + "선택되었습니다.",Toast.LENGTH_SHORT).show();
                        final TextView MenuTv = (TextView) findViewById(R.id.MenuTextView);
                        MenuStr = items[number];
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
        AlertDialog.Builder ab = new AlertDialog.Builder(WriteActivity.this);
        ab.setTitle("인원 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        number1 = whichButton;
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        Toast.makeText(getApplicationContext(), items[number1] + "선택되었습니다.",Toast.LENGTH_SHORT).show();
                        final TextView PeopleTv = (TextView) findViewById(R.id.PeopleTextView);
                        PeopleStr = items[number1];
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
        final String items[] = { "공학관", "경영관", "학생회관", "기숙사", "긱휴"};
        AlertDialog.Builder ab = new AlertDialog.Builder(WriteActivity.this);
        ab.setTitle("장소 선택");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                        number2 = whichButton;
                }
    }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        Toast.makeText(getApplicationContext(), items[number2] + "선택되었습니다.", Toast.LENGTH_SHORT).show();
                        final TextView PlaceTv = (TextView) findViewById(R.id.PlaceTextView);
                        PlaceStr = items[number2];
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
}
