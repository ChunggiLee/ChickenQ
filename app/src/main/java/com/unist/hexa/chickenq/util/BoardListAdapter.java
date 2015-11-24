package com.unist.hexa.chickenq.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unist.hexa.chickenq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JM on 15. 8. 13..
 */
public class BoardListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<BoardData> listDatas = new ArrayList<>();

    public static int search_menu, search_place;

    private class ViewHolder {
        public TextView title;
        public TextView party_maker;
        public TextView menu;
        public TextView limit_num;

        // Additional
        public LinearLayout ll_additional;
        public TextView location;
        public TextView duration;
    }

    public BoardListAdapter(Context context) {
        this.context = context;
    }


    /**
     * BaseAdapter 메소드들
     */
    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Search
        BoardData listData = listDatas.get(position);
        if ((search_menu != 0 && listData.menu != search_menu-1) ||
                (search_place != 0 && listData.location != search_place-1)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview_null, parent, false);
            convertView.setTag(null);
            return convertView;
        }

        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);

            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.party_maker = (TextView) convertView.findViewById(R.id.tv_party_maker);
            holder.menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.limit_num = (TextView) convertView.findViewById(R.id.tv_limit_num);
            holder.ll_additional = (LinearLayout) convertView.findViewById(R.id.ll_additional_data);
            holder.location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.duration = (TextView) convertView.findViewById(R.id.tv_duration);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(listData.title);
        holder.party_maker.setText(listData.name);
        holder.menu.setText(getMenu(listData.menu));
        holder.limit_num.setText("" + listData.limit_num);
        if (listData.additional_data_visible) {
            holder.ll_additional.setVisibility(View.VISIBLE);
            holder.location.setText("위치: " + listData.location);
            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            try {
                Date startDate = sdFormat.parse(listData.start_time);
                holder.duration.setText("남은시간: " + (new Date().getTime() - startDate.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.ll_additional.setVisibility(View.GONE);
        }

        return convertView;
    }

    String getMenu(int menu) {
        switch(menu) {
            case 0: return "치킨";
            case 1: return "피자";
            case 2: return "짜장면";
            case 3: return "탕수육";
            case 4: return "패스트푸드";
            default: return "ERROR";
        }
    }

    /**
     * 커스텀 메소드
     * addItem      : item 추가
     * remove       : item 제거
     * sort         : item 정렬 - 구현안함
     * dataChange   : 데이터 변경 후 호출해야함
     */
    public void addItem(BoardData data) {
        listDatas.add(new BoardData(data));
    }

    public void remove(int position) {
        listDatas.remove(position);
        dataChange();
    }

    public void sort() {
    }

    public void dataChange() {
        this.notifyDataSetChanged();
    }

    public void parseJson(JSONArray jArr) {
        for (int i=0; i<jArr.length(); ++i) {
            try {
                JSONObject jObj = jArr.getJSONObject(i);
                BoardData data = new BoardData();
                data._id = jObj.getInt("_id");
                data.name = jObj.getString("name");
                data.title = jObj.getString("title");
                data.contents = jObj.getString("contents");
                data.start_time = jObj.getString("start_time");
                data.menu = jObj.getInt("menu");
                data.limit_num = jObj.getInt("limit_num");
                data.location = jObj.getInt("location");
                data.duration = jObj.getInt("duration");
                data.additional_data_visible = false;
                addItem(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dataChange();
    }

    public BoardData getBoardData(int position) {
        return listDatas.get(position);
    }
}