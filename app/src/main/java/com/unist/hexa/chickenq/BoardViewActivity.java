package com.unist.hexa.chickenq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.unist.hexa.chickenq.util.BoardData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2015-08-11.
 */
public class BoardViewActivity extends Activity implements View.OnClickListener {

    private final String imgSrcLink = "http://chunggi.net/icon.png";
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    String setupChatURL, text, SpaceStr="", nameStr, comment, name, portal, title, contents;
    String userIdStr[];
    Bundle bundle;
    BoardData boardData;
    Window win;
    LinearLayout linear;
    int num, userNum=0, pos, time = 1;
    int [] chatId;
    EditText CommentEdt;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListView myList;
    SimpleAdapter adapter;
    private final Handler handler = new Handler();
    HashMap<String, String> map;
    ImageButton cancelBtn;


    TextView txt;
    private int id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = getWindow();
        win.setContentView(R.layout.activity_board_view);
        oslist = new ArrayList<HashMap<String, String>>();

        // parse id, portal_id, and boardData
        id1 = getSharedPreferences("setting_login", 0).getInt("uid", 0);
        portal  = getSharedPreferences("setting_login", 0).getString("id", "");
        bundle = getIntent().getExtras();
        boardData = bundle.getParcelable("boardData");

        // set view information
        title = boardData.title;
        contents = "메뉴 : " + SelectMenu(boardData.menu) + '\n'
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
        findViewById(R.id.Kakao_btn).setOnClickListener(this);
        CommentEdt = (EditText) findViewById(R.id.CommentEdt);

