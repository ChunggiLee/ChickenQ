package com.unist.hexa.chickenq;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JM on 2015. 11. 21..
 */
public class BoardSearchDialogFragment extends DialogFragment implements View.OnClickListener {
    private final static String menuItems[] = { "전체", "치킨", "피자", "짜장면", "탕수육", "패스트푸드"};
    private final static String placeItems[] = { "전체", "공학관", "경영관", "학생회관", "기숙사", "기숙사 휴게실"};

    Spinner menuSpinner, placeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_popup, container, false);
        rootView.findViewById(R.id.OkButton).setOnClickListener(this);
        rootView.findViewById(R.id.CancelButton).setOnClickListener(this);

        setup(rootView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.OkButton:
                SharedPreferences pref = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("menu", menuSpinner.getSelectedItemPosition());
                editor.putInt("place", placeSpinner.getSelectedItemPosition());
                editor.apply();
                break;
            case R.id.CancelButton: break;
        }
        dismiss();
    }

    private void setup(View rootView) {
        ArrayAdapter<String> MenuAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(Arrays.asList(menuItems)));

        ArrayAdapter<String> PlaceAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(Arrays.asList(placeItems)));

        menuSpinner = (Spinner) rootView.findViewById(R.id.MenuSpinner);
        menuSpinner.setPrompt("메뉴를 선택하세요.");
        menuSpinner.setAdapter(MenuAdapter);

        placeSpinner = (Spinner) rootView.findViewById(R.id.PlaceSpinner);
        placeSpinner.setPrompt("장소를 선택하세요");
        placeSpinner.setAdapter(PlaceAdapter);
    }
}
