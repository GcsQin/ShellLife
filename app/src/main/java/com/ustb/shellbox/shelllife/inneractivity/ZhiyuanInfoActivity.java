package com.ustb.shellbox.shelllife.inneractivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkVolunteerAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.ZhiyuanInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ZhiyuanInfoActivity extends BaseSetupWizardActivity {
    ArrayList<String> arrayList;
    TextView tv_name;
    TextView tv_xueyuan;
    TextView tv_banji;
    TextView tv_minzu;
    TextView tv_gongshi;
    ArrayList<ZhiyuanInfo> listOfZhiyuanInfo;
    ArrayList<Map<String,Object>> mapArrayList;
    ListView listview;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDELER_SHOW_TOAST:
                    Toast.makeText(ZhiyuanInfoActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_GET_ZHIYUAN_DETIALS_SUCCESS:
                    initListView();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhiyuan_info);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        arrayList=(ArrayList<String>)bundle.getSerializable("volInfo");
        initData();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(ZhiyuanInfoActivity.this,LinkVolunteerAct.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        listOfZhiyuanInfo=new ArrayList<ZhiyuanInfo>();
        mapArrayList=new ArrayList<Map<String,Object>>();
        listview=(ListView)findViewById(R.id.lv_zhiyuaninfo_joinActDetials);
        tv_name=(TextView)findViewById(R.id.tv_zhiyuan_name);
        tv_xueyuan=(TextView)findViewById(R.id.tv_zhiyuan_xy);
        tv_banji=(TextView)findViewById(R.id.tv_zhiyuan_bj);
        tv_minzu=(TextView)findViewById(R.id.tv_zhiyuan_mz);
        tv_gongshi=(TextView)findViewById(R.id.tv_zhiyuan_gs);
        tv_name.setText(arrayList.get(0));
        tv_xueyuan.setText(arrayList.get(1));
        tv_banji.setText(arrayList.get(2));
        tv_minzu.setText(arrayList.get(3));
        tv_gongshi.setText(arrayList.get(4));
        getHaveJoinAct();
    }
    private void getHaveJoinAct(){
        Request request=new Request.Builder().url(Constants.URL_HAVE_JOIN_ACT).build();
        LinkVolunteerAct.okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_SHOW_TOAST;
                message.obj="获取已参加活动失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Document document= Jsoup.parse(responseData);
                Elements divs=document.getElementsByAttributeValueContaining("class", "cjhd_text");
                for(int i=0;i<divs.size();i++){
                    Element div=divs.get(i);
                    String actName=div.getElementsByTag("a").first().text();
                    String actTime=div.getElementsByTag("p").first().ownText();
                    for(int j=0;j<div.getElementsByTag("p").size();j++){
                        Log.e("p",div.getElementsByTag("p").get(j).text());
                    }
                    Elements lis=div.getElementsByTag("li");
                    Element li_renshu=lis.first();
                    String planRenshu=li_renshu.getElementsByTag("span").get(0).ownText();
                    String joinRenshu=li_renshu.getElementsByTag("span").get(1).ownText();
                    String actNum=lis.get(1).child(0).text();
                    String actLocation=lis.get(3).child(0).text();
                    String actGongshi=lis.get(4).child(0).text();
                    String actDeadline=lis.get(5).child(0).text();
                    String actIntroduce=lis.get(6).child(0).text();
                    String actDuty=lis.get(7).child(0).text();
                    String finalGongshi=lis.get(8).child(0).text();
                    String actResult=div.getElementsByTag("div").get(2).text();
                    listOfZhiyuanInfo.add(new ZhiyuanInfo(actName,actTime,actDeadline,planRenshu,joinRenshu,actNum,actLocation,actGongshi,actDuty,finalGongshi,actIntroduce,actResult));
                }

                handler.sendEmptyMessage(Constants.HANDELER_GET_ZHIYUAN_DETIALS_SUCCESS);
            }
        });
    }
    private void initListView(){
    for(int i=0;i<listOfZhiyuanInfo.size();i++){
        Map<String,Object> item=new HashMap<String,Object>();
        ZhiyuanInfo zhiyuanInfo=listOfZhiyuanInfo.get(i);
        item.put("actName",zhiyuanInfo.getActName());
        item.put("actTime",zhiyuanInfo.getActTime());
        item.put("planRenshu",zhiyuanInfo.getPlanRenshu());
        item.put("joinRenshu",zhiyuanInfo.getJoinRenshu());
        item.put("actDeadline",zhiyuanInfo.getActDeadline());
        item.put("actNum",zhiyuanInfo.getActNum());
        item.put("actLocation",zhiyuanInfo.getActLocation());
        item.put("actGongshi",zhiyuanInfo.getActGongshi());
        item.put("actIntrouce",zhiyuanInfo.getActIntroduce());
        item.put("actDuty",zhiyuanInfo.getActDuty());
        item.put("resultGs",zhiyuanInfo.getActFinalGongshi());
        item.put("actResult", zhiyuanInfo.getActResult());
        mapArrayList.add(item);
    }
        listview.setAdapter(new SimpleAdapter(ZhiyuanInfoActivity.this, mapArrayList, R.layout.item_lv_zhiyuan_joininfo, new String[]{
                "actName", "actTime", "planRenshu", "joinRenshu", "actDeadline", "actNum", "actLocation", "actGongshi", "actIntrouce", "actDuty", "resultGs", "actResult"
        }, new int[]{
                R.id.tv_zhiyuaninfo_name, R.id.tv_zhiyuaninfo_time, R.id.tv_zhiyuan_planrenshu, R.id.tv_zhiyuan_joinrenshu, R.id.tv_zhiyuaninfo_deadtime,
                R.id.tv_zhiyuan_actnum, R.id.tv_zhiyuan_actlocation, R.id.tv_zhiyuan_actgongshi, R.id.tv_zhiyuaninfo_introduce, R.id.tv_zhiyuaninfo_dutyperson
                , R.id.tv_zhiyuaninfo_gongshi, R.id.tv_zhiyuaninfo_result
        }));
    }
    public void joinActivity(View view){
        startActivity(new Intent(ZhiyuanInfoActivity.this,JoinPartyActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(ZhiyuanInfoActivity.this,LinkVolunteerAct.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
