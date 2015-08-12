package com.unist.hexa.chickenq;

/**
 * 게시판 정보를 담고 있는 객체
 */
public class BoardData {
    public int _id;
    public int user_id;
    public String title;
    public String contents;
    public int start_time;
    public int menu;
    public int limit_num;
    public int location;
    public int duration;

    public BoardData() { }

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
}