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
        final String contents = "메뉴 : " + SelectMenu(boardData.menu) + '\n'
                + "인원 : " + SelectPeople(boardData.limit_num) + '\n'
                + "장소 : " + SelectPlace(boardData.location) + '\n'
                + boardData.contents;

        TextView tv_title = (TextView) findViewById(R.id.TitleTextView);
        tv_title.setText(title);
        TextView tv_contents = (TextView) findViewById(R.id.ContentTextView);
        tv_contents.setText(contents);

    }

    public String SelectMenu(int MenuNum) {
        String[] Menu = {"치킨", "피자", "짜장면", "탕수육", "패스트푸드"};
        return Menu[MenuNum];
    }

    public String SelectPeople(int PeopleNum) {
        String[] People = {"1명", "2명", "3명", "4명", "5명", "6명 이상"};
        return People[PeopleNum];
    }

    public String SelectPlace(int PlaceNum) {
        String[] Place = {"공학관", "경영관", "학생회관", "기숙사", "기숙사 휴게실"};
        return Place[PlaceNum];
    }

}
