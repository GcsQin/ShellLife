package com.ustb.shellbox.shelllife.inneractivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkVolunteerAct;
import com.ustb.shellbox.shelllife.apater.PtrrvBaseAdapter;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.ZhiyuanJoinActInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
public class JoinPartyActivity extends BaseSetupWizardActivity{
    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    PtrrvAdapter ptrrvAdapter;
    int pageIndex=2;
    ArrayList<ZhiyuanJoinActInfo> actInfos;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDELER_SHOW_TOAST:
                    Toast.makeText(JoinPartyActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_GET_DETIALS_FINISH:
                    initPullToRefresh();
                    break;
                case Constants.HANDELER_SHOW_JOIN_DIALOG:

                    break;
                case Constants.HANDELER_FUNCTION_TO_LOAD_DETIALS:
                    loadMoreActInfo();
                    break;
            }
            if(msg.what==Constants.HANDELER_REFRESH_DETIASL){
                ptrrvAdapter.setCount(10);
                ptrrvAdapter.notifyDataSetChanged();
                pullToRefreshRecyclerView.setOnRefreshComplete();
                pullToRefreshRecyclerView.onFinishLoading(true,false);
            }
            while (msg.what!=Constants.HANDELER_LOADMORE_DETIALS){
                return;
            }
            if(ptrrvAdapter.getItemCount()==actInfos.size()){
                Toast.makeText(JoinPartyActivity.this,"无更多数据",Toast.LENGTH_SHORT).show();
                pullToRefreshRecyclerView.onFinishLoading(false,false);
                return;
            }
            ptrrvAdapter.setCount(actInfos.size());
            ptrrvAdapter.notifyDataSetChanged();
            pullToRefreshRecyclerView.onFinishLoading(true,false);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_join_party);
        actInfos=new ArrayList<>();
        initJoinActivity();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(JoinPartyActivity.this,LinkVolunteerAct.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initJoinActivity(){
        Request request=new Request.Builder().url(Constants.URL_JOIN_ACT).build();
        LinkVolunteerAct.okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = Constants.HANDELER_SHOW_TOAST;
                message.obj = "志愿活动信息获取失败" + e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Document document = Jsoup.parse(responseData);
                Elements divs = document.getElementsByAttributeValueContaining("class", "cjhd_text");
                if (divs.size() <= 0) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDELER_SHOW_TOAST;
                    message.obj = "没有更多活动了";
                    handler.sendMessage(message);
                    return;
                }
                for (int i = 0; i < divs.size(); i++) {
                    Element div=divs.get(i);
                    String joinActName=div.getElementsByTag("a").get(0).text();
                    String joinActTime=div.getElementsByTag("p").get(0).ownText();
                    Elements lis=div.getElementsByTag("li");
                    Element li_renshu=lis.get(0);
                    String joinActPlanNum=li_renshu.getElementsByTag("span").get(0).text();
                    String joinActJoinNum=li_renshu.getElementsByTag("span").get(1).text();
                    String joinActNum=lis.get(1).child(0).text();
                    String joinActType=lis.get(2).child(0).text();
                    String joinActLocation=lis.get(3).child(0).text();
                    String joinActGongshi=lis.get(4).child(0).text();
                    String joinActDeadline=lis.get(5).child(0).text();
                    String joinActIntroduce=lis.get(6).child(0).text();
                    String joinActDuty=lis.get(7).child(0).text();
                    actInfos.add(new ZhiyuanJoinActInfo(joinActName, joinActTime, joinActPlanNum, joinActJoinNum, joinActNum,
                            joinActType, joinActLocation, joinActGongshi, joinActDeadline, joinActIntroduce, joinActDuty));
                }
                handler.sendEmptyMessage(Constants.HANDELER_GET_DETIALS_FINISH);
            }
        });
    }
    private void loadMoreActInfo(){
        Request request=new Request.Builder().url(Constants.URL_JOIN_ACT+"&pageNum="+pageIndex+Constants.URL_LOAD_MORE_ACT+System.currentTimeMillis()).build();
        pageIndex++;
        LinkVolunteerAct.okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = Constants.HANDELER_SHOW_TOAST;
                message.obj = "加载更多活动信息失败" + e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Document document = Jsoup.parse(responseData);
                Elements divs = document.getElementsByAttributeValueContaining("class", "cjhd_text");
                if (divs.size() <= 0) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDELER_SHOW_TOAST;
                    handler.sendMessage(message);
                    return;
                }
                for (int i = 0; i < divs.size(); i++) {
                    Element div = divs.get(i);
                    String joinActName = div.getElementsByTag("a").get(0).text();
                    String joinActTime = div.getElementsByTag("p").get(0).ownText();
                    Elements lis = div.getElementsByTag("li");
                    Element li_renshu = lis.get(0);
                    String joinActPlanNum = li_renshu.getElementsByTag("span").get(0).text();
                    String joinActJoinNum = li_renshu.getElementsByTag("span").get(1).text();
                    String joinActNum = lis.get(1).child(0).text();
                    String joinActType = lis.get(2).child(0).text();
                    String joinActLocation = lis.get(3).child(0).text();
                    String joinActGongshi = lis.get(4).child(0).text();
                    String joinActDeadline = lis.get(5).child(0).text();
                    String joinActIntroduce = lis.get(6).child(0).text();
                    String joinActDuty = lis.get(7).child(0).text();
                    actInfos.add(new ZhiyuanJoinActInfo(joinActName, joinActTime, joinActPlanNum, joinActJoinNum, joinActNum,
                            joinActType, joinActLocation, joinActGongshi, joinActDeadline, joinActIntroduce, joinActDuty));
                }
            }
        });
        handler.sendEmptyMessageDelayed(Constants.HANDELER_LOADMORE_DETIALS,3000L);
    }
    private void initPullToRefresh(){
        pullToRefreshRecyclerView=(PullToRefreshRecyclerView)findViewById(R.id.ptrrv_joinact_actdetials);
        pullToRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pullToRefreshRecyclerView.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                handler.sendEmptyMessageDelayed(Constants.HANDELER_FUNCTION_TO_LOAD_DETIALS, 3000L);
            }
        });
        pullToRefreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(Constants.HANDELER_REFRESH_DETIASL);
            }
        });
        ptrrvAdapter=new PtrrvAdapter(JoinPartyActivity.this,LayoutInflater.from(JoinPartyActivity.this));
        ptrrvAdapter.setCount(10);//展现多少item
        pullToRefreshRecyclerView.setAdapter(ptrrvAdapter);
        pullToRefreshRecyclerView.onFinishLoading(true,false);
    }
    class PtrrvAdapter extends PtrrvBaseAdapter<PtrrvAdapter.MyViewHolder>{
        public PtrrvAdapter(Context mContext, LayoutInflater mInflater) {
            super(mContext, mInflater);
        }
        @Override
        public PtrrvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder=new MyViewHolder(mInflater.inflate(R.layout.item_ptrrv_zhiyuan_actinfo,null));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(PtrrvAdapter.MyViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            ZhiyuanJoinActInfo info=actInfos.get(position);
            holder.joinActTitle.setText(info.getJoinActName());
            holder.joinActTime.setText(info.getJoinActTime());
            holder.joinActPlanNum.setText(info.getJoinNumOfPlan());
            holder.joinActJoinNum.setText(info.getJoinNumOfJoin());
            holder.joinActNum.setText(info.getJoinActNum());
            final String actId=info.getJoinActNum();
            holder.joinActType.setText(info.getJoinActType());
            holder.joinActLocation.setText(info.getJoinActLocation());
            holder.joinActGongshi.setText(info.getJoinActGongshi());
            holder.joinActDeadline.setText(info.getJoinActDeadline());
            holder.joinActIntroduce.setText(info.getJoinActDetials());
            holder.joinActDuty.setText(info.getJoinActDuty());
            holder.joinActButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","yes");
                    showJoinActDialog(actId);
                }
            });
        }

        @Override
        public void setCount(int paramInt) {
            super.setCount(paramInt);
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView joinActTitle;
            TextView joinActTime;
            TextView joinActPlanNum;
            TextView joinActJoinNum;
            TextView joinActNum;
            TextView joinActType;
            TextView joinActLocation;
            TextView joinActGongshi;
            TextView joinActDeadline;
            TextView joinActIntroduce;
            TextView joinActDuty;
            Button joinActButton;
            public MyViewHolder(View itemView) {
                super(itemView);
                joinActTitle=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_name);
                joinActTime=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_time);
                joinActPlanNum=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_planNum);
                joinActJoinNum=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_joinNum);
                joinActNum=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actNum);
                joinActType=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actType);
                joinActLocation=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actLocation);
                joinActGongshi=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actGongshi);
                joinActDeadline=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actDeadline);
                joinActIntroduce=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actIntroduce);
                joinActDuty=(TextView)itemView.findViewById(R.id.tv_zhiyuan_actinfo_actDuty);
                joinActButton=(Button)itemView.findViewById(R.id.btn_zhiyuan_joinact);
            }

        }
    }
    private void showJoinActDialog(final String actID){
        AlertDialog.Builder builder=new AlertDialog.Builder(JoinPartyActivity.this);
        final AlertDialog dialog=builder.create();
        View dialogView=View.inflate(JoinPartyActivity.this, R.layout.layout_custom_dialog_joinparty, null);
        Button button_yes=(Button)dialogView.findViewById(R.id.dialog_yes);
        Button button_no=(Button)dialogView.findViewById(R.id.dialog_no);
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinAnActivity(actID);
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    private void joinAnActivity(String actID){
        Request request=new Request.Builder().url(Constants.URL_JOIN_AN_ACT+actID+"&_="+System.currentTimeMillis()).build();
        LinkVolunteerAct.okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_SHOW_TOAST;
                message.obj="参加活动失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                if(responseData.contains("加入成功！")){
                    Message msg=handler.obtainMessage();
                    msg.what=Constants.HANDELER_SHOW_TOAST;
                    msg.obj="加入成功！";
                    handler.sendMessage(msg);
                }else {
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDELER_SHOW_TOAST;
                    message.obj="参加活动失败";
                    handler.sendMessage(message);
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(JoinPartyActivity.this,LinkVolunteerAct.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
