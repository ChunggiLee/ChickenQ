package com.unist.hexa.chickenq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.unist.hexa.chickenq.util.AsyncJsonParser;
import com.unist.hexa.chickenq.util.BoardData;
import com.unist.hexa.chickenq.util.BoardListAdapter;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2015-08-11.
 */
public class BoardViewActivity extends Activity implements View.OnClickListener {

    String UrlStr, text, SpaceStr="", nameStr, comment, mResult;
    String userIdStr[];
    Bundle bundle;
    BoardData boardData;
    Window win;
    LinearLayout linear;
    int num, userNum=0, pos, arrayLength, cancelNum;
    EditText CommentEdt;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListView myList;
    SimpleAdapter adapter;
    HashMap<String, String> map;
    ImageButton cancelBtn;
    LayoutInflater inflater;
    View view1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = getWindow();
        win.setContentView(R.layout.activity_board_view);
        oslist = new ArrayList<HashMap<String, String>>();

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
            case R.id.PartyPeopleBtn:

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
            case R.id.JoinBtn:
                UrlStr = "http://chickenq.hexa.pro/party/join.php?boardid=" + boardData._id + "&userid=" + "10032";
                urlOpenFunc();
                break;
            case R.id.DropBtn:
                UrlStr = "http://chickenq.hexa.pro/party/drop.php?boardid=" + boardData._id + "&userid=" + "10032";
                urlOpenFunc();
                break;

            case R.id.chat:
                comment = CommentEdt.getText().toString();
                nameStr = boardData.user_id + comment;

                char ch[] = comment.toCharArray();

                Log.d("comment.length()", " " + comment.length());
                for (int i = 0; i < comment.length(); i++) {
                    if (ch[i] == ' ') {
                        SpaceStr += "%20";
                    } else {
                        SpaceStr += ch[i];
                    }
                }
                Log.d("SpaceStr", SpaceStr);
                comment = SpaceStr;
                SpaceStr = "";
                UrlStr = "http://chickenq.hexa.pro/reply/comment.php?boardid=" + boardData._id + "&userid=10032&content=" + comment;
                userNum++;
                userIdStr = new String[userNum];
                userIdStr[userNum - 1] = Integer.toString(boardData._id);
                comment = boardData.user_id + " : " + comment;
                map = new HashMap<String, String>();
                map.put("comment", comment);
                map.put("cancelBtn", "CancelBtn");
                oslist.add(map);
                myList = (ListView) findViewById(R.id.lv_comment);
                comment();
                break;

        }
        Log.d("S3 : ", " s3 ");


        urlOpenFunc();
        CommentEdt.setText("");

    }


    public void urlOpenFunc() {
        try{
            URL url = new URL(UrlStr);

            Log.d("UrlStr : ", UrlStr);

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

            readStream(conn.getInputStream(), txt);

        } catch (Exception e) {
            txt.setText("Error\n" + e.toString());
            e.printStackTrace();
        }
    }

    private void readStream(InputStream in, TextView txt) {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                num = json.getInt("num");
                text = "인원 : " + num + '\n';
                for (int i = 0; i < num; i++) {
                    text += "이름 : " + Integer.toString(json.getInt("user" + i)) + "\n";
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
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        String line, result = "";

        while((line = br.readLine()) != null)
        {
            result += line;
        }
        mResult = result;

        Log.d("result : ", result);

        try
        {
            JSONArray array = new JSONArray(mResult);

            arrayLength = array.length();

            Log.d("arrayLength", " : " + arrayLength);

            for(int i = 0 ; i < arrayLength ; i++)
            {
                JSONObject object = array.getJSONObject(i);
                comment = object.getString("user_id") + " : " + object.getString("contents");

                map = new HashMap<String, String>();
                Log.d("comment : ", comment);

                map.put("comment", comment);
                map.put("cancelBtn", "CancelBtn");
                oslist.add(map);
                Log.d("Size : ", " " + map.size());
                myList = (ListView) findViewById(R.id.lv_comment);
                Log.d("before remove : ", " " + oslist.size());


            }
        }
        catch(JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            e.printStackTrace();
        }


        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View converView = vi.inflate(R.layout.listitem, null);
        converView.findViewById(R.id.CancelBtn).setOnClickListener(this);
    }
    public void comment(){

        adapter = new SimpleAdapter(BoardViewActivity.this, oslist, R.layout.listitem, new String[]{"comment", "cancelBtn"}, new int[]{R.id.textView, R.id.CancelBtn});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                  @Override
                                  public boolean setViewValue(View view, Object data, String textRepresentation) {

                                      if (view.getId() == R.id.CancelBtn) {
                                          cancelBtn = (ImageButton) view;
                                          cancelBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Log.d("adsf", "adsf");

                                                  View parentRow = (View) v.getParent();
                                                  ListView listView = (ListView) parentRow.getParent();
                                                  pos = listView.getPositionForView(parentRow);

                                                  Log.d("pos : ", " " + pos);
                                                  if (pos != myList.INVALID_POSITION) {

                                                      Log.d("Size : ", " " + oslist.size());

                                                      UrlStr = "http://chickenq.hexa.pro/reply/decomment.php?id=" + boardData._id + "&num=" + pos;
                                                      Log.d("Decoment : ", UrlStr);
                                                      oslist.remove(pos);
                                                      getComment();
                                                      Log.d("after remove : ", " " + oslist.size());
                                                      urlOpenFunc();
                                                      for (int i = 0; i < oslist.size(); i++) {
                                                          Log.d("after remove : ", " " + oslist.get(i));
                                                      }
                                                      Log.d("S1 : ", "s1 ");
                                                      adapter.notifyDataSetChanged();
                                                      Log.d("S2 : ", " s2 ");
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
        //adapter = new SimpleAdapter(BoardViewActivity.this, oslist, R.layout.listitem, new String[]{"comment", "cancelBtn"}, new int[]{R.id.textView, R.id.CancelBtn});
        myList.setAdapter(adapter);
    }
}


