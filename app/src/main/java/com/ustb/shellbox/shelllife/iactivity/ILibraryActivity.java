package com.ustb.shellbox.shelllife.iactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkLibraryAct;
import com.ustb.shellbox.shelllife.apater.PtrrvBaseAdapter;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ILibraryActivity extends BaseSetupWizardActivity {
    public static OkHttpClient okHttpClient_lib;
    EditText editText;
    String searchKey;
    int countInt;
    int IndexPage=2;
    PullToRefreshRecyclerView pullToRefreshRecyclerView;
    PtrrvAdapter ptrrvAdapter;
    List<Map<String,Object>> list;
    SharedPreferences sharedPreferences;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_SEARCH_RESULT_ISNULL:
                    Toast.makeText(ILibraryActivity.this,"本馆没有您检索的图书~",Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_SHOW_TOAST:

                    break;
                case Constants.HANDELER_ACCESS_FAILURE:
                    Toast.makeText(ILibraryActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_UPDATE_DATA_PTR:
                    initPTRData();
                    break;
                case Constants.HANDELER_FUNCTION_TO_LOAD:
                    loadMore(searchKey,countInt);
                    break;
            }
            //----
            //该handler只执行下拉刷新和加载更多操作
            if (msg.what==Constants.HANDELER_REFRESH_LIB_DETIALS){//执行下拉刷新刷新操作
                if(list.size()<20){
                    ptrrvAdapter.setCount(list.size());
                }else {
                    ptrrvAdapter.setCount(20);
                }
                ptrrvAdapter.notifyDataSetChanged();//通知刷新
                pullToRefreshRecyclerView.setOnRefreshComplete();//通知刷新完成
                pullToRefreshRecyclerView.onFinishLoading(true,false);//第一个参数决定是否能够继续加载数据
                Log.e("refresh", "下拉刷新啦");
            }
            while (msg.what!=Constants.HANDELER_LOADMORE_LIB_DETIAILS){//当handler接收到消息后,消息不是加载更多直接返回,使得下面的操作无法进行
                Log.e("noloadmore","不需要加载更多");
                return;                                                     //如果是执行加载更多操作,则往下执行。
            }
            if(ptrrvAdapter.getItemCount()==list.size()){//判断数据是否已经加载到容量的最大值,是则返回,如果不是就继续。
                Log.e("nodate","没有数据了");
                Toast.makeText(ILibraryActivity.this,"到底了",Toast.LENGTH_LONG).show();
                pullToRefreshRecyclerView.onFinishLoading(false, false);//数据到底,不能继续加载数据
                return;
            }
            Log.e("loadmore","加载更多");
            ptrrvAdapter.setCount(list.size());
            ptrrvAdapter.notifyDataSetChanged();
            pullToRefreshRecyclerView.onFinishLoading(true, false);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilibrary);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(ILibraryActivity.this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        sharedPreferences=getSharedPreferences("AccountInfo",MODE_PRIVATE);
        list=new ArrayList<Map<String,Object>>();
        editText=(EditText)findViewById(R.id.et_lib_search);
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.cookieJar(new LibCookierManager());
        builder.readTimeout(10, TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS);
        okHttpClient_lib=builder.build();
    }
    private void initPTRData(){
        pullToRefreshRecyclerView=(PullToRefreshRecyclerView)findViewById(R.id.ptrrv_library_search_result);
        pullToRefreshRecyclerView.setSwipeEnable(true);//是否能偶刷新
        pullToRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置线性布局管理器
        pullToRefreshRecyclerView.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {//设置加载更多
                handler.sendEmptyMessageDelayed(Constants.HANDELER_FUNCTION_TO_LOAD,3000L);
//                handler.sendEmptyMessageDelayed(Constants.HANDELER_LOADMORE_LIB_DETIAILS, 3000L);
            }
        });
        pullToRefreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//设置下拉刷新
                handler.sendEmptyMessageAtTime(Constants.HANDELER_REFRESH_LIB_DETIALS, 1000L);
            }
        });
        ptrrvAdapter=new PtrrvAdapter(ILibraryActivity.this,LayoutInflater.from(ILibraryActivity.this));
        if(list.size()<20){
            ptrrvAdapter.setCount(list.size());
        }else {
            ptrrvAdapter.setCount(20);
        }
        pullToRefreshRecyclerView.setAdapter(ptrrvAdapter);
        pullToRefreshRecyclerView.onFinishLoading(true,false);
    }
    class PtrrvAdapter extends PtrrvBaseAdapter<PtrrvAdapter.SearchResultViewHolder>{
        public PtrrvAdapter(Context mContext, LayoutInflater mInflater) {
            super(mContext, mInflater);
        }
        @Override
        public PtrrvAdapter.SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SearchResultViewHolder searchResultViewHolder=new SearchResultViewHolder(mInflater.inflate(R.layout.item_ptrrv_lib_searchresult,null));
            return searchResultViewHolder;
        }
        @Override
        public void onBindViewHolder(PtrrvAdapter.SearchResultViewHolder holder, int position) {
            holder.tv_bookAuthor.setText((String)list.get(position).get("bookAuthor"));
            holder.tv_bookCollection.setText((String)list.get(position).get("bookCollection"));
            holder.tv_bookType.setText((String)list.get(position).get("bookType"));
            holder.tv_bookName.setText((String)list.get(position).get("bookName"));
            super.onBindViewHolder(holder, position);
        }
        class SearchResultViewHolder extends RecyclerView.ViewHolder{
            TextView tv_bookType;
            TextView tv_bookName;
            TextView tv_bookCollection;
            TextView tv_bookAuthor;
            public SearchResultViewHolder(View itemView) {
                super(itemView);
                tv_bookType=(TextView)itemView.findViewById(R.id.tv_ptrrv_lib_bookType_result);
                tv_bookName=(TextView)itemView.findViewById(R.id.tv_ptrrv_lib_bookName_result);
                tv_bookCollection=(TextView)itemView.findViewById(R.id.tv_ptrrv_lib_bookCollection_result);
                tv_bookAuthor=(TextView)itemView.findViewById(R.id.tv_ptrrv_lib_bookAuthor_result);
            }
        }
    }

    public void search_Book(View view){
        searchKey=editText.getText().toString();
        if(searchKey.isEmpty()){
            Toast.makeText(ILibraryActivity.this,"查询信息不能为空!",Toast.LENGTH_LONG).show();
        }
        Request request=new Request.Builder().url(Constants.URL_LIB_SEARCH_START+searchKey+Constants.URL_LIB_SEARCH_END).build();
        okHttpClient_lib.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_FAILURE;
                message.obj="信息查询失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if(responseData.contains("本馆没有您检索的图书")){
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDLER_SEARCH_RESULT_ISNULL;
                    handler.sendMessage(message);
                }else {
                    Document document = Jsoup.parse(responseData);
                    Element ol = document.getElementsByTag("ol").first();
                    Element strong=document.getElementsByTag("strong").last();
                    String  count = strong.text();
                    countInt=Integer.valueOf(count);
                    Log.e("strongsize","共检测到"+strong.text()+"条，提名等于"+searchKey+"的结果");
                    Elements lis = ol.children();
                    list.clear();
                    for (int i = 0; i < lis.size(); i++) {
                        Map<String, Object> listItem = new HashMap<String, Object>();
                        Element h3 = lis.get(i).getElementsByTag("h3").first();
                        Element h3_span = h3.child(0);//图书类型
                        Element h3_a = h3.child(1);//图书名称
                        Element p = lis.get(i).getElementsByTag("p").first();
                        Element p_span = p.child(0);//馆藏副本
                        String bookType = h3_span.text();//图书类型
                        String bookName = h3_a.text();//图书名称
                        String href = h3_a.attr("href");//图书信息详细链接
                        String bookCollection = p_span.text();
                        String bookAuthor = p.ownText();
                        listItem.put("bookType", bookType);
                        listItem.put("bookName", bookName);
                        listItem.put("href", href);
                        listItem.put("bookCollection", bookCollection);
                        listItem.put("bookAuthor", bookAuthor);
                        list.add(listItem);
                    }
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDELER_UPDATE_DATA_PTR;
                    handler.sendMessage(message);
                }

            }
        });
    }
    private void loadMore(String searchKey,int countInt){
        Request request=new Request.Builder().url("http://lib.ustb.edu.cn:8080/opac/openlink.php?dept=ALL&title="+searchKey+"&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&count="
        +countInt+"+&with_ebook=&page="+IndexPage).build();
        IndexPage++;
        okHttpClient_lib.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_FAILURE;
                message.obj="信息查询失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Document document = Jsoup.parse(responseData);
                Element ol = document.getElementsByTag("ol").first();
                Elements lis = ol.children();
                for (int i = 0; i < lis.size(); i++) {
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    Element h3 = lis.get(i).getElementsByTag("h3").first();
                    Element h3_span = h3.child(0);//图书类型
                    Element h3_a = h3.child(1);//图书名称
                    Element p = lis.get(i).getElementsByTag("p").first();
                    Element p_span = p.child(0);//馆藏副本
                    String bookType = h3_span.text();//图书类型
                    String bookName = h3_a.text();//图书名称
                    String href = h3_a.attr("href");//图书信息详细链接
                    String bookCollection = p_span.text();
                    String bookAuthor = p.ownText();
                    listItem.put("bookType", bookType);
                    listItem.put("bookName", bookName);
                    listItem.put("href", href);
                    listItem.put("bookCollection", bookCollection);
                    listItem.put("bookAuthor", bookAuthor);
                    list.add(listItem);
                }
            }
        });
        handler.sendEmptyMessageDelayed(Constants.HANDELER_LOADMORE_LIB_DETIAILS, 3000L);
    }
    public void search_libInfo(View view){
        startActivity(new Intent(ILibraryActivity.this, LinkLibraryAct.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ILibraryActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    class LibCookierManager implements CookieJar {
        final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
