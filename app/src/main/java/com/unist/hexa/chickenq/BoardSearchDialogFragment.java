package com.unist.hexa.chickenq;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.unist.hexa.chickenq.util.BoardListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JM on 2015. 11. 21..
 */
public class BoardSearchDialogFragment extends DialogFragment implements View.OnClickListener {
    private final static String TAG = "BoardSearchDialogFrag";
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
                BoardListAdapter.search_menu = menuSpinner.getSelectedItemPosition();
                BoardListAdapter.search_place = placeSpinner.getSelectedItemPosition();
                Log.d(TAG, String.format("menu=%d/place=%d",
                        BoardListAdapter.search_menu,
                        BoardListAdapter.search_place));
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        BoardFragment.mAdapter.dataChange();
    }
}