        // Connect kakako
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.getMessage();
        }

        // Set chat list content
        setup_board();

    }

    private void setup_board() {

        // renew chat contents after 30 seconds
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (time > 0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            oslist = new ArrayList<HashMap<String, String>>();
                            setupChatURL = "http://chickenq.hexa.pro/reply/view.php?id=" + boardData._id;
                            urlReadFunc("chat", setupChatURL);
                            time--;
                        }
                    });
                    try {
                        Thread.sleep(30000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                time = 1;
            }
        });
        t.start();

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
                txt = (TextView) findViewById(R.id.PartyPeopleTxt);

                // Send Url
                String partyListURL = "http://chickenq.hexa.pro/party/status.php?id=" + boardData._id;
                urlOpenFunc(partyListURL);
                urlReadFunc("party", partyListURL);

                Button ok = (Button) findViewById(R.id.OkButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });

                break;
            case R.id.JoinBtn: // Join Party
                String joinURL = "http://chickenq.hexa.pro/party/join.php?boardid=" + boardData._id + "&userid=" + id1;
                urlOpenFunc(joinURL);
                Toast.makeText(this,"파티에 참여하셨습니다.",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.DropBtn: // Drop Party
                String dropURL = "http://chickenq.hexa.pro/party/drop.php?boardid=" + boardData._id + "&userid=" + id1;
                urlOpenFunc(dropURL);
                Toast.makeText(this,"파티에 탈퇴하셨습니다.",Toast.LENGTH_SHORT ).show();
                break;

            case R.id.chat: // Write chat
                // Receive comment
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

                // Send Url
                String chatURL = "http://chickenq.hexa.pro/reply/comment.php?boardid=" + boardData._id + "&userid=" + id1 + "&content=" + comment;
                urlOpenFunc(chatURL);

                userNum++;
                userIdStr = new String[userNum];
                userIdStr[userNum - 1] = Integer.toString(boardData._id);
                comment = name + " : " + comment;

                setup_board();
                Toast.makeText(this,"댓글을 쓰셨습니다..",Toast.LENGTH_SHORT ).show();
                break;

            case R.id.Kakao_btn:
                sendLink(imgSrcLink);


        }
        CommentEdt.setText("");
    }


    public void urlOpenFunc(String URL) {
        // Send Url to server
        try{
            URL url = new URL(URL);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //Log.d("BoardViewActivity", "URLTEST : " + URL);

            url.openStream();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "오류!", Toast.LENGTH_LONG).show();
        }
    }

    public void urlReadFunc(String type, String URL) {
        // Read Url from server
        try{
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            if(type == "party"){
                // Read Url about party
                partyReadStream(reader);
            } else if(type == "chat"){
                // Read Url about chat
                chatReadStream(reader);
            }
            setup_board();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void partyReadStream(BufferedReader reader) {

        String line = "";
        try {
            reader.readLine(); // erase <meta ...>
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);

                // Set information
                num = json.getInt("num");
                Log.d("BoardViewActivity", num+"");

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

    private void chatReadStream(BufferedReader reader) {

        String line = "";
        try {
            reader.readLine(); // Erase <meta ..>
            //Log.d("BoardViewActivity", "reader.readLine() : " + reader.readLine());
            JSONObject jsonnum = new JSONObject(reader.readLine());
            //Log.d("BoardViewActivity", " jsonnum : " + jsonnum.toString());

            // Parse information
            int i = jsonnum.getInt("num");
            int k = 0;
            chatId = new int[i];
            //Log.d("BoardViewActivity", "asdfasdf");
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                //Log.d("BoardViewActivity", "chatline : " + line);
                comment = json.getString("name") + " : " + json.getString("contents");
                chatId[k++] = json.getInt("user_id");
                map = new HashMap<String, String>();
                map.put("comment", comment);
                map.put("cancelBtn", "CancelBtn");
                oslist.add(map);
            }
            myList = (ListView) findViewById(R.id.lv_comment);

            // Set adapter
            adapter = new SimpleAdapter(BoardViewActivity.this, oslist, R.layout.listitem, new String[]{"comment", "cancelBtn"}, new int[]{R.id.textView, R.id.CancelBtn});
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                      @Override
                                      public boolean setViewValue(View view, Object data, String textRepresentation) {

                                          // Check whether cancel button
                                          if (view.getId() == R.id.CancelBtn) {
                                              cancelBtn = (ImageButton) view;
                                              cancelBtn.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      View parentRow = (View) v.getParent();
                                                      ListView listView = (ListView) parentRow.getParent();

                                                      // Get chat position
                                                      pos = listView.getPositionForView(parentRow);

                                                      // Log.d("BoardViewActivity", "pos : " + pos + " , " + "myList.INVALID_POSITION : " +  myList.INVALID_POSITION );
                                                      //Log.d("BoardViewActivity", "pos : " + pos  + "chatId[pos] : " + chatId[pos] );

                                                      // Compare chat writer and client id
                                                      if (chatId[pos] == id1) {
                                                          AlertDialog.Builder db = new AlertDialog.Builder(BoardViewActivity.this);
                                                          db.setMessage("댓글을 삭제하시겠습니까?")
                                                                  .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                      }
                                                                  })
                                                                  .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                          String dropURL = "http://chickenq.hexa.pro/reply/decomment.php?id=" + boardData._id + "&num=" + pos;
                                                                          //Log.d("BoardViewActivity", "dropURL : " + dropURL );
                                                                          oslist = new ArrayList<HashMap<String, String>>();
                                                                          urlOpenFunc(dropURL);
                                                                          urlReadFunc("chat", setupChatURL);
                                                                          adapter.notifyDataSetChanged();
                                                                          myList.setAdapter(adapter);
                                                                      }
                                                                  }).show();
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

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View converView = vi.inflate(R.layout.listitem, null);
            converView.findViewById(R.id.CancelBtn).setOnClickListener(this);
            //Log.d("BoardViewActivity", "" + map.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Send image(link) kakao
    private void sendLink(String imgSrcLink){
        try {
            kakaoTalkLinkMessageBuilder.addText(title + " " + contents)
                    .addImage(imgSrcLink, 300, 300)
                    //.addAppButton("자세히 보기", new AppActionBuilder().setUrl("market://details?id=com.unist.hexa.chickenq")
                            //.build());
            .build();
            //.addAppButton("앱으로 이동")
//      kakaoTalkLinkMessageBuilder.addWebButton("웹으로 이동");
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
        } catch (KakaoParameterException e) {
            e.getMessage();
        }
    }
}




