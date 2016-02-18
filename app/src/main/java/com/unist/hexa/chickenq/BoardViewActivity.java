package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.BoardData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2015-08-11.
 */
public class BoardViewActivity extends Activity implements View.OnClickListener {

    String UrlStr, text, SpaceStr="", nameStr, comment, mResult, name, portal;
    String userIdStr[];
    Bundle bundle;
    BoardData boardData;
    Window win;
    LinearLayout linear;
    int num, userNum=0, pos, arrayLength, check=0;
    EditText CommentEdt;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListView myList;
    SimpleAdapter adapter;
    HashMap<String, String> map;
    ImageButton cancelBtn;
    private int id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = getWindow();
        win.setContentView(R.layout.activity_board_view);
        oslist = new ArrayList<HashMap<String, String>>();

        id1 = getSharedPreferences("setting_login", 0).getInt("id", 0);
        portal  = getSharedPreferences("setting_login", 0).getString("portal_id", "");
        bundle = getIntent().getExtras();
        boardData = bundle.getParcelable("boardData");

        final String title = boardData.title;
        final String contents = "메뉴 : " + SelectMenu(boardData.menu) + '\n'
                + "인원 : " + SelectPeople(boardData.limit_num) + '\n'
                + "장소 : " + SelectPlace(boardData.location) + '\n'
                + "올린 시간 : " + boardData.start_time + '\n'
                + boardData.contents;

        TextView tv_title = (TextView) findViewById(R.id.TitleTextView);
        tv_title.setText(title);
        TextView tv_contents = (TextView) findViewById(R.id.ContentTextView);
        tv_contents.setText(contents);

        findViewById(R.id.PartyPeopleBtn).setOnClickListener(this);
        findViewById(R.id.JoinBtn).setOnClickListener(this);
        findViewById(R.id.DropBtn).setOnClickListener(this);
        findViewById(R.id.chat).setOnClickListener(this);
        CommentEdt = (EditText) findViewById(R.id.CommentEdt);

