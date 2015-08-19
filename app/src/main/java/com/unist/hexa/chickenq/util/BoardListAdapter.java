package com.unist.hexa.chickenq.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unist.hexa.chickenq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JM on 15. 8. 13..
 */
public class BoardListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<BoardData> listDatas = new ArrayList<>();

    private class ViewHolder {
        public TextView _id;
        public TextView user_id;
        public TextView title;
        public TextView contents;
        public TextView start_time;
        public TextView menu;
        public TextView limit_num;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, null);

            holder._id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.user_id = (TextView) convertView.findViewById(R.id.tv_user_id);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.contents = (TextView) convertView.findViewById(R.id.tv_contents);
            holder.start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            holder.menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.limit_num = (TextView) convertView.findViewById(R.id.tv_limit_num);
            holder.location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.duration = (TextView) convertView.findViewById(R.id.tv_duration);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BoardData listData = listDatas.get(position);
        holder._id.setText("_id: " + listData._id);
        holder.user_id.setText("user_id: " + listData.user_id);
        holder.title.setText("title: " + listData.title);
        holder.contents.setText("contents: " + listData.contents);
        holder.start_time.setText("start_time: " + listData.start_time);
        holder.menu.setText("menu: " + listData.menu);
        holder.limit_num.setText("limit_num: " + listData.limit_num);
        holder.location.setText("location: " + listData.location);
        holder.duration.setText("duration: " + listData.duration);

        return convertView;
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
                data.user_id = jObj.getInt("user_id");
                data.title = jObj.getString("title");
                data.contents = jObj.getString("contents");
                data.start_time = jObj.getInt("start_time");
                data.menu = jObj.getInt("menu");
                data.limit_num = jObj.getInt("limit_num");
                data.location = jObj.getInt("location");
                data.duration = jObj.getInt("duration");
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