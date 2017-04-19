package com.ustb.shellbox.shelllife.inneractivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkLibraryAct;
import com.ustb.shellbox.shelllife.apater.PtrrvBaseAdapter;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.webactivity.WebLibYuyueActivity;
import com.ustb.shellbox.shelllife.webactivity.WebLibraryActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class LibraryInfoActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    ArrayList<String> arrayListInfo;
    ArrayList<String> arrayListBook;
    private static boolean hasDate;
    private int pageIndex=2;
    ArrayList<Map<String,Object>> arrayListBorrowDetials;
    ArrayList<Map<String,Object>> arrayListDeptDetials;
    PullToRefreshRecyclerView ptrry_dialog;
    PtrrvAdapter pttrvAdapter;
    PopupWindow popupWindow;
    ListView deptListView;
    View popupContentView;
    GridView gv_Popup;
    List<Map<String,Object>> popupList;
    private String[] popup_itemnames=new String[]{"借阅历史","欠款信息","座位预约"};
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);//该handler只执行下拉刷新和加载更多操作
            switch (msg.what){
                case Constants.HANDELER_ACCESS_FAILURE:
                    Toast.makeText(LibraryInfoActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }

            //该handler只执行下拉刷新和加载更多操作
            if (msg.what==Constants.HANDELER_REFRESH_LIB_DETIALS){//执行下拉刷新刷新操作
                pttrvAdapter.setCount(10);
                pttrvAdapter.notifyDataSetChanged();//通知刷新
                ptrry_dialog.setOnRefreshComplete();//通知刷新完成
                ptrry_dialog.onFinishLoading(true,false);//第一个参数决定是否能够继续加载数据
                Log.e("refresh","下拉刷新啦");
            }
            while (msg.what!=Constants.HANDELER_LOADMORE_LIB_DETIAILS){//当handler接收到消息后,消息不是加载更多直接返回,使得下面的操作无法进行
                Log.e("noloadmore","不需要加载更多");
                return;                                                     //如果是执行加载更多操作,则往下执行。
            }
            if(pttrvAdapter.getItemCount()==arrayListBorrowDetials.size()){//判断数据是否已经加载到容量的最大值,是则返回,如果不是就继续。
                Log.e("nodate","没有数据了");
                Toast.makeText(LibraryInfoActivity.this,"到底了",Toast.LENGTH_LONG).show();
                ptrry_dialog.onFinishLoading(false,false);//数据到底,不能继续加载数据
                return;
            }
            Log.e("loadmore","加载更多");
            pttrvAdapter.setCount(arrayListBorrowDetials.size());
            pttrvAdapter.notifyDataSetChanged();
            ptrry_dialog.onFinishLoading(true,false);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_info);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent=getIntent();
        bundle=intent.getExtras();
        arrayListInfo=(ArrayList)bundle.getSerializable("info");
        arrayListBook=(ArrayList)bundle.getSerializable("bookInfo");
        arrayListBorrowDetials=new ArrayList<>();
        arrayListDeptDetials=new ArrayList<>();
        BorrowDetials();
        DetbDetials();
        ListView listView_book=(ListView)findViewById(R.id.lv_library_info_book);
        ListView listView_info=(ListView)findViewById(R.id.lv_library_info);
        listView_info.setAdapter(new ArrayAdapter<String>(LibraryInfoActivity.this,R.layout.item_listview_libinfo,R.id.tv_list_lib_info,arrayListInfo));
        listView_book.setAdapter(new ArrayAdapter<String>(LibraryInfoActivity.this, R.layout.item_listview_info_book, R.id.tv_list_lib_info_book, arrayListBook));

    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(LibraryInfoActivity.this, LinkLibraryAct.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("volInfo",null);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void BorrowDetials() {
        Request request=new Request.Builder().url(Constants.URL_GET_LIBRARY_HISTORY).build();
        LinkLibraryAct.okHttpClient_libInfo.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_FAILURE;
                message.obj="借阅历史信息获取失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Document document= Jsoup.parse(responseData);
                Element table=document.getElementsByTag("table").get(0);
                Elements trs=table.getElementsByTag("tr");
                Log.e("trssize",trs.size()+"");
                if(trs.size()<=1){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("number","无借阅历史");
                    map.put("name","无");
                    map.put("author","无");
                    map.put("btime", "无");
                    map.put("rtime","无");
                    arrayListBorrowDetials.add(map);
                }else {
                    for(int i=1;i<trs.size();i++){
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("number",trs.get(i).child(1).text());
                        map.put("name",trs.get(i).child(2).text());
                        map.put("author",trs.get(i).child(3).text());
                        map.put("btime", trs.get(i).child(4).text());
                        map.put("rtime",trs.get(i).child(5).text());
                        arrayListBorrowDetials.add(map);
                    }
                }
            }
        });
        loadMoreInfo();
    }
    private void DetbDetials(){
        Request request=new Request.Builder().url(Constants.URL_GET_LIB_DEBT).build();
        LinkLibraryAct.okHttpClient_libInfo.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_FAILURE;
                message.obj="欠款信息获取失败"+e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Document document=Jsoup.parse(responseData);
                Element div=document.getElementsByAttributeValueContaining("id","mylib_content").first();
                Element table=div.getElementsByTag("table").last();
                Elements trs=table.getElementsByTag("tr");
                if(trs.size()<=1){
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("tiaoma","无欠款信息");
                    map.put("bookName"," ");
                    map.put("author","信誉良好");
                    map.put("borTime"," ");
                    map.put("retTime"," ");
                    map.put("shouldPay"," ");
                    map.put("realPay"," ");
                    map.put("state"," ");
                    arrayListDeptDetials.add(map);
                }else {
                    for(int i=1;i<trs.size();i++){
                        Element tr=trs.get(i);
                        String tiaoma=tr.child(0).text();
                        String bookName=tr.child(2).text();
                        String author=tr.child(3).text();
                        String borTime=tr.child(4).text();
                        String retTime=tr.child(5).text();
                        String shouldPay=tr.child(7).text();
                        String realPay=tr.child(8).text();
                        String state=tr.child(9).text();
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("tiaoma",tiaoma);
                        map.put("bookName",bookName);
                        map.put("author",author);
                        map.put("borTime",borTime);
                        map.put("retTime",retTime);
                        map.put("shouldPay",shouldPay);
                        map.put("realPay",realPay);
                        map.put("state",state);
                        arrayListDeptDetials.add(map);
                    }
                }
            }
        });
    }
    public void showPopupWindows(View view){
        if(popupWindow==null){
            LayoutInflater layoutInflater=getLayoutInflater();
            popupContentView=layoutInflater.inflate(R.layout.layout_popupwindows,null);
            gv_Popup=(GridView)popupContentView.findViewById(R.id.gv_popupwindows);
            popupList=new ArrayList<Map<String,Object>>();
            for (int i=0;i<popup_itemnames.length;i++){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("popName",popup_itemnames[i]);
                popupList.add(map);
            }
            gv_Popup.setAdapter(new SimpleAdapter(LibraryInfoActivity.this,popupList,R.layout.item_gv_popup,new String[]{"popName"}
            ,new int[]{R.id.tv_lib_popup}));
            popupWindow=new PopupWindow(popupContentView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //使其聚焦
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindows_anim_style);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //这个是为了点击"返回back"也能使其消失,并且不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(popupContentView, Gravity.BOTTOM,0,0);
        gv_Popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showBorrrowDetiasl();
                        break;
                    case 1:
                        showDebtDetials();
                        break;
                    case 2:
                        enterLibYuyue();
                        break;
                }
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
            }
        });

    }
    private void enterLibYuyue(){
        Intent intent=new Intent(LibraryInfoActivity.this, WebLibYuyueActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("fromLibInfo",true);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void showBorrrowDetiasl(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LibraryInfoActivity.this);
        final AlertDialog alertDialog=builder.create();
        View dialogView=View.inflate(LibraryInfoActivity.this, R.layout.layout_custom_dialog_showdetials, null);
        ImageButton imageButton=(ImageButton)dialogView.findViewById(R.id.imgbtn_cancle_lib);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ptrry_dialog=(PullToRefreshRecyclerView)dialogView.findViewById(R.id.ptrrv_libary_borrow_details);
        ptrry_dialog.setSwipeEnable(true);//是否能刷新
        ptrry_dialog.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        ptrry_dialog.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {//设置加载更多
                handler.sendEmptyMessageDelayed(Constants.HANDELER_LOADMORE_LIB_DETIAILS,3000L);
//                handler.sendEmptyMessageAtTime(Constants.HANDELER_LOADMORE_LIB_DETIAILS,5000L);//这个之所以被注释了,因为我的下拉刷新,速度太快了。
            }
        });
        ptrry_dialog.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//设置下拉刷新
                handler.sendEmptyMessageAtTime(Constants.HANDELER_REFRESH_LIB_DETIALS, 1000L);
            }
        });
        pttrvAdapter=new PtrrvAdapter(this,LayoutInflater.from(LibraryInfoActivity.this));
        pttrvAdapter.setCount(10);//设置一开始显示的条目数量,超过这个数量就会触发加载更多
        ptrry_dialog.setAdapter(pttrvAdapter);//设置适配器
        ptrry_dialog.onFinishLoading(true,false);//是否能加载更多,true为可以。

        alertDialog.setView(dialogView);
        alertDialog.show();
    }
    private void showDebtDetials(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LibraryInfoActivity.this);
        final AlertDialog dialog=builder.create();
        View viewDept=View.inflate(LibraryInfoActivity.this,R.layout.layout_custom_dialog_deptdetials,null);
        ImageButton imageButton=(ImageButton)viewDept.findViewById(R.id.imgbtn_cancle_lib_dept);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        deptListView=(ListView)viewDept.findViewById(R.id.lv_dialog_lib_dept);
        deptListView.setAdapter(new SimpleAdapter(LibraryInfoActivity.this,arrayListDeptDetials,R.layout.item_list_lib_dept
        ,new String[]{"tiaoma","bookName","author","borTime","retTime","shouldPay","realPay","state"},new int[]{
                R.id.tv_lib_dept_tiaoma,R.id.tv_lib_dept_bookName,R.id.tv_lib_dept_bookAuthor,R.id.tv_lib_dept_borrowtime,
                R.id.tv_lib_dept_returntime,R.id.tv_lib_dept_shouldpay,R.id.tv_lib_dept_realpay,R.id.tv_lib_dept_state
        }));
        dialog.setView(viewDept);
        dialog.show();

    }
    class PtrrvAdapter extends PtrrvBaseAdapter<PtrrvAdapter.LibViewHolder>{//注意这里的范式,并不是recycleViewAdapter.Viewholder，而是自身的holder
        public PtrrvAdapter(Context mContext, LayoutInflater mInflater) {
            super(mContext, mInflater);
        }
        @Override
        public PtrrvAdapter.LibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LibViewHolder holder=new LibViewHolder(mInflater.inflate(R.layout.item_ptrrv_lib_details,null));
            return holder;
        }
        @Override
        public void onBindViewHolder(PtrrvAdapter.LibViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.tv_number.setText((String) arrayListBorrowDetials.get(position).get("number"));
            holder.tv_name.setText((String)arrayListBorrowDetials.get(position).get("name"));
            holder.tv_name.getPaint().setFakeBoldText(true);
            holder.tv_author.setText((String)arrayListBorrowDetials.get(position).get("author"));
            holder.tv_btime.setText((String)arrayListBorrowDetials.get(position).get("btime"));
            holder.tv_rtime.setText((String)arrayListBorrowDetials.get(position).get("rtime"));
        }
        class LibViewHolder extends RecyclerView.ViewHolder{
            TextView tv_number;
            TextView tv_name;
            TextView tv_author;
            TextView tv_btime;
            TextView tv_rtime;
            public LibViewHolder(View itemView) {
                super(itemView);
                tv_number=(TextView)itemView.findViewById(R.id.tv_ptrrv_number);
                tv_name=(TextView)itemView.findViewById(R.id.tv_ptrrv_bookname);
                tv_author=(TextView)itemView.findViewById(R.id.tv_ptrrv_author);
                tv_btime=(TextView) itemView.findViewById(R.id.tv_ptrrv_borrow_time);
                tv_rtime=(TextView)itemView.findViewById(R.id.tv_ptrrv_return_time);
            }
        }
    }
    private void loadMoreInfo()  {//加载最多5页借阅历史()包括Page=1，,如果有4页的话，这里只有4页循环,如果小于4页的话,跳出循环
        for(int i=0;i<4;i++) {//Indexpage的初始值2，最大值为5
            Request request = new Request.Builder().url(Constants.URL_GET_LIBRARY_HISTORY + "?page=" + pageIndex).build();
            pageIndex++;//访问完后,Indexpage自动+1;
            if(pageIndex>5){//如果超过了5页 那就跳出循环,没有就继续
                break;
            }
            LinkLibraryAct.okHttpClient_libInfo.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDELER_ACCESS_FAILURE;
                    message.obj = "加载失败" + e.toString();
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Document document = Jsoup.parse(responseData);
                    Element table = document.getElementsByTag("table").get(0);
                    Elements trs = table.getElementsByTag("tr");
                    Log.e("size", trs.size() + "");
                    if (trs.size() < 2) {//如果只有一个tr就表示这一页就没有数据了
                        hasDate = false;//没有数据了,设置为false，否则,继续解析数据
                    } else {
                        for (int j = 1; j < trs.size(); j++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("number", trs.get(j).child(1).text());
                            map.put("name", trs.get(j).child(2).text());
                            map.put("author", trs.get(j).child(3).text());
                            map.put("btime", trs.get(j).child(4).text());
                            map.put("rtime", trs.get(j).child(5).text());
                            arrayListBorrowDetials.add(map);
                        }
                    }
                }
            });
            if (!hasDate){//每循环过一次就判断有没有数据,如果有数据,就继续循环,否则,跳出循环
                break;
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(LibraryInfoActivity.this, LinkLibraryAct.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("volInfo",null);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
