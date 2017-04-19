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
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EduTeachPlanBean_Must;

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

public class MustCourseActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    String bundleString;
    ArrayList<EduTeachPlanBean_Must> listOfEduTeachPMust=new ArrayList<EduTeachPlanBean_Must>();
//    List<Map<String,Object>> listItems=new ArrayList<HashMap<String,Object>>();
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
        setContentView(R.layout.activity_must_course);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        bundleString=bundle.getString("uid");
        searchTeachPlan_Must();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(MustCourseActivity.this, TeachPlanActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);//无效？
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void searchTeachPlan_Must(){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("uid", bundleString);
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().post(requestBody).url(Constants.URL_TeachPlan).build();
        LinkEducationSystemAct.okHttpClient_edu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Score", "FAILURE");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                Log.e("Score11111111", html);
                Document document = Jsoup.parse(html);
                String teachPTerm_must;
                String teachPCourseID_must;
                String teachPCourseName_must;
                String teachPCourseType_must;
                String teachPCourseTime_must;
                String teachPCourseScore_must;
                String teachPFstSc_must;
                String teachPFnlSc_must;
                Elements tbodyArray = document.getElementsByTag("tbody");
                Element tbodyEle_must = tbodyArray.get(0);
                for (int i = 0; i < tbodyEle_must.children().size(); i++) {
                    Element trEle_must = tbodyEle_must.child(i);
                    teachPTerm_must = trEle_must.child(0).text();
                    teachPCourseID_must = trEle_must.child(1).text();
                    teachPCourseName_must = trEle_must.child(2).text();
                    teachPCourseType_must = trEle_must.child(4).text();
                    teachPCourseTime_must = trEle_must.child(5).text();
                    teachPCourseScore_must = trEle_must.child(6).text();
                    teachPFstSc_must = trEle_must.child(7).text();
                    teachPFnlSc_must = trEle_must.child(8).text();
                    listOfEduTeachPMust.add(new EduTeachPlanBean_Must(teachPTerm_must, teachPCourseID_must, teachPCourseName_must, teachPCourseType_must, teachPCourseTime_must
                            , teachPCourseScore_must, teachPFstSc_must, teachPFnlSc_must));
                }
                Message msg = handler.obtainMessage();
                msg.what = Constants.HANDLER_UPDATA_LISTVIEW;
                handler.sendMessage(msg);
            }
        });
}
    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_must_teachPlan);
        for (int i=0;i<listOfEduTeachPMust.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("Term_must",listOfEduTeachPMust.get(i).getTeachPTerm_must());
            listItem.put("CourseID_must",listOfEduTeachPMust.get(i).getTeachPCourseID_must());
            listItem.put("CourseName_must",listOfEduTeachPMust.get(i).getTeachPCourseName_must());
            listItem.put("CourseType_must",listOfEduTeachPMust.get(i).getTeachPCourseType_must());
            listItem.put("CourseTime_must",listOfEduTeachPMust.get(i).getTeachPCourseTime_must());
            listItem.put("CourseScore_must",listOfEduTeachPMust.get(i).getTeachPCourseScore_must());
            listItem.put("FstSc_must",listOfEduTeachPMust.get(i).getTeachPFstSc());
            listItem.put("FnlSc_must",listOfEduTeachPMust.get(i).getTeachPFnlSc());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(MustCourseActivity.this,listItems,R.layout.item_listview_must_tp,
                new String[]{"Term_must","CourseID_must","CourseName_must","CourseType_must","CourseTime_must","CourseScore_must","FstSc_must","FnlSc_must"}
        ,new int[]{R.id.tv_list_must_tp_term,R.id.tv_list_must_tp_id,R.id.tv_list_must_tp_name,R.id.tv_list_must_tp_type,R.id.tv_list_must_tp_time,
        R.id.tv_list_must_tp_credit,R.id.tv_list_must_tp_fstSc,R.id.tv_list_must_tp_fnlSc}));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MustCourseActivity.this, TeachPlanActivity.class);
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
