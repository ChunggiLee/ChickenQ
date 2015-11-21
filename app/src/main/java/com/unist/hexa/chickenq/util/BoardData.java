package com.unist.hexa.chickenq.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 게시판 정보를 담고 있는 객체
 */
public class BoardData implements Parcelable {
    public int _id;
    public int user_id;
    public String title;
    public String contents;
    public String start_time;
    public int menu;
    public int limit_num;
    public int location;
    public int duration;
    public boolean additional_data_visible;

    /**
     * Constructors
     */

    public BoardData() {
    }

    public BoardData(BoardData copy) {
        this._id = copy._id;
        this.user_id = copy.user_id;
        this.title = copy.title;
        this.contents = copy.contents;
        this.start_time = copy.start_time;
        this.menu = copy.menu;
        this.limit_num = copy.limit_num;
        this.location = copy.location;
        this.duration = copy.duration;
    }

    public BoardData(Parcel in) {
        readFromParcel(in);
    }

    public BoardData(int _id, int user_id, String title, String contents, String start_time, int menu, int limit_num, int location, int duration) {
        this._id = _id;
        this.user_id = user_id;
        this.title = title;
        this.contents = contents;
        this.start_time = start_time;
        this.menu = menu;
        this.limit_num = limit_num;
        this.location = location;
        this.duration = duration;
    }

    /**
     * Parcel methods
     */

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeInt(this.user_id);
        dest.writeString(this.title);
        dest.writeString(this.contents);
        dest.writeString(this.start_time);
        dest.writeInt(this.menu);
        dest.writeInt(this.limit_num);
        dest.writeInt(this.location);
        dest.writeInt(this.duration);
    }

    private void readFromParcel(Parcel in) {
        this._id = in.readInt();
        this.user_id = in.readInt();
        this.title = in.readString();
        this.contents = in.readString();
        this.start_time = in.readString();
        this.menu = in.readInt();
        this.limit_num = in.readInt();
        this.location = in.readInt();
        this.duration = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BoardData> CREATOR = new Parcelable.Creator<BoardData>() {
        public BoardData createFromParcel(Parcel in) {
            return new BoardData(in);
        }
        public BoardData[] newArray (int size) {
            return new BoardData[size];
        }
    };
}