        setup_board();

    }

    private void setup_board() {

        UrlStr = "http://chickenq.hexa.pro/reply/view.php?id=" + boardData._id;
        getComment();
        comment();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PartyPeopleBtn: // Current Party members list

                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                linear = (LinearLayout) inflater.inflate(R.layout.layout_popup_partypeople, null);

                LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                win.addContentView(linear, paramlinear);

                TextView txt = (TextView) findViewById(R.id.PartyPeopleTxt);

                UrlStr = "http://chickenq.hexa.pro/party/status.php?id=" + boardData._id;
                urlReadFunc(txt);

                Button ok = (Button) findViewById(R.id.OkButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });

                break;
            case R.id.JoinBtn: // Join Party
                UrlStr = "http://chickenq.hexa.pro/party/join.php?boardid=" + boardData._id + "&userid=" + id1;
                urlOpenFunc();
                Toast.makeText(this,"파티에 참여하셨습니다.",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.DropBtn: // Drop Party
                UrlStr = "http://chickenq.hexa.pro/party/drop.php?boardid=" + boardData._id + "&userid=" + id1;
                urlOpenFunc();
                Toast.makeText(this,"파티에 탈퇴하셨습니다.",Toast.LENGTH_SHORT ).show();
                break;

            case R.id.chat: // Write chat
                comment = CommentEdt.getText().toString();
                nameStr = boardData.user_id + comment;
                char ch[] = comment.toCharArray();
                for (int i = 0; i < comment.length(); i++) {
                    if (ch[i] == ' ') {
                        SpaceStr += "%20";
                    } else {
                        SpaceStr += ch[i];
                    }
                }
                comment = SpaceStr;
                SpaceStr = "";
                UrlStr = "http://chickenq.hexa.pro/reply/comment.php?boardid=" + boardData._id + "&userid=" + id1 + "&content=" + comment;
                urlOpenFunc();
                check = 1;
                getComment();
                check = 0;
                userNum++;
                userIdStr = new String[userNum];
                userIdStr[userNum - 1] = Integer.toString(boardData._id);
                comment = name + " : " + comment;
                map = new HashMap<String, String>();
                map.put("comment", comment);
                map.put("cancelBtn", "CancelBtn");
                oslist.add(map);
                comment();
                Toast.makeText(this,"댓글을 쓰셨습니다..",Toast.LENGTH_SHORT ).show();
                break;

        }
        urlOpenFunc();
        CommentEdt.setText("");
    }


    public void urlOpenFunc() {
        try{
            URL url = new URL(UrlStr);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            url.openStream();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "오류!", Toast.LENGTH_LONG).show();
        }
    }

    public void urlReadFunc(TextView txt) {
        try{
            URL url = new URL(UrlStr);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            readStream(reader, txt);
        } catch (Exception e) {
            txt.setText("Error\n" + e.toString());
            e.printStackTrace();
        }
    }

    private void readStream(BufferedReader reader, TextView txt) {

        String line = "";
        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                Log.d("line", " " +line);
                num = json.getInt("num");
                Log.d("num", " " +num);
                if (num == 0) {
                    text = "현재 파티에 아무도 없습니다.";
                }else {
                    text = "인원 : " + num + '\n';
                    for (int i = 1; i <= num; i++) {
                        text += "이름 : " + json.getString("name" + i) + "\n";
                    }
                }
                txt.append(text);
            }
        }
        catch (Exception e){
            txt.append("Error\n" + e.toString() + "\n");
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) reader.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void getComment()
    {
        if(android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpGet httpGet = new HttpGet(UrlStr);

        try
        {
            HttpResponse response = client.execute(httpGet);
            processEntity(response.getEntity());
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void processEntity(HttpEntity entity) throws IllegalStateException, IOException
    {
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        String line, result = "";
        br.readLine();
        while((line = br.readLine()) != null)
        {
            result += line;
        }
        mResult = result;
        try
        {
           if (check == 0) {
               JSONArray array = new JSONArray(mResult);
               arrayLength = array.length();
               for (int i = 0; i < arrayLength; i++) {
                   JSONObject object = array.getJSONObject(i);
                   comment = object.getString("name") + " : " + object.getString("contents");
                   map = new HashMap<String, String>();
                   map.put("comment", comment);
                   map.put("cancelBtn", "CancelBtn");
                   oslist.add(map);
                   myList = (ListView) findViewById(R.id.lv_comment);
               }
           }
            if (check == 1){
                JSONObject object = new JSONObject(mResult);
                name = object.getString("name");
            }

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View converView = vi.inflate(R.layout.listitem, null);
        converView.findViewById(R.id.CancelBtn).setOnClickListener(this);
    }
    public void comment(){
        myList = (ListView) findViewById(R.id.lv_comment);
        adapter = new SimpleAdapter(BoardViewActivity.this, oslist, R.layout.listitem, new String[]{"comment", "cancelBtn"}, new int[]{R.id.textView, R.id.CancelBtn});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                  @Override
                                  public boolean setViewValue(View view, Object data, String textRepresentation) {

                                      if (view.getId() == R.id.CancelBtn) {
                                          cancelBtn = (ImageButton) view;
                                          cancelBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  View parentRow = (View) v.getParent();
                                                  ListView listView = (ListView) parentRow.getParent();
                                                  pos = listView.getPositionForView(parentRow);

                                                  if (pos != myList.INVALID_POSITION) {
                                                      UrlStr = "http://chickenq.hexa.pro/reply/decomment.php?id=" + boardData._id + "&num=" + pos;
                                                      oslist.remove(pos);
                                                      getComment();
                                                      urlOpenFunc();
                                                      adapter.notifyDataSetChanged();
                                                      myList.setAdapter(adapter);
                                                  }
                                              }
                                          });
                                          return true;
                                      }
                                      return false;

                                  }
                              }
        );
        myList.setAdapter(adapter);
    }
}




