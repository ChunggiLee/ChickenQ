package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by user on 2015-08-11.
 */
public class PartyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party);

        Intent parameter = getIntent();
        final String Title = parameter.getStringExtra("Title");
        final String MenuStr = parameter.getStringExtra("MenuStr");
        final String PeopleStr = parameter.getStringExtra("PeopleStr");
        final String PlaceStr = parameter.getStringExtra("PlaceStr");

        TextView TitleTv = (TextView) findViewById(R.id.TitleTextView);
        TitleTv.setText(Title);
        TextView ContentTv = (TextView) findViewById(R.id.ContentTextView);
        ContentTv.setText("메뉴 : " + MenuStr + '\n' + "인원 : " + PeopleStr + '\n'
                + "장소 : " + PlaceStr + '\n' );

    }
}
