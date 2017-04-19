package com.ustb.shellbox.shelllife.new_edu_sys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.iactivity.INewEduSysActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EduScoreInfoBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseScoreActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    ArrayList<EduScoreInfoBean> listOfEduScore=new ArrayList<EduScoreInfoBean>();
    List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_UPDATA_LISTVIEW:
                    initListView();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        searchCourseScore();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(CourseScoreActivity.this, INewEduSysActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);//无效？
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_score);
        for (int i=0;i<listOfEduScore.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("scoreTerm",listOfEduScore.get(i).getScoreTerm());
            listItem.put("scoreID",listOfEduScore.get(i).getScoreCourseID());
            listItem.put("scoreName",listOfEduScore.get(i).getScoreCourseName());
            listItem.put("scoreType",listOfEduScore.get(i).getScoreCourseType());
            listItem.put("scoreTime",listOfEduScore.get(i).getScoreStudyTime());
            listItem.put("scoreScore",listOfEduScore.get(i).getScoreScore());
            listItem.put("scoreFirst",listOfEduScore.get(i).getScoreFirstScore());
            listItem.put("scoreFinal",listOfEduScore.get(i).getScoerFinalScore());
            listItem.put("scoreRebuild",listOfEduScore.get(i).getScoreRebuildSign());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(this,listItems,R.layout.item_listview_score,new String[]{
                "scoreTerm","scoreID","scoreName","scoreType","scoreTime", "scoreScore", "scoreFirst","scoreFinal","scoreRebuild"
        },new int[]{R.id.tv_listscore_term,R.id.tv_listscore_courseID,R.id.tv_listscore_courseName,R.id.tv_listscore_courseType
                ,R.id.tv_listscore_courseTime,R.id.tv_listscore_courseScore,R.id.tv_listscore_firstScore,R.id.tv_listscore_finalScore
                ,R.id.tv_listscore_rebuild}));
    }
    private void searchCourseScore(){
        FormBody.Builder builder =new FormBody.Builder();
        builder.add("uid",bundle.getString("uid"));
        RequestBody requestBody= builder.build();
        Request request=new Request.Builder().url(Constants.URL_CourseScore).post(requestBody).build();
        LinkEducationSystemAct.okHttpClient_edu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                Log.e("Score", html);
                String scoreTerm;
                String scoreCourseID;
                String scoreCourseName;
                String scoreCourseType;
                String scoreStudyTime;
                String scoreScore;
                String scoreFirstScore;
                String scoerFinalScore;
                String scoreRebuildSign;
                Document document = Jsoup.parse(html);
                String title = document.title();//学生课程成绩
                Elements tbodyArray = document.getElementsByTag("tbody");
                Element tbodyEle = tbodyArray.get(0);
                for (int i = 0; i < tbodyEle.children().size(); i++) {
                    Element trEle = tbodyEle.child(i);
                    scoreTerm=setValueForCxxf(trEle.child(0).text());
                    scoreCourseID=setValueForCxxf(trEle.child(1).text());
                    scoreCourseName=setValueForCxxf(trEle.child(2).text());
                    scoreCourseType=setValueForCxxf(trEle.child(3).text());
                    scoreStudyTime=setValueForCxxf(trEle.child(4).text());
                    scoreScore=setValueForCxxf(trEle.child(5).text());
                    scoreFirstScore=setValueForCxxf(trEle.child(6).text());
                    scoerFinalScore=setValueForCxxf(trEle.child(7).text());
                    scoreRebuildSign=setValueForCxxf(trEle.child(8).text());
                    listOfEduScore.add(new EduScoreInfoBean(scoreTerm,scoreCourseID,scoreCourseName,scoreCourseType,scoreStudyTime,scoreScore
                    ,scoreFirstScore,scoerFinalScore,scoreRebuildSign));
                }
                Message message = handler.obtainMessage();
                message.what = Constants.HANDLER_UPDATA_LISTVIEW;
                handler.sendMessage(message);
            }
        });
    }
    private String setValueForCxxf(String str){
        String string=str.trim();
        if (string != null) {
            return str;
        } else {
            return "  ";
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(CourseScoreActivity.this, INewEduSysActivity.class);
            Bundle bundleBack=new Bundle();
            bundleBack.putString("uid",bundle.getString("uid"));
            intent.putExtras(bundleBack);
            startActivity(intent);//无效？
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